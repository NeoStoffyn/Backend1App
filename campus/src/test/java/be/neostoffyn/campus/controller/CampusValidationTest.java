package be.neostoffyn.campus.controller;

import be.neostoffyn.campus.model.Campus;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.Test;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CampusValidationTest {

    @Test
    void testCampusValidation() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();

            Campus campus = new Campus();
            campus.setName(""); // Invalid
            campus.setAddress(""); // Invalid
            campus.setParkingSpots(-1); // Invalid

            Set<ConstraintViolation<Campus>> violations = validator.validate(campus);
            assertFalse(violations.isEmpty(), "Should have validation violations");
            
            boolean hasNameError = violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name"));
            boolean hasAddressError = violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("address"));
            boolean hasParkingError = violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("parkingSpots"));
            
            assertTrue(hasNameError, "Should have name validation error");
            assertTrue(hasAddressError, "Should have address validation error");
            assertTrue(hasParkingError, "Should have parkingSpots validation error");
        }
    }
}
