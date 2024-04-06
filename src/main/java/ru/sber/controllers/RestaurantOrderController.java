package ru.sber.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.sber.models.LimitOrderRestaurant;
import ru.sber.models.Message;
import ru.sber.services.OrderService;

import java.util.List;

/**
 * Контроллер для взаимодействия сервером Ресторан
 */
@Slf4j
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("orders")
public class RestaurantOrderController {
    private final OrderService orderService;

    @Autowired
    public RestaurantOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Обновляет статус заказа с id
     *
     * @param id    id заказа
     * @param order заказ
     * @return Результат
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> updateOrderStatus(@PathVariable Long id, @RequestBody LimitOrderRestaurant order) {
        log.info("Обновляет статус заказа с id {}", id);

        return orderService.updateOrderStatus(id, order);
    }

    /**
     * Получает заказы для ресторана
     *
     * @return Результат
     */
    @GetMapping
    @PreAuthorize("hasRole('client_user')")
    public ResponseEntity<List<LimitOrderRestaurant>> getListOrder() {
        log.info("Получает заказы со статусами на рассмотрении, в процессе готовки и готов");

        return ResponseEntity
                .ok()
                .body(orderService.getListOrder());
    }

    /**
     * Обновляет заказ от курьера
     *
     * @param id id заказа
     * @return Результат
     */
    @PutMapping("/{id}/payment")
    @PreAuthorize("hasRole('client_user')")
    public ResponseEntity<String> paymentOfOrderById(@PathVariable Long id) {
        log.info("Оплачивает заказ с id {}", id);

        return orderService.paymentOfOrderById(id);
    }

    /**
     * Отменяет заказ с id
     *
     * @param id      id заказа
     * @param message причина
     * @return Результат
     */
    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasRole('client_user')")
    public ResponseEntity<String> cancellationOfOrderById(@PathVariable Long id, @RequestBody Message message) {
        log.info("Отменяет заказ с id {}", id);

        return orderService.cancellationOfOrderById(id, message.getMessage());
    }

    /**
     * Обновляет заказ от курьера
     *
     * @param listId  лист заказов
     * @param message причина
     * @return Результат
     */
    @PutMapping("/cancel")
    @PreAuthorize("hasRole('client_user')")
    public ResponseEntity<String> cancellationOfOrderById(@RequestParam String listId, @RequestBody Message message) {
        log.info("Отменяет заказы с id {}", listId);

        return orderService.cancellationOfOrderByListId(listId, message.getMessage());
    }

    /**
     * Получает заказы для уведомления
     *
     * @param orderId id заказа
     * @return Результат
     */
    @GetMapping("orders/notify/{orderId}")
    @PreAuthorize("hasRole('client_user')")
    public List<LimitOrderRestaurant> getListOrderByNotify(@PathVariable String orderId) {
        log.info("Получает заказы для уведомления для ресторана {}", orderId);

        return orderService.getListOrderByNotify(orderId);
    }

}