package akin.backend.user.exception;

public class UserNotFoundException extends RuntimeException {

    public static final String MESSAGE = "Kullanıcı bulunamadı.";

    public UserNotFoundException() {
        super(MESSAGE);
    }
}
