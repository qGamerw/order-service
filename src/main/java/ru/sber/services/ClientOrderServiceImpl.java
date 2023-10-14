package ru.sber.services;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sber.entities.DishOrder;
import ru.sber.entities.Order;
import ru.sber.models.ClientOrder;
import ru.sber.models.LimitOrderClient;
import ru.sber.models.LimitDishesOrder;
import ru.sber.repositories.DishesOrderRepository;
import ru.sber.repositories.OrderRepository;

import java.util.List;
import java.util.function.Function;

@Slf4j
@Service
public class ClientOrderServiceImpl implements ClientOrderService {

    private final DishesOrderRepository dishesOrderRepository;
    private final OrderRepository orderRepository;

    @Autowired
    public ClientOrderServiceImpl(DishesOrderRepository dishesOrderRepository, OrderRepository orderRepository) {
        this.dishesOrderRepository = dishesOrderRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    @Override
    public Long createOrder(ClientOrder clientOrder) {
        log.info("Создаем заказ клиента с id {}", clientOrder);

        Order order = orderRepository.save(new Order(clientOrder));

        List<DishOrder> dishOrders = clientOrder.getListDishesFromOrder()
                .stream()
                .map(dishId -> new DishOrder(dishId.getDishId(), dishId.getDishName(), order , dishId.getQuantity()))
                .toList();

        dishesOrderRepository.saveAll(dishOrders);

        return order.getId();
    }

    @Override
    public List<LimitOrderClient> getAllOrdersByClientId(long clientId) {
        log.info("Получаем все заказы клиента с id {}", clientId);

        return orderRepository.findOrderByClientId(clientId).stream()
                .map(getLimitOrderRestoranFunction())
                .toList();
    }

    /**
     * Преобразует класс Order {@link Order} в {@link LimitOrderClient}
     *
     * @return ClientOrder
     */
    private Function<Order, LimitOrderClient> getLimitOrderRestoranFunction() {
        return order -> {
            List<LimitDishesOrder> dishesOrders = dishesOrderRepository.findAllByOrderId(order.getId())
                    .stream()
                    .map(LimitDishesOrder::new)
                    .toList();
            return new LimitOrderClient(order, dishesOrders);
        };
    }
}