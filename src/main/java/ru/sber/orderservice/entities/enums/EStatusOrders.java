package ru.sber.orderservice.entities.enums;

/**
 * Перечисление для статуса у {@link ru.sber.orderservice.entities.Order заказа}
 */
public enum EStatusOrders {
    NOT_PAID,
    REVIEW,
    COOKING,
    COOKED,
    CANCELLED,
    DELIVERY,
    COMPLETED
}
