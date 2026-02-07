package akin.backend.call.exception;

public class InvalidCallStatusException extends RuntimeException {

    public static final String MESSAGE = "Bu çağrı için işlem yapılamaz.";

    public InvalidCallStatusException() {
        super(MESSAGE);
    }
}
