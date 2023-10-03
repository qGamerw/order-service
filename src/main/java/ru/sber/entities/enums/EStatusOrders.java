package ru.sber.entities.enums;

import ru.sber.entities.Order;

/**
 * Перечисление для статуса у {@link Order заказа}
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
