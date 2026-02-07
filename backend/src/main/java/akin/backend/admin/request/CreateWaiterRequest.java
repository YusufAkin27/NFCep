package akin.backend.admin.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateWaiterRequest {

    @NotBlank(message = "Kullanıcı adı gerekli")
    private String username;

    @NotBlank(message = "Şifre gerekli")
    @Size(min = 6, message = "Şifre en az 6 karakter olmalı")
    private String password;

    private String firstName;
    private String lastName;
}
