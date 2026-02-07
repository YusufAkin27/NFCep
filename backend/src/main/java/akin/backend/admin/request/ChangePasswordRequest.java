package akin.backend.admin.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangePasswordRequest {

    @NotBlank(message = "Yeni şifre gerekli")
    @Size(min = 6, message = "Şifre en az 6 karakter olmalı")
    private String newPassword;
}
