package it.cgm.planner.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.cgm.planner.model.Room;

public interface RoomRepository extends JpaRepository<Room, Long> {

	boolean existsByName(String name);

}
