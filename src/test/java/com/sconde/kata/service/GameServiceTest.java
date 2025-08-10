package com.sconde.kata.service;

import com.sconde.kata.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class GameServiceTest {

    private GameService gameService;

    @BeforeEach
    void setUp() {
        gameService = new GameService();
        gameService.resetGame();
    }

    @Test
    void shouldReturnInitialScore_givenNewGame_whenGetCurrentScore() {
        String score = gameService.getCurrentScore();
        assertThat(score).isEmpty();  // no points yet, so no messages
    }

    @Test
    void shouldIncrementScore_givenPlayerAWinsPoint_whenProcessPoint() {
        gameService.processPoint(Player.A);
        String score = gameService.getCurrentScore();
        assertThat(score).contains("Player A : 15 / Player B : 0");
    }

    @Test
    void shouldIncrementScore_givenPlayerBWinsPoint_whenProcessPoint() {
        gameService.processPoint(Player.B);
        String score = gameService.getCurrentScore();
        assertThat(score).contains("Player A : 0 / Player B : 15");
    }

    @Test
    void shouldShowDeuce_givenBothPlayersHaveForty_whenProcessPoint() {
        // Sequence to reach deuce: A-B-A-B-A-B
        gameService.processPoint(Player.A); // 15-0
        gameService.processPoint(Player.B); // 15-15
        gameService.processPoint(Player.A); // 30-15
        gameService.processPoint(Player.B); // 30-30
        gameService.processPoint(Player.A); // 40-30
        gameService.processPoint(Player.B); // 40-40 (Deuce)

        String score = gameService.getCurrentScore();
        assertThat(score).contains("Deuce");
    }

    @Test
    void shouldShowAdvantagePlayerA_givenPlayerAHasAdvantage_whenProcessPoint() {
        // Reach deuce first
        processSequence("ABABAB");
        gameService.processPoint(Player.A); // Advantage Player A

        String score = gameService.getCurrentScore();
        assertThat(score).contains("Advantage Player A");
    }

    @Test
    void shouldShowAdvantagePlayerB_givenPlayerBHasAdvantage_whenProcessPoint() {
        processSequence("ABABAB");
        gameService.processPoint(Player.B); // Advantage Player B

        String score = gameService.getCurrentScore();
        assertThat(score).contains("Advantage Player B");
    }

    @Test
    void shouldBackToDeuce_givenPlayerWithoutAdvantageScores_whenProcessPoint() {
        processSequence("ABABABA"); // Advantage Player A
        gameService.processPoint(Player.B); // Back to Deuce

        String score = gameService.getCurrentScore();
        assertThat(score).contains("Deuce");
    }

    @Test
    void shouldDeclarePlayerAWinner_givenPlayerAWinsAfterAdvantage_whenProcessPoint() {
        processSequence("ABABABA"); // Advantage Player A
        gameService.processPoint(Player.A); // Player A wins

        String score = gameService.getCurrentScore();
        assertThat(score).contains("Player A wins the game");
    }

    @Test
    void shouldDeclarePlayerBWinner_givenPlayerBWinsWithoutDeuce_whenProcessPoint() {
        processSequence("BBBB"); // Player B wins straight

        String score = gameService.getCurrentScore();
        assertThat(score).contains("Player B wins the game");
    }

    @Test
    void shouldResetGameAutomatically_givenGameIsFinished_whenProcessPoint() {
        processSequence("AAAA"); // Player A wins

        // Next point should start a new game
        gameService.processPoint(Player.B);
        String score = gameService.getCurrentScore();

        // Only one message for the new game (Player B scores 15-0)
        assertThat(score).contains("Player A : 0 / Player B : 15");
    }

    private void processSequence(String sequence) {
        for (char c : sequence.toCharArray()) {
            Player player = Player.valueOf(String.valueOf(c));
            gameService.processPoint(player);
        }
    }
}
