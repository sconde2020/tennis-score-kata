package com.sconde.kata.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Game {

    Score playerAScore = Score.LOVE;

    Score playerBScore = Score.LOVE;

    boolean gameFinished;

    Player winner;

}
