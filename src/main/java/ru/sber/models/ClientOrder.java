package ru.sber.models;

import lombok.Data;
import ru.sber.entities.DishOrder;

import java.math.BigDecimal;
import java.util.List;

/**
 * Класс для создания заказа
 */
@Data
public class ClientOrder {
    private long clientId;
    private String clientName;
    private String clientPhoneNumber;
    private BigDecimal totalPrice;
    private Integer totalWeight;
    private String description;
    private String address;
    private Integer flat;
    private Integer floor;
    private Integer frontDoor;
    private List<DishOrder> listDishesFromOrder;
}