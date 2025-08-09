package com.sconde.kata.service;

import com.sconde.kata.model.Player;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KafkaProducerService {
    static String TOPIC = "tennis-points";

    KafkaTemplate<String, String> kafkaTemplate;

    public void sendPoint(Player player) {
        kafkaTemplate.send(TOPIC, player.toString());
    }
}
