package be.neostoffyn.campus.controller;

import be.neostoffyn.campus.dto.CampusDto;
import be.neostoffyn.campus.error.FieldMessage;
import be.neostoffyn.campus.exception.Campus.CampusAlreadyExistsException;
import be.neostoffyn.campus.exception.Campus.CampusInvalidDataException;
import be.neostoffyn.campus.exception.Campus.CampusNotFoundException;
import be.neostoffyn.campus.model.Campus;
import be.neostoffyn.campus.service.CampusService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

// BRON: https://medium.com/@fingervinicius/how-to-handle-validation-errors-in-spring-boot-to-become-human-friendly-90bd2ec3ed6e

@RestController
@RequestMapping("/campus")
public class CampusController {

    private final CampusService campusService;

    public CampusController(CampusService campusService) {
        this.campusService = campusService;
    }

    // GET:/campus
    @GetMapping
    public ResponseEntity<List<CampusDto>> getAllCampuses() {
        return ResponseEntity.ok(campusService.getAllCampuses().stream()
                .map(CampusDto::from)
                .toList());
    }

    // GET: /campus/{name}
    @GetMapping("/{name}")
    public ResponseEntity<CampusDto> getCampus(@PathVariable String name) {
        Campus campus = campusService.getCampusByName(name);
        return ResponseEntity.ok(CampusDto.from(campus));
    }



    // POST: /campus
    @PostMapping
    public ResponseEntity<CampusDto> addCampus(@Valid @RequestBody Campus campus) {
        Campus savedCampus = campusService.saveCampus(campus);
        return new ResponseEntity<>(CampusDto.from(savedCampus), HttpStatus.CREATED);
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<FieldMessage> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String msg = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + " " + e.getDefaultMessage())
                .collect(java.util.stream.Collectors.joining(", "));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new FieldMessage("validation", msg));
    }
}
