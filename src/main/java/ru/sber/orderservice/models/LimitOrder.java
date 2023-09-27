package ru.sber.orderservice.models;

import lombok.Data;
import ru.sber.orderservice.entities.Order;
import ru.sber.orderservice.entities.enums.EStatusOrders;

import java.time.LocalDateTime;

/**
 * Обрезанный {@link Order заказ} для вывода информации из БД
 */
@Data
public class LimitOrder {
    private Long id;
    private String clientName;
    private int clientPhone;
    private EStatusOrders eStatusOrders;
    private LocalDateTime orderTime;

    public LimitOrder(Order order) {
        this.id = order.getId();
        this.clientName = order.getClientName();
        this.clientPhone = order.getClientPhoneNumber();
        this.eStatusOrders = order.getStatusOrders();
        this.orderTime = order.getOrderTime();
    }
}
