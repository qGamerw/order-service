package ru.sber;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;

import ru.sber.models.kafka_models.PageModel;

@SpringBootApplication
public class OrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }

    // Do not try to uncomment code below
    // @Bean
    // CommandLineRunner commandLineRunner(KafkaTemplate<String, PageModel> kafkaTemplate){
    //     return args -> {
    //         for (int i = 0; i < 1; i++) {
    //             kafkaTemplate.send("get_awaiting_delivery_by_page", new PageModel(1, 10));
    //         }
    //     };
    // }

}