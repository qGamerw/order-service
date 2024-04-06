package ru.sber.entities.enums;

import ru.sber.entities.Order;

/**
 * Статус у {@link Order заказа}
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
