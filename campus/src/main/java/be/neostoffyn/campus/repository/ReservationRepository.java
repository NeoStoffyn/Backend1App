package be.neostoffyn.campus.repository;

import be.neostoffyn.campus.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByUserId(Long userId);
    Optional<Reservation> findByUserIdAndId(Long userId, Long reservationId);
    List<Reservation> findByRooms_Id(Long roomId);
}
