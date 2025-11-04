package be.neostoffyn.campus.controller;

import be.neostoffyn.campus.error.FieldMessage;
import be.neostoffyn.campus.exception.Campus.CampusAlreadyExistsException;
import be.neostoffyn.campus.exception.Campus.CampusInvalidDataException;
import be.neostoffyn.campus.exception.Campus.CampusNotFoundException;
import be.neostoffyn.campus.model.Campus;
import be.neostoffyn.campus.service.CampusService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/campus")
public class CampusController {

    private final CampusService campusService;

    public CampusController(CampusService campusService) {
        this.campusService = campusService;
    }

    // GET:/campus
    @GetMapping
    public ResponseEntity<List<Campus>> getAllCampuses() {
        return ResponseEntity.ok(campusService.getAllCampuses());
    }

    // GET: /campus/{name}
    @GetMapping("/{name}")
    public ResponseEntity<Campus> getCampus(@PathVariable String name) {
        Campus campus = campusService.getCampusByName(name);
        return ResponseEntity.ok(campus);
    }



    // POST: /campus
    @PostMapping
    public ResponseEntity<Campus> addCampus(@RequestBody Campus campus) {
        Campus savedCampus = campusService.saveCampus(campus);
        return new ResponseEntity<>(savedCampus, HttpStatus.CREATED);
    }

    // EXCEPTIONS
    @ExceptionHandler(CampusNotFoundException.class)
    public ResponseEntity<FieldMessage> handleCampusNotFound(CampusNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new FieldMessage("campus", ex.getMessage()));
    }

    @ExceptionHandler({
            CampusAlreadyExistsException.class,
            CampusInvalidDataException.class
    })
    public ResponseEntity<FieldMessage> handleCampusBadRequest(RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new FieldMessage("campus", ex.getMessage()));
    }
}
