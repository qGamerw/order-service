package ru.sber.services;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import ru.sber.entities.Order;
import ru.sber.models.LimitOrder;
import ru.sber.models.LimitOrderRestaurant;

import java.util.List;
import java.util.Optional;

/**
 * Сервис для взаимодействия с {@link Order заказом}
 */
public interface OrderService {

    /**
     * Обновляет статус заказа
     *
     * @param id           id заказ
     * @param orderRequest заказ
     * @return Результат
     */
    ResponseEntity<String> updateOrderStatus(long id, LimitOrderRestaurant orderRequest);

    /**
     * Устанавливает курьера на заказ
     *
     * @param idCourier id курьера
     * @param idOrder   id заказа
     * @return Результат
     */
    ResponseEntity<String> updateOrderCourierId(String idCourier, long idOrder);

    /**
     * Получает все заказы со статусом на рассмотрении и в процессе
     *
     * @return список заказов
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
     * @param page     номер страницы
     * @param pageSize размер страницы
     * @return список заказов ограниченный страницей
     */
    Page<LimitOrder> findAllActiveOrdersByPage(int page, int pageSize);

    /**
     * Ищет заказ по id
     *
     * @param id id заказа
     * @return заказ
     */
    Optional<LimitOrder> getOrderById(long id);

    /**
     * Ищет заказ по id
     *
     * @param id id заказа
     * @return Результат
     */
    ResponseEntity<?> getOrderByIdWithCoordinates(long id);

    /**
     * Оплачивает заказ
     *
     * @param idOrder id заказа
     * @return Результат
     */
    ResponseEntity<String> paymentOfOrderById(long idOrder);

    /**
     * Отменяет заказ
     *
     * @param id      id заказа
     * @param massage причина отказа
     * @return Результат
     */
    ResponseEntity<String> cancellationOfOrderById(Long id, String massage);

    /**
     * Отменяет заказы
     *
     * @param listId id заказов
     * @return Результат
     */
    ResponseEntity<String> cancellationOfOrderByListId(String listId, String massage);

    /**
     * Возвращает все заказы которые брал курьер
     *
     * @param id id курьера
     * @return Список заказов
     */
    Page<LimitOrder> findOrdersByCourierId(String id, int page, int pageSize);

    /**
     * Возвращает заказы, которые доставляет курьер
     *
     * @param id id курьера
     * @return Список заказов
     */
    List<LimitOrder> findOrdersCourierIsDelivering(String id);

    /**
     * Получает заказы для уведомления
     *
     * @param ordersId id заказов
     * @return Список заказов
     */
    List<LimitOrderRestaurant> getListOrderByNotify(String ordersId);
}
