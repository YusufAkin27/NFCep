package akin.backend.call.exception;

public class CallNotFoundException extends RuntimeException {

    public static final String MESSAGE = "Çağrı bulunamadı.";

    public CallNotFoundException() {
        super(MESSAGE);
    }
}
