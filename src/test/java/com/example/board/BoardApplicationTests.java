package com.example.board;

import com.example.board.rest.dto.person.PersonRegisterDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.Base64Utils;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@EnabledIf(expression = "#{environment['spring.profiles.active'] == 'integration'}", loadContext = true)
@SpringBootTest()
@AutoConfigureMockMvc
class BoardApplicationTests {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	Logger logger = LoggerFactory.getLogger(BoardApplicationTests.class);
	@Value("${spring.datasource.url}")
	String db;
	@Value("${api.path}")
	String api;

	String adminAuthorization = "Basic " + Base64Utils.encodeToString("admin:adminpassword".getBytes(StandardCharsets.UTF_8));

	@Test
	void correctDbIsLinked() throws Exception{
		logger.warn("Used DB: " + db);
		assertTrue(db.contains("board_test"), "Test DB should be used!");
	}

	@Test
	void canRegisterPersonWithoutAuthentication() throws Exception {
		this.mockMvc.perform(
				post(api+"/persons/register")
					.content(objectMapper.writeValueAsString(new PersonRegisterDto("Konstantin", "secret")))
						.contentType(MediaType.APPLICATION_JSON)
				)
				.andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$").isNumber());
	}

	@Test
	void canGetProjectsWithAuthentication() throws Exception {
		this.mockMvc.perform(get(api+"/projects")
				.header(HttpHeaders.AUTHORIZATION, adminAuthorization))
				.andDo(print()).andExpect(status().isOk());
	}


}
