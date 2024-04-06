package ru.sber.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;
import ru.sber.entities.Order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Модель для вывода заказа
 */
@Data
public class LimitOrderClient {
    private long id;
    private String clientId;
    private String clientName;
    private String clientPhoneNumber;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime orderTime;

    private BigDecimal totalPrice;
    private Integer weight;
    private String description;
    private String refusalReason;
    private String address;
    private Integer flat;
    private Integer floor;
    private Integer frontDoor;
    private String status;
    private List<LimitDishesOrder> listDishesFromOrder;

    public LimitOrderClient(Order order, List<LimitDishesOrder> dishesOrder) {
        this.id = order.getId();
        this.clientId = order.getClientId();
        this.clientName = order.getClientName();
        this.clientPhoneNumber = order.getClientPhoneNumber();
        this.orderTime = order.getOrderTime();
        this.totalPrice = order.getTotalPrice();
        this.weight = order.getWeight();
        this.description = order.getDescription();
        this.refusalReason = order.getRefusalReason();
        this.address = order.getAddress();
        this.flat = order.getFlat();
        this.floor = order.getFloor();
        this.frontDoor = order.getFrontDoor();
        this.status = order.getStatusOrders().name();
        this.listDishesFromOrder = dishesOrder;
    }

}