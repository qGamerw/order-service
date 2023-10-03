package ru.sber.orderservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sber.orderservice.entities.Order;
import ru.sber.orderservice.entities.enums.EStatusOrders;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Обрезанный {@link Order заказ} для вывода информации из БД для сервиса Ресторан
 */
@AllArgsConstructor
@Data
@NoArgsConstructor
public class LimitOrderRestoran {
    private Long id;
    private String clientName;
    private String description;
    private int clientPhone;
    private EStatusOrders eStatusOrders;
    private LocalDateTime orderTime;
    private List<LimitDishesOrder> dishesOrders;

    public LimitOrderRestoran(Order order, List<LimitDishesOrder> dishesOrder) {
        this.id = order.getId();
        this.clientName = order.getClientName();
        this.clientPhone = order.getClientPhoneNumber();
        this.eStatusOrders = order.getStatusOrders();
        this.orderTime = order.getOrderTime();
        this.description = order.getDescription();
        this.dishesOrders = dishesOrder;
    }
}
