package akin.backend.kitchen.controller;

import akin.backend.call.dto.request.CreateCallRequest;
import akin.backend.call.dto.response.WaiterCallResponse;
import akin.backend.call.service.CallService;
import akin.backend.kitchen.dto.UpdateOrderStatusRequest;
import akin.backend.kitchen.service.KitchenService;
import akin.backend.order.dto.response.OrderResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mutfak")
@RequiredArgsConstructor
@PreAuthorize("hasRole('MUTFAK')")
public class KitchenController {

    private final KitchenService kitchenService;
    private final CallService callService;

    @GetMapping("/orders")
    public ResponseEntity<Page<OrderResponse>> getActiveOrders(
            Authentication auth,
            @PageableDefault(size = 10) Pageable pageable) {
        Long userId = (Long) auth.getPrincipal();
        return ResponseEntity.ok(kitchenService.getActiveOrders(userId, pageable));
    }

    @PatchMapping("/orders/{id}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            Authentication auth,
            @PathVariable Long id,
            @RequestBody @Valid UpdateOrderStatusRequest request) {
        Long userId = (Long) auth.getPrincipal();
        return ResponseEntity.ok(kitchenService.updateOrderStatus(userId, id, request.getStatus()));
    }

    @PostMapping("/calls")
    public ResponseEntity<WaiterCallResponse> createCall(
            Authentication auth,
            @RequestBody @Valid CreateCallRequest request) {
        Long userId = (Long) auth.getPrincipal();
        return ResponseEntity.ok(callService.createCallFromMutfak(userId, request));
    }
}
