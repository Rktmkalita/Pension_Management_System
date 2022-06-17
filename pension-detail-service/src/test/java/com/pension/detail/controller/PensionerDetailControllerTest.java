package com.pension.detail.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pension.detail.model.Bank;
import com.pension.detail.model.PensionerDetail;
import com.pension.detail.model.TokenValidationResponse;
import com.pension.detail.restClient.AuthClient;
import com.pension.detail.service.PensionerDetailServiceImpl;
import com.pension.detail.util.CsvToListConverter;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
class PensionerDetailControllerTest {

	@MockBean
	private AuthClient authorisingClient;

	@MockBean
	private PensionerDetailServiceImpl pensionerDetailServiceImpl;

	@MockBean
	private CsvToListConverter pensionerDetailRepository;

	@Autowired
	private MockMvc mockMvc;

	@Test
	void testClientNotNull() {
		assertThat(authorisingClient).isNotNull();
	}

	@Test
	void testMockMvcNotNull() {
		assertThat(mockMvc).isNotNull();
	}

	String mapToJson(Object object) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(object);
	}

	@Test
	void testGetResponse() throws Exception {
		when(authorisingClient.authorizeTheRequest("Bearer @token@token"))
				.thenReturn(new TokenValidationResponse(true));
		mockMvc.perform(
				get("/api/v1/findPensionerDetailByAadhaar/420559429029").header("Authorization", "Bearer @token@token"))
				.andExpect(status().isOk());
	}

	@Test
	void testGetResponseWrongAuthorization() throws Exception {
		when(authorisingClient.authorizeTheRequest("Bearer @token@token"))
				.thenReturn(new TokenValidationResponse(false));
		mockMvc.perform(
				get("/api/v1/findPensionerDetailByAadhaar/420559429029").header("Authorization", "Bearer @token@token"))
				.andExpect(status().isForbidden());
	}

	@Test
	void testGetPensionDetailsValidAadharCard() throws Exception {

		when(authorisingClient.authorizeTheRequest("Bearer @token@token"))
				.thenReturn(new TokenValidationResponse(true));
		Bank bank = new Bank("SBI", "private", "1234567890");
		PensionerDetail pensionerDetail = new PensionerDetail(567891782653l, "SAHRUKH KHAN", LocalDate.of(1994, 11, 03),
				"WERKC4227L", 33000, 1400, "family", bank);
		when(pensionerDetailServiceImpl.getPensionerDetailByAadharCard(567891782653l)).thenReturn(pensionerDetail);

		List<PensionerDetail> list = new ArrayList<PensionerDetail>();
		list.add(pensionerDetail);
		Mockito.when(pensionerDetailRepository.processTxt()).thenReturn(list);
		mockMvc.perform(get("/api/v1/findPensionerDetailByAadhaar/567891782653").contentType("application/json")
				.header("Authorization", "Bearer @token@token")).andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.aadharNumber").value(567891782653l))
				.andExpect(MockMvcResultMatchers.jsonPath("$.name").value("SAHRUKH KHAN"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.dateOfBirth").value("1994-11-03"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.pan").value("WERKC4227L"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.salaryEarned").value(33000))
				.andExpect(MockMvcResultMatchers.jsonPath("$.allowances").value(1400))
				.andExpect(MockMvcResultMatchers.jsonPath("$.pensionType").value("family"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.bank.name").value("SBI"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.bank.bankType").value("private"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.bank.accountNumber").value("1234567890"));

	}

	@Test
	void testGetAllPensioner() throws Exception {
		when(authorisingClient.authorizeTheRequest("Bearer @token@token"))
				.thenReturn(new TokenValidationResponse(true));
		mockMvc.perform(get("/api/v1/findAllPensionerDetails").header("Authorization", "Bearer @token@token"))
				.andExpect(status().isOk());
	}

	@Test
	void testGetAllPensionerWrongAuthorization() throws Exception {
		when(authorisingClient.authorizeTheRequest("Bearer @token@token"))
				.thenReturn(new TokenValidationResponse(false));
		mockMvc.perform(get("/api/v1/findAllPensionerDetails")
				.header("Authorization", "Bearer @token@token"))
				.andExpect(status().isForbidden());
	}

	@Test
	void testGetAllPensionerBadRequest() throws Exception {

		mockMvc.perform(get("/api/v1/findAllPensionerDetails"))
		.andExpect(status().isBadRequest());
	}

}
