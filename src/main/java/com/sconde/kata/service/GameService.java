package com.sconde.kata.service;

import com.sconde.kata.model.Game;
import com.sconde.kata.model.Player;
import com.sconde.kata.model.Score;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GameService {
    private Game game = new Game();
    private final List<String> scoreMessages = new ArrayList<>();
    private final Object lock = new Object();

    public void processPoint(Player winner) {
        synchronized (lock) {
            if (game.isGameFinished()) {
                // Start new game automatically
                game = new Game();
                scoreMessages.clear();
            }

            Game newGame = new Game(game); // copy the original for concurrency
            updateScore(newGame, winner);

            // Commit updated state
            this.game = newGame;

            // Record message for this game
            scoreMessages.add(formatScoreMessage(newGame));
        }
    }

    public String getCurrentScore() {
        synchronized (lock) {
            return String.join("\n", scoreMessages);
        }
    }

    public void resetGame() {
        synchronized (lock) {
            this.game = new Game();
            scoreMessages.clear();
        }
    }

    private void updateScore(Game game, Player player) {
        Score playerScore = getPlayerScore(game, player);
        Score opponentScore = getOpponentScore(game, player);

        if (playerHasAdvantage(playerScore)) {
            handlePlayerWinsGame(game, player);
            return;
        }

        if (playerWinsWithoutAdvantage(playerScore, opponentScore)) {
            handlePlayerWinsGame(game, player);
            return;
        }

        if (playerBacksToDeuce(playerScore, opponentScore)) {
            resetToDeuce(game);
            return;
        }

        if (playerGainsAdvantage(playerScore, opponentScore)) {
            setAdvantage(game, player);
            return;
        }

        incrementNormalScore(game, player, playerScore);
    }

    private Score getPlayerScore(Game game, Player player) {
        return player == Player.A ? game.getPlayerAScore() : game.getPlayerBScore();
    }

    private Score getOpponentScore(Game game, Player player) {
        return player == Player.A ? game.getPlayerBScore() : game.getPlayerAScore();
    }

    private boolean playerHasAdvantage(Score playerScore) {
        return playerScore == Score.ADVANTAGE;
    }

    private boolean playerWinsWithoutAdvantage(Score playerScore, Score opponentScore) {
        return playerScore == Score.FORTY && opponentScore != Score.FORTY && opponentScore != Score.ADVANTAGE;
    }

    private boolean playerBacksToDeuce(Score playerScore, Score opponentScore) {
        return playerScore == Score.FORTY && opponentScore == Score.ADVANTAGE;
    }

    private boolean playerGainsAdvantage(Score playerScore, Score opponentScore) {
        return playerScore == Score.FORTY && opponentScore == Score.FORTY;
    }

    private void handlePlayerWinsGame(Game game, Player player) {
        game.setGameFinished(true);
        game.setWinner(player);
    }

    private void resetToDeuce(Game game) {
        game.setPlayerAScore(Score.FORTY);
        game.setPlayerBScore(Score.FORTY);
    }

    private void setAdvantage(Game game, Player player) {
        if (player == Player.A) {
            game.setPlayerAScore(Score.ADVANTAGE);
        } else {
            game.setPlayerBScore(Score.ADVANTAGE);
        }
    }

    private void incrementNormalScore(Game game, Player player, Score playerScore) {
        Score newScore = playerScore.incrementScore();
        if (player == Player.A) {
            game.setPlayerAScore(newScore);
        } else {
            game.setPlayerBScore(newScore);
        }
    }

    private String formatScoreMessage(Game game) {
        if (game.isGameFinished()) {
            return "Player " + game.getWinner() + " wins the game";
        }
        if (game.getPlayerAScore() == Score.FORTY && game.getPlayerBScore() == Score.FORTY) {
            return "Deuce";
        }
        if (game.getPlayerAScore() == Score.ADVANTAGE) {
            return "Advantage Player A";
        }
        if (game.getPlayerBScore() == Score.ADVANTAGE) {
            return "Advantage Player B";
        }
        return "Player A : " + game.getPlayerAScore().getValue() +
                " / Player B : " + game.getPlayerBScore().getValue();
    }

}
