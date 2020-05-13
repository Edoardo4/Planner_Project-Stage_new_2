package it.cgm.planner.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import it.cgm.planner.model.UserProfessor;

public interface UserProfessorRepository extends JpaRepository<UserProfessor, Long>{

    Optional<UserProfessor> findByUsername(String username);

    
}
