package com.pension.process.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pension.process.exception.AadharNumberNotFound;
import com.pension.process.exception.AuthorizationException;
import com.pension.process.exception.PensionerDetailException;
import com.pension.process.model.JwtRequest;
import com.pension.process.model.JwtResponse;
import com.pension.process.model.PensionDetail;
import com.pension.process.model.PensionerDetail;
import com.pension.process.model.ProcessPensionInput;
import com.pension.process.restClients.AuthClient;
import com.pension.process.restClients.PensionerDetailClient;
import com.pension.process.service.ProcessPensionServiceImpl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping(value = "/api/v1")
public class ProcessPensionController {

	@Autowired
	ProcessPensionServiceImpl processPensionServiceImpl;
	
	@Autowired
	private AuthClient authClient;
	
	@Autowired
	private PensionerDetailClient pensionerDetailClient;
	
	@PostMapping("/authenticate")
	public JwtResponse getAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws AuthorizationException{
		log.debug("Received authenticate request in Process Pension");
		return authClient.createAuthenticationToken(authenticationRequest);
	}
	
	@GetMapping("/getAllPensioner")
	public List<PensionerDetail> getAllPensionerDetails(
			@RequestHeader(value = "Authorization", required = true) String requestTokenHeader) throws AuthorizationException, NumberFormatException, IOException{
		return pensionerDetailClient.getAllPensioner(requestTokenHeader);
	}

	@GetMapping("/PensionerDetailByAadhaar/{aadharNumber}")
	public PensionerDetail getPensionerDetailByAadhaar(
			@RequestHeader(value = "Authorization", required = true) String requestTokenHeader,
			@PathVariable long aadharNumber) throws AuthorizationException, AadharNumberNotFound{
		return pensionerDetailClient.getPensionerDetailByAadhaar(requestTokenHeader, aadharNumber);
	}

	
	@PostMapping("/ProcessPension")
	public PensionDetail getPensionDetail(
			@RequestHeader(value = "Authorization", required = true) 
			String requestTokenHeader,
			@RequestBody ProcessPensionInput processPensionInput) throws AuthorizationException, PensionerDetailException, AadharNumberNotFound
	{
			log.debug("In process pension controller");
			log.debug("Token Passed: "+requestTokenHeader );
			if(authClient.authorizeTheRequest(requestTokenHeader).isValid()) 
			{
				log.debug("authorization success");
				log.info("Token valid :"+authClient.authorizeTheRequest(requestTokenHeader).isValid());
				return processPensionServiceImpl.processPension(requestTokenHeader,processPensionInput);
			}
			else
			{
				log.error("Invalid Token");
				throw new AuthorizationException("Not allowed, Please pass a valid token");
			}
	}
	
}
