package br.com.ferreira.snacks.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
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

import br.com.ferreira.snacks.model.Employee;
import br.com.ferreira.snacks.model.SnacksWork;
import br.com.ferreira.snacks.repository.SnacksWorkRepository;

@ExtendWith(MockitoExtension.class)
class SnacksWorkServiceTest {

	@Mock
	private SnacksWorkRepository repositoryMock;
	
	@Mock
	private EmployeeService serviceMock;
	
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
	
	@Test
	void startWork_callWhenListWorkIsEmpty_shouldReturnFirstSnacksWork() {
		SnacksWork snacksWork = this.listWorks.get(0);
		Employee employee = new Employee(5L, "Felipe Ferreira", false, false, null, null);
		
		when(repositoryMock.save(any())).thenReturn(snacksWork);
		when(serviceMock.findAllEmployees()).thenReturn(Arrays.asList(employee));
		when(serviceMock.findWorkingEmployee()).thenReturn(employee);
		
		SnacksWork actualWork = service.startWork();
		
		assertEquals(actualWork.getIdWork(), snacksWork.getIdWork());
		assertEquals(actualWork.getDateStartWork().toLocalDate(), LocalDateTime.now().minusDays(7L).toLocalDate());
		assertEquals(actualWork.getIdWorkingEmployee(), employee.getId());
	}
	
	@Test
	void updateWork_callAfter7DaysOfLastWork_shouldReturnNewSnacksWork() {
		List<SnacksWork> listWorks = Arrays.asList(this.listWorks.get(0));
		Employee actualEmployee = new Employee(5L, "Felipe Ferreira", true, false, null, 6L);
		Employee nextEmployee = new Employee(6L,"Ricardo Maldonado", true, false, null, 7L);
		SnacksWork newSnacksWork = new SnacksWork(2L, LocalDateTime.now(), nextEmployee.getId());
		
		when(repositoryMock.findAll()).thenReturn(listWorks);
		when(serviceMock.updateWorkingStatus(actualEmployee.getId())).thenReturn(nextEmployee.getId());
		when(repositoryMock.save(any())).thenReturn(newSnacksWork);
		
		SnacksWork newWork = service.updateWork();
		
		assertEquals(newWork.getIdWork(), 2L);
		assertEquals(newWork.getDateStartWork(), newSnacksWork.getDateStartWork());
		assertEquals(newWork.getIdWorkingEmployee(), 6L);
	}
	
	
	
	
	
	
}
