package be.neostoffyn.campus.service;

import be.neostoffyn.campus.exception.Campus.CampusAlreadyExistsException;
import be.neostoffyn.campus.model.Campus;
import be.neostoffyn.campus.repository.CampusRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CampusServiceTest {

    private CampusRepository campusRepository;
    private CampusService campusService;

    @BeforeEach
    void setUp() {
        campusRepository = mock(CampusRepository.class); // Mockito mock
        campusService = new CampusService(campusRepository);
    }

    @Test
    void saveCampus_shouldSaveCampus_whenValid() {
        Campus campus = new Campus("Brugge", "Straat 1", 50);

        when(campusRepository.existsCampusByName("Brugge")).thenReturn(false);
        when(campusRepository.save(campus)).thenReturn(campus);

        Campus saved = campusService.saveCampus(campus);

        assertEquals("Brugge", saved.getName());
        verify(campusRepository, times(1)).save(campus);
    }

    @Test
    void saveCampus_shouldThrowException_whenCampusAlreadyExists() {
        Campus campus = new Campus("Brugge", "Straat 1", 50);

        when(campusRepository.existsCampusByName("Brugge")).thenReturn(true);

        assertThrows(CampusAlreadyExistsException.class, () ->
                campusService.saveCampus(campus)
        );

        verify(campusRepository, never()).save(any());
    }
}
