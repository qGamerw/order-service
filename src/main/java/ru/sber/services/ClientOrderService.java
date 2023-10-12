package ru.sber.services;

import ru.sber.entities.Order;
import ru.sber.models.ClientOrder;
import ru.sber.models.LimitOrderClient;

import java.util.List;

/**
 * Сервис для взаимодействия с {@link Order заказом клиента}
 */
public interface ClientOrderService {
    /**
     * Создает заказ
     *
     * @param clientOrder заказ клиента
     * @return созданный заказ
     */
    Long createOrder(ClientOrder clientOrder);

    /**
     * Получает все заказы клиента по его id
     *
     * @param clientId id клиента
     * @return список заказов клиента
     */
    List<LimitOrderClient> getAllOrdersByClientId(long clientId);
}