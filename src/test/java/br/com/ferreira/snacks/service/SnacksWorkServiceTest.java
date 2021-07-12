package br.com.ferreira.snacks.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.ferreira.snacks.model.SnacksWork;
import br.com.ferreira.snacks.repository.SnacksWorkRepository;

@ExtendWith(MockitoExtension.class)
class SnacksWorkServiceTest {

	@Mock
	private SnacksWorkRepository repositoryMock;
	
	@InjectMocks
	private SnacksWorkService service;
	
	@InjectMocks
	private ObjectMapper mapper;
	
	private List<SnacksWork> listWorks;
	
	@BeforeEach
	void beforeEach() {
		this.listWorks = 
				Arrays.asList(new SnacksWork(1L, LocalDateTime.now().minusDays(7L), 5L),
							new SnacksWork(2L, LocalDateTime.now().minusDays(4L), 10L),
							new SnacksWork(3L, LocalDateTime.now(), 2L));
	}
	
	@Test
	void listAllWorks_shouldReturnListSnacksWork() throws Exception {
		when(repositoryMock.findAll()).thenReturn(this.listWorks);
		
		List<SnacksWork> returnedList =  service.findAllWorks();
	
		String expectedListString = mapper.writeValueAsString(this.listWorks);
		String returnedListString = mapper.writeValueAsString(returnedList);
		
		assertThat(expectedListString).isEqualToIgnoringWhitespace(returnedListString);
	}
	
}
