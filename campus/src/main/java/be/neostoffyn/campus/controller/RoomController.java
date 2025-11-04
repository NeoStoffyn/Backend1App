package be.neostoffyn.campus.controller;

import be.neostoffyn.campus.error.FieldMessage;
import be.neostoffyn.campus.exception.Campus.CampusAlreadyExistsException;
import be.neostoffyn.campus.exception.Campus.CampusInvalidDataException;
import be.neostoffyn.campus.exception.Campus.CampusNotFoundException;
import be.neostoffyn.campus.exception.Room.RoomAlreadyExistsException;
import be.neostoffyn.campus.exception.Room.RoomInvalidDataException;
import be.neostoffyn.campus.exception.Room.RoomNotFoundException;
import be.neostoffyn.campus.model.Reservation;
import be.neostoffyn.campus.model.Room;
import be.neostoffyn.campus.service.ReservationService;
import be.neostoffyn.campus.service.RoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/campus/{campusName}/rooms")
public class RoomController {

    private final RoomService roomService;
    private final ReservationService reservationService;

    public RoomController(RoomService roomService, ReservationService reservationService) {
        this.roomService = roomService;
        this.reservationService = reservationService;
    }

    // GET:/campus/{campusName}/rooms
    @GetMapping
    public ResponseEntity<List<Room>> getRoomsForCampus(
            @PathVariable String campusName,
            @RequestParam(required = false) LocalDateTime availableFrom,
            @RequestParam(required = false) LocalDateTime availableUntil,
            @RequestParam(required = false) Integer minCapacity
    ) {
        List<Room> rooms = roomService.getRoomsForCampus(
                campusName, availableFrom, availableUntil, minCapacity
        );
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("{roomId}")
    public ResponseEntity<Room> getRoomById(
            @PathVariable String campusName,
            @PathVariable Long roomId
    ) {
        Room room = roomService.getRoomByIdAndCampus(campusName, roomId);
        return ResponseEntity.ok(room);
    }

    // POST:/campus/{campusName}/rooms
    @PostMapping
    public ResponseEntity<Room> addRoom(
            @PathVariable String campusName,
            @RequestBody Room room
    ) {
        Room newRoom = roomService.addRoom(campusName, room);
        return new ResponseEntity<>(newRoom, HttpStatus.CREATED);
    }


    // GET: /user/{userId}/rooms/{roomId}/reservation
    @GetMapping("{roomId}/reservations")
    public ResponseEntity<List<Reservation>> getReservationsForRoom(
            @PathVariable String campusName,
            @PathVariable Long roomId
    ) {
        List<Reservation> reservations = reservationService.getReservationsByRoom(campusName, roomId);
        return ResponseEntity.ok(reservations);
    }

    // EXCEPTIONS
    @ExceptionHandler({
            RoomNotFoundException.class,
            CampusNotFoundException.class
    })
    public ResponseEntity<FieldMessage> handleNotFoundExceptions(RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new FieldMessage("error", ex.getMessage()));
    }

    @ExceptionHandler({
            RoomInvalidDataException.class,
            RoomAlreadyExistsException.class
    })
    public ResponseEntity<FieldMessage> handleCampusBadRequest(RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new FieldMessage("room", ex.getMessage()));
    }

}
