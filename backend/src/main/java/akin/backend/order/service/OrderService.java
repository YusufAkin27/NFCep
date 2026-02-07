package akin.backend.order.service;

import akin.backend.order.dto.request.CreateOrderRequest;
import akin.backend.order.dto.response.OrderResponse;
import akin.backend.order.entity.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {

    OrderResponse createOrder(Long adminUserId, CreateOrderRequest request);

    Page<OrderResponse> findActiveOrders(Pageable pageable);

    OrderResponse updateOrderStatus(Long orderId, OrderStatus status);
}
