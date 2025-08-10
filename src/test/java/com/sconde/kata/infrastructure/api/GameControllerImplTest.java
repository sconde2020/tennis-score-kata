package com.sconde.kata.infrastructure.api;

import com.sconde.kata.domain.model.Player;
import com.sconde.kata.domain.service.GameServiceImpl;
import com.sconde.kata.infrastructure.consumer.KafkaProducerImpl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GameControllerImpl.class)
class GameControllerImplTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameServiceImpl gameServiceImpl;

    @MockBean
    private KafkaProducerImpl kafkaProducerImpl;

    @Test
    void shouldReturnCreated_givenValidPlayer_whenRecordPoint() throws Exception {
        mockMvc.perform(post("/api/game/point")
                        .param("player", "A"))
                .andExpect(status().isCreated())
                .andExpect(content().string("Point submitted for processing."));

        verify(kafkaProducerImpl).sendPoint(Player.A);
    }

    @Test
    void shouldReturnOk_givenGameInProgress_whenGetScore() throws Exception {
        when(gameServiceImpl.getCurrentScore()).thenReturn("Score: 15");

        mockMvc.perform(get("/api/game/score"))
                .andExpect(status().isOk())
                .andExpect(content().string("Score: 15"));
    }

    @Test
    void shouldReturnOk_givenResetRequest_whenResetGame() throws Exception {
        mockMvc.perform(post("/api/game/reset"))
                .andExpect(status().isOk())
                .andExpect(content().string("Game is reset."));

        verify(gameServiceImpl).resetGame();
    }

    @Test
    void shouldReturnCreated_givenValidSequence_whenProcessSequence() throws Exception {
        mockMvc.perform(post("/api/game/process-sequence")
                        .param("sequence", "AB"))
                .andExpect(status().isCreated())
                .andExpect(content().string("Sequence submitted for processing."));

        verify(kafkaProducerImpl).sendPoint(Player.A);
        verify(kafkaProducerImpl).sendPoint(Player.B);
    }

    @Test
    void shouldReturnOk_givenValidSequence_whenProcessSequenceSync() throws Exception {
        when(gameServiceImpl.getCurrentScore()).thenReturn("Score: 40");

        mockMvc.perform(post("/api/game/process-sequence-sync")
                        .param("sequence", "AB"))
                .andExpect(status().isCreated())
                .andExpect(content().string("Score: 40"));

        verify(gameServiceImpl).processPoint(Player.A);
        verify(gameServiceImpl).processPoint(Player.B);
    }

    @Test
    void shouldReturnBadRequest_givenInvalidChar_whenProcessSequence() throws Exception {
        mockMvc.perform(post("/api/game/process-sequence")
                        .param("sequence", "AX"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid player code 'X'. Valid values: [A, B]"));
    }
}

