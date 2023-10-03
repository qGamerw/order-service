package ru.sber.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sber.entities.DishesOrder;
import ru.sber.entities.Order;
import ru.sber.models.ClientOrder;
import ru.sber.repositories.DishesOrderRepository;
import ru.sber.repositories.OrderRepository;

import java.util.List;

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

    @Override
    public Long createOrder(ClientOrder clientOrder) {
        log.info("Создаем заказ клиента с id {}", clientOrder.getClientId());

        Order order = orderRepository.save(new Order(clientOrder));

        List<DishesOrder> dishesOrders = clientOrder.getDishesId()
                .stream()
                .map(dishId -> new DishesOrder(dishId.getDishId(), dishId.getDishName(), order))
                .toList();

        dishesOrderRepository.saveAll(dishesOrders);

        return order.getId();
    }

    @Override
    public List<Order> getAllOrdersByClientId(long clientId) {
        log.info("Получаем все заказы клиента с id {}", clientId);

        return orderRepository.findOrderByClientId(clientId);
    }
}
