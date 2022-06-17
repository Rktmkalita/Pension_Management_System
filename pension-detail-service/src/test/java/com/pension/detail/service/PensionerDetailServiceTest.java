package com.pension.detail.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.pension.detail.exception.AadharNumberNotFound;
import com.pension.detail.model.Bank;
import com.pension.detail.model.PensionerDetail;
import com.pension.detail.util.CsvToListConverter;


@ExtendWith(MockitoExtension.class)
@SpringBootTest
class PensionerDetailServiceTest {

	@InjectMocks
	private PensionerDetailServiceImpl pensionerDetailServiceImpl;

	@Mock
	private CsvToListConverter csvUtils;

	@Test
    void testGetPensionDetailByAadharCard() throws AadharNumberNotFound, NumberFormatException, IOException   {
		Bank bank = new Bank("SBI", "1029486523", "private");
		PensionerDetail pensionerDetail = new PensionerDetail(420559429029l, "Ravi", LocalDate.of(1997, 12, 03),
				"BSDKS1495N", 29000, 1200, "self", bank);

		List<PensionerDetail> list = new ArrayList<PensionerDetail>();
		list.add(pensionerDetail);

		Mockito.when(csvUtils.processTxt()).thenReturn(list);
		assertEquals(pensionerDetailServiceImpl.getPensionerDetailByAadharCard(420559429029l), pensionerDetail);
	}

}