package com.pension.process.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pension.process.exception.AadharNumberNotFound;
import com.pension.process.exception.AuthorizationException;
import com.pension.process.exception.PensionerDetailException;
import com.pension.process.model.PensionDetail;
import com.pension.process.model.PensionerDetail;
import com.pension.process.model.ProcessPensionInput;
import com.pension.process.restClients.PensionerDetailClient;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProcessPensionServiceImpl implements ProcessPensionService {

	@Autowired
	private PensionerDetailClient pensionerDetailClient;
	
	@Override
	public PensionDetail processPension(String token, ProcessPensionInput processPensionInput ) throws PensionerDetailException, AuthorizationException, AadharNumberNotFound	
	{		
		log.debug("Aadhar No Input "+processPensionInput.getAadharNumber());		
		PensionerDetail pensionerDetail = null;		
		try
		{
			pensionerDetail = pensionerDetailClient.getPensionerDetailByAadhaar(token, processPensionInput.getAadharNumber());
			log.debug("pensionerDetail"+pensionerDetail);
		
		}
		catch (AadharNumberNotFound e) {
			throw new AadharNumberNotFound("Aadhar Card Number is not Valid. Please check it and try again");
		}
		
		catch (Exception e) {
			throw new PensionerDetailException("Invalid pensioner detail provided, please provide valid detail.");
		}
		
		double pensionAmount = calculatePensionAmmount(pensionerDetail.getPensionType(), pensionerDetail.getSalaryEarned(), pensionerDetail.getAllowances());				
		PensionDetail pensionDetail = new PensionDetail();
		pensionDetail.setBankServiceCharge(calculateBankCharge(pensionerDetail.getBank().getBankType()));
		pensionDetail.setPensionAmount(pensionAmount);
		return pensionDetail;
		
	}
	
	private double calculatePensionAmmount(String pensionType, double salary, double allowances) {
		double pensionAmount = 0;
		if(pensionType.equalsIgnoreCase("self"))
		{
			pensionAmount = 0.8 * salary + allowances;
		}
		else if(pensionType.equalsIgnoreCase("family"))
		{
			pensionAmount = 0.5 * salary + allowances;
		}
		return pensionAmount;	
		
	}
	
	private double calculateBankCharge(String bankType) {	
		double bankServiceCharge = 0;
		if (bankType.equalsIgnoreCase("public")) {
			bankServiceCharge = 500;
		} else {
			bankServiceCharge = 550;
		}
		return bankServiceCharge ;
		
	}
	
}
