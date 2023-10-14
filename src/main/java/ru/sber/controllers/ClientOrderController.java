package ru.sber.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sber.models.ClientOrder;
import ru.sber.models.LimitOrderClient;
import ru.sber.services.ClientOrderService;

import java.net.URI;
import java.util.List;

/**
 * Контроллер для взаимодействия сервером Клиент
 */
@Slf4j
@RestController
@RequestMapping("/orders")
public class ClientOrderController {

    private final ClientOrderService clientOrderService;

    @Autowired
    public ClientOrderController(ClientOrderService clientOrderService) {
        this.clientOrderService = clientOrderService;
    }

    @PostMapping
    public ResponseEntity<Void> createOrder(@RequestBody ClientOrder clientOrder) {
        log.info("Создает заказ клиента {}", clientOrder);

        return ResponseEntity.created(URI.create("orders/" + clientOrderService.createOrder(clientOrder)))
                .build();
    }

    @GetMapping("/client/{id}")
    public ResponseEntity<List<LimitOrderClient>> getOrdersByClientId(@PathVariable long id) {

        log.info("Получает заказы по id клиента {}", id);
        List<LimitOrderClient> orders = clientOrderService.getAllOrdersByClientId(id);
        log.info("Заказы пользователя: {}", orders);

        return ResponseEntity.ok()
                .body(orders);
    }
}