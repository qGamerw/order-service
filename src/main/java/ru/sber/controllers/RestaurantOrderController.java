package ru.sber.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import ru.sber.models.LimitOrder;
import ru.sber.models.LimitOrderRestaurant;
import ru.sber.models.Message;
import ru.sber.services.OrderService;

import java.util.List;
import java.util.Optional;

/**
 * Контроллер для взаимодействия сервером Ресторан
 */
@Slf4j
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("orders")
public class RestaurantOrderController {
    private final OrderService orderService;
    private KafkaTemplate<String, LimitOrderRestaurant> kafkaLimitOrderRestaurantTemplate;

    public RestaurantOrderController(OrderService orderService, KafkaTemplate<String, LimitOrderRestaurant> kafkaLimitOrderRestaurantTemplate) {
        this.orderService = orderService;
        this.kafkaLimitOrderRestaurantTemplate = kafkaLimitOrderRestaurantTemplate;
    }

    @PutMapping
    public void updateOrderStatus(@RequestBody LimitOrderRestaurant order) {
        log.info("Обновляет статус заказа с id {}", order.getId());

        var isUpdate = orderService.updateOrderStatus(order.getId(), order);

        if (isUpdate) {
            switch (order.getStatus()) {
                case "REVIEW"-> kafkaLimitOrderRestaurantTemplate.send("restaurant_status", order);
                case "COOKING", "COOKED" -> {
                    kafkaLimitOrderRestaurantTemplate.send("courier_status", order);
                    kafkaLimitOrderRestaurantTemplate.send("client_status", order);
                }
                case "CANCELLED" -> {
                    kafkaLimitOrderRestaurantTemplate.send("restaurant_status", order);
                    kafkaLimitOrderRestaurantTemplate.send("client_status", order);
                }
                case "DELIVERY", "COMPLETED" -> {
                    kafkaLimitOrderRestaurantTemplate.send("client_status", order);
                }
            }
        } 
    }

    @GetMapping
    public ResponseEntity<List<LimitOrderRestaurant>> getListOrder() {
        log.info("Получает заказы со статусами на рассмотрении, в процессе готовки и готов");

        List<LimitOrderRestaurant> orders = orderService.getListOrder();

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