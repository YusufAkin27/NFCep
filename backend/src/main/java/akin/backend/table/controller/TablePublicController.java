package akin.backend.table.controller;

import akin.backend.table.dto.response.TableResponse;
import akin.backend.table.service.TableService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tables")
@RequiredArgsConstructor
public class TablePublicController {

    private final TableService tableService;

    @GetMapping("/{tableNumber}")
    public ResponseEntity<TableResponse> getByTableNumber(@PathVariable String tableNumber) {
        return ResponseEntity.ok(tableService.getByTableNumber(tableNumber));
    }
}
