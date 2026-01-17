package be.neostoffyn.campus.service;

import be.neostoffyn.campus.model.Campus;
import be.neostoffyn.campus.repository.CampusRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class CampusServiceSpringBootTest {

    @Autowired
    private CampusService campusService;

    @Autowired
    private CampusRepository campusRepository;

    @Test
    void saveCampus_persistsCampusInDatabase() {
        campusRepository.deleteAll();

        Campus campus = new Campus("Gent", "Straat 3", 30);

        Campus saved = campusService.saveCampus(campus);
        Campus found = campusRepository.findById("Gent").orElse(null);

        assertNotNull(saved);
        assertNotNull(found);
        assertEquals("Gent", found.getName());
    }
}

