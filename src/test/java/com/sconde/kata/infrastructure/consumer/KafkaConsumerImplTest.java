package com.sconde.kata.infrastructure.consumer;

import com.sconde.kata.domain.model.Player;
import com.sconde.kata.domain.service.GameServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class KafkaConsumerImplTest {

    private GameServiceImpl gameServiceImpl;
    private KafkaConsumerImpl kafkaConsumerImpl;

    @BeforeEach
    void setUp() {
        gameServiceImpl = mock(GameServiceImpl.class);
        kafkaConsumerImpl = new KafkaConsumerImpl(gameServiceImpl);
    }

    @Test
    void shouldConsumePlayerAPointAndCallGameService_givenPlayerA_whenConsumeInvoked() {
        // Given
        String point = "A";

        // When
        kafkaConsumerImpl.consume(point);

        // Then
        verify(gameServiceImpl, times(1)).processPoint(Player.A);
    }

    @Test
    void shouldConsumePlayerBPointAndCallGameService_givenPlayerB_whenConsumeInvoked() {
        // Given
        String point = "B";

        // When
        kafkaConsumerImpl.consume(point);

        // Then
        verify(gameServiceImpl, times(1)).processPoint(Player.B);
    }

    @Test
    void shouldThrowException_whenInvalidPlayerString() {
        // Given
        String invalidPoint = "X";

        // Then
        try {
            kafkaConsumerImpl.consume(invalidPoint);
            assert false : "Expected IllegalArgumentException";
        } catch (IllegalArgumentException e) {
            // Expected, Player.valueOf throws this if enum is invalid
        }

        verify(gameServiceImpl, never()).processPoint(any());
    }
}
