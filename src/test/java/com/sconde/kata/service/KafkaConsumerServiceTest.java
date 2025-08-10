package com.sconde.kata.service;

import com.sconde.kata.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class KafkaConsumerServiceTest {

    private GameService gameService;
    private KafkaConsumerService kafkaConsumerService;

    @BeforeEach
    void setUp() {
        gameService = mock(GameService.class);
        kafkaConsumerService = new KafkaConsumerService(gameService);
    }

    @Test
    void shouldConsumePlayerAPointAndCallGameService_givenPlayerA_whenConsumeInvoked() {
        // Given
        String point = "A";

        // When
        kafkaConsumerService.consume(point);

        // Then
        verify(gameService, times(1)).processPoint(Player.A);
    }

    @Test
    void shouldConsumePlayerBPointAndCallGameService_givenPlayerB_whenConsumeInvoked() {
        // Given
        String point = "B";

        // When
        kafkaConsumerService.consume(point);

        // Then
        verify(gameService, times(1)).processPoint(Player.B);
    }

    @Test
    void shouldThrowException_whenInvalidPlayerString() {
        // Given
        String invalidPoint = "X";

        // Then
        try {
            kafkaConsumerService.consume(invalidPoint);
            assert false : "Expected IllegalArgumentException";
        } catch (IllegalArgumentException e) {
            // Expected, Player.valueOf throws this if enum is invalid
        }

        verify(gameService, never()).processPoint(any());
    }
}
