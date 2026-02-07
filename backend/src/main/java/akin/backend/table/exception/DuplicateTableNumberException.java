package akin.backend.table.exception;

public class DuplicateTableNumberException extends RuntimeException {

    public static final String MESSAGE = "Bu masa numarası zaten kayıtlı.";

    public DuplicateTableNumberException() {
        super(MESSAGE);
    }
}
