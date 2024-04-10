package ru.sber.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.sber.entities.DishOrder;
import ru.sber.entities.Order;
import ru.sber.models.ClientOrder;
import ru.sber.models.LimitDishesOrder;
import ru.sber.models.LimitOrderClient;
import ru.sber.repositories.DishesOrderRepository;
import ru.sber.repositories.OrderRepository;

import java.util.List;
import java.util.function.Function;

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
    public ResponseEntity<String> createOrder(ClientOrder clientOrder) {
        Order order = orderRepository.save(new Order(clientOrder));

        List<DishOrder> dishOrders = clientOrder.getListDishesFromOrder()
                .stream()
                .map(dishId -> new DishOrder(dishId.getDishId(), dishId.getDishName(), order, dishId.getQuantity()))
                .toList();

        dishesOrderRepository.saveAll(dishOrders);
        return new ResponseEntity<>("Заказ успешно создался.", HttpStatus.CREATED);
    }

    @Override
    public List<LimitOrderClient> getAllOrdersByClientId(String clientId) {
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