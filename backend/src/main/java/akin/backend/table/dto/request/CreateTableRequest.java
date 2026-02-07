package akin.backend.table.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateTableRequest {

    @NotBlank(message = "Masa numarası gerekli")
    private String tableNumber;

    @NotBlank(message = "Masa adı gerekli")
    private String tableName;
}
