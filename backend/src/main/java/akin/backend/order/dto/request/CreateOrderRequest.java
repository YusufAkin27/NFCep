package akin.backend.order.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class CreateOrderRequest {

    @NotBlank(message = "Masa numarası gerekli")
    private String tableNumber;

    @NotBlank(message = "Masa adı gerekli")
    private String tableName;

    @NotEmpty(message = "En az bir kalem gerekli")
    @Valid
    private List<OrderItemRequest> items;
}
