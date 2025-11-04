package be.neostoffyn.campus.service;

import be.neostoffyn.campus.exception.Campus.CampusNotFoundException;
import be.neostoffyn.campus.exception.Reservation.ReservationInvalidDataException;
import be.neostoffyn.campus.exception.Reservation.ReservationNotFoundException;
import be.neostoffyn.campus.exception.Room.RoomNotFoundException;
import be.neostoffyn.campus.exception.User.UserNotFoundException;
import be.neostoffyn.campus.model.Reservation;
import be.neostoffyn.campus.model.Room;
import be.neostoffyn.campus.model.User;
import be.neostoffyn.campus.repository.CampusRepository;
import be.neostoffyn.campus.repository.ReservationRepository;
import be.neostoffyn.campus.repository.RoomRepository;
import be.neostoffyn.campus.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final CampusRepository campusRepository;

    public ReservationService(
            ReservationRepository reservationRepository,
            RoomRepository roomRepository,
            UserRepository userRepository,
            CampusRepository campusRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
        this.campusRepository = campusRepository;
    }

    public List<Reservation> getReservationsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userId));

        return reservationRepository.findByUserId(userId);
    }

    public Reservation getReservationByIdAndUser(Long userId, Long reservationId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userId));

        return reservationRepository.findByUserIdAndId(userId, reservationId)
                .orElseThrow(() -> new ReservationNotFoundException(
                        "Reservation " + reservationId + " not found for user " + userId
                ));
    }

    public Reservation createReservation(Long userId, Reservation request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));


        if (request.getStartTime() == null)
            throw new ReservationInvalidDataException("Start time is required");
        if (request.getEndTime() == null)
            throw new ReservationInvalidDataException("End time is required");

        if (request.getStartTime().isAfter(request.getEndTime())) {
            throw new ReservationInvalidDataException("Start time must be before end time");
        }
        if (request.getEndTime().isBefore(LocalDateTime.now())) {
            throw new ReservationInvalidDataException("End time cannot be in the past");
        }

        Reservation r = new Reservation();
        r.setUser(user);
        r.setStartTime(request.getStartTime());
        r.setEndTime(request.getEndTime());
        r.setComment(request.getComment());
        r.setRooms(request.getRooms());

        return reservationRepository.save(r);
    }

    public Reservation addRoomToReservation(Long userId, Long reservationId, Long roomId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userId));

        Reservation reservation = reservationRepository.findById(reservationId)
                .filter(r -> r.getUser().getId().equals(userId))
                .orElseThrow(() -> new ReservationNotFoundException(
                        "Reservation " + reservationId + " not found for user " + userId));

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException("Room not found: " + roomId));

        if (reservation.getRooms().contains(room)) {
            throw new ReservationInvalidDataException("Room already assigned to this reservation");
        }

        reservation.getRooms().add(room);
        return reservationRepository.save(reservation);
    }

    public List<Reservation> getReservationsByRoom(String campusId, Long roomId) {
        if (!campusRepository.existsCampusByName(campusId))
            throw new CampusNotFoundException("Campus not found: " + campusId);


        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException("Room not found: " + roomId));


        if (!room.getCampus().getName().equals(campusId))
            throw new RoomNotFoundException("Room " + roomId + " does not belong to campus " + campusId);

        return room.getReservations();
    }
}
