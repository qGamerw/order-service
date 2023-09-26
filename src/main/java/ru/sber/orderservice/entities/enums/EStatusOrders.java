package ru.sber.orderservice.entities.enums;

/**
 * Перечисление для статуса у {@link ru.sber.entities.Order заказа}
 */
public enum EStatusOrders {
    TODO,
    IN_PROCESS,
    COMPLETE,
    CANCELLED,
    COURIERED
}
