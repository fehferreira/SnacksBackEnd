package br.com.ferreira.snacks.controller;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ferreira.snacks.exception.ImpossibleStartWorkException;
import br.com.ferreira.snacks.exception.UpdateWorkingStatusException;
import br.com.ferreira.snacks.model.SnacksWork;
import br.com.ferreira.snacks.service.SnacksWorkService;

@RestController
@RequestMapping("/work")
public class SnacksWorkController {

	@Autowired
	private SnacksWorkService service;
	
	@GetMapping
	public ResponseEntity<List<SnacksWork>> findAllWorks(){
		return ResponseEntity.ok(service.findAllWorks());
	}
	
	@GetMapping(path = "/start")
	@Transactional
	public ResponseEntity<SnacksWork> startWork(){
		try {
			return ResponseEntity.ok(service.startWork());
		}catch(ImpossibleStartWorkException exception) {
			return new ResponseEntity(exception.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping(path = "/update")
	@Transactional
	public ResponseEntity<SnacksWork> updateWork(){
		try {
			return ResponseEntity.ok(service.updateWork());
		}catch(UpdateWorkingStatusException exception) {
			return new ResponseEntity(exception.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@PutMapping(path = "/ausent/{idEmployeeAusent}")
	@Transactional
	public ResponseEntity<SnacksWork> updateAusentStatus(@PathVariable Long idEmployeeAusent){
		try {
			return ResponseEntity.ok(service.updateAusentStatus(idEmployeeAusent));
		}catch(UpdateWorkingStatusException exception) {
			return new ResponseEntity("Employee update! Had no change in working status!", HttpStatus.OK);
		}
	}
	
}
