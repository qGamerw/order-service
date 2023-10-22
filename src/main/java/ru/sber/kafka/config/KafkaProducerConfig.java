package ru.sber.kafka.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.BooleanSerializer;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;


import ru.sber.models.LimitOrder;
import ru.sber.models.kafka_models.PageModel;

@Configuration
public class KafkaProducerConfig {
    
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    public Map<String, Object> producerConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return props;
    }

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfig());
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate(
        ProducerFactory<String, String> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }


    public Map<String, Object> producerConfigOfJson() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }

    @Bean
    public ProducerFactory<String, Long> producerFactoryOfLongJson() {
        return new DefaultKafkaProducerFactory<>(producerConfigOfJson());
    }

    @Bean
    public KafkaTemplate<String, Long> kafkaTemplateOfLongJson(
        ProducerFactory<String, Long> producerFactoryOfLongJson) {
        return new KafkaTemplate<>(producerFactoryOfLongJson);
    }

    @Bean
    public ProducerFactory<String, List<LimitOrder>> producerFactoryOfLimitOrderList() {
        return new DefaultKafkaProducerFactory<>(producerConfigOfJson());
    }
    @Bean
    public KafkaTemplate<String, List<LimitOrder>> kafkaLimitOrderListTemplate(
        ProducerFactory<String, List<LimitOrder>> producerFactoryOfLimitOrderList) {
        return new KafkaTemplate<>(producerFactoryOfLimitOrderList);
    }

    @Bean
    public ProducerFactory<String, Page<LimitOrder>> producerFactoryOfLimitOrderPage() {
        return new DefaultKafkaProducerFactory<>(producerConfigOfJson());
    }
    @Bean
    public KafkaTemplate<String, Page<LimitOrder>> kafkaLimitOrderPageTemplate(
        ProducerFactory<String, Page<LimitOrder>> producerFactoryOfLimitOrderPage) {
        return new KafkaTemplate<>(producerFactoryOfLimitOrderPage);
    }

    @Bean
    public ProducerFactory<String, Optional<LimitOrder>> producerFactoryOfLimitOrder() {
        return new DefaultKafkaProducerFactory<>(producerConfigOfJson());
    }
    @Bean
    public KafkaTemplate<String, Optional<LimitOrder>> kafkaLimitOrderTemplate(
        ProducerFactory<String, Optional<LimitOrder>> producerFactoryOfLimitOrder) {
        return new KafkaTemplate<>(producerFactoryOfLimitOrder);
    }

    // 
    @Bean
    public ProducerFactory<String, PageModel> producerFactoryOfPageModel() {
        return new DefaultKafkaProducerFactory<>(producerConfigOfJson());
    }
    @Bean
    public KafkaTemplate<String, PageModel> kafkaPageModelTemplate(
        ProducerFactory<String, PageModel> producerFactoryOfPageModel) {
        return new KafkaTemplate<>(producerFactoryOfPageModel);
    }


    public Map<String, Object> producerConfigOfBoolean() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, BooleanSerializer.class);
        return props;
    }

    @Bean
    public ProducerFactory<String, Boolean> producerFactoryOfBoolean() {
        return new DefaultKafkaProducerFactory<>(producerConfigOfBoolean());
    }
    @Bean
    public KafkaTemplate<String, Boolean> kafkaBooleanTemplate(
        ProducerFactory<String, Boolean> producerFactoryOfBoolean) {
        return new KafkaTemplate<>(producerFactoryOfBoolean);
    }
}
