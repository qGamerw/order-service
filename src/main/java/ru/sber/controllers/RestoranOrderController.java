package ru.sber.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sber.models.CancellationOfOrder;
import ru.sber.models.LimitOrderRestoran;
import ru.sber.services.OrderService;

import java.util.List;

/**
 * Контроллер для взаимодействия сервером Ресторан
 */
@Slf4j
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("orders")
public class RestoranOrderController {
    private final OrderService orderService;

    public RestoranOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PutMapping
    public ResponseEntity<?> updateOrderStatus(@RequestBody LimitOrderRestoran order) {
        log.info("Обновляет статус заказа с id {}", order);

        var isUpdate = orderService.updateOrderStatus(order.getId(), order. getStatus());

        if (isUpdate) {
            return ResponseEntity.ok()
                    .build();
        } else {
            return ResponseEntity.badRequest()
                    .build();
        }
    }

    @GetMapping
    public ResponseEntity<List<LimitOrderRestoran>> getListOrder() {
        log.info("Получает заказы со статусами на рассмотрении, в процессе готовки и готов");

        List<LimitOrderRestoran> orders = orderService.getListOrder();

        return ResponseEntity.ok()
                .body(orders);
    }

    @PutMapping("/cancel")
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