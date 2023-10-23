package ru.sber.kafka.config;

import org.springframework.kafka.config.TopicBuilder;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {
    

    @Bean
    public NewTopic clientOrderStatus() {
        return TopicBuilder.name("client-order-status").build();
    }
    @Bean
    public NewTopic courierOrderStatus() {
        return TopicBuilder.name("courier_order_status").build();
    }
    @Bean
    public NewTopic updateCourierOrder() {
        return TopicBuilder.name("update_courier_order").build();
    }
}
