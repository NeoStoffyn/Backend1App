package be.neostoffyn.campus.service;

import be.neostoffyn.campus.exception.Campus.CampusAlreadyExistsException;
import be.neostoffyn.campus.exception.Campus.CampusInvalidDataException;
import be.neostoffyn.campus.exception.Campus.CampusNotFoundException;
import be.neostoffyn.campus.model.Campus;
import be.neostoffyn.campus.repository.CampusRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CampusService {

    private final CampusRepository campusRepository;

    public CampusService(CampusRepository campusRepository) {
        this.campusRepository = campusRepository;
    }

    public List<Campus> getAllCampuses() {
        return campusRepository.findAll();
    }

    public Campus getCampusByName(String name) {
        return campusRepository.findById(name)
                .orElseThrow(() -> new CampusNotFoundException("Campus not found: " + name));
    }

    public Campus saveCampus(Campus campus) {
        final String campusName = campus.getName();

        if (campusName == null || campusName.isBlank())
            throw new CampusInvalidDataException("Campus name is invalid");

        if (campus.getAddress() == null || campus.getAddress().isBlank()) {
            throw new CampusInvalidDataException("Campus address is required");
        }

        if (campusRepository.existsCampusByName(campusName))
            throw new CampusAlreadyExistsException("Campus already exists: " + campusName);

        return campusRepository.save(campus);
    }
}
