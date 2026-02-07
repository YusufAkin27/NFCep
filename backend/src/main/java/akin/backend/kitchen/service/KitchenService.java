package akin.backend.kitchen.service;

import akin.backend.order.dto.response.OrderResponse;
import akin.backend.order.entity.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface KitchenService {

    Page<OrderResponse> getActiveOrders(Long userId, Pageable pageable);

    OrderResponse updateOrderStatus(Long userId, Long orderId, OrderStatus status);
}
