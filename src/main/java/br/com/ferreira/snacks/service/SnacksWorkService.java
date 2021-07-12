package br.com.ferreira.snacks.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ferreira.snacks.repository.SnacksWorkRepository;

@Service
public class SnacksWorkService {

	@Autowired
	private SnacksWorkRepository repository;
	
}
