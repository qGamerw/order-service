package ru.sber.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.sber.services.AnalyticService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Контроллер для взаимодействия с аналитикой
 */
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

    /**
     * Получает количество заказов у клиента
     *
     * @param idClient id заказа
     * @return Результат
     */
    @GetMapping("/client/{id}")
    @PreAuthorize("hasRole('client_user')")
    public ResponseEntity<Integer> getCountOrderFromClient(@PathVariable("id") String idClient) {
        log.info("Получает количество заказов сделанных клиентом");

        return ResponseEntity
                .ok()
                .body(analyticService.findCountOrderFromClient(idClient));
    }

    /**
     * Получает количество заказов у курьера
     *
     * @param idCourier id курьера
     * @return Результат
     */
    @GetMapping("/courier/{id}")
    @PreAuthorize("hasRole('client_user')")
    public ResponseEntity<Integer> getCountOrderFromCourier(@PathVariable("id") String idCourier) {
        log.info("Получает количество заказов сделанных курьером");

        return ResponseEntity
                .ok()
                .body(analyticService.findCountOrderFromCourier(idCourier));
    }

    /**
     * Получает количество заказов у сотрудника ресторана
     *
     * @param idEmployeeRestaurant id сотрудника ресторана
     * @return Результат
     */
    @GetMapping("/employee/{id}")
    @PreAuthorize("hasRole('client_user')")
    public ResponseEntity<Integer> getCountOrderFromEmployeeRestaurant(@PathVariable("id") String idEmployeeRestaurant) {
        log.info("Получает количество заказов сделанных работником ресторана");

        return ResponseEntity
                .ok()
                .body(analyticService.findCountOrderFromEmployeeRestaurantId(idEmployeeRestaurant));
    }

    /**
     * Получает сумму, которую клиент потратил на заказы
     *
     * @param idClient id клиента
     * @return Результат
     */
    @GetMapping("/sum/price/client/{id}")
    @PreAuthorize("hasRole('client_user')")
    public ResponseEntity<BigDecimal> getSumPriceClient(@PathVariable("id") String idClient) {
        log.info("Получает сумму, которую клиент потратил на заказы.");

        return ResponseEntity
                .ok()
                .body(analyticService.sumPriceClient(idClient));
    }

    /**
     * Получает количество заказов за месяц
     *
     * @param year  год
     * @param month месяц
     * @return Результат
     */
    @GetMapping("/orders/per/month")
    @PreAuthorize("hasRole('client_user')")
    public ResponseEntity<Long> getOrderPerMonth(@RequestParam(required = false) Integer year,
                                                 @RequestParam(required = false) Integer month) {
        log.info("Получает количество заказов поступивших за месяц");

        return ResponseEntity
                .ok()
                .body(analyticService.findOrdersPerMonth(LocalDate.of(
                        year == null ? LocalDate.now().getYear() : year,
                        month == null ? LocalDate.now().getMonthValue() : month,
                        LocalDate.now().getDayOfMonth())));
    }

    /**
     * Получает количество заказов за год
     *
     * @param year год
     * @return Результат
     */
    @GetMapping("/orders/per/year")
    @PreAuthorize("hasRole('client_user')")
    public ResponseEntity<List<?>> getOrderPerYear(@RequestParam Integer year) {
        log.info("Получает количество заказов поступивших за год: {}", year);

        return ResponseEntity
                .ok()
                .body(analyticService.findOrdersPerYear(year));
    }
}
