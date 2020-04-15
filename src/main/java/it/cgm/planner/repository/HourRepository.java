package it.cgm.planner.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import it.cgm.planner.model.Hour;
import it.cgm.planner.model.HourName;

public interface HourRepository extends JpaRepository<Hour, Long>{


	Hour findByname(HourName name);


}
