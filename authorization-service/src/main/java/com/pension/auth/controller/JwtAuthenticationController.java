package com.pension.auth.controller;

import static com.pension.auth.Constants.INVALID_CREDENTIALS;
import static com.pension.auth.Constants.USER_DISABLED;
import static com.pension.auth.Constants.VALIDITY;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pension.auth.config.JwtTokenUtil;
import com.pension.auth.exception.AuthorizationException;
import com.pension.auth.model.JwtRequest;
import com.pension.auth.model.JwtResponse;
import com.pension.auth.model.TokenValidationResponse;
import com.pension.auth.service.MyUserDetailsService;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin
@RestController
@Slf4j
@RequestMapping(value = "/api/v1")
public class JwtAuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private MyUserDetailsService userDetailsService;

	@PostMapping(value = "/authenticateuser")
	public ResponseEntity<JwtResponse> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest)
			throws AuthorizationException {
		log.debug("Username"+authenticationRequest.getUserName());
		log.debug("Password"+authenticationRequest.getPassword());
		Authentication auth=authenticate(authenticationRequest.getUserName(), authenticationRequest.getPassword());
		
		log.info(auth.toString());
		
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUserName());
		log.debug(userDetails.toString());
		final String token = jwtTokenUtil.generateToken(userDetails);
		return ResponseEntity.ok(new JwtResponse(token,VALIDITY));
	}

	private Authentication authenticate(String userName, String password) throws AuthorizationException {
		try {
			log.info("=============Inside authenticate Method==========");
			log.debug("Username: "+userName);
			Authentication auth=authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, password));
			log.info("Authentication Successful.....");
			log.debug("+++++++++++"+auth.getCredentials()+"++++++++++");
			return auth;
			
		} catch (DisabledException e) {
			log.error(USER_DISABLED);
			throw new AuthorizationException(USER_DISABLED);
			
		} catch (BadCredentialsException e) {
			log.error(INVALID_CREDENTIALS);
			throw new AuthorizationException(INVALID_CREDENTIALS);
		}
		
	}

	@PostMapping(value = "/authorizeuser")
	public ResponseEntity<TokenValidationResponse> authorizeTheRequest(
			@RequestHeader(value = "Authorization", required = true) String requestTokenHeader) {
		log.debug("=============Inside authorize =============="+requestTokenHeader);
		String jwtToken = null;
		String userName = null;
		boolean isValid = false;
		
		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			jwtToken = requestTokenHeader.substring(7);
			log.debug("================JWT Token ================== "+jwtToken);
			
			try {
				userName = jwtTokenUtil.getUsernameFromToken(jwtToken);
			} catch (IllegalArgumentException | ExpiredJwtException e) {
				log.error("Token is Not Valid/Expired");
				 isValid = false;
			}
			
			if (userName != null) {
				 isValid = true;
			}
		}
		
		return  ResponseEntity.ok(new TokenValidationResponse(isValid));

	}

}