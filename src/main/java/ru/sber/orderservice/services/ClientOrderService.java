package ru.sber.orderservice.services;

import ru.sber.orderservice.entities.Order;
import ru.sber.orderservice.models.OrderResponse;

import java.util.List;

/**
 * Сервис для взаимодействия с {@link Order заказом клиента}
 */
public interface ClientOrderService {
    /**
     * Создает заказ
     *
     * @param clientId id клиента
     * @param clientName имя клиента
     * @param clientPhoneNumber номер телефона клиента
     * @param description описание заказа
     * @param address адрес доставки
     * @param flat номер квартиры
     * @param floor этаж
     * @param frontDoor номер подъезда
     * @param dishIds список id блюд в заказе
     * @return созданный заказ
     */
    Order createOrder(
            long clientId,
            String clientName,
            int clientPhoneNumber,
            String description,
            String address,
            Integer flat,
            Integer floor,
            Integer frontDoor,
            List<Long> dishIds
    );

    /**
     * Получает заказ с ограниченной информацией по id
     *
     * @param orderId id заказа
     * @return объект заказа
     */
    OrderResponse getOrderById(long orderId);

    /**
     * Получает все заказы клиента по его id
     *
     * @param clientId id клиента
     * @return список заказов клиента
     */
    List<Order> getAllOrdersByClientId(long clientId);
}
