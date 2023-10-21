package ru.sber.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sber.models.LimitOrderRestoran;
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
public class RestoranOrderController {
    private final OrderService orderService;

    public RestoranOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long id, @RequestBody LimitOrderRestoran order) {
        log.info("Обновляет статус заказа с id {}", id);

        var isUpdate = orderService.updateOrderStatus(id, order);

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

    @PutMapping("/{id}/payment")
    public ResponseEntity<?> paymentOfOrderById(@PathVariable Long id) {
        log.info("Оплачивает заказ с id {}", id);

        boolean isPaid = orderService.paymentOfOrderById(id);

        if (isPaid) {
            return ResponseEntity.accepted()
                    .build();
        } else {
            return ResponseEntity.badRequest()
                    .build();
        }
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<?> cancellationOfOrderById(@PathVariable Long id, @RequestBody Message message) {
        log.info("Отменяет заказ с id {}", id);

        var isCancel = orderService.cancellationOfOrderById(id, message.getMessage());

        if (isCancel) {
            return ResponseEntity.accepted()
                    .build();
        } else {
            return ResponseEntity.badRequest()
                    .build();
        }
    }
}