package akin.backend.user.exception;

public class DuplicateUsernameException extends RuntimeException {

    public static final String MESSAGE = "Bu kullanıcı adı zaten kullanılıyor.";

    public DuplicateUsernameException() {
        super(MESSAGE);
    }
}
