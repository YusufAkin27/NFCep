package akin.backend.order.repository;

import akin.backend.order.entity.Order;
import akin.backend.order.entity.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findAllByStatusIn(Set<OrderStatus> statuses, Pageable pageable);
}
