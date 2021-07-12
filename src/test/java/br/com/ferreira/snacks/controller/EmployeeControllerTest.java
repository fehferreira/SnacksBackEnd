package br.com.ferreira.snacks.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.ferreira.snacks.controller.form.EmployeeForm;
import br.com.ferreira.snacks.model.Employee;
import br.com.ferreira.snacks.service.EmployeeService;

@WebMvcTest(value = EmployeeController.class)
@ActiveProfiles("test")
class EmployeeControllerTest {
	
	@Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;
    
    @MockBean
    private EmployeeService serviceMock;
	
    private final String uri = "/employee";

	private List<Employee> listEmployee;
    
    @BeforeEach
    void beforeEach() {
    	Employee employee1 = new Employee(1L,"Felipe Ferreira", false, false, null);
    	Employee employee2 = new Employee(5L,"Nathalia Goncalves", false, false, employee1.getId());
    	Employee employee3 = new Employee(7L,"Rogerio Marinho", false, false, employee2.getId());
    	
    	this.listEmployee = Arrays.asList(employee1, employee2, employee3);
    }
    
    @Test 
    void findAllEmployees_shouldReturn200_ListEmployee() throws Exception {
    	String expectedReturnJSON = mapper.writeValueAsString(listEmployee);
    	
    	when(serviceMock.findAllEmployees()).thenReturn(this.listEmployee);
    	
    	MvcResult returnedRequest = mockMvc.perform(get(uri)
    					.contentType(MediaType.APPLICATION_JSON))
    			.andExpect(status().isOk())
    			.andReturn();
    	
    	String returnedStringJSON = returnedRequest.getResponse().getContentAsString();
    	
    	assertThat(returnedStringJSON).isEqualToIgnoringWhitespace(expectedReturnJSON);
    }
    
    @Test
    void createEmployee_shouldReturn200AndEmployeeCreated() throws Exception {
    	EmployeeForm form = new EmployeeForm("Felipe Ferreira", false, false);
    	Employee employeeCreated = form.getEmployee();
    	employeeCreated.setId(1L);
    	
    	when(serviceMock.createEmployee(any())).thenReturn(employeeCreated);
    
    	mockMvc.perform(post(uri)
    					.contentType(MediaType.APPLICATION_JSON)
    					.content(mapper.writeValueAsString(form)))
    			.andExpect(status().isOk())
    			.andExpect(jsonPath("$.id", is(employeeCreated.getId().intValue())))
    			.andExpect(jsonPath("$.name", is(employeeCreated.getName())));
    }
    
    @Test
    void deleteEmployee_shouldReturnEmployeeDeleted() throws Exception {
    	Employee employeeDeleted = this.listEmployee.get(0);
    	
    	when(serviceMock.findEmployeeDetails(employeeDeleted.getId())).thenReturn(employeeDeleted);
    	when(serviceMock.deleteEmployee(any())).thenReturn(employeeDeleted);
    	
    	mockMvc.perform(delete(uri + "/" + employeeDeleted.getId().toString())
    					.contentType(MediaType.APPLICATION_JSON))
    			.andExpect(status().isOk())
    			.andExpect(jsonPath("$.id", is(employeeDeleted.getId().intValue())))
    			.andExpect(jsonPath("$.name", is(employeeDeleted.getName())));
    }
    
    @Test
    void updateEmployee_shouldReturnUpdatedEmployee() throws Exception {
    	EmployeeForm updateForm = new EmployeeForm("Felipe Ferreira", false, false);
    	Employee oldEmployee = this.listEmployee.get(2);
    	Employee updateEmployee = updateForm.getEmployee();
    	updateEmployee.setId(oldEmployee.getId());
    	
    	
    	when(serviceMock.updateEmployee(any(), any()))
    			.thenReturn(updateEmployee);
    	
    	mockMvc.perform(put(uri + "/" + oldEmployee.getId().toString())
    					.contentType(MediaType.APPLICATION_JSON)
    					.content(mapper.writeValueAsString(updateForm)))
    			.andExpect(status().isOk())
    			.andExpect(jsonPath("$.id", is(oldEmployee.getId().intValue())))
    			.andExpect(jsonPath("$.name", is(updateEmployee.getName())));
    	
    }
    
    
    
    
    
    
    
    
}
