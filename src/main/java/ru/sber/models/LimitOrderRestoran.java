package ru.sber.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sber.entities.Order;
import ru.sber.entities.enums.EStatusOrders;

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
    private String clientPhone;
    private String status;
    private LocalDateTime orderTime;
    private LocalDateTime orderCookingTime;
    private LocalDateTime orderCookedTime;
    private String address;
    private String branchAddress;
    private Long branchId;
    private List<LimitDishesOrder> dishesOrders;

    public LimitOrderRestoran(Order order, List<LimitDishesOrder> dishesOrder) {
        this.id = order.getId();
        this.clientName = order.getClientName();
        this.clientPhone = order.getClientPhoneNumber();
        this.status = order.getStatusOrders().toString();
        this.orderTime = order.getOrderTime();
        this.orderCookingTime = order.getStartCookingTime();
        this.orderCookedTime = order.getEndCookingTime();
        this.description = order.getDescription();
        this.dishesOrders = dishesOrder;
        this.address = order.getAddress();
        this.branchAddress = order.getBranchAddress();
        this.branchId = order.getBranchOfficeId();
    }
}