package be.neostoffyn.campus.service;

import be.neostoffyn.campus.exception.Reservation.ReservationInvalidDataException;
import be.neostoffyn.campus.exception.User.UserNotFoundException;
import be.neostoffyn.campus.model.Reservation;
import be.neostoffyn.campus.model.User;
import be.neostoffyn.campus.repository.CampusRepository;
import be.neostoffyn.campus.repository.ReservationRepository;
import be.neostoffyn.campus.repository.RoomRepository;
import be.neostoffyn.campus.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservationServiceTest {

    private ReservationService reservationService;
    private ReservationRepository reservationRepository;
    private RoomRepository roomRepository;
    private UserRepository userRepository;
    private CampusRepository campusRepository;

    @BeforeEach
    void setUp() {
        reservationRepository = mock(ReservationRepository.class);
        roomRepository = mock(RoomRepository.class);
        userRepository = mock(UserRepository.class);
        campusRepository = mock(CampusRepository.class);

        reservationService = new ReservationService(
                reservationRepository, roomRepository, userRepository, campusRepository
        );
    }

    @Test
    void createReservation_shouldSaveReservation_whenValid() {
  
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Reservation request = new Reservation();
        request.setStartTime(LocalDateTime.now().plusHours(1));
        request.setEndTime(LocalDateTime.now().plusHours(2));
        request.setComment("Test");

        Reservation saved = new Reservation();
        saved.setId(99L);
        when(reservationRepository.save(any(Reservation.class))).thenReturn(saved);

    
        Reservation result = reservationService.createReservation(userId, request);


        assertNotNull(result);
        assertEquals(99L, result.getId());
    }

    @Test
    void createReservation_shouldThrowException_whenUserNotFound() {
        Long userId = 99L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Reservation request = new Reservation();
        request.setStartTime(LocalDateTime.now());
        request.setEndTime(LocalDateTime.now().plusHours(1));

        assertThrows(UserNotFoundException.class, () ->
                reservationService.createReservation(userId, request)
        );
    }

    @Test
    void createReservation_shouldThrowException_whenStartAfterEnd() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Reservation request = new Reservation();
        request.setStartTime(LocalDateTime.now().plusHours(2));
        request.setEndTime(LocalDateTime.now().plusHours(1));

        assertThrows(ReservationInvalidDataException.class, () ->
                reservationService.createReservation(userId, request)
        );
    }
}
