package akin.backend.config;

import akin.backend.auth.exception.InvalidCredentialsException;
import akin.backend.auth.exception.RateLimitExceededException;
import akin.backend.order.exception.InvalidOrderStatusException;
import akin.backend.order.exception.OrderNotFoundException;
import akin.backend.call.exception.CallNotFoundException;
import akin.backend.call.exception.InvalidCallStatusException;
import akin.backend.table.exception.DuplicateTableNumberException;
import akin.backend.table.exception.TableNotFoundException;
import akin.backend.product.exception.CloudinaryUploadException;
import akin.backend.product.exception.ProductNotFoundException;
import akin.backend.user.exception.DuplicateUsernameException;
import akin.backend.user.exception.UserNotAdminException;
import akin.backend.user.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String KEY_ERROR = "error";

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Map<String, String>> handleInvalidCredentials(InvalidCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(KEY_ERROR, ex.getMessage()));
    }

    @ExceptionHandler(RateLimitExceededException.class)
    public ResponseEntity<Map<String, String>> handleRateLimit(RateLimitExceededException ex) {
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(Map.of(KEY_ERROR, ex.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUserNotFound(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(KEY_ERROR, ex.getMessage()));
    }

    @ExceptionHandler(UserNotAdminException.class)
    public ResponseEntity<Map<String, String>> handleUserNotAdmin(UserNotAdminException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of(KEY_ERROR, ex.getMessage()));
    }

    @ExceptionHandler(DuplicateUsernameException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateUsername(DuplicateUsernameException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(KEY_ERROR, ex.getMessage()));
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleProductNotFound(ProductNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(KEY_ERROR, ex.getMessage()));
    }

    @ExceptionHandler(CloudinaryUploadException.class)
    public ResponseEntity<Map<String, String>> handleCloudinaryUpload(CloudinaryUploadException ex) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(Map.of(KEY_ERROR, ex.getMessage()));
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleOrderNotFound(OrderNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(KEY_ERROR, ex.getMessage()));
    }

    @ExceptionHandler(InvalidOrderStatusException.class)
    public ResponseEntity<Map<String, String>> handleInvalidOrderStatus(InvalidOrderStatusException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(KEY_ERROR, ex.getMessage()));
    }

    @ExceptionHandler(TableNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleTableNotFound(TableNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(KEY_ERROR, ex.getMessage()));
    }

    @ExceptionHandler(DuplicateTableNumberException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateTableNumber(DuplicateTableNumberException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(KEY_ERROR, ex.getMessage()));
    }

    @ExceptionHandler(CallNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCallNotFound(CallNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(KEY_ERROR, ex.getMessage()));
    }

    @ExceptionHandler(InvalidCallStatusException.class)
    public ResponseEntity<Map<String, String>> handleInvalidCallStatus(InvalidCallStatusException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(KEY_ERROR, ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(err -> {
            String field = err instanceof FieldError ? ((FieldError) err).getField() : err.getObjectName();
            errors.put(field, err.getDefaultMessage());
        });
        Map<String, Object> body = new HashMap<>();
        body.put(KEY_ERROR, "Validasyon hatası");
        body.put("details", errors);
        return ResponseEntity.badRequest().body(body);
    }
}
