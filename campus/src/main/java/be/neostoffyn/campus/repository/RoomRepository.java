package be.neostoffyn.campus.repository;

import be.neostoffyn.campus.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByCampus_Name(String campusName);

    boolean existsByNameAndCampusName(String name, String campusName);
}
