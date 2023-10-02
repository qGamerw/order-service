package ru.sber.orderservice.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sber.orderservice.entities.Order;
import ru.sber.orderservice.models.OrderResponse;
import ru.sber.orderservice.exception.OrderNotFoundException;
import ru.sber.orderservice.services.ClientOrderService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/orders")
public class ClientOrderController {

    private final ClientOrderService clientOrderService;

    @Autowired
    public ClientOrderController(ClientOrderService clientOrderService) {
        this.clientOrderService = clientOrderService;
    }

    @PostMapping("/create")
    public Order createOrder(
            @RequestParam long clientId,
            @RequestParam String clientName,
            @RequestParam int clientPhoneNumber,
            @RequestParam String description,
            @RequestParam String address,
            @RequestParam Integer flat,
            @RequestParam Integer floor,
            @RequestParam Integer frontDoor,
            @RequestParam List<Long> dishIds
    ) {
        return clientOrderService.createOrder(
                clientId,
                clientName,
                clientPhoneNumber,
                description,
                address,
                flat,
                floor,
                frontDoor,
                dishIds
        );
    }

    @GetMapping("/{clientId}")
    public List<Order> getAllOrdersByClientId(@PathVariable long clientId) {
        return clientOrderService.getAllOrdersByClientId(clientId);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrderById(@PathVariable long orderId) {
        try {
            log.info("Получаем заказ с id {}", orderId);
            OrderResponse clientResponse = clientOrderService.getOrderById(orderId);
            return ResponseEntity.ok(clientResponse);
        } catch (OrderNotFoundException ex) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ex.getMessage());
        }
    }
}
