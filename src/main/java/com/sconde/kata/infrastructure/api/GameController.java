package com.sconde.kata.infrastructure.api;

import com.sconde.kata.domain.model.Player;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Game API", description = "Operations related to game scoring")
public interface GameController {

    @Operation(summary = "Submit a point for a player",
            description = "Sends a single point event to Kafka for processing.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Point submitted successfully",
                    content = @Content(mediaType = "text/plain",
                            examples = @ExampleObject(value = "Point submitted for processing."))),
            @ApiResponse(responseCode = "400", description = "Invalid player parameter")
    })
    ResponseEntity<String> recordPoint(
            @Parameter(description = "Player who won the point. Allowed values: A or B",
                    required = true,
                    examples = @ExampleObject(value = "A"))
            @RequestParam Player player);

    @Operation(summary = "Get the current game score",
            description = "Returns the current score synchronously or asynchronously updated by Kafka.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Score retrieved successfully",
                    content = @Content(mediaType = "text/plain",
                            examples = @ExampleObject(value = "Player A: 30, Player B: 15")))
    })
    ResponseEntity<String> getScore();

    @Operation(summary = "Reset the game",
            description = "Resets the game state in memory.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Game reset successfully",
                    content = @Content(mediaType = "text/plain",
                            examples = @ExampleObject(value = "Game is reset.")))
    })
    ResponseEntity<String> resetGame();

    @Operation(summary = "Process a sequence of points asynchronously",
            description = "Sends a sequence of points to Kafka without waiting for processing.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Sequence submitted successfully",
                    content = @Content(mediaType = "text/plain",
                            examples = @ExampleObject(value = "Sequence submitted for processing."))),
            @ApiResponse(responseCode = "400", description = "Invalid sequence parameter")
    })
    ResponseEntity<String> processSequence(
            @Parameter(description = "Sequence of points as string. Allowed characters: A, B. Example: 'ABABABA'",
                    required = true,
                    examples = @ExampleObject(value = "ABABABA"))
            @RequestParam String sequence);

    @Operation(summary = "Process a sequence of points synchronously",
            description = "Processes a sequence of points and returns updated score after completion.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Sequence processed successfully",
                    content = @Content(mediaType = "text/plain",
                            examples = @ExampleObject(value = "Player A: 40, Player B: 30"))),
            @ApiResponse(responseCode = "400", description = "Invalid sequence parameter")
    })
    ResponseEntity<String> processSequenceSynchronously(
            @Parameter(description = "Sequence of points as string. Allowed characters: A, B. Example: 'ABABABA'",
                    required = true,
                    examples = @ExampleObject(value = "ABABABA"))
            @RequestParam String sequence);
}

