package it.cgm.planner.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import it.cgm.planner.model.Argument;

public interface ArgumentRepository extends JpaRepository<Argument, Long>{

	List<Argument> findByisValidTrueOrderByName();

	  Boolean existsByCode(String code);

	  Boolean existsByName(String name);
}
