# Game Score API
A RESTful Spring Boot service to manage a game’s scoring system. It supports recording points for players, retrieving current scores, resetting the game, and processing sequences of points both asynchronously (via Kafka) and synchronously.

## Features
- Record a point for a player asynchronously using Kafka.
- Retrieve the current game score.
- Reset the game state.
- Process sequences of points asynchronously or synchronously.

## API Endpoints
- POST /api/game/point?player={player} — Submit a point for a player.
- GET /api/game/score — Get current score.
- POST /api/game/reset — Reset the game.
- POST /api/game/process-sequence?sequence={sequence} — Process a sequence asynchronously.
- POST /api/game/process-sequence-sync?sequence={sequence} — Process a sequence synchronously.
- Swagger : http://localhost:8090/api/game/swagger-ui/index.html#/

## Running the Application

1. Ensure Kafka is running and configured.

2. Build the app:

   ```mvn clean install```

3. Run the app:

    ```mvn spring-boot:run```

4. Interact with the API:

    Use any HTTP client (swagger, curl, Postman, etc.) to interact with the API.

## Notes
- Player values must correspond to the defined enum.
- KafkaProducer handles async point submissions.
- Sync processing returns immediate score results.
