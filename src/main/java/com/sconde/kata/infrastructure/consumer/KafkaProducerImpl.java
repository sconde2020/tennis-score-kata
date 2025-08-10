package com.sconde.kata.infrastructure.consumer;

import com.sconde.kata.domain.model.Player;
import com.sconde.kata.infrastructure.producer.KafkaProducer;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KafkaProducerImpl implements KafkaProducer {

    public static String TOPIC = "tennis-points";

    KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void sendPoint(Player player) {
        kafkaTemplate.send(TOPIC, player.toString());
    }
}
