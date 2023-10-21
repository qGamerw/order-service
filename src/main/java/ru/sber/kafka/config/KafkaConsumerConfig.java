package ru.sber.kafka.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import ru.sber.models.LimitOrder;
import ru.sber.models.kafka_models.PageModel;

@Configuration
public class KafkaConsumerConfig {
    
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    public Map<String, Object> consumerConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return props;
    }

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfig());
    }
    @Bean
    public KafkaListenerContainerFactory<
        ConcurrentMessageListenerContainer<String, String>> factory(
            ConsumerFactory<String, String> consumerFactory){
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
    

    public Map<String, Object> consumerConfigOfLong() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, LongSerializer.class);
        return props;
    }

    @Bean
    public ConsumerFactory<String, Long> consumerFactoryOfLong() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigOfLong());
    }
    @Bean
    public KafkaListenerContainerFactory<
            ConcurrentMessageListenerContainer<String, Long>> factoryOfLong(
            ConsumerFactory<String, Long> consumerFactoryOfLong){
        ConcurrentKafkaListenerContainerFactory<String, Long> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactoryOfLong());
        return factory;
    }
    

    public Map<String, Object> consumerConfigOfJson() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }

    @Bean
    public ConsumerFactory<String, PageModel> consumerFactoryOfPageModel() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigOfJson());
    }
    @Bean
    public KafkaListenerContainerFactory<
            ConcurrentMessageListenerContainer<String, PageModel>> factoryOfPageModel(
            ConsumerFactory<String, PageModel> consumerFactoryOfPageModel){
        ConcurrentKafkaListenerContainerFactory<String, PageModel> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactoryOfPageModel);
        return factory;
    }

    @Bean
    public ConsumerFactory<String, List<PageModel>> consumerFactoryOfPageModelList() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigOfJson());
    }
    @Bean
    public KafkaListenerContainerFactory<
            ConcurrentMessageListenerContainer<String, List<PageModel>>> factoryOfPageModelList(
            ConsumerFactory<String, List<PageModel>> consumerFactoryOfPageModelList){
        ConcurrentKafkaListenerContainerFactory<String, List<PageModel>> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactoryOfPageModelList);
        return factory;
    }

    @Bean
    public ConsumerFactory<String, LimitOrder> consumerFactoryOfLimitOrder() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigOfJson());
    }
    @Bean
    public KafkaListenerContainerFactory<
            ConcurrentMessageListenerContainer<String, LimitOrder>> factoryOfLimitOrder(
            ConsumerFactory<String, LimitOrder> consumerFactoryOfLimitOrder){
        ConcurrentKafkaListenerContainerFactory<String, LimitOrder> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactoryOfLimitOrder());
        return factory;
    }
}
