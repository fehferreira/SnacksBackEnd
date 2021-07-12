package br.com.ferreira.snacks.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
		
		Employee firstEmployee = employeeService.findAllEmployees().get(0);
		employeeService.startWorkingStatus(firstEmployee.getId());
		return repository.save(new SnacksWork(firstEmployee.getId()));
	}

	public SnacksWork updateWork() {
		List<SnacksWork> listWorks = findAllWorks();
		if(listWorks.isEmpty())
			throw new UpdateWorkingStatusException("Can't update status without a previous SnacksWork!");
		

		if(!listWorks.get(listWorks.size()-1).getDateStartWork().isBefore(LocalDateTime.now().minusDays(7L)))
			throw new UpdateWorkingStatusException("Can't update status because this week's not over yet");
		
		Long nextEmployeeId = employeeService
				.updateWorkingStatus(listWorks.get(listWorks.size()-1).getIdWorkingEmployee());
		return repository.save(new SnacksWork(nextEmployeeId));
	}
	
}
