package br.com.ferreira.snacks.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ferreira.snacks.exception.ImpossibleStartWorkException;
import br.com.ferreira.snacks.model.SnacksWork;
import br.com.ferreira.snacks.service.SnacksWorkService;

@RestController
@RequestMapping("/work")
public class SnacksWorkController {

	@Autowired
	private SnacksWorkService service;
	
	@GetMapping(path = "/start")
	public ResponseEntity<SnacksWork> startWork(){
		try {
			return ResponseEntity.ok(service.startWork());
		}catch(ImpossibleStartWorkException exception) {
			return new ResponseEntity(exception.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
}
