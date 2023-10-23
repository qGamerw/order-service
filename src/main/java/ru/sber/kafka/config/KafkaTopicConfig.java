package ru.sber.kafka.config;

import org.springframework.kafka.config.TopicBuilder;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {
    

    @Bean
    public NewTopic courierClientStatus() {
        return TopicBuilder.name("courier_status").build();
    }
    @Bean
    public NewTopic restaurantClientStatus() {
        return TopicBuilder.name("restaurant_status").build();
    }
    @Bean
    public NewTopic clientStatus() {
        return TopicBuilder.name("client_status").build();
    }
    @Bean
    public NewTopic updateCourierOrder() {
        return TopicBuilder.name("update_courier_order").build();
    }
}
