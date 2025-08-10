package com.sconde.kata.controller;

import com.sconde.kata.model.Player;
import com.sconde.kata.service.GameService;
import com.sconde.kata.service.KafkaProducerService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GameController.class)
class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    @MockBean
    private KafkaProducerService kafkaProducerService;

    @Test
    void shouldReturnCreated_givenValidPlayer_whenRecordPoint() throws Exception {
        mockMvc.perform(post("/api/game/point")
                        .param("player", "A"))
                .andExpect(status().isCreated())
                .andExpect(content().string("Point submitted for processing."));

        verify(kafkaProducerService).sendPoint(Player.A);
    }

    @Test
    void shouldReturnOk_givenGameInProgress_whenGetScore() throws Exception {
        when(gameService.getCurrentScore()).thenReturn("Score: 15");

        mockMvc.perform(get("/api/game/score"))
                .andExpect(status().isOk())
                .andExpect(content().string("Score: 15"));
    }

    @Test
    void shouldReturnOk_givenResetRequest_whenResetGame() throws Exception {
        mockMvc.perform(post("/api/game/reset"))
                .andExpect(status().isOk())
                .andExpect(content().string("Game is reset."));

        verify(gameService).resetGame();
    }

    @Test
    void shouldReturnCreated_givenValidSequence_whenProcessSequence() throws Exception {
        mockMvc.perform(post("/api/game/process-sequence")
                        .param("sequence", "AB"))
                .andExpect(status().isCreated())
                .andExpect(content().string("Sequence submitted for processing."));

        verify(kafkaProducerService).sendPoint(Player.A);
        verify(kafkaProducerService).sendPoint(Player.B);
    }

    @Test
    void shouldReturnOk_givenValidSequence_whenProcessSequenceSync() throws Exception {
        when(gameService.getCurrentScore()).thenReturn("Score: 40");

        mockMvc.perform(post("/api/game/process-sequence-sync")
                        .param("sequence", "AB"))
                .andExpect(status().isCreated())
                .andExpect(content().string("Score: 40"));

        verify(gameService).processPoint(Player.A);
        verify(gameService).processPoint(Player.B);
    }

    @Test
    void shouldReturnBadRequest_givenInvalidChar_whenProcessSequence() throws Exception {
        mockMvc.perform(post("/api/game/process-sequence")
                        .param("sequence", "AX"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid player code 'X'. Valid values: [A, B]"));
    }
}

