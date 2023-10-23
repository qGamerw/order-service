package ru.sber.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import ru.sber.models.LimitOrder;
import ru.sber.services.OrderService;

@Slf4j
@Component
public class CourierKafkaListeners {
    private final OrderService orderService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public CourierKafkaListeners(OrderService orderService){
        this.orderService = orderService;
    }

    /**
     * Устанавливает курьера на заказ
     */
    @KafkaListener(topics = "update_courier_order", groupId = "updateCourierOrder")
    void courierOrderListener(ConsumerRecord<String, Object> record) throws JsonProcessingException {
        log.info("Обновляет id курьера у заказа с id {}", record);
        String value = record.value().toString();
        LimitOrder order = objectMapper.readValue(value, LimitOrder.class);
        log.info("Order: {}", order);
        orderService.updateOrderCourierId(order.getCourierId(), order.getId());
    }
}
