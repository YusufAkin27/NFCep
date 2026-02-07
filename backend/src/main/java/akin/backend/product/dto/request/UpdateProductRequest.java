package akin.backend.product.dto.request;

import jakarta.validation.constraints.DecimalMin;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateProductRequest {

    private String name;
    private String description;
    private Boolean inStock;

    @DecimalMin(value = "0", inclusive = false, message = "Fiyat 0'dan büyük olmalı")
    private BigDecimal price;
}
