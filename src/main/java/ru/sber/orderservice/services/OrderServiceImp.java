package ru.sber.orderservice.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sber.orderservice.entities.Order;
import ru.sber.orderservice.entities.enums.EStatusOrders;
import ru.sber.orderservice.models.CancellationOfOrder;
import ru.sber.orderservice.models.LimitDishesOrder;
import ru.sber.orderservice.models.LimitOrder;
import ru.sber.orderservice.models.LimitOrderRestoran;
import ru.sber.orderservice.repositories.DishesOrderRepository;
import ru.sber.orderservice.repositories.OrderRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * Контроллер для взаимодействия {@link Order заказами}
 */
@Service
@Slf4j
public class OrderServiceImp implements OrderService {
    private final OrderRepository orderRepository;
    private final DishesOrderRepository dishesOrderRepository;

    @Autowired
    public OrderServiceImp(OrderRepository orderRepository, DishesOrderRepository dishesOrderRepository) {
        this.orderRepository = orderRepository;
        this.dishesOrderRepository = dishesOrderRepository;
    }

    @Override
    public boolean updateOrderStatus(long id, String eStatusOrders) {
        log.info("Обновляет статус заказа с id {} на статус {}", id, eStatusOrders);

        Optional<Order> order = orderRepository.findById(id);

        if (order.isPresent()) {
            order.get().setStatusOrders(EStatusOrders.valueOf(eStatusOrders));
            orderRepository.save(order.get());

            return true;
        }

        return false;
    }

    @Override
    public List<LimitOrderRestoran> getListOrder() {
        log.info("Получает список заказов со статусом: в ожидании, в процессе готовки и готов");

        List<EStatusOrders> eStatusOrdersList = Arrays.asList(
                EStatusOrders.REVIEW,
                EStatusOrders.COOKING,
                EStatusOrders.COOKED);

        return orderRepository.findByStatusOrdersIn(eStatusOrdersList)
                .stream()
                .map(getLimitOrderRestoranFunction())
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

    @Override
    public List<LimitOrderRestoran> findAllActiveOrder() {
        log.info("Получает список заказов со статусом: готовится, готов");

        List<EStatusOrders> eStatusOrdersList = Arrays.asList(
                EStatusOrders.COOKING,
                EStatusOrders.COOKED);

        return orderRepository.findByStatusOrdersInAndCourierIdNull(eStatusOrdersList)
                .stream()
                .map(getLimitOrderRestoranFunction())
                .toList();
    }

    @Override
    public Optional<LimitOrder> findOrderById(long id) {
        log.info("Поиск заказа с id {}", id);

        Optional<Order> order = orderRepository.findById(id);

        if (order.isPresent()) {
            List<LimitDishesOrder> dishesOrders = dishesOrderRepository.findAllByOrderId(id)
                    .stream()
                    .map(LimitDishesOrder::new)
                    .toList();

            return Optional.of(new LimitOrder(order.get(), dishesOrders));
        }

        return Optional.empty();
    }

    @Override
    public List<LimitOrder> findOrdersByCourierId(long id) {
        return orderRepository.findOrderByCourierId(id)
                .stream()
                .map(getLimitOrderFunction())
                .toList();
    }

    /**
     * Преобразует класс Order {@link Order} в {@link LimitOrder}
     *
     * @return урезанный заказ
     */
    private Function<Order, LimitOrder> getLimitOrderFunction() {
        return order -> {
            List<LimitDishesOrder> dishesOrders = dishesOrderRepository.findAllByOrderId(order.getId())
                    .stream()
                    .map(LimitDishesOrder::new)
                    .toList();
            return new LimitOrder(order, dishesOrders);
        };
    }

    /**
     * Преобразует класс Order {@link Order} в {@link LimitOrderRestoran}
     *
     * @return LimitOrderRestoran
     */
    private Function<Order, LimitOrderRestoran> getLimitOrderRestoranFunction() {
        return order -> {
            List<LimitDishesOrder> dishesOrders = dishesOrderRepository.findAllByOrderId(order.getId())
                    .stream()
                    .map(LimitDishesOrder::new)
                    .toList();
            return new LimitOrderRestoran(order, dishesOrders);
        };
    }
}
