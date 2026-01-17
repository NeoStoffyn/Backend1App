package be.neostoffyn.campus.repository;

import be.neostoffyn.campus.model.Campus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class CampusRepositoryTest {

    @Autowired
    private CampusRepository campusRepository;

    @Test
    void existsCampusByName_returnsTrueWhenCampusSaved() {
        Campus campus = new Campus("Antwerpen", "Straat 2", 20);
        campusRepository.save(campus);

        boolean exists = campusRepository.existsCampusByName("Antwerpen");

        assertTrue(exists);
    }
}

