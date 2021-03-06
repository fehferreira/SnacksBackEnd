package br.com.ferreira.snacks.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
		return repository.getById(findLastEmployee().getId());
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
	
	public Long updateWorkingStatus(Long idWorkingEmployee) {
		Employee actualWorkingEmployee = repository.getById(idWorkingEmployee);
		
		Employee nextWorkingEmployee = null;
		
		if(actualWorkingEmployee.getNextEmployeeId() == null)
			nextWorkingEmployee = findEmployeeSorted().get(0);
		else
			nextWorkingEmployee = repository.getById(actualWorkingEmployee.getNextEmployeeId());
		
		actualWorkingEmployee.setWorking(false);
		nextWorkingEmployee.setWorking(true);
		return nextWorkingEmployee.getId();
	}

	public void startWorkingStatus(Long id) {
		repository.getById(id).setWorking(true);		
	}
	
	public void updateRemoveEmployeeStatus(Long previousEmployeeId, Long nextEmployeeId) {
		if(previousEmployeeId != null)
			repository.getById(previousEmployeeId).setNextEmployeeId(nextEmployeeId);
		if(nextEmployeeId != null)
			repository.getById(nextEmployeeId).setPreviousEmployeeId(previousEmployeeId);
	}
	
	public Employee findLastEmployee() {
		List<Employee> listEmployees = findEmployeeSorted();
		Optional<Employee> optional = listEmployees.stream().filter(e -> (e.getNextEmployeeId() == null) && 
				(e.getPreviousEmployeeId() != null)).findFirst();
		
		return optional.isPresent() ? optional.get() : listEmployees.get(listEmployees.size()-1);
	}
	
	public Employee updatePreviousNextEmployeeValues(Employee updateEmployee, Employee workingEmployee) {
		
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
	
	private Employee includeNewEmployeeOnLinkedList(Employee createEmployee){
		Employee working = findWorkingEmployee();
		
		if(working == null)
			return repository.save(createEmployee);
		
		return updatePreviousNextEmployeeValues(createEmployee, working);
	}

	public List<Employee> findEmployeeSorted() {
		List<Employee> sortedList = findAllEmployees().stream().filter(e -> e.isAusent() == false)
				.collect(Collectors.toList());
		
		sortedList.sort((e1,e2)->e1.compareTo(e2));
		sortedList.sort((e1,e2)->e2.compareTo(e1));
		
		return sortedList;
	}

	public List<Employee> findEmployeeAusent() {
		return findAllEmployees().stream().filter(e -> e.isAusent() == true)
				.collect(Collectors.toList());
	}
}
