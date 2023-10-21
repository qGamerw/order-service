package ru.sber.kafka;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import ru.sber.models.LimitOrder;
import ru.sber.models.kafka_models.PageModel;
import ru.sber.services.OrderService;

@Slf4j
@Component
public class CourierKafkaListeners {
    private final OrderService orderService;
    private KafkaTemplate<String, List<LimitOrder>> kafkaLimitOrderListTemplate;
    private KafkaTemplate<String, Page<LimitOrder>> kafkaLimitOrderPageTemplate;
    private KafkaTemplate<String, Optional<LimitOrder>> kafkaLimitOrderTemplate;
    private KafkaTemplate<String, Boolean> kafkaBooleanTemplate;


    @Autowired
    public CourierKafkaListeners(OrderService orderService, 
                KafkaTemplate<String, List<LimitOrder>> kafkaLimitOrderListTemplate,
                KafkaTemplate<String, Page<LimitOrder>> kafkaLimitOrderPageTemplate,
                KafkaTemplate<String, Optional<LimitOrder>> kafkaLimitOrderTemplate,
                KafkaTemplate<String, Boolean> kafkaBooleanTemplate) {
        this.orderService = orderService;
        this.kafkaLimitOrderListTemplate = kafkaLimitOrderListTemplate;
        this.kafkaLimitOrderPageTemplate = kafkaLimitOrderPageTemplate;
        this.kafkaLimitOrderTemplate = kafkaLimitOrderTemplate;
        this.kafkaBooleanTemplate = kafkaBooleanTemplate;
    }

    @KafkaListener(topics = "get_awaiting_delivery_by_page", groupId = "getAwaitingDeliveryByPage")
    // This doesn't work, it's List, Java sees it as List, Java outputs it as List but Java can't work with it AS WITH A LIST
    void awaitingDeliveryByPageListener(List<PageModel> data) {
        log.info("Получает заказы со статусами готовится и готов, ограниченный страницей: {}", data);
        log.info("Класс: {}", data.getClass());
        log.info("Пустой: {}", data.isEmpty());
        // ArrayList<PageModel> data2 = (ArrayList<PageModel>) data;
        // String page_model = data.get(0);
        // log.info("Получает заказы со статусами готовится и готов, ограниченный страницей: {}", page_model);

        kafkaLimitOrderPageTemplate.send("awaiting_delivery_by_page", 
                orderService.findAllActiveOrdersByPage(data.get(0).getPage(), data.get(0).getPageSize()));
    }

    @KafkaListener(topics = "get_delivering", groupId = "getDelivering")
    void deliveringListener(Long id) {
        log.info("Получает заказы которые доставляет курьер по id: {}", id);

        kafkaLimitOrderListTemplate.send("delivering", 
                orderService.findOrdersCourierIsDelivering(id));
    }

    @KafkaListener(topics = "get_order_by_id", groupId = "getOrderById")
    void orderByIdListener(Long id) {
        log.info("Возвращает заказ по id: {}", id);

        kafkaLimitOrderTemplate.send("order_by_id", orderService.findOrderById(id));
    }

    @KafkaListener(topics = "get_all_orders_by_courier_id", groupId = "getAllOrdersByCourierId")
    void allOrdersByCourierIdListener(Long id) {
        log.info("Возвращает все заказы курьера с id: {}", id);

        kafkaLimitOrderListTemplate.send("all_orders_by_courier_id", orderService.findOrdersByCourierId(id));
    }

    @KafkaListener(topics = "update_courier_order", groupId = "updateCourierOrder")
    void courierOrderListener(LimitOrder order) {
        log.info("Обновляет id курьера у заказа с id {}", order.getId());

        kafkaBooleanTemplate.send("courier_order", orderService.updateOrderCourierId(order.getCourierId(), order.getId()));
    }
}
