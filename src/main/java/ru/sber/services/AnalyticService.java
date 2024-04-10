package ru.sber.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Сервис для вывода аналитики по микросервису Restaurant
 */
public interface AnalyticService {
    /**
     * Определяет сумму которую клиент потратил на заказы
     *
     * @param clientId id клиента
     * @return Сумма
     */
    BigDecimal sumPriceClient(String clientId);

    /**
     * Определяет количество заказов сделанных клиентом
     *
     * @param clientId id клиента
     * @return Количество заказов
     */
    int findCountOrderFromClient(String clientId);

    /**
     * Определяет количество заказов принятых курьером
     *
     * @param courierId id курьера
     * @return Количество заказов
     */
    int findCountOrderFromCourier(String courierId);

    /**
     * Определяет количество заказов принятых работником ресторана
     *
     * @param employeeRestaurantId id работника ресторана
     * @return Количество заказов
     */
    int findCountOrderFromEmployeeRestaurantId(String employeeRestaurantId);

    /**
     * Определяет количество заказов поступивших за месяц
     *
     * @param localDate - дата из которой извлекается месяц и год
     * @return Количество заказов
     */
    long findOrdersPerMonth(LocalDate localDate);

    /**
     * Определяет количество заказов поступивших за год
     *
     * @param year год
     * @return Количество заказов
     */
    List<?> findOrdersPerYear(Integer year);
}
