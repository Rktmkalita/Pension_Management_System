package com.pension.process.restClients;

import java.io.IOException;
import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import com.pension.process.exception.AadharNumberNotFound;
import com.pension.process.exception.AuthorizationException;
import com.pension.process.model.PensionerDetail;

@FeignClient(name ="PensionerDetail-Microservice",url = "${url.app.details:http://localhost:8200/pensioner/api/v1}")
public interface PensionerDetailClient {
	
	@GetMapping("/findPensionerDetailByAadhaar/{aadharNumber}")
	public PensionerDetail getPensionerDetailByAadhaar(
			@RequestHeader(value = "Authorization", required = true) String requestTokenHeader,
			@PathVariable long aadharNumber) throws AuthorizationException, AadharNumberNotFound;
	
	@GetMapping("/findAllPensionerDetails")
	public List<PensionerDetail> getAllPensioner(
			@RequestHeader(value = "Authorization", required = true) String requestTokenHeader) throws AuthorizationException, NumberFormatException, IOException;

}
