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
}
