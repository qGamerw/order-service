package ru.sber.orderservice.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sber.orderservice.entities.DishesOrder;
import ru.sber.orderservice.entities.Order;
import ru.sber.orderservice.models.OrderResponse;
import ru.sber.orderservice.entities.enums.EStatusOrders;
import ru.sber.orderservice.repositories.ClientOrderRepository;
import ru.sber.orderservice.repositories.DishesOrderRepository;
import ru.sber.orderservice.repositories.OrderRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public List<Order> getAllOrdersByClientId(long clientId) {
        log.info("Получаем все заказы клиента с id {}", clientId);

        return orderRepository.findOrderByClientId(clientId);
    }

    @Override
    public OrderResponse getOrderById(long orderId) {
        log.info("Получаем заказ с id {}", orderId);

        Optional<Order> optionalOrder = orderRepository.findById(orderId);

        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            OrderResponse orderResponse = new OrderResponse();
            orderResponse.setId(order.getId());
            orderResponse.setStatusOrders(order.getStatusOrders());
            orderResponse.setDeliveryTime(order.getDeliveryTime());

            return orderResponse;
        } else {
            return new OrderResponse();
        }
    }

    @Override
    public Order createOrder(
            long clientId,
            String clientName,
            int clientPhoneNumber,
            String description,
            String address,
            Integer flat,
            Integer floor,
            Integer frontDoor,
            List<Long> dishIds
    ) {
        log.info("Создаем заказ клиента с id {}", clientId);

        Order order = new Order();
        order.setClientId(clientId);
        order.setClientPhoneNumber(clientPhoneNumber);
        order.setAddress(address);
        order.setClientName(clientName);
        order.setDescription(description);
        order.setFlat(flat);
        order.setFloor(floor);
        order.setFrontDoor(frontDoor);
        order.setOrderTime(LocalDateTime.now());
        order.setStatusOrders(EStatusOrders.TODO);
        order.setPrice(order.getPrice());

        Order savedOrder = orderRepository.save(order);

        List<DishesOrder> dishesOrders = dishIds.stream()
                .map(dishId -> {
                    DishesOrder dishesOrder = new DishesOrder();
                    dishesOrder.setDishId(dishId);
                    dishesOrder.setOrder(savedOrder);
                    return dishesOrder;
                })
                .collect(Collectors.toList());

        dishesOrderRepository.saveAll(dishesOrders);

        return savedOrder;
    }
}
