package ru.sber.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sber.entities.Order;
import ru.sber.entities.enums.EStatusOrders;
import ru.sber.repositories.OrderRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Slf4j
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

    @Override
    public List<?> findOrdersPerYear(Integer year) {

        List<LocalDateTime> ordersLocalDateTime = new ArrayList<>();

        // Заполняем список с данными LocalDateTime
        ordersLocalDateTime.add(LocalDateTime.of(2022, Month.JANUARY, 15, 10, 30));
        ordersLocalDateTime.add(LocalDateTime.of(2022, Month.FEBRUARY, 25, 12, 45));
        ordersLocalDateTime.add(LocalDateTime.of(2022, Month.JANUARY, 5, 8, 15));
        ordersLocalDateTime.add(LocalDateTime.of(2022, Month.MARCH, 10, 9, 0));
        ordersLocalDateTime.add(LocalDateTime.of(2022, Month.FEBRUARY, 20, 18, 30));

//        List<LocalDateTime> ordersLocalDateTime = orderRepository.findAll().stream()
//                .filter(order ->
//                        (order.getOrderTime().getYear() == LocalDate.of(year, 1, 1).getYear()))
//                .map(item -> item.getEndCookingTime())
//                .toList();

        Map<Month, Long> countByMonth = orderRepository.findAll().stream()
                .filter(order -> (order.getOrderTime().getYear() == LocalDate.of(year, 1, 1).getYear()))
                .map(Order::getEndCookingTime)
                .collect(Collectors.groupingBy(LocalDateTime::getMonth, Collectors.counting()));

        // Сгруппируем элементы по месяцу и подсчитаем количество для каждого месяца
//        Map<Month, Long> countByMonth = ordersLocalDateTime.stream()
//                .collect(Collectors.groupingBy(LocalDateTime::getMonth, Collectors.counting()));

//         Выводим результаты
        countByMonth.forEach((month, count) -> System.out.println("Месяц: " + month + ", Количество: " + count));

//        log.info("{}", ordersLocalDateTime);

        return List.of();
    }
}
