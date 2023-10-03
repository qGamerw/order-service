package ru.sber.orderservice.repositories;

import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sber.orderservice.entities.Order;
import ru.sber.orderservice.entities.enums.EStatusOrders;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий с {@link Order заказами}
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByStatusOrdersIn(List<EStatusOrders> statusOrders);
    List<Order> findByStatusOrdersInAndCourierIdEmpty(List<EStatusOrders> statusOrders);
    List<Order> findOrderByCourierId(long id);

    List<Order> findOrderByClientId(long id);
}
