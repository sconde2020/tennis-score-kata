package com.sconde.kata.service;

import static org.mockito.Mockito.*;

import com.sconde.kata.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaTemplate;

public class KafkaProducerServiceTest {

    private KafkaTemplate<String, String> kafkaTemplate;
    private  KafkaProducerService producerService;

    @BeforeEach
    void setUp() {
        kafkaTemplate = mock(KafkaTemplate.class);
        producerService = new KafkaProducerService(kafkaTemplate);
    }

    @Test
    void shouldSendPoint_givenPlayerA_whenSendPointCalled() {
        // Given
        Player player = Player.A;

        // When
        producerService.sendPoint(player);

        // Then
        verify(kafkaTemplate, times(1)).send(KafkaProducerService.TOPIC, player.name());
    }

    @Test
    void shouldSendPoint_givenPlayerB_whenSendPointCalled() {
        // Given
        Player player = Player.B;

        // When
        producerService.sendPoint(player);

        // Then
        verify(kafkaTemplate, times(1)).send(KafkaProducerService.TOPIC, player.name());
    }
}
