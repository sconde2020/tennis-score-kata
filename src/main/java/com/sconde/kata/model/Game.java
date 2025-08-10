package com.sconde.kata.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Game {

    Score playerAScore = Score.LOVE;

    Score playerBScore = Score.LOVE;

    boolean gameFinished;

    Player winner;

    // Copy constructor
    public Game(Game original) {
        this.playerAScore = original.getPlayerAScore();
        this.playerBScore = original.getPlayerBScore();
        this.gameFinished = original.isGameFinished();
        this.winner = original.getWinner();
    }
}
