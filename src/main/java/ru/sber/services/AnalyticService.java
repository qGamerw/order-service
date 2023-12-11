package ru.sber.services;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Сервис для аналитических запросов
 */
public interface AnalyticService {
    /**
     * Определяет сумму которую клиент потратил на заказы
     * @param clientId id клиента
     * @return сумма
     */
    BigDecimal sumPriceClient(long clientId);

    /**
     * Определяет количество заказов сделанных клиентом
     * @param clientId id клиента
     * @return количество заказов
     */
    int findCountOrderFromClient(long clientId);

    /**
     * Определяет количество заказов принятых курьером
     * @param courierId id курьера
     * @return количество заказов
     */
    int findCountOrderFromCourier(long courierId);

    /**
     * Определяет количество заказов принятых работником ресторана
     * @param employeeRestaurantId id работника ресторана
     * @return количество заказов
     */
    int findCountOrderFromEmployeeRestaurantId(long employeeRestaurantId);

    /**
     * Определяет количество заказов поступивших за месяц
     * @param localDate - дата из которой извлекается месяц и год
     * @return количество заказов
     */
    long findOrdersPerMonth(LocalDate localDate);
}
