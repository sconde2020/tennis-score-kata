package com.sconde.kata.domain.service;

import com.sconde.kata.domain.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class GameServiceImplTest {

    private GameServiceImpl gameServiceImpl;

    @BeforeEach
    void setUp() {
        gameServiceImpl = new GameServiceImpl();
        gameServiceImpl.resetGame();
    }

    @Test
    void shouldReturnInitialScore_givenNewGame_whenGetCurrentScore() {
        String score = gameServiceImpl.getCurrentScore();
        assertThat(score).isEmpty();  // no points yet, so no messages
    }

    @Test
    void shouldIncrementScore_givenPlayerAWinsPoint_whenProcessPoint() {
        gameServiceImpl.processPoint(Player.A);
        String score = gameServiceImpl.getCurrentScore();
        assertThat(score).contains("Player A : 15 / Player B : 0");
    }

    @Test
    void shouldIncrementScore_givenPlayerBWinsPoint_whenProcessPoint() {
        gameServiceImpl.processPoint(Player.B);
        String score = gameServiceImpl.getCurrentScore();
        assertThat(score).contains("Player A : 0 / Player B : 15");
    }

    @Test
    void shouldShowDeuce_givenBothPlayersHaveForty_whenProcessPoint() {
        // Sequence to reach deuce: A-B-A-B-A-B
        gameServiceImpl.processPoint(Player.A); // 15-0
        gameServiceImpl.processPoint(Player.B); // 15-15
        gameServiceImpl.processPoint(Player.A); // 30-15
        gameServiceImpl.processPoint(Player.B); // 30-30
        gameServiceImpl.processPoint(Player.A); // 40-30
        gameServiceImpl.processPoint(Player.B); // 40-40 (Deuce)

        String score = gameServiceImpl.getCurrentScore();
        assertThat(score).contains("Deuce");
    }

    @Test
    void shouldShowAdvantagePlayerA_givenPlayerAHasAdvantage_whenProcessPoint() {
        // Reach deuce first
        processSequence("ABABAB");
        gameServiceImpl.processPoint(Player.A); // Advantage Player A

        String score = gameServiceImpl.getCurrentScore();
        assertThat(score).contains("Advantage Player A");
    }

    @Test
    void shouldShowAdvantagePlayerB_givenPlayerBHasAdvantage_whenProcessPoint() {
        processSequence("ABABAB");
        gameServiceImpl.processPoint(Player.B); // Advantage Player B

        String score = gameServiceImpl.getCurrentScore();
        assertThat(score).contains("Advantage Player B");
    }

    @Test
    void shouldBackToDeuce_givenPlayerWithoutAdvantageScores_whenProcessPoint() {
        processSequence("ABABABA"); // Advantage Player A
        gameServiceImpl.processPoint(Player.B); // Back to Deuce

        String score = gameServiceImpl.getCurrentScore();
        assertThat(score).contains("Deuce");
    }

    @Test
    void shouldDeclarePlayerAWinner_givenPlayerAWinsAfterAdvantage_whenProcessPoint() {
        processSequence("ABABABA"); // Advantage Player A
        gameServiceImpl.processPoint(Player.A); // Player A wins

        String score = gameServiceImpl.getCurrentScore();
        assertThat(score).contains("Player A wins the game");
    }

    @Test
    void shouldDeclarePlayerBWinner_givenPlayerBWinsWithoutDeuce_whenProcessPoint() {
        processSequence("BBBB"); // Player B wins straight

        String score = gameServiceImpl.getCurrentScore();
        assertThat(score).contains("Player B wins the game");
    }

    @Test
    void shouldResetGameAutomatically_givenGameIsFinished_whenProcessPoint() {
        processSequence("AAAA"); // Player A wins

        // Next point should start a new game
        gameServiceImpl.processPoint(Player.B);
        String score = gameServiceImpl.getCurrentScore();

        // Only one message for the new game (Player B scores 15-0)
        assertThat(score).contains("Player A : 0 / Player B : 15");
    }

    private void processSequence(String sequence) {
        for (char c : sequence.toCharArray()) {
            Player player = Player.valueOf(String.valueOf(c));
            gameServiceImpl.processPoint(player);
        }
    }
}
