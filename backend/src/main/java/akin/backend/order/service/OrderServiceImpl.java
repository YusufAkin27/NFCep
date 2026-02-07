package akin.backend.order.service;

import akin.backend.order.dto.request.CreateOrderRequest;
import akin.backend.order.dto.request.OrderItemRequest;
import akin.backend.order.dto.response.OrderItemResponse;
import akin.backend.order.dto.response.OrderResponse;
import akin.backend.order.entity.Order;
import akin.backend.order.entity.OrderItem;
import akin.backend.order.entity.OrderStatus;
import akin.backend.order.exception.InvalidOrderStatusException;
import akin.backend.order.exception.OrderNotFoundException;
import akin.backend.order.repository.OrderRepository;
import akin.backend.product.entity.Product;
import akin.backend.product.exception.ProductNotFoundException;
import akin.backend.product.repository.ProductRepository;
import akin.backend.user.entity.Role;
import akin.backend.user.entity.User;
import akin.backend.user.exception.UserNotFoundException;
import akin.backend.user.exception.UserNotAdminException;
import akin.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    private static final Set<OrderStatus> ACTIVE_STATUSES = Set.of(OrderStatus.ALINDI, OrderStatus.HAZIRLANIYOR);

    private void ensureAdmin(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        if (!user.getRoles().contains(Role.ADMIN)) {
            throw new UserNotAdminException();
        }
    }

    @Override
    @Transactional
    public OrderResponse createOrder(Long adminUserId, CreateOrderRequest request) {
        ensureAdmin(adminUserId);
        Order order = new Order();
        order.setTableNumber(request.getTableNumber());
        order.setTableName(request.getTableName());
        order.setStatus(OrderStatus.ALINDI);
        order.setOrderItems(new ArrayList<>());
        for (OrderItemRequest itemReq : request.getItems()) {
            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(ProductNotFoundException::new);
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemReq.getQuantity());
            order.getOrderItems().add(orderItem);
        }
        order = orderRepository.save(order);
        log.info("Sipariş oluşturuldu: masa {} id {}", order.getTableNumber(), order.getId());
        return toResponse(order);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponse> findActiveOrders(Pageable pageable) {
        return orderRepository.findAllByStatusIn(ACTIVE_STATUSES, pageable).map(this::toResponse);
    }

    @Override
    @Transactional
    public OrderResponse updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
        if (!isValidTransition(order.getStatus(), status)) {
            throw new InvalidOrderStatusException();
        }
        order.setStatus(status);
        order = orderRepository.save(order);
        log.info("Sipariş durumu güncellendi: id {} -> {}", orderId, status);
        return toResponse(order);
    }

    private boolean isValidTransition(OrderStatus from, OrderStatus to) {
        return switch (from) {
            case ALINDI -> to == OrderStatus.HAZIRLANIYOR || to == OrderStatus.TAMAMLANDI;
            case HAZIRLANIYOR -> to == OrderStatus.TAMAMLANDI;
            case TAMAMLANDI -> false;
        };
    }

    public OrderResponse toResponse(Order order) {
        List<OrderItemResponse> itemResponses = order.getOrderItems().stream()
                .map(oi -> {
                    BigDecimal unitPrice = oi.getProduct().getPrice();
                    BigDecimal lineTotal = unitPrice.multiply(BigDecimal.valueOf(oi.getQuantity()));
                    return OrderItemResponse.builder()
                            .productId(oi.getProduct().getId())
                            .productName(oi.getProduct().getName())
                            .quantity(oi.getQuantity())
                            .unitPrice(unitPrice)
                            .lineTotal(lineTotal)
                            .build();
                })
                .toList();
        BigDecimal totalAmount = itemResponses.stream()
                .map(OrderItemResponse::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return OrderResponse.builder()
                .id(order.getId())
                .tableNumber(order.getTableNumber())
                .tableName(order.getTableName())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .items(itemResponses)
                .totalAmount(totalAmount)
                .build();
    }
}
