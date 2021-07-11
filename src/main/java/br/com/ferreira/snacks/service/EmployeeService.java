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
		
		return updateWorkingStatus(createEmployee);
	}
	
	public Employee updateWorkingStatus(Employee createEmployee){
		Employee working = findWorkingEmployee();
		createEmployee.setNextEmployeeId(working.getNextEmployeeId());
		createEmployee = repository.save(createEmployee);
		working.setNextEmployeeId(createEmployee.getId());
		return createEmployee;
	}
		
	private Employee findWorkingEmployee() {
		List<Employee> listEmployees = findAllEmployees();
		for(Employee employee : listEmployees) {
			if(employee.isWorking()) {
				return repository.getById(employee.getId());
			}
		}
		return repository.getById(listEmployees.get(listEmployees.size()-1).getId());
	}
	
	
	
	
	
	
}
