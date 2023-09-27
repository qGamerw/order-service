package ru.sber.orderservice.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.sber.orderservice.entities.Order;
import ru.sber.orderservice.entities.enums.EStatusOrders;
import ru.sber.orderservice.models.CancellationOfOrder;
import ru.sber.orderservice.models.LimitOrder;
import ru.sber.orderservice.repositories.OrderRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Контроллер для взаимодействия {@link Order заказами}
 */
@Service
@Slf4j
public class OrderServiceImp implements OrderService {
    private final OrderRepository orderRepository;

    public OrderServiceImp(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public boolean updateOrderStatus(long id, EStatusOrders eStatusOrders) {
        log.info("Обновляет статус заказа с id {} на статус {}", id, eStatusOrders);

        Optional<Order> order = orderRepository.findById(id);

        if (order.isPresent()) {
            order.get().setStatusOrders(eStatusOrders);
            orderRepository.save(order.get());

            return true;
        }

        return false;
    }

    @Override
    public List<LimitOrder> getListOrder() {
        log.info("Получает список заказов со статусом: в ожидании, в процессе");

        List<EStatusOrders> eStatusOrdersList = Arrays.asList(
                EStatusOrders.TODO,
                EStatusOrders.IN_PROCESS);

        return orderRepository.findByStatusOrdersIn(eStatusOrdersList)
                .stream()
                .map(LimitOrder::new)
                .toList();
    }

    @Override
    public boolean cancellationOfOrderById(CancellationOfOrder cancellationOfOrder) {
        log.info("Отказывается от заказа с id {}}", cancellationOfOrder.getId());

        Optional<Order> order = orderRepository.findById(cancellationOfOrder.getId());

        if (order.isPresent()) {

            order.get().setStatusOrders(EStatusOrders.CANCELLED);
            order.get().setRefusalReason(cancellationOfOrder.getMassage());
            orderRepository.save(order.get());

            return true;
        }

        return false;
    }
}
