package com.sconde.kata.infrastructure.producer;

import com.sconde.kata.domain.model.Player;
import com.sconde.kata.domain.service.GameService;
import com.sconde.kata.infrastructure.consumer.KafkaConsumer;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KafkaConsumerImpl implements KafkaConsumer {

    GameService gameService;

    @Override
    @KafkaListener(topics = "tennis-points", groupId = "tennis-group")
    public void consume(String point) {
        Player player = Player.valueOf(point);
        gameService.processPoint(player);
    }
}