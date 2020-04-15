package it.cgm.planner.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.cgm.planner.model.Role;
import it.cgm.planner.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
    Optional<User> findByEmail(String email);
    Optional<User> findByUsernameOrEmail(String username, String email);
    
    List<User> findByIdIn(List<Long> userIds);

    Optional<User> findByUsername(String username);
    Optional<User> findByLastname(String lastname);
    Optional<User> findById(Long id);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
    
    long countByNameAndLastname(String name, String lastname);
   
    List<User> findByroles(Optional<Role> role);
    
    
}
