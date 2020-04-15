package it.cgm.planner.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import it.cgm.planner.model.Group;

import it.cgm.planner.model.UserStudent;

public interface GroupRepository extends JpaRepository<Group, Long>{

	List<Group> findByisValidTrueOrderByName();

	Boolean existsByName(String name);

	Boolean existsByusers(Optional<UserStudent> user);

	List<UserStudent> findByIdIn(List<Long> idUsers);
	
}
