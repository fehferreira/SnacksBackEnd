package br.com.ferreira.snacks.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.ferreira.snacks.controller.form.EmployeeForm;
import br.com.ferreira.snacks.exception.EntityPresentException;
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
	
	
	@Test
	void findEmployeeDetails_shouldReturnAEmployee() {
		Employee employee = this.listEmployees.get(0);
		
		when(repositoryMock.findById(employee.getId())).thenReturn(Optional.of(employee));
		
		Employee returnedEmployee = service.findEmployeeDetails(employee.getId());
	
		assertEquals(returnedEmployee.getId(), employee.getId());
		assertEquals(returnedEmployee.getName(), employee.getName());
	}
	
	@Test
	void findEmployeeDetails_shouldThrowException_EntityNotFound() {
		when(repositoryMock.findById(any())).thenReturn(Optional.empty());
		
		try {
			service.findEmployeeDetails(1L);
		}catch (RuntimeException exception) {
			assertThat(exception.getClass()).isEqualTo(EntityNotFoundException.class);
		}
		
		Mockito.verify(repositoryMock).findById(1L);
	}
	
	@Test
	void createEmployee_shouldReturnEmployeeCreated() {
		EmployeeForm form = new EmployeeForm("Felipe Ferreira", false, false);
		Employee createdEmployee = form.getEmployee();
		createdEmployee.setId(1L);
		Employee workingEmployee = new Employee(1L,"Nathalica Goncalves", true, false, null);
	
		when(repositoryMock.findByName(createdEmployee.getName())).thenReturn(Optional.empty());
		when(repositoryMock.findAll()).thenReturn(this.listEmployees);
		when(repositoryMock.getById(any())).thenReturn(workingEmployee);
		when(repositoryMock.save(any())).thenReturn(createdEmployee);
		
		Employee returnedEmployee = service.createEmployee(form);
		
		assertEquals(returnedEmployee.getId(), createdEmployee.getId());
		assertEquals(returnedEmployee.getName(), createdEmployee.getName());
	}
	
	@Test
	void createEmployee_shouldThrowException_EmployeeExist() {
		EmployeeForm form = new EmployeeForm("Felipe Ferreira", false, false);
		Employee createdEmployee = form.getEmployee();
		createdEmployee.setId(1L);
	
		when(repositoryMock.findByName(createdEmployee.getName()))
			.thenReturn(Optional.of(createdEmployee));
		
		try {
			service.createEmployee(form);
		}catch (Exception exception) {
			assertThat(exception.getClass()).isEqualTo(EntityPresentException.class);
		}
		
		Mockito.verify(repositoryMock).findByName(createdEmployee.getName());
	}
	
}
