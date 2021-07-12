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
	
	public Employee createEmployee(EmployeeForm form) {
		Employee createEmployee = form.getEmployee();
	
		Optional<Employee> optional = repository.findByName(createEmployee.getName());
		if(optional.isPresent()) {
			throw new EntityPresentException();
		}
		
		return includeNewEmployeeOnLinkedList(createEmployee);
	}
	
	public List<Employee> findAllEmployees(){
		return repository.findAll(); 
	}
	
	public Employee findEmployeeDetails(Long idEmployee) {
		Optional<Employee> optional = repository.findById(idEmployee);
		if(optional.isPresent())
			return optional.get();
		throw new EntityNotFoundException();
	}
		
	public Employee findWorkingEmployee() {
		List<Employee> listEmployees = findAllEmployees();
		for(Employee employee : listEmployees) {
			if(employee.isWorking()) {
				return repository.getById(employee.getId());
			}
		}
		if(listEmployees.isEmpty())
			return null;
		return repository.getById(listEmployees.get(listEmployees.size()-1).getId());
	}

	public Employee deleteEmployee(Long id) {
		Employee deletedEmployee = findEmployeeDetails(id);
		updateRemoveEmployeeStatus(deletedEmployee.getPreviousEmployeeId(), deletedEmployee.getNextEmployeeId());
		repository.delete(deletedEmployee);
		return deletedEmployee;
	}

	public Employee updateEmployee(EmployeeForm updateForm, Long id) {
		Employee employee = updateForm.getEmployee();
		Employee updateEmployee = repository.getById(id);
		
		copyProperties(employee, updateEmployee);
		
		return updateEmployee;
	}

	
	private Employee includeNewEmployeeOnLinkedList(Employee createEmployee){
		Employee working = findWorkingEmployee();
		
		if(working == null)
			return repository.save(createEmployee);
		
		return updatePreviousNextEmployeeValues(createEmployee, working);
	}
	
	private Employee updatePreviousNextEmployeeValues(Employee updateEmployee, Employee workingEmployee) {
		
		if(updateEmployee.getId() == null)
			updateEmployee = repository.save(updateEmployee);
		
		updateEmployee.setNextEmployeeId(workingEmployee.getNextEmployeeId());
		workingEmployee.setNextEmployeeId(updateEmployee.getId());
		updateEmployee.setPreviousEmployeeId(workingEmployee.getId());
		
		if(updateEmployee.getNextEmployeeId() != null) {
			Employee nextEmployee = repository.getById(updateEmployee.getNextEmployeeId());
			nextEmployee.setPreviousEmployeeId(updateEmployee.getId());
		}
		
		return updateEmployee;
	}
	
	private void copyProperties(Employee employee, Employee updateEmployee) {
		if(!employee.getName().equals(updateEmployee.getName()))
			updateEmployee.setName(employee.getName());
		if(employee.isWorking() != updateEmployee.isWorking())
			updateEmployee.setWorking(employee.isWorking());
		if(employee.isAusent() != updateEmployee.isAusent())
			updateEmployee.setAusent(employee.isAusent());
	}
	
	private void updateRemoveEmployeeStatus(Long previousEmployeeId, Long nextEmployeeId) {
		if(previousEmployeeId != null)
			repository.getById(previousEmployeeId).setNextEmployeeId(nextEmployeeId);
		if(nextEmployeeId != null)
			repository.getById(nextEmployeeId).setPreviousEmployeeId(previousEmployeeId);
	}

	public Long updateWorkingStatus(Long idWorkingEmployee) {
		Employee actualWorkingEmployee = repository.getById(idWorkingEmployee);
		
		Employee nextWorkingEmployee = null;
		
		if(actualWorkingEmployee.getNextEmployeeId() == null)
			nextWorkingEmployee = findAllEmployees().get(0);
		else
			nextWorkingEmployee = repository.getById(actualWorkingEmployee.getNextEmployeeId());
		
		actualWorkingEmployee.setWorking(false);
		nextWorkingEmployee.setWorking(true);
		return nextWorkingEmployee.getId();
	}
}
