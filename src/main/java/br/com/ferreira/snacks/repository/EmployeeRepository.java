package br.com.ferreira.snacks.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.ferreira.snacks.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}
