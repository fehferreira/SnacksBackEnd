package br.com.ferreira.snacks.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "employee")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Employee {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull(message = "This value cant be NULL")
	private String name;
	
	private boolean isWorking = false;
	
	private boolean isAusent = false;
	
	private Long previousEmployeeId;
	
	private Long nextEmployeeId;
	
	@OneToMany
	private List<SnacksWork> realizedWorks;
}
