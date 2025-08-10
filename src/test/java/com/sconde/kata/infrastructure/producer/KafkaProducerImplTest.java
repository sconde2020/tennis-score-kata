package com.sconde.kata.infrastructure.producer;

import static org.mockito.Mockito.*;

import com.sconde.kata.domain.model.Player;
import com.sconde.kata.infrastructure.consumer.KafkaProducerImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaTemplate;

public class KafkaProducerImplTest {

    private KafkaTemplate<String, String> kafkaTemplate;
    private KafkaProducerImpl producerService;

    @BeforeEach
    void setUp() {
        kafkaTemplate = mock(KafkaTemplate.class);
        producerService = new KafkaProducerImpl(kafkaTemplate);
    }

    @Test
    void shouldSendPoint_givenPlayerA_whenSendPointCalled() {
        // Given
        Player player = Player.A;

        // When
        producerService.sendPoint(player);

        // Then
        verify(kafkaTemplate, times(1)).send(KafkaProducerImpl.TOPIC, player.name());
    }

    @Test
    void shouldSendPoint_givenPlayerB_whenSendPointCalled() {
        // Given
        Player player = Player.B;

        // When
        producerService.sendPoint(player);

        // Then
        verify(kafkaTemplate, times(1)).send(KafkaProducerImpl.TOPIC, player.name());
    }
}
