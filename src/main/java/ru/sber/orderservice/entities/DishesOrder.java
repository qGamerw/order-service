package ru.sber.orderservice.entities;

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
public class DishesOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dish_id", nullable = false)
    private Long dishId;

    @Column(nullable = false)
    private String dishName;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
}
