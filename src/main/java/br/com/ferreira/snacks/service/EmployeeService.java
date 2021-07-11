package br.com.ferreira.snacks.service;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ferreira.snacks.controller.form.EmployeeForm;
import br.com.ferreira.snacks.exception.EntityPresentException;
import br.com.ferreira.snacks.model.Employee;
import br.com.ferreira.snacks.repository.EmployeeRepository;

@Service
public class EmployeeService {

	@Autowired
	private EmployeeRepository repository;
	
	public List<Employee> findAllEmployees(){
		return repository.findAll(); 
	}
	
	public Employee findEmployeeDetails(Long idEmployee) {
		Optional<Employee> optional = repository.findById(idEmployee);
		if(optional.isPresent())
			return optional.get();
		throw new EntityNotFoundException();
	}
	
	public Employee createEmployee(EmployeeForm form) {
		Employee createEmployee = form.getEmployee();
	
		Optional<Employee> optional = repository.findByName(createEmployee.getName());
		if(optional.isPresent()) {
			throw new EntityPresentException();
		}
		
		return repository.save(createEmployee);
	}
	
	
	
	
	
	
}
