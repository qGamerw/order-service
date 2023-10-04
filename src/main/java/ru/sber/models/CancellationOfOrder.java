package ru.sber.models;

import lombok.Data;

/**
 * Класс для получения ответа для отказа
 */
@Data
public class CancellationOfOrder {
    private Long id;
    private String massage;
}