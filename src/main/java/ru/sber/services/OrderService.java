package ru.sber.services;

import ru.sber.entities.Order;
import ru.sber.models.LimitOrder;
import ru.sber.models.LimitOrderRestaurant;

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
    boolean updateOrderStatus(long id, LimitOrderRestaurant orderRequest);

    /**
     * Устанавливает курьера на заказ
     * @param idCourier индификатор курьера
     * @param idOrder индификатор заказа
     * @return true в случае успеха
     */
    boolean updateOrderCourierId(String idCourier, long idOrder);

    /**
     * Получает все заказы со статусом на рассмотрении и в процессе
     *
     * @return List<LimitOrder>
     */
    List<LimitOrderRestaurant> getListOrder();

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

    Optional<LimitOrder> findOrderByIdWithCoordinates(long id);

    /**
     * Оплачивает заказ и изменяет статус заказа
     *
     * @param idOrder идентификатор заказа
     */
    boolean paymentOfOrderById(long idOrder);

    /**
     * Отменяет заказ
     *
     * @param id id заказа
     * @param massage причина отказа
     */
    boolean cancellationOfOrderById(Long id, String massage);

    /**
     * Отменяет заказы
     *
     * @param listId listId заказов
     */
    boolean cancellationOfOrderByListId(String listId, String massage);

    /**
     * Возвращает все заказы которые брал курьер
     *
     * @param id идентификатор курьера
     * @return список заказов курьера
     */
    Page<LimitOrder> findOrdersByCourierId(String id, int page, int pageSize);

    /**
     * Возвращает заказы, которые доставляет курьер
     * @param id индификатор курьера
     * @return список курьеров
     */
    List<LimitOrder> findOrdersCourierIsDelivering(String id);

    /**
     * Получает заказы для уведомления
     * @param ordersId list of order id
     * @return List<LimitOrder>
     */
    List<LimitOrderRestaurant> getListOrderByNotify(String ordersId);
}
