package br.com.ferreira.snacks.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import br.com.ferreira.snacks.exception.ImpossibleStartWorkException;
import br.com.ferreira.snacks.exception.UpdateWorkingStatusException;
import br.com.ferreira.snacks.model.SnacksWork;
import br.com.ferreira.snacks.service.SnacksWorkService;

@WebMvcTest(SnacksWorkController.class)
@ActiveProfiles("test")
class SnacksWorkControllerTest {

	@MockBean
	private SnacksWorkService serviceMock;
	
	@Autowired
	private MockMvc mockMvc;

	private final String uri = "/work";
	
	@Test
	void startWork_shouldReturn200AndWorkingEmployee() throws Exception {
		LocalDateTime nowDate = LocalDateTime.now();
		when(serviceMock.startWork()).thenReturn(new SnacksWork(2L, nowDate, 63L));
		
		mockMvc.perform(get(uri + "/start"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.idWork", is(2)))
				.andExpect(jsonPath("$.dateStartWork", is(nowDate.toString())))
				.andExpect(jsonPath("$.idWorkingEmployee", is(63)));
	}
	
	@Test
	void startWork_shouldReturn400AndException_ImpossibleStartWorking() throws Exception {
		when(serviceMock.startWork())
			.thenThrow(new ImpossibleStartWorkException("Can't start working without Employee!"));
		
		mockMvc.perform(get(uri + "/start"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$", is("Can't start working without Employee!")));
	}
	
	@Test
	void updateWork_shouldReturn200AndNewSnacksWork() throws Exception{
		LocalDateTime nowDate = LocalDateTime.now();
		when(serviceMock.updateWork()).thenReturn(new SnacksWork(2L, nowDate , 2L));
		
		mockMvc.perform(get(uri + "/update"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.idWork", is(2)))
				.andExpect(jsonPath("$.dateStartWork", is(nowDate.toString())))
				.andExpect(jsonPath("$.idWorkingEmployee", is(2)));
	}
	
	@Test
	void updateWork_shouldReturn400AndException_UpdateWorkingStatusException() throws Exception {
		when(serviceMock.updateWork())
			.thenThrow(new UpdateWorkingStatusException("Can't update status without a previous SnacksWork!"));
		
		mockMvc.perform(get(uri + "/update"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$", is("Can't update status without a previous SnacksWork!")));
	}
	
}
