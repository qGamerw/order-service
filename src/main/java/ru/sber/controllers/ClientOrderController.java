package ru.sber.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sber.entities.Order;
import ru.sber.models.ClientOrder;
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
        log.info("Создает заказ c id клиента {}", clientOrder.getClientId());

        return ResponseEntity.created(URI.create("orders/" + clientOrderService.createOrder(clientOrder)))
                .build();
    }

    @GetMapping("/client/{id}")
    public ResponseEntity<?> getOrdersByClientId(@PathVariable long id) {
        log.info("Получает заказы по id клиента {}", id);

        List<Order> order = clientOrderService.getAllOrdersByClientId(id);

        return ResponseEntity.ok()
                .body(order);
    }
}