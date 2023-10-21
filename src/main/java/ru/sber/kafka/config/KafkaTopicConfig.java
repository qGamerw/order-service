package ru.sber.kafka.config;

import org.springframework.kafka.config.TopicBuilder;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {
    
    @Bean
    public NewTopic newTopic() {
        return TopicBuilder.name("self_messages_topic").build();
    }

    @Bean
    public NewTopic awaitingDeliveryByPage() {
        return TopicBuilder.name("awaiting_delivery_by_page").build();
    }
    @Bean
    public NewTopic getAwaitingDeliveryByPage() {
        return TopicBuilder.name("get_awaiting_delivery_by_page").build();
    }

    @Bean
    public NewTopic delivering() {
        return TopicBuilder.name("delivering").build();
    }
    @Bean
    public NewTopic getDelivering() {
        return TopicBuilder.name("get_delivering").build();
    }

    @Bean
    public NewTopic orderById() {
        return TopicBuilder.name("order_by_id").build();
    }
    @Bean
    public NewTopic getOrderById() {
        return TopicBuilder.name("get_order_by_id").build();
    }

    @Bean
    public NewTopic allOrdersByCourierId() {
        return TopicBuilder.name("all_orders_by_courier_id").build();
    }
    @Bean
    public NewTopic getAllOrdersByCourierId() {
        return TopicBuilder.name("get_all_orders_by_courier_id").build();
    }

    @Bean
    public NewTopic courierOrder() {
        return TopicBuilder.name("courier_order").build();
    }
    @Bean
    public NewTopic updateCourierOrder() {
        return TopicBuilder.name("update_courier_order").build();
    }
}
