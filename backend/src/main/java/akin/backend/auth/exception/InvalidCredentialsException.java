package akin.backend.auth.exception;

public class InvalidCredentialsException extends RuntimeException {

    public static final String MESSAGE = "Kullanıcı adı veya şifre hatalı.";

    public InvalidCredentialsException() {
        super(MESSAGE);
    }
}
