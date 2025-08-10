package com.sconde.kata.domain.service;

import com.sconde.kata.domain.model.Player;

public interface GameService {

    void processPoint(Player winner);

    String getCurrentScore();

    void resetGame();
}
