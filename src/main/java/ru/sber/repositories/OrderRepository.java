package ru.sber.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    
    Page<Order> findByStatusOrdersInAndCourierIdNull(List<EStatusOrders> statusOrders, Pageable pageable);

    List<Order> findOrderByCourierId(long id);

    List<Order> findOrderByClientId(long id);

    List<Order> findOrderByCourierIdAndStatusOrdersNot(Long courierId, EStatusOrders statusOrders);
}
