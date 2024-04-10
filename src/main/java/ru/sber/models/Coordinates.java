package ru.sber.models;

import java.math.BigDecimal;

/**
 * Модель для хранения координат у заказа
 */
public record Coordinates(BigDecimal latitude, BigDecimal longitude) {
}