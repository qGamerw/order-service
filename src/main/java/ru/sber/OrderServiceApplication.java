package ru.sber;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootApplication
public class OrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }

    // @Bean
    // CommandLineRunner commandLineRunner(KafkaTemplate<String, Long> kafkaTemplate){
    //     return args -> {
    //         for (int i = 0; i < 1; i++) {
    //             kafkaTemplate.send("get_all_orders_by_courier_id", 2l);
    //         }
    //     };
    // }

}