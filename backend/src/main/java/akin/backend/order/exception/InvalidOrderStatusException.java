package akin.backend.order.exception;

public class InvalidOrderStatusException extends RuntimeException {

    public static final String MESSAGE = "Geçersiz sipariş durumu geçişi.";

    public InvalidOrderStatusException() {
        super(MESSAGE);
    }
}
