package ru.sber.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sber.entities.DishesOrder;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class LimitDishesOrder {
    private Long orderId;
    private Long dishId;
    private String dishName;

    public LimitDishesOrder(DishesOrder dishesOrder) {
        this.orderId = dishesOrder.getOrder().getId();
        this.dishId = dishesOrder.getDishId();
        this.dishName = dishesOrder.getDishName();
    }
}
