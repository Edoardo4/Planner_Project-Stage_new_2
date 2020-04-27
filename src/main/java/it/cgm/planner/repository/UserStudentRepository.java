package it.cgm.planner.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import it.cgm.planner.model.User;
import it.cgm.planner.model.UserStudent;

public interface UserStudentRepository extends JpaRepository<UserStudent, Long> {


}
