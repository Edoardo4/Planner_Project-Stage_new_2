package it.cgm.planner.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.cgm.planner.model.Material;

public interface MaterialRepository extends JpaRepository<Material, Long>{

}
