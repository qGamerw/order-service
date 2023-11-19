package ru.sber.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sber.entities.Order;
import ru.sber.repositories.OrderRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class AnalyticServiceImpl implements AnalyticService {

    private final OrderRepository orderRepository;

    @Autowired
    public AnalyticServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public BigDecimal sumPriceClient(long clientId) {
        Optional<Order> optionalOrder = orderRepository.findOrderByClientId(clientId).stream().reduce((order, order2) -> {

            order.setTotalPrice(order.getTotalPrice().add(order2.getTotalPrice()));
            return order;
        });
        if (optionalOrder.isPresent()) {
            return optionalOrder.get().getTotalPrice();
        }
        return BigDecimal.valueOf(0.0);
    }

    @Override
    public int findCountOrderFromClient(long clientId) {
        return orderRepository.countOrderByClientId(clientId);
    }

    @Override
    public int findCountOrderFromCourier(long courierId) {
        return orderRepository.countOrderByCourierId(courierId);
    }

    @Override
    public int findCountOrderFromEmployeeRestaurantId(long employeeRestaurantId) {
        return orderRepository.countOrderByEmployeeRestaurantId(employeeRestaurantId);
    }

    @Override
    public long findOrdersPerMonth(LocalDate localDate) {
        return orderRepository.findAll().stream()
                .filter(order ->
                        order.getOrderTime().getMonth().equals(localDate.getMonth())
                                && (order.getOrderTime().getYear() == localDate.getYear()))
                .count();
    }
}
