package br.com.ferreira.snacks.controller;

import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ferreira.snacks.controller.form.EmployeeForm;
import br.com.ferreira.snacks.model.Employee;
import br.com.ferreira.snacks.service.EmployeeService;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

	@Autowired
	private EmployeeService service;
		
	@GetMapping
	public List<Employee> findAllEmployees(){
		return service.findAllEmployees();
	}
	
	@PostMapping
	@Transactional
	public Employee createEmployee(@RequestBody @Valid EmployeeForm form) {
		return service.createEmployee(form);
	}
	
	@DeleteMapping
	@GetMapping("/${id}")
	public Employee deleteEmployee(@PathVariable Long id) {
		return service.deleteEmployee(id);
	}
	
	@PutMapping
	public Employee updateEmployee(@PathVariable Long id, @RequestBody @Valid EmployeeForm updateForm) {
		return service.updateEmployee(updateForm, id);
	}
	
}



