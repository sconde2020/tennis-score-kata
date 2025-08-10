package com.sconde.kata.service;

import com.sconde.kata.model.Player;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KafkaConsumerService {
    private GameService gameService;

    @KafkaListener(topics = "tennis-points", groupId = "tennis-group")
    public void consume(String point) {
        Player player = Player.valueOf(point);
        gameService.processPoint(player);
    }
}