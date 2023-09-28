package ru.sber.orderservice.services;

import ru.sber.orderservice.entities.Order;
import ru.sber.orderservice.entities.enums.EStatusOrders;
import ru.sber.orderservice.models.CancellationOfOrder;
import ru.sber.orderservice.models.LimitOrder;

import java.util.List;
import java.util.Optional;

/**
 * Сервис для взаимодействия с {@link Order заказом}
 */
public interface OrderService {

    /**
     * Обновляет статус заказа
     *
     * @param id            id заказ
     * @param eStatusOrders статус
     * @return boolean
     */
    boolean updateOrderStatus(long id, EStatusOrders eStatusOrders);

    /**
     * Получает все заказы со статусом на рассмотрении и в процессе
     *
     * @return List<LimitOrder>
     */
    List<LimitOrder> getListOrder();

    /**
     * Ищет список заказов которые готовятся или уже готовы, но не доставляются
     * @return список заказов
     */
    List<LimitOrder> findAllActiveOrder();

    /**
     * Ищет заказ по id
     * @param id
     * @return
     */
    Optional<LimitOrder> findOrderById(long id);

    /**
     * Отменяет заказ
     *
     * @param cancellationOfOrder id заказа и причина отказа
     */
    boolean cancellationOfOrderById(CancellationOfOrder cancellationOfOrder);
}
