package akin.backend.garson.controller;

import akin.backend.call.dto.response.WaiterCallResponse;
import akin.backend.garson.dto.SetAvailableRequest;
import akin.backend.garson.service.GarsonService;
import akin.backend.user.dto.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/garson")
@RequiredArgsConstructor
@PreAuthorize("hasRole('GARSON')")
public class GarsonController {

    private final GarsonService garsonService;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMe(Authentication auth) {
        Long userId = (Long) auth.getPrincipal();
        return ResponseEntity.ok(garsonService.getMe(userId));
    }

    @PatchMapping("/me/available")
    public ResponseEntity<UserResponse> setAvailable(
            Authentication auth,
            @RequestBody SetAvailableRequest request) {
        Long userId = (Long) auth.getPrincipal();
        return ResponseEntity.ok(garsonService.setAvailable(userId, request.isAvailable()));
    }

    @GetMapping("/calls")
    public ResponseEntity<List<WaiterCallResponse>> getCalls(Authentication auth) {
        Long userId = (Long) auth.getPrincipal();
        return ResponseEntity.ok(garsonService.getCalls(userId));
    }

    @PatchMapping("/calls/{id}/accept")
    public ResponseEntity<WaiterCallResponse> acceptCall(Authentication auth, @PathVariable Long id) {
        Long userId = (Long) auth.getPrincipal();
        return ResponseEntity.ok(garsonService.acceptCall(userId, id));
    }

    @PatchMapping("/calls/{id}/complete")
    public ResponseEntity<WaiterCallResponse> completeCall(Authentication auth, @PathVariable Long id) {
        Long userId = (Long) auth.getPrincipal();
        return ResponseEntity.ok(garsonService.completeCall(userId, id));
    }
}
