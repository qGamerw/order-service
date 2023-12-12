package ru.sber.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sber.entities.Order;
import ru.sber.entities.enums.EStatusOrders;
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
    public BigDecimal sumPriceClient(String clientId) {
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
    public int findCountOrderFromClient(String clientId) {
        return orderRepository.countOrderByClientId(clientId);
    }

    @Override
    public int findCountOrderFromCourier(String courierId) {
        return orderRepository.countOrderByCourierId(courierId);
    }

    @Override
    public int findCountOrderFromEmployeeRestaurantId(String employeeRestaurantId) {

        return orderRepository.countOrderByEmployeeRestaurantId(employeeRestaurantId);
    }

    @Override
    public long findOrdersPerMonth(LocalDate localDate) {
        return orderRepository.findAll().stream()
                .filter(order ->
                        (order.getOrderTime().getMonth().equals(localDate.getMonth()) ||
                                order.getOrderTime().getMonth().equals(localDate.minusMonths(1).getMonth())
                                && order.getOrderTime().getDayOfMonth() == localDate.getDayOfMonth())

                                && (order.getOrderTime().getYear() == localDate.getYear())
                                && order.getStatusOrders().equals(EStatusOrders.COMPLETED))
                .count();
    }
}
