package ru.sber.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Список блюд для {@link Order заказа}
 */
@Entity
@AllArgsConstructor
@Data
@NoArgsConstructor
@Table(name = "dishes_orders")
public class DishOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dish_id", nullable = false)
    private Long dishId;

    @Column(nullable = false)
    private String dishName;

    @Column(nullable = false)
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    public DishOrder(Long dishId, String dishName, Order order, int quantity) {
        this.dishId = dishId;
        this.dishName = dishName;
        this.order = order;
        this.quantity = quantity;
    }
}