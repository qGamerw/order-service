package ru.sber.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.sber.models.LimitOrder;
import ru.sber.services.OrderService;

import java.util.List;

/**
 * Контроллер для взаимодействия сервером Курьер
 */
@Slf4j
@RestController
@RequestMapping("orders")
public class CourierOrderController {
    private final OrderService orderService;

    @Autowired
    public CourierOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Обновляет заказ от курьера
     *
     * @param courierId id курьера
     * @param order     заказ
     * @return Результат
     */
    @PutMapping("/courier/{courierId}")
    public ResponseEntity<String> updateOrderCourier(@PathVariable("courierId") String courierId, @RequestBody LimitOrder order) {
        log.info("Обновляет id курьера у заказа с id {}", order.getId());

        return orderService.updateOrderCourierId(courierId, order.getId());
    }

    /**
     * Получает активные заказы для курьера
     *
     * @return Результат
     */
    @GetMapping("/awaiting-delivery")
    @PreAuthorize("hasRole('client_user')")
    public ResponseEntity<List<LimitOrder>> getActiveListOrder() {
        log.info("Получает заказы со статусами готовится и готов");

        return ResponseEntity
                .ok()
                .body(orderService.findAllActiveOrder());
    }

    /**
     * Получает активные заказы по страницам для курьера
     *
     * @param page     номер страницы
     * @param pageSize размер страницы
     * @return Результат
     */
    @GetMapping("/awaiting-delivery/by-page")
    @PreAuthorize("hasRole('client_user')")
    public ResponseEntity<Page<LimitOrder>> getActiveListOrder(@RequestParam int page, @RequestParam int pageSize) {
        log.info("Получает заказы со статусами готовится и готов, ограниченный страницей");

        return ResponseEntity
                .ok()
                .body(orderService.findAllActiveOrdersByPage(page, pageSize));
    }

    /**
     * Получает заказы по id курьера
     *
     * @param id id курьера
     * @return Результат
     */
    @GetMapping("/delivering/courier/{idCourier}")
    public ResponseEntity<List<LimitOrder>> getOrdersIsDelivering(@PathVariable("idCourier") String id) {
        log.info("Получает заказы которые доставляет курьер");

        return ResponseEntity
                .ok()
                .body(orderService.findOrdersCourierIsDelivering(id));
    }

    /**
     * Возвращает заказ по id
     *
     * @param id заказ клиента
     * @return Результат
     */
    @GetMapping("/{idOrder}")
    @PreAuthorize("hasRole('client_user')")
    public ResponseEntity<?> getOrderById(@PathVariable("idOrder") long id) {
        log.info("Возвращает заказ по id: {}", id);

        return orderService.getOrderByIdWithCoordinates(id);
    }

    /**
     * Создает заказ от клиента
     *
     * @param id       id курьера
     * @param page     номер страницы
     * @param pageSize размер страницы
     * @return Результат
     */
    @GetMapping("/courier/{idCourier}")
    public ResponseEntity<Page<LimitOrder>> getAllOrdersByCourierId(@PathVariable("idCourier") String id, @RequestParam int page, @RequestParam int pageSize) {
        log.info("Возвращает все заказы курьера с id: {}", id);

        return ResponseEntity
                .ok()
                .body(orderService.findOrdersByCourierId(id, page, pageSize));
    }
}