package br.com.ferreira.snacks.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "work")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SnacksWork {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idWork;
	
	private LocalDateTime dateStartWork;
	
	private Long idWorkingEmployee;
	
}
