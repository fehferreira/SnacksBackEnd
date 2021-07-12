package br.com.ferreira.snacks.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.ferreira.snacks.model.SnacksWork;

@Repository
public interface SnacksWorkRepository extends JpaRepository<SnacksWork, Long> {

}
