package akin.backend.kitchen.dto;

import akin.backend.order.entity.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateOrderStatusRequest {

    @NotNull(message = "Durum gerekli")
    private OrderStatus status;
}
