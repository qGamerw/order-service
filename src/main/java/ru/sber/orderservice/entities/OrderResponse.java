package ru.sber.orderservice.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sber.orderservice.entities.enums.EStatusOrders;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Сущность заказа с ограниченной информацией
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private long id;
    private EStatusOrders statusOrders;
    private LocalDateTime deliveryTime;
    List<DishesOrder> dishesOrders;
}
