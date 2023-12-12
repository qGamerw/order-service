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
import java.util.Optional;

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

    @PutMapping("/courier/{courierId}")
    public ResponseEntity<?> updateOrderCourier(@PathVariable("courierId") String courierId, @RequestBody LimitOrder order) {
        log.info("Обновляет id курьера у заказа с id {}", order.getId());

        var isUpdate = orderService.updateOrderCourierId(courierId, order.getId());

        if (isUpdate) {
            return ResponseEntity.ok()
                    .build();
        } else {
            return ResponseEntity.badRequest()
                    .build();
        }
    }

    @GetMapping("/awaiting-delivery")
    @PreAuthorize("hasRole('client_user')")
    public ResponseEntity<List<LimitOrder>> getActiveListOrder() {
        log.info("Получает заказы со статусами готовится и готов");

        List<LimitOrder> orders = orderService.findAllActiveOrder();

        return ResponseEntity.ok()
                .body(orders);
    }

    @GetMapping("/awaiting-delivery/by-page")
    @PreAuthorize("hasRole('client_user')")
    public ResponseEntity<Page<LimitOrder>> getActiveListOrder(@RequestParam int page, @RequestParam int pageSize) {
        log.info("Получает заказы со статусами готовится и готов, ограниченный страницей");

        Page<LimitOrder> orders = orderService.findAllActiveOrdersByPage(page, pageSize);

        return ResponseEntity.ok()
                .body(orders);
    }

    @GetMapping("/delivering/courier/{idCourier}")
    public ResponseEntity<List<LimitOrder>> getOrdersIsDelivering(@PathVariable("idCourier") String id) {
        log.info("Получает заказы которые доставляет курьер");

        List<LimitOrder> orders = orderService.findOrdersCourierIsDelivering(id);

        return ResponseEntity.ok()
                .body(orders);
    }

    @GetMapping("/{idOrder}")
    @PreAuthorize("hasRole('client_user')")
    public ResponseEntity<LimitOrder> getOrderById(@PathVariable("idOrder") long id) {
        log.info("Возвращает заказ по id: {}", id);

        Optional<LimitOrder> order = orderService.findOrderById(id);
        return order.map(
                        limitOrderCourier -> ResponseEntity.ok()
                                .body(limitOrderCourier))
                .orElseGet(() -> ResponseEntity.notFound()
                        .build()
                );
    }

    @GetMapping("/courier/{idCourier}")
    public ResponseEntity<Page<LimitOrder>> getAllOrdersByCourierId(@PathVariable("idCourier") String id, @RequestParam int page, @RequestParam int pageSize) {
        log.info("Возвращает все заказы курьера с id: {}", id);

        Page<LimitOrder> orders = orderService.findOrdersByCourierId(id, page, pageSize);

        return ResponseEntity.ok()
                .body(orders);
    }
}