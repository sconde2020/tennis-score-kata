package com.sconde.kata.infrastructure.producer;

import com.sconde.kata.domain.model.Player;

public interface KafkaProducer {

    void sendPoint(Player player);

}
