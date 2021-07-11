package br.com.ferreira.snacks.controller.form;

import br.com.ferreira.snacks.model.Employee;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeForm {

	private String name;
	private boolean isWorking = false;
	private boolean isAusent = false;
	
	public Employee getEmployee() {
		return new Employee(null, this.name, this.isWorking, this.isAusent, null);
	}
	
}
