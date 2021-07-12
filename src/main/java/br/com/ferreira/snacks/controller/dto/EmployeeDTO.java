package br.com.ferreira.snacks.controller.dto;

import org.springframework.beans.BeanUtils;

import br.com.ferreira.snacks.model.Employee;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDTO {

	private Long id;	
	private String name;
	private boolean isWorking = false;
	private boolean isAusent = false;
	private Long nextEmployeeId;
	
	public EmployeeDTO(Employee employee) {
		BeanUtils.copyProperties(employee, this);
	}
	
}
