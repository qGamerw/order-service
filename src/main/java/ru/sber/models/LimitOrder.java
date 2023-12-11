package ru.sber.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sber.entities.Order;
import ru.sber.entities.enums.EStatusOrders;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Обрезанный {@link Order заказ} для вывода информации из БД дял сервиса Курьер
 */
@AllArgsConstructor
@Data
@NoArgsConstructor
public class LimitOrder {
    private Long id;
    private String courierId;
    private String clientName;
    private String description;
    private String clientPhone;
    private EStatusOrders eStatusOrders;
    private LocalDateTime orderTime;
    private String address;
    private String branchAddress;
    private Integer flat;
    private Integer frontDoor;
    private Integer floor;
    private Integer weight;
    private LocalDateTime endCookingTime;
    private List<LimitDishesOrder> dishesOrders;

    public LimitOrder(Order order, List<LimitDishesOrder> dishesOrder) {
        this.id = order.getId();
        this.clientName = order.getClientName();
        this.clientPhone = order.getClientPhoneNumber();
        this.eStatusOrders = order.getStatusOrders();
        this.orderTime = order.getOrderTime();
        this.description = order.getDescription();
        this.address = order.getAddress();
        this.branchAddress = order.getBranchAddress();
        this.flat = order.getFlat();
        this.frontDoor = order.getFrontDoor();
        this.floor = order.getFloor();
        this.weight = order.getWeight();
        this.endCookingTime = order.getEndCookingTime();
        this.courierId = order.getCourierId();
        this.dishesOrders = dishesOrder;
    }
}