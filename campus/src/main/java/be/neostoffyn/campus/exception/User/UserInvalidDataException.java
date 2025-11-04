package be.neostoffyn.campus.exception.User;

public class UserInvalidDataException extends RuntimeException {
    public UserInvalidDataException(String message) {
        super(message);
    }
}