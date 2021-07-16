package br.com.ferreira.snacks.service;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ferreira.snacks.controller.form.EmployeeForm;
import br.com.ferreira.snacks.exception.ImpossibleStartWorkException;
import br.com.ferreira.snacks.exception.UpdateWorkingStatusException;
import br.com.ferreira.snacks.model.Employee;
import br.com.ferreira.snacks.model.SnacksWork;
import br.com.ferreira.snacks.repository.SnacksWorkRepository;

@Service
public class SnacksWorkService {

	@Autowired
	private SnacksWorkRepository repository;

	@Autowired
	private EmployeeService employeeService;
	
	public List<SnacksWork> findAllWorks() {
		return repository.findAll();
	}

	public SnacksWork startWork() {
		Employee workingEmployee = employeeService.findWorkingEmployee();
		if(workingEmployee == null)
			throw new ImpossibleStartWorkException("Can't start working without Employee!");
		
		if(workingEmployee.isWorking())
			throw new ImpossibleStartWorkException("Already start working!");
		
		Employee firstEmployee = employeeService.findEmployeeSorted().get(0);
		employeeService.startWorkingStatus(firstEmployee.getId());
		return repository.save(new SnacksWork(firstEmployee.getId()));
	}

	public SnacksWork updateWork() {
		List<SnacksWork> listWorks = findAllWorks();
		if(listWorks.isEmpty())
			throw new UpdateWorkingStatusException("Can't update status without a previous SnacksWork!");

		SnacksWork actualWork = listWorks.get(listWorks.size()-1);
		Employee actualEmployeeWork = employeeService.findEmployeeDetails(actualWork.getIdWorkingEmployee());
		
		if(!actualWork.getDateStartWork().isBefore(LocalDateTime.now().minusDays(7L)))
			if(!actualEmployeeWork.isAusent())
				throw new UpdateWorkingStatusException("Can't update status because this week's not over yet");
		
		Long nextEmployeeId = employeeService
				.updateWorkingStatus(listWorks.get(listWorks.size()-1).getIdWorkingEmployee());
		return repository.save(new SnacksWork(nextEmployeeId));
	}
	
	public SnacksWork updateAusentStatus(Long idEmployeeAusent) {
		Employee ausentEmployee = employeeService.findEmployeeDetails(idEmployeeAusent);
		ausentEmployee = employeeService.updateEmployee(
				new EmployeeForm(ausentEmployee.getName(), ausentEmployee.isWorking(), 
						!ausentEmployee.isAusent()), idEmployeeAusent);
		
		if(ausentEmployee.isAusent()) {
			employeeService.updateRemoveEmployeeStatus(
					ausentEmployee.getPreviousEmployeeId(), ausentEmployee.getNextEmployeeId());
			if(ausentEmployee.isWorking())
				employeeService.updateWorkingStatus(idEmployeeAusent);
		}
		
		if(!ausentEmployee.isAusent())
			employeeService.updatePreviousNextEmployeeValues(ausentEmployee, employeeService.findLastEmployee());
		
		return updateWork();
	}

	public SnacksWork findActualWork() {
		List<SnacksWork> allWorks = findAllWorks();
		if(allWorks.isEmpty())
			throw new EntityNotFoundException();
		return allWorks.get(allWorks.size()-1);
	}

}
