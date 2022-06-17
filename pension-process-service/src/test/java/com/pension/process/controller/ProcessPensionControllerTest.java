package com.pension.process.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pension.process.exception.AadharNumberNotFound;
import com.pension.process.model.Bank;
import com.pension.process.model.PensionDetail;
import com.pension.process.model.PensionerDetail;
import com.pension.process.model.ProcessPensionInput;
import com.pension.process.model.TokenValidationResponse;
import com.pension.process.restClients.AuthClient;
import com.pension.process.restClients.PensionerDetailClient;
import com.pension.process.service.ProcessPensionServiceImpl;


@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
class ProcessPensionControllerTest {

	@MockBean
	private AuthClient authClient;

	@MockBean
	private PensionerDetailClient pensionerDetailClient;

	@MockBean
	private ProcessPensionServiceImpl processPensionServiceImpl;

	@Autowired
	private MockMvc mockMvc;

	@Test
	void testAuthorizeClientNotNull() {
		assertThat(authClient).isNotNull();
	}

	@Test
	void testPensionerDetailClientNotNull() {
		assertThat(pensionerDetailClient).isNotNull();
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
	void testGetResponseValidAuthorization() throws Exception {

		when(authClient.authorizeTheRequest("Bearer @token@token")).thenReturn(new TokenValidationResponse(true));
		ProcessPensionInput processPensionInput = new ProcessPensionInput(157389437816l);
		String jsonPensionerInput = this.mapToJson(processPensionInput);
		mockMvc.perform(post("/api/v1/ProcessPension").contentType("application/json").content(jsonPensionerInput)
				.header("Authorization", "Bearer @token@token")).andExpect(status().isOk());
	}

	@Test
	void testGetResponseInvalidAuthorization() throws Exception {
		when(authClient.authorizeTheRequest("InvalidToken")).thenReturn(new TokenValidationResponse(false));
		ProcessPensionInput processPensionInput = new ProcessPensionInput(420559429029l);
		String jsonPensionerInput = this.mapToJson(processPensionInput);
		mockMvc.perform(post("/api/v1/ProcessPension").contentType("application/json").content(jsonPensionerInput)
				.header("Authorization", "InvalidToken")).andExpect(status().isForbidden());

	}

	@Test
	void testGetResponseWithoutHeader() throws Exception {

		ProcessPensionInput processPensionInput = new ProcessPensionInput(157389437816l);
		String jsonPensionerInput = this.mapToJson(processPensionInput);
		mockMvc.perform(post("/api/v1/ProcessPension").contentType("application/json").content(jsonPensionerInput))
				.andExpect(status().isBadRequest());

	}

	@Test
	void testGetPensionDetailsInvalidAadharCard() throws Exception {

		when(authClient.authorizeTheRequest("Bearer @token@token")).thenReturn(new TokenValidationResponse(true));
		when(pensionerDetailClient.getPensionerDetailByAadhaar("Bearer @token@token", 157389437816l))
				.thenThrow(AadharNumberNotFound.class);
		ProcessPensionInput pensionerInput = new ProcessPensionInput(157389437816l);
		String jsonPensionerInput = this.mapToJson(pensionerInput);
		when(processPensionServiceImpl.processPension("Bearer @token@token", pensionerInput))
				.thenThrow(AadharNumberNotFound.class);

		mockMvc.perform(post("/api/v1/ProcessPension").contentType("application/json").content(jsonPensionerInput)
				.header("Authorization", "Bearer @token@token")).andExpect(status().isNotFound());

	}

	@Test
	void testGetPensionDetailsValidAadharCard() throws Exception {

		when(authClient.authorizeTheRequest("Bearer @token@token")).thenReturn(new TokenValidationResponse(true));
		Bank bank = new Bank("SBI", "private","4059586623");
		PensionerDetail pensionerDetail = new PensionerDetail(157389437816l, "Naveen", LocalDate.of(1997, 07, 13),
				"EKJPK2920N", 29000, 1200, "self", bank);
		when(pensionerDetailClient.getPensionerDetailByAadhaar("Bearer @token@token", 157389437816l))
				.thenReturn(pensionerDetail);
		ProcessPensionInput pensionerInput = new ProcessPensionInput(157389437816l);
		PensionDetail pensionDetail = new PensionDetail();
		pensionDetail.setBankServiceCharge(550);
		pensionDetail.setPensionAmount(24900);
		when(processPensionServiceImpl.processPension("Bearer @token@token", pensionerInput))
				.thenReturn(pensionDetail);
		String jsonPensionerInput = this.mapToJson(pensionerInput);
		mockMvc.perform(post("/api/v1/ProcessPension").contentType("application/json").content(jsonPensionerInput)
				.header("Authorization", "Bearer @token@token")).andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.pensionAmount").value(24900))
				.andExpect(jsonPath("$.bankServiceCharge").value(550));
	}

}
