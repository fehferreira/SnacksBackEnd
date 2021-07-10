package br.com.ferreira.snacks.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ferreira.snacks.model.Employee;
import br.com.ferreira.snacks.repository.EmployeeRepository;

@Service
public class EmployeeService {

	@Autowired
	private EmployeeRepository repository;
	
	public List<Employee> findAllEmployees(){
		return repository.findAll(); 
	}
	
}
