package akin.backend.kitchen.service;

import akin.backend.order.dto.response.OrderResponse;
import akin.backend.order.entity.OrderStatus;
import akin.backend.order.service.OrderService;
import akin.backend.user.entity.Role;
import akin.backend.user.entity.User;
import akin.backend.user.exception.UserNotFoundException;
import akin.backend.user.exception.UserNotAdminException;
import akin.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class KitchenServiceImpl implements KitchenService {

    private final OrderService orderService;
    private final UserRepository userRepository;

    private void ensureMutfak(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        if (user.getRole() != Role.MUTFAK) {
            throw new UserNotAdminException();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponse> getActiveOrders(Long userId, Pageable pageable) {
        ensureMutfak(userId);
        return orderService.findActiveOrders(pageable);
    }

    @Override
    @Transactional
    public OrderResponse updateOrderStatus(Long userId, Long orderId, OrderStatus status) {
        ensureMutfak(userId);
        return orderService.updateOrderStatus(orderId, status);
    }
}
