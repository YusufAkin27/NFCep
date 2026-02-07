package akin.backend.admin.controller;

import akin.backend.admin.request.ChangePasswordRequest;
import akin.backend.admin.request.CreateMutfakRequest;
import akin.backend.admin.request.CreateWaiterRequest;
import akin.backend.admin.request.SetWorkingTodayRequest;
import akin.backend.admin.response.EmployeeStatisticsResponse;
import akin.backend.admin.service.AdminService;
import akin.backend.user.dto.response.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    /* ===================== GARSON ===================== */

    @PostMapping("/garsons")
    public ResponseEntity<UserResponse> createWaiter(
            Authentication auth,
            @RequestBody @Valid CreateWaiterRequest request) {
        Long adminId = (Long) auth.getPrincipal();
        return ResponseEntity.ok(adminService.createWaiter(adminId, request));
    }

    @GetMapping("/garsons")
    public ResponseEntity<List<UserResponse>> getAllGarsons(Authentication auth) {
        Long adminId = (Long) auth.getPrincipal();
        return ResponseEntity.ok(adminService.getAllGarsons(adminId));
    }

    @PutMapping("/garsons/{id}/password")
    public ResponseEntity<Void> changeGarsonPassword(
            Authentication auth,
            @PathVariable Long id,
            @RequestBody @Valid ChangePasswordRequest request) {
        Long adminId = (Long) auth.getPrincipal();
        adminService.changeGarsonPassword(adminId, id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/garsons/{id}")
    public ResponseEntity<Void> deleteGarson(Authentication auth, @PathVariable Long id) {
        Long adminId = (Long) auth.getPrincipal();
        adminService.deleteGarson(adminId, id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/garsons/{id}/working-today")
    public ResponseEntity<Void> setGarsonWorkingToday(
            Authentication auth,
            @PathVariable Long id,
            @RequestBody SetWorkingTodayRequest request) {
        Long adminId = (Long) auth.getPrincipal();
        adminService.setGarsonWorkingToday(adminId, id, request);
        return ResponseEntity.ok().build();
    }

    /* ===================== MUTFAK ===================== */

    @PostMapping("/mutfak")
    public ResponseEntity<UserResponse> createMutfak(
            Authentication auth,
            @RequestBody @Valid CreateMutfakRequest request) {
        Long adminId = (Long) auth.getPrincipal();
        return ResponseEntity.ok(adminService.createMutfak(adminId, request));
    }

    @GetMapping("/mutfak")
    public ResponseEntity<List<UserResponse>> getAllMutfak(Authentication auth) {
        Long adminId = (Long) auth.getPrincipal();
        return ResponseEntity.ok(adminService.getAllMutfak(adminId));
    }

    @PutMapping("/mutfak/{id}/password")
    public ResponseEntity<Void> changeMutfakPassword(
            Authentication auth,
            @PathVariable Long id,
            @RequestBody @Valid ChangePasswordRequest request) {
        Long adminId = (Long) auth.getPrincipal();
        adminService.changeMutfakPassword(adminId, id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/mutfak/{id}")
    public ResponseEntity<Void> deleteMutfak(Authentication auth, @PathVariable Long id) {
        Long adminId = (Long) auth.getPrincipal();
        adminService.deleteMutfak(adminId, id);
        return ResponseEntity.noContent().build();
    }

    /* ===================== İSTATİSTİKLER ===================== */

    @GetMapping("/statistics")
    public ResponseEntity<List<EmployeeStatisticsResponse>> getEmployeeStatistics(Authentication auth) {
        Long adminId = (Long) auth.getPrincipal();
        return ResponseEntity.ok(adminService.getEmployeeStatistics(adminId));
    }
}
