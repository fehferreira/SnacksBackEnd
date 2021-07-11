package br.com.ferreira.snacks.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

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
import br.com.ferreira.snacks.repository.EmployeeRepository;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

	@Mock
	private EmployeeRepository repositoryMock;
	
	@InjectMocks
	private EmployeeService service;
	
	@InjectMocks
	private ObjectMapper mapper;
	
	private List<Employee> listEmployees;

	@BeforeEach
	void beforeEach() {
		Employee employee1 = new Employee(1L,"Felipe Ferreira", false, false, null);
		Employee employee2 = new Employee(15L,"Felipe Ferreira", false, false, employee1.getId());
		Employee employee3 = new Employee(34L,"Felipe Ferreira", false, false, employee2.getId());
	
		this.listEmployees = Arrays.asList(employee1,employee2,employee3);
	}
	
	@Test
	void findAllEmployees_shouldReturnListEmployees() throws Exception {
		when(repositoryMock.findAll()).thenReturn(this.listEmployees);
		
		List<Employee> returnListEmployee = service.findAllEmployees();
	
		String returnedList = mapper.writeValueAsString(returnListEmployee);
		String listToCompare = mapper.writeValueAsString(this.listEmployees);
		
		assertThat(returnedList).isEqualToIgnoringWhitespace(listToCompare);
	}
}
