package ru.sber.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sber.entities.DishOrder;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class LimitDishesOrder {
    private Long orderId;
    private Long dishId;
    private String dishName;
    private Integer quantity;

    public LimitDishesOrder(DishOrder dishOrder) {
        this.orderId = dishOrder.getOrder().getId();
        this.dishId = dishOrder.getDishId();
        this.dishName = dishOrder.getDishName();
        this.quantity = dishOrder.getQuantity();
    }
}