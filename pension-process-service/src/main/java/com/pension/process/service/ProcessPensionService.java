package com.pension.process.service;

import com.pension.process.exception.AadharNumberNotFound;
import com.pension.process.exception.AuthorizationException;
import com.pension.process.exception.PensionerDetailException;
import com.pension.process.model.PensionDetail;
import com.pension.process.model.ProcessPensionInput;

public interface ProcessPensionService {
	
	public PensionDetail processPension(String requestTokenHeader,ProcessPensionInput processPensionInput) throws PensionerDetailException, AuthorizationException, AadharNumberNotFound;
	
}
