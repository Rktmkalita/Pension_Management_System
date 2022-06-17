package com.pension.detail.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pension.detail.exception.AadharNumberNotFound;
import com.pension.detail.exception.AuthorizationException;
import com.pension.detail.model.PensionerDetail;
import com.pension.detail.restClient.AuthClient;
import com.pension.detail.service.PensionerDetailServiceImpl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping(value = "/api/v1")
public class PensionerDetailController {
	
	@Autowired
	PensionerDetailServiceImpl pensionerDetailServiceImpl;
	
	@Autowired
	private AuthClient authClient;
	
	@GetMapping("/findAllPensionerDetails")
	public List<PensionerDetail> getAllPensioner(
			@RequestHeader(value = "Authorization", required = true) String requestTokenHeader) throws AuthorizationException, NumberFormatException, IOException
	{
		 log.debug("Token Passed: "+requestTokenHeader );
		if (authClient.authorizeTheRequest(requestTokenHeader).isValid())
		{
			log.info("Token valid :"+authClient.authorizeTheRequest(requestTokenHeader).isValid());
			return pensionerDetailServiceImpl.getAllPensioner();
		}
		
		else
		{
			log.error("Invalid Token");
			throw new AuthorizationException("Not allowed, Please pass a valid token");
		}
		
	}

	@GetMapping("/findPensionerDetailByAadhaar/{aadharNumber}")
	public PensionerDetail getPensionerDetailByAadhar(
			@RequestHeader(value = "Authorization", required = false) String requestTokenHeader, 
			@PathVariable long aadharNumber) throws AuthorizationException, AadharNumberNotFound, NumberFormatException, IOException
	{
		   log.debug("aadharNumber Input :" +aadharNumber+"  "+requestTokenHeader );
		if (authClient.authorizeTheRequest(requestTokenHeader).isValid())
		{
			log.info("Token valid :"+authClient.authorizeTheRequest(requestTokenHeader).isValid());
			return pensionerDetailServiceImpl.getPensionerDetailByAadharCard(aadharNumber);
		}
		
		else
		{
			log.error("Invalid Token");
			throw new AuthorizationException("Not allowed, Please pass a valid token");
		}
		
	}
			
}
