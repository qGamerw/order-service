package ru.sber.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private final KafkaTemplate<String, LimitOrderRestaurant> kafkaLimitOrderRestaurantTemplate;

    public RestaurantOrderController(OrderService orderService, KafkaTemplate<String, LimitOrderRestaurant> kafkaLimitOrderRestaurantTemplate) {
        this.orderService = orderService;
        this.kafkaLimitOrderRestaurantTemplate = kafkaLimitOrderRestaurantTemplate;
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateOrderStatus(@PathVariable Long id, @RequestBody LimitOrderRestaurant order) {
        log.info("Обновляет статус заказа с id {}", id);

        order.setId(id);
        var isUpdate = orderService.updateOrderStatus(id, order);

        if (isUpdate) {
            switch (order.getStatus()) {
                case "REVIEW" -> kafkaLimitOrderRestaurantTemplate.send("restaurant_status", order);
                case "COOKING", "COOKED" -> {
                    Optional<LimitOrder> limitOrder = orderService.findOrderById(id);
                    limitOrder.ifPresent(value -> {
                        order.setBranchAddress(value.getBranchAddress());
                        order.setClientId(value.getClientId());
                    });
                    kafkaLimitOrderRestaurantTemplate.send("courier_status", order);
                    kafkaLimitOrderRestaurantTemplate.send("client_status", order);
                }
                case "CANCELLED" -> {
                    Optional<LimitOrder> limitOrder = orderService.findOrderById(id);
                    limitOrder.ifPresent(value -> order.setClientId(value.getClientId()));
                    kafkaLimitOrderRestaurantTemplate.send("restaurant_status", order);
                    kafkaLimitOrderRestaurantTemplate.send("client_status", order);
                }
                case "DELIVERY", "COMPLETED" -> {
                    Optional<LimitOrder> limitOrder = orderService.findOrderById(id);
                    limitOrder.ifPresent(value -> order.setClientId(value.getClientId()));
                    kafkaLimitOrderRestaurantTemplate.send("client_status", order);
                }
            }
            return ResponseEntity.accepted().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    @PreAuthorize("hasRole('client_user')")
    public ResponseEntity<List<LimitOrderRestaurant>> getListOrder() {
        log.info("Получает заказы со статусами на рассмотрении, в процессе готовки и готов");

        List<LimitOrderRestaurant> orders = orderService.getListOrder();

        return ResponseEntity.ok()
                .body(orders);
    }

    @PutMapping("/{id}/payment")
    @PreAuthorize("hasRole('client_user')")
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
    @PreAuthorize("hasRole('client_user')")
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

    @PutMapping("/cancel")
    @PreAuthorize("hasRole('client_user')")
    public ResponseEntity<?> cancellationOfOrderById(@RequestParam String listId, @RequestBody Message message) {
        log.info("Отменяет заказы с id {}", listId);

        var isCancel = orderService.cancellationOfOrderByListId(listId, message.getMessage());

        if (isCancel) {
            return ResponseEntity.accepted()
                    .build();
        } else {
            return ResponseEntity.badRequest()
                    .build();
        }
    }

    @GetMapping("orders/notify/{orderId}")
    @PreAuthorize("hasRole('client_user')")
    public List<LimitOrderRestaurant> getListOrderByNotify(@PathVariable String orderId) {
        log.info("Получает заказы для уведомления для ресторана {}", orderId);

        return orderService.getListOrderByNotify(orderId);
    }

}