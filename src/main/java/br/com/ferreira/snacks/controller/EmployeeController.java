package br.com.ferreira.snacks.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ferreira.snacks.controller.dto.EmployeeDTO;
import br.com.ferreira.snacks.controller.form.EmployeeForm;
import br.com.ferreira.snacks.service.EmployeeService;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

	@Autowired
	private EmployeeService service;
		
	@GetMapping
	public ResponseEntity<List<EmployeeDTO>> findAllEmployees(){
		return ResponseEntity.ok(service.findAllEmployees().stream().map(EmployeeDTO::new).collect(Collectors.toList()));
	}
	
	@PostMapping
	@Transactional
	public ResponseEntity<EmployeeDTO> createEmployee(@RequestBody @Valid EmployeeForm form) {
		return ResponseEntity.ok(new EmployeeDTO(service.createEmployee(form)));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<EmployeeDTO> deleteEmployee(@PathVariable Long id) {
		return ResponseEntity.ok(new EmployeeDTO(service.deleteEmployee(id)));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<EmployeeDTO> updateEmployee(@PathVariable Long id, @RequestBody @Valid EmployeeForm updateForm) {
		return ResponseEntity.ok(new EmployeeDTO(service.updateEmployee(updateForm, id)));
	}
	
}



