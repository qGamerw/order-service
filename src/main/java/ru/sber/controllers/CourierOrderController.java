package ru.sber.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sber.models.LimitOrderRestoran;
import ru.sber.models.LimitOrder;
import ru.sber.services.OrderService;

import java.util.List;
import java.util.Optional;

/**
 * Контроллер для взаимодействия сервером Курьер
 */
@Slf4j
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("orders")
public class CourierOrderController {
    private final OrderService orderService;

    @Autowired
    public CourierOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PutMapping("/courier")
    public ResponseEntity<?> updateOrderCourier(@RequestBody LimitOrder order) {
        log.info("Обновляет id курьера у заказа с id {}", order.getId());

        var isUpdate = orderService.updateOrderCourierId(order.getCourierId(), order.getId());

        if (isUpdate) {
            return ResponseEntity.ok()
                    .build();
        } else {
            return ResponseEntity.badRequest()
                    .build();
        }
    }

    @GetMapping("/awaiting-delivery")
    public ResponseEntity<List<LimitOrder>> getActiveListOrder() {
        log.info("Получает заказы со статусами готовится и готов");

        List<LimitOrder> orders = orderService.findAllActiveOrder();

        return ResponseEntity.ok()
                .body(orders);
    }

    @GetMapping("/delivering/courier/{idCourier}")
    public ResponseEntity<List<LimitOrder>> getOrdersIsDelivering(@PathVariable("idCourier") long id) {
        log.info("Получает заказы со статусами готовится и готов");

        List<LimitOrder> orders = orderService.findOrdersCourierIsDelivering(id);

        return ResponseEntity.ok()
                .body(orders);
    }

    @GetMapping("/{idOrder}")
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
    public ResponseEntity<List<LimitOrder>> getAllOrdersByCourierId(@PathVariable("idCourier") long id) {
        log.info("Возвращает все заказы курьера с id: {}", id);

        List<LimitOrder> orders = orderService.findOrdersByCourierId(id);

        return ResponseEntity.ok()
                .body(orders);
    }
}