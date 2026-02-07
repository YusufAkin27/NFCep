package akin.backend.product.exception;

public class CloudinaryUploadException extends RuntimeException {

    public static final String MESSAGE = "Fotoğraf yüklenirken bir hata oluştu.";

    public CloudinaryUploadException() {
        super(MESSAGE);
    }

    public CloudinaryUploadException(Throwable cause) {
        super(MESSAGE, cause);
    }
}
