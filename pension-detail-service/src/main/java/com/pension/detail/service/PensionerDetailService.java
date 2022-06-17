package com.pension.detail.service;

import java.io.IOException;

import com.pension.detail.exception.AadharNumberNotFound;
import com.pension.detail.model.PensionerDetail;

public interface PensionerDetailService {
	
	public PensionerDetail getPensionerDetailByAadharCard(long aadharNumber) throws AadharNumberNotFound, NumberFormatException, IOException;

}
