package it.cgm.planner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.cgm.planner.model.Role;
import it.cgm.planner.model.RoleName;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	
    Optional<Role> findByName(RoleName roleName);
    
    Boolean existsByName(RoleName roleName);
}
