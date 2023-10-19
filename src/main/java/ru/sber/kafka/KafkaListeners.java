package ru.sber.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class KafkaListeners {
    
    @KafkaListener(topics = "self_messages_topic", groupId = "selfMessages")
    void listener(String data) {
        log.info("Listener получил: " + data);
    }
}
