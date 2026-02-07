package akin.backend.auth.controller;

import akin.backend.auth.request.LoginRequest;
import akin.backend.auth.response.AuthResponse;
import akin.backend.auth.service.AuthService;
import akin.backend.user.entity.Role;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/admin/login")
    public ResponseEntity<AuthResponse> adminLogin(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request, Role.ADMIN));
    }

    @PostMapping("/garson/login")
    public ResponseEntity<AuthResponse> garsonLogin(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request, Role.GARSON));
    }

    @PostMapping("/mutfak/login")
    public ResponseEntity<AuthResponse> mutfakLogin(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request, Role.MUTFAK));
    }
}
