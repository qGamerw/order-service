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

    Page<Order> findOrderByCourierId(String id, Pageable pageable);

    List<Order> findOrderByClientId(String id);

    List<Order> findOrderByCourierIdAndStatusOrdersNot(String courierId, EStatusOrders statusOrders);

    int countOrderByClientId(String id);

    int countOrderByCourierId(String id);

    int countOrderByEmployeeRestaurantId(String id);
}
