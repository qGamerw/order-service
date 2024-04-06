package ru.sber.models;

import lombok.Data;

/**
 * Модель для приема сообщения для отказа {@link ru.sber.entities.Order заказа}
 */
@Data
public class Message {

    private String message;
}
