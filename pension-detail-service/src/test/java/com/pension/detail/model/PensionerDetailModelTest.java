package com.pension.detail.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

class PensionerDetailModelTest {
	
	
	@Test
	void testNoArgs() {
		assertThat(new PensionerDetail()).isNotNull();
	}

	Bank bank = new Bank("SBI", "1027886573", "private");

	@Test
	void testAllArgs() {
		PensionerDetail pensionerDetail = new PensionerDetail(420559429029l, "Ravi", LocalDate.of(1997, 12, 03),
				"BSDKS1495N", 30000, 1500, "self", bank);
		assertThat(pensionerDetail).isNotNull();
	}

	@Test
	void testPensionerDetailSetterMethod() {
		PensionerDetail pensionerDetail = new PensionerDetail();
		pensionerDetail.setAadharNumber(420559429029l);
		pensionerDetail.setName("Ravi");
		pensionerDetail.setDateOfBirth(LocalDate.of(1999, 12, 03));
		pensionerDetail.setPan("BSDKS1495N");
		pensionerDetail.setSalaryEarned(30000);
		pensionerDetail.setAllowances(1500);
		pensionerDetail.setPensionType("self");
		pensionerDetail.setBank(bank);

		assertThat(pensionerDetail).isNotNull();
	}

}
