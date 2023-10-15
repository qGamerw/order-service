package ru.sber.services;

import ru.sber.entities.Order;
import ru.sber.models.LimitOrder;
import ru.sber.models.LimitOrderRestoran;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

/**
 * Сервис для взаимодействия с {@link Order заказом}
 */
public interface OrderService {

    /**
     * Обновляет статус заказа
     *
     * @param id            id заказ
     * @param orderRequest заказ
     * @return boolean
     */
    boolean updateOrderStatus(long id, LimitOrderRestoran orderRequest);

    /**
     * Устанавливает курьера на заказ
     * @param idCourier индификатор курьера
     * @param idOrder индификатор заказа
     * @return true в случае успеха
     */
    boolean updateOrderCourierId(long idCourier, long idOrder);

    /**
     * Получает все заказы со статусом на рассмотрении и в процессе
     *
     * @return List<LimitOrder>
     */
    List<LimitOrderRestoran> getListOrder();

    /**
     * Ищет список заказов которые готовятся или уже готовы, но не доставляются
     *
     * @return список заказов
     */
    List<LimitOrder> findAllActiveOrder();

    /**
     * Ищет список заказов которые готовятся или уже готовы, но не доставляются
     *
     * @return список заказов ограниченный страницей
     */
    Page<LimitOrder> findAllActiveOrdersByPage(int page, int pageSize);

    /**
     * Ищет заказ по id
     *
     * @param id идентификатор заказа
     * @return заказ
     */
    Optional<LimitOrder> findOrderById(long id);

    /**
     * Отменяет заказ
     *
     * @param id id заказа
     * @param massage причина отказа
     */
    boolean cancellationOfOrderById(Long id, String massage);

    /**
     * Возвращает все заказы которые брал курьер
     *
     * @param id идентификатор курьера
     * @return список заказов курьера
     */
    List<LimitOrder> findOrdersByCourierId(long id);

    /**
     * Возвращает заказы, которые доставляет курьер
     * @param id индификатор курьера
     * @return список курьеров
     */
    List<LimitOrder> findOrdersCourierIsDelivering(long id);
}
