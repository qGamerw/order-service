package ru.sber.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sber.entities.Order;
import ru.sber.entities.enums.EStatusOrders;

import java.util.List;

/**
 * Репозиторий с {@link Order заказами}
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByStatusOrdersIn(List<EStatusOrders> statusOrders);

    List<Order> findByStatusOrdersInAndCourierIdNull(List<EStatusOrders> statusOrders);

    List<Order> findOrderByCourierId(long id);

    List<Order> findOrderByClientId(long id);
}
