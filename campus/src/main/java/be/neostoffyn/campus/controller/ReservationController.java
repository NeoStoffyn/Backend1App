package be.neostoffyn.campus.controller;

import be.neostoffyn.campus.error.FieldMessage;
import be.neostoffyn.campus.exception.Campus.CampusNotFoundException;
import be.neostoffyn.campus.exception.Reservation.ReservationInvalidDataException;
import be.neostoffyn.campus.exception.Reservation.ReservationNotFoundException;
import be.neostoffyn.campus.exception.Room.RoomNotFoundException;
import be.neostoffyn.campus.exception.User.UserNotFoundException;
import be.neostoffyn.campus.model.Reservation;
import be.neostoffyn.campus.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/{userId}/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    // GET: /user/{userId}/reservations
    @GetMapping
    public ResponseEntity<List<Reservation>> getReservationsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(reservationService.getReservationsByUser(userId));
    }

    @GetMapping("{reservationId}")
    public ResponseEntity<Reservation> getReservationById(
            @PathVariable Long userId,
            @PathVariable Long reservationId
    ) {
        final Reservation reservation = reservationService.getReservationByIdAndUser(userId, reservationId);
        return ResponseEntity.ok(reservation);
    }


    // POST: /user/{userId}/reservations
    @PostMapping
    public ResponseEntity<Reservation> createReservation(
            @PathVariable Long userId,
            @RequestBody Reservation request
    ) {
        Reservation reservatie = reservationService.createReservation(userId, request);
        return new ResponseEntity<>(reservatie, HttpStatus.CREATED);
    }


    // PUT: /user/{userId}/reservation/{reservationId}/rooms/{roomId}
    @PutMapping("/{reservationId}/rooms/{roomId}")
    public ResponseEntity<Reservation> addRoomToReservation(
            @PathVariable Long userId,
            @PathVariable Long reservationId,
            @PathVariable Long roomId
    ) {
        Reservation updated = reservationService.addRoomToReservation(userId, reservationId, roomId);
        return ResponseEntity.ok(updated);
    }





    // EXCEPTIONS
    @ExceptionHandler({
            UserNotFoundException.class,
            ReservationNotFoundException.class,
            RoomNotFoundException.class,
            CampusNotFoundException.class
    })
    public ResponseEntity<FieldMessage> handleNotFound(RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new FieldMessage("error", ex.getMessage()));
    }

    @ExceptionHandler(ReservationInvalidDataException.class)
    public ResponseEntity<FieldMessage> handleBadRequest(ReservationInvalidDataException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new FieldMessage("reservation", ex.getMessage()));
    }
}
