package ru.sber.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.sber.models.ClientOrder;
import ru.sber.models.LimitOrderClient;
import ru.sber.services.ClientOrderService;

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

    /**
     * Создает заказ от клиента
     *
     * @param clientOrder заказ клиента
     * @return Результат
     */
    @PostMapping
    @PreAuthorize("hasRole('client_user')")
    public ResponseEntity<String> createOrder(@RequestBody ClientOrder clientOrder) {
        log.info("Создает заказ клиента {}", clientOrder);

        return clientOrderService.createOrder(clientOrder);
    }

    /**
     * Получение заказа по id
     *
     * @param id id заказ
     * @return Результат
     */
    @GetMapping("/client/{id}")
    @PreAuthorize("hasRole('client_user')")
    public ResponseEntity<List<LimitOrderClient>> getOrdersByClientId(@PathVariable String id) {
        log.info("Получает заказы по id клиента {}", id);

        return ResponseEntity
                .ok()
                .body(clientOrderService.getAllOrdersByClientId(id));
    }
}