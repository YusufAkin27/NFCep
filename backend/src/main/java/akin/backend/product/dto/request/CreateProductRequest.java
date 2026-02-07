package akin.backend.product.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateProductRequest {

    @NotBlank(message = "Ürün adı gerekli")
    private String name;

    private String description;

    @NotNull(message = "Stok durumu gerekli")
    private Boolean inStock;

    @NotNull(message = "Fiyat gerekli")
    @DecimalMin(value = "0", inclusive = false, message = "Fiyat 0'dan büyük olmalı")
    private BigDecimal price;
}
