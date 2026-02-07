package akin.backend.order.exception;

public class OrderNotFoundException extends RuntimeException {

    public static final String MESSAGE = "Sipariş bulunamadı.";

    public OrderNotFoundException() {
        super(MESSAGE);
    }
}
