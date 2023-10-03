package ru.sber.orderservice.models;

import lombok.Data;
import ru.sber.orderservice.entities.DishesOrder;

import java.util.List;

/**
 * Класс для создания заказа
 */
@Data
public class ClientOrder {
    private long clientId;
    private String clientName;
    private int clientPhoneNumber;
    private String description;
    private String address;
    private Integer flat;
    private Integer floor;
    private Integer frontDoor;
    private List<DishesOrder> dishesId;
}
