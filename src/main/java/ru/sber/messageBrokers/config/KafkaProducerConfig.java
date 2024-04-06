package ru.sber.messageBrokers.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ru.sber.models.LimitOrderRestaurant;

import java.util.HashMap;
import java.util.Map;

/**
 * Настройка message broker
 */
@Configuration
public class KafkaProducerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    public Map<String, Object> producerConfigOfJson() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }

    @Bean
    public ProducerFactory<String, LimitOrderRestaurant> producerFactoryOfLimitOrderRestaurant() {
        return new DefaultKafkaProducerFactory<>(producerConfigOfJson());
    }

    @Bean
    public KafkaTemplate<String, LimitOrderRestaurant> kafkaLimitOrderRestaurantTemplate(
            ProducerFactory<String, LimitOrderRestaurant> producerFactoryOfLimitOrderRestaurant) {
        return new KafkaTemplate<>(producerFactoryOfLimitOrderRestaurant);
    }
}
