package akin.backend.call.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateCallRequest {

    @NotBlank(message = "Masa numarası gerekli")
    private String tableNumber;

    @NotBlank(message = "Masa adı gerekli")
    private String tableName;

    private String message;
}
