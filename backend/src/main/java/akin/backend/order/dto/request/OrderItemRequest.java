package akin.backend.order.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderItemRequest {

    @NotNull(message = "Ürün id gerekli")
    private Long productId;

    @NotNull(message = "Adet gerekli")
    @Min(value = 1, message = "Adet en az 1 olmalı")
    private Integer quantity;
}
