package akin.backend.auth.exception;

public class RateLimitExceededException extends RuntimeException {

    public static final String MESSAGE = "Çok fazla istek. Lütfen daha sonra tekrar deneyin.";

    public RateLimitExceededException() {
        super(MESSAGE);
    }
}
