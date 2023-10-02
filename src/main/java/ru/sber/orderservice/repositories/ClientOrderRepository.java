package ru.sber.orderservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sber.orderservice.entities.Order;
import ru.sber.orderservice.entities.OrderResponse;

/**
 * Репозиторий с {@link Order блюдами}
 */
@Repository
public interface ClientOrderRepository extends JpaRepository<Order, Long> {
}
