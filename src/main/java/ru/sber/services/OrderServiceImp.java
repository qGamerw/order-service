package ru.sber.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sber.entities.Order;
import ru.sber.entities.enums.EStatusOrders;
import ru.sber.models.LimitDishesOrder;
import ru.sber.models.LimitOrder;
import ru.sber.models.LimitOrderRestoran;
import ru.sber.repositories.DishesOrderRepository;
import ru.sber.repositories.OrderRepository;

import java.time.LocalDateTime;
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
    public boolean updateOrderStatus(long id, LimitOrderRestoran orderRequest) {
        log.info("Обновляет статус заказа с id {} на статус {}", id, orderRequest.getStatus());

        Optional<Order> order = orderRepository.findById(id);

        if (order.isPresent()) {
            switch (orderRequest.getStatus()) {
                case "COOKING" -> {
                    Order currentOrder = order.get();
                    currentOrder.setStartCookingTime(LocalDateTime.now());
                    currentOrder.setBranchOfficeId(orderRequest.getBranchId());
                    currentOrder.setBranchAddress(orderRequest.getBranchAddress());
                }
                case "COOKED" -> order.get().setEndCookingTime(LocalDateTime.now());
                case "DELIVERY" -> order.get().setDeliveryTime(LocalDateTime.now());
            }

            order.get().setStatusOrders(EStatusOrders.valueOf(orderRequest.getStatus()));
            orderRepository.save(order.get());

            return true;
        }

        return false;
    }

    @Override
    public boolean updateOrderCourierId(long idCourier, long idOrder) {
        log.info("Обновляет курьера (id={}) заказа с id {}", idCourier, idOrder);
        Optional<Order> order = orderRepository.findById(idOrder);
        if(order.isPresent()) {
            order.get().setCourierId(idCourier);
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
    public boolean cancellationOfOrderById(Long id, String massage) {
        log.info("Отказывается от заказа с id {}", id);

        Optional<Order> order = orderRepository.findById(id);

        if (order.isPresent()) {
            order.get().setStatusOrders(EStatusOrders.CANCELLED);
            order.get().setRefusalReason(massage);
            orderRepository.save(order.get());

            return true;
        }

        return false;
    }

    @Override
    public List<LimitOrder> findAllActiveOrder() {
        log.info("Получает список заказов со статусом: готовится, готов");

        List<EStatusOrders> eStatusOrdersList = Arrays.asList(
                EStatusOrders.COOKING,
                EStatusOrders.COOKED);

        return orderRepository.findByStatusOrdersInAndCourierIdNull(eStatusOrdersList)
                .stream()
                .map(getLimitOrderFunction())
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

    @Override
    public List<LimitOrder> findOrdersCourierIsDelivering(long id) {
        return orderRepository.findOrderByCourierIdAndStatusOrdersNot(id, EStatusOrders.CANCELLED)
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