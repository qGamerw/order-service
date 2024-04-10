package ru.sber.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sber.entities.Order;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Обрезанный {@link Order заказ} для вывода информации из БД для сервиса Ресторан
 */
@AllArgsConstructor
@Data
@NoArgsConstructor
public class LimitOrderRestaurant {
    private Long id;
    private String clientId;
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
    private String employeeRestaurantId;
    private List<LimitDishesOrder> dishesOrders;

    public LimitOrderRestaurant(Order order, List<LimitDishesOrder> dishesOrder) {
        this.id = order.getId();
        this.clientId = order.getClientId();
        this.clientName = order.getClientName();
        this.clientPhone = order.getClientPhoneNumber();
        this.status = order.getStatusOrders().toString();
        this.orderTime = order.getOrderTime();
        this.orderCookingTime = order.getStartCookingTime();
        this.orderCookedTime = order.getEndCookingTime();
        this.description = order.getDescription();
        this.address = order.getAddress();
        this.branchAddress = order.getBranchAddress();
        this.branchId = order.getBranchOfficeId();
        this.employeeRestaurantId = order.getEmployeeRestaurantId();
        this.dishesOrders = dishesOrder;
    }
}