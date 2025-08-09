package com.sconde.kata.controller;

import com.sconde.kata.model.Player;
import com.sconde.kata.service.GameService;
import com.sconde.kata.service.KafkaProducerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/game")
public class GameController {

    @Autowired
    private GameService gameService;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    /**
     * Send a single point event to Kafka.
     */
    @PostMapping("/point")
    public String recordPoint(@RequestParam Player player) {
        kafkaProducerService.sendPoint(player);
        return "Point submitted for processing.\n";
    }

    /**
     * Get the current score (updated asynchronously by Kafka consumer).
     */
    @GetMapping("/score")
    public String getScore() {
        return gameService.getCurrentScore();
    }

    /**
     * Reset the game state in memory.
     */
    @PostMapping("/reset")
    public String resetGame() {
        gameService.resetGame();
        return "Game is reset.\n";
    }

    /**
     * Send a sequence of points to Kafka in order.
     * No waiting — score will be updated asynchronously.
     */
    @PostMapping("/process-sequence")
    public String processSequence(@RequestParam String sequence) {
        gameService.resetGame();

        for (char c : sequence.toCharArray()) {
            try {
                Player player = Player.valueOf(String.valueOf(c).toUpperCase());
                kafkaProducerService.sendPoint(player);
            } catch (IllegalArgumentException e) {
                return "Invalid character in sequence: " + c;
            }
        }
        return "Sequence submitted for processing.\n";
    }

    /**
     * Send a sequence of points to Kafka in order.
     * Waiting score to return to user
     */
    @PostMapping("/process-sequence-sync")
    public String processSequenceSynchronously(@RequestParam String sequence) {
        gameService.resetGame();

        for (char c : sequence.toCharArray()) {
            try {
                Player player = Player.valueOf(String.valueOf(c).toUpperCase());
                kafkaProducerService.sendPoint(player);
                Thread.sleep(100);
            } catch (IllegalArgumentException e) {
                return "Invalid character in sequence: " + c;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return gameService.getCurrentScore();
    }
}
