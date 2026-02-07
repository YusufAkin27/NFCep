package akin.backend.table.exception;

public class TableNotFoundException extends RuntimeException {

    public static final String MESSAGE = "Masa bulunamadı.";

    public TableNotFoundException() {
        super(MESSAGE);
    }
}
