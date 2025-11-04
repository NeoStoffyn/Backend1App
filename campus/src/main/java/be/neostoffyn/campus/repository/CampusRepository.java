package be.neostoffyn.campus.repository;

import be.neostoffyn.campus.model.Campus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampusRepository extends JpaRepository<Campus, String> {
    boolean existsCampusByName(String name);
}
