package ru.sber.services;

import org.springframework.http.ResponseEntity;
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
     * @return Результат
     */
    ResponseEntity<String> createOrder(ClientOrder clientOrder);

    /**
     * Получает все заказы клиента по id
     *
     * @param clientId id клиента
     * @return Список заказов клиента
     */
    List<LimitOrderClient> getAllOrdersByClientId(String clientId);
}