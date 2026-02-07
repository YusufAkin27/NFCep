package akin.backend.admin.controller;

import akin.backend.table.dto.request.CreateTableRequest;
import akin.backend.table.dto.request.UpdateTableRequest;
import akin.backend.table.dto.response.TableResponse;
import akin.backend.table.service.TableService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/tables")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminTableController {

    private final TableService tableService;

    @GetMapping
    public ResponseEntity<List<TableResponse>> list(Authentication auth) {
        Long adminId = (Long) auth.getPrincipal();
        return ResponseEntity.ok(tableService.findAll(adminId));
    }

    @PostMapping
    public ResponseEntity<TableResponse> create(Authentication auth, @RequestBody @Valid CreateTableRequest request) {
        Long adminId = (Long) auth.getPrincipal();
        return ResponseEntity.ok(tableService.create(adminId, request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TableResponse> update(
            Authentication auth,
            @PathVariable Long id,
            @RequestBody @Valid UpdateTableRequest request) {
        Long adminId = (Long) auth.getPrincipal();
        return ResponseEntity.ok(tableService.update(adminId, id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(Authentication auth, @PathVariable Long id) {
        Long adminId = (Long) auth.getPrincipal();
        tableService.delete(adminId, id);
        return ResponseEntity.noContent().build();
    }
}
