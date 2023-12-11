package ru.sber.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sber.services.AnalyticService;

import java.math.BigDecimal;
import java.time.LocalDate;

@Slf4j
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("analytic")
public class AnalyticController {
    private final AnalyticService analyticService;

    @Autowired
    public AnalyticController(AnalyticService analyticService) {
        this.analyticService = analyticService;
    }

    @GetMapping("/client/{id}")
    public ResponseEntity<Integer> getCountOrderFromClient(@PathVariable("id") long idClient) {
        log.info("Получает количество заказов сделанных клиентом");
        int countOrder = analyticService.findCountOrderFromClient(idClient);

        return ResponseEntity.ok()
                .body(countOrder);

    }

    @GetMapping("/courier/{id}")
    public ResponseEntity<Integer> getCountOrderFromCourier(@PathVariable("id") String idCourier) {
        log.info("Получает количество заказов сделанных курьером");
        int countOrder = analyticService.findCountOrderFromCourier(idCourier);

        return ResponseEntity.ok()
                .body(countOrder);

    }

    @GetMapping("/employee/{id}")
    public ResponseEntity<Integer> getCountOrderFromEmployeeRestaurant(@PathVariable("id") long idEmployeeRestaurant) {
        log.info("Получает количество заказов сделанных работником ресторана");
        int countOrder = analyticService.findCountOrderFromEmployeeRestaurantId(idEmployeeRestaurant);

        return ResponseEntity.ok()
                .body(countOrder);
    }

    @GetMapping("/sum/price/client/{id}")
    public ResponseEntity<BigDecimal> getSumPriceClient(@PathVariable("id") long idClient) {
        log.info("Получает количество заказов сделанных клиентом");
        BigDecimal sumPriceClient = analyticService.sumPriceClient(idClient);
        return ResponseEntity.ok()
                .body(sumPriceClient);
    }

    @GetMapping("/orders/per/month")
    public ResponseEntity<Long> getOrderPerMonth(@RequestParam(required = false) Integer year,
                                                 @RequestParam(required = false) Integer month) {
        log.info("Получает количество заказов поступивших за месяц");

        Long countOrder = analyticService.findOrdersPerMonth(LocalDate.of(
                year == null? LocalDate.now().getYear(): year,
                month == null? LocalDate.now().getMonthValue(): month,
                LocalDate.now().getDayOfMonth()));


        return ResponseEntity.ok()
                .body(countOrder);
    }
}
