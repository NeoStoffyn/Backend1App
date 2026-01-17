package be.neostoffyn.campus.controller;

import be.neostoffyn.campus.model.Campus;
import be.neostoffyn.campus.repository.CampusRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CampusHttpIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CampusRepository campusRepository;

    @Test
    void getAllCampuses_returnsCampusList() throws Exception {
        campusRepository.deleteAll();
        campusRepository.save(new Campus("Brugge", "Straat 1", 10));

        mockMvc.perform(get("/campus"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Brugge"));
    }
}

