package ru.sber.orderservice.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sber.orderservice.entities.Order;
import ru.sber.orderservice.models.CancellationOfOrder;
import ru.sber.orderservice.models.LimitOrder;
import ru.sber.orderservice.services.OrderService;

import java.net.URI;
import java.util.List;
import java.util.Optional;

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

    @GetMapping("/awaiting-delivery")
    public ResponseEntity<List<LimitOrder>> getActiveListOrder() {
        log.info("Получает заказы со статусами готовится и готов");

        List<LimitOrder> orders = orderService.findAllActiveOrder();

        return ResponseEntity.ok()
                .body(orders);
    }

    @GetMapping("/{idOrder}")
    public ResponseEntity<LimitOrder> cancellationOfOrderById(@PathVariable("idOrder") long id) {
        log.info("Возвращает заказ по id: {}", id);
        Optional<LimitOrder> order = orderService.findOrderById(id);
        return order.map(
                limitOrder -> ResponseEntity.ok()
                        .body(limitOrder))
                .orElseGet(
                        () -> ResponseEntity.notFound().build()
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

