package akin.backend.admin.controller;

import akin.backend.order.dto.request.CreateOrderRequest;
import akin.backend.order.dto.response.OrderResponse;
import akin.backend.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/orders")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminOrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            Authentication auth,
            @RequestBody @Valid CreateOrderRequest request) {
        Long adminId = (Long) auth.getPrincipal();
        return ResponseEntity.ok(orderService.createOrder(adminId, request));
    }
}
