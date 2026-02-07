package akin.backend.user.controller;

import akin.backend.user.dto.request.UpdateProfileRequest;
import akin.backend.user.dto.response.ProfileResponse;
import akin.backend.user.dto.response.UserResponse;
import akin.backend.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PutMapping("/profile")
    public ResponseEntity<ProfileResponse> updateProfile(
            @Valid @RequestBody UpdateProfileRequest request,
            Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return ResponseEntity.ok(userService.updateProfile(userId, request));
    }

    @GetMapping("/profile")
    public ResponseEntity<UserResponse> getProfile(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return ResponseEntity.ok(userService.getProfile(userId));
    }



    @PatchMapping("/account/deactivate")
    public ResponseEntity<Void> deactivateAccount(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        userService.deactivateAccount(userId);
        return ResponseEntity.noContent().build();
    }
}
