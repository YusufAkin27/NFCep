package akin.backend.product.exception;

public class ProductNotFoundException extends RuntimeException {

    public static final String MESSAGE = "Ürün bulunamadı.";

    public ProductNotFoundException() {
        super(MESSAGE);
    }
}
