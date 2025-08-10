package com.sconde.kata.controller;

import com.sconde.kata.model.Player;
import com.sconde.kata.service.GameService;
import com.sconde.kata.service.KafkaProducerService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/game")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GameController {

    GameService gameService;
    KafkaProducerService kafkaProducerService;

    @PostMapping("/point")
    public ResponseEntity<String> recordPoint(@RequestParam Player player) {
        kafkaProducerService.sendPoint(player);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Point submitted for processing.");
    }

    @GetMapping("/score")
    public ResponseEntity<String> getScore() {
        return ResponseEntity
                .ok(gameService.getCurrentScore());
    }

    @PostMapping("/reset")
    public ResponseEntity<String> resetGame() {
        gameService.resetGame();
        return ResponseEntity
                .ok("Game is reset.");
    }

    @PostMapping("/process-sequence")
    public ResponseEntity<String> processSequence(@RequestParam String sequence) {
        gameService.resetGame();

        for (char c : sequence.toCharArray()) {
            Player player = Player.valueOf(String.valueOf(c).toUpperCase());
            kafkaProducerService.sendPoint(player);
        }
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Sequence submitted for processing.");
    }

    @PostMapping("/process-sequence-sync")
    public ResponseEntity<String> processSequenceSynchronously(@RequestParam String sequence) {
        gameService.resetGame();

        for (char c : sequence.toCharArray()) {
            Player player = Player.valueOf(String.valueOf(c).toUpperCase());
            gameService.processPoint(player);
        }
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(gameService.getCurrentScore());
    }
}
