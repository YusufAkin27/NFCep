package akin.backend.user.exception;

public class UserNotAdminException extends RuntimeException {

    public static final String MESSAGE = "Bu işlem için yetkiniz yok.";

    public UserNotAdminException() {
        super(MESSAGE);
    }
}
