package ru.sber.orderservice.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sber.orderservice.models.CancellationOfOrder;
import ru.sber.orderservice.models.LimitOrder;
import ru.sber.orderservice.services.OrderService;

import java.util.List;

/**
 * Контроллер для взаимодействия {@link ru.sber.orderservice.entities.Order заказами}
 */
@Slf4j
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PutMapping
    public ResponseEntity<?> updateOrderStatus(@RequestBody LimitOrder order) {
        log.info("Обновляет статус заказа с id {}", order.getId());

        var isUpdate = orderService.updateOrderStatus(order.getId(), order.getEStatusOrders());

        if (isUpdate) {
            return ResponseEntity.ok()
                    .build();
        } else {
            return ResponseEntity.badRequest()
                    .build();
        }
    }

    @GetMapping
    public ResponseEntity<List<LimitOrder>> getListOrder() {
        log.info("Получает заказы со статусами на рассмотрении и в процессе");

        List<LimitOrder> orders = orderService.getListOrder();

        return ResponseEntity.ok()
                .body(orders);
    }

    @GetMapping
    public ResponseEntity<?> cancellationOfOrderById(@RequestBody CancellationOfOrder cancellationOfOrder) {
        log.info("Отменяет заказ");

        var isCancel = orderService.cancellationOfOrderById(cancellationOfOrder);

        if (isCancel) {
            return ResponseEntity.accepted()
                    .build();
        } else {
            return ResponseEntity.badRequest()
                    .build();
        }
    }

}
