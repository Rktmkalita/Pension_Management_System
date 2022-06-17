package com.pension.process.restClients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.pension.process.exception.AuthorizationException;
import com.pension.process.model.JwtRequest;
import com.pension.process.model.JwtResponse;
import com.pension.process.model.TokenValidationResponse;

@FeignClient(name = "Authorization-Microservice", url = "${url.app.auth:http://localhost:8100/auth/api/v1}")
public interface AuthClient {

	@PostMapping("/authorizeuser")
	public TokenValidationResponse authorizeTheRequest(@RequestHeader(value = "Authorization", required = true) String requestTokenHeader);
	
	@PostMapping(value = "/authenticateuser")
	public JwtResponse createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws AuthorizationException;
}
