package ru.sber.messageBrokers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.sber.models.LimitOrder;
import ru.sber.services.OrderService;

/**
 * Класс для чтения информации из Kafka
 */
@Slf4j
@Component
public class CourierKafkaListeners {
    private final OrderService orderService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public CourierKafkaListeners(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Устанавливает курьера на заказ
     *
     * @param record прочитанные данные
     * @throws JsonProcessingException ошибка при чтении данных
     */
    @KafkaListener(topics = "update_courier_order", groupId = "updateCourierOrder")
    void courierOrderListener(ConsumerRecord<String, Object> record) throws JsonProcessingException {
        log.info("Обновляет id курьера у заказа с id {}", record);
        String value = record.value().toString();
        LimitOrder order = objectMapper.readValue(value, LimitOrder.class);

        orderService.updateOrderCourierId(order.getCourierId(), order.getId());
    }
}
