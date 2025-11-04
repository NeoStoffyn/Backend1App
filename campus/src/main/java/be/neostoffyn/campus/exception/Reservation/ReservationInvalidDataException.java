package be.neostoffyn.campus.exception.Reservation;

public class ReservationInvalidDataException extends RuntimeException {
    public ReservationInvalidDataException(String message) {
        super(message);
    }
}