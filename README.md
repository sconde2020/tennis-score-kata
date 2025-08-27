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

## Prerequisites
- Docker 20.10+
- Docker Compose 2.0+
- Java 21 (only needed for local development)

## Running the Application Locally

1. Ensure Kafka is running and configured.

2. Verify code coverage

   ```mvn clean verify```

3. Build the app:

   ```mvn clean install```

4. Run the app:

    ```mvn spring-boot:run```

## Running the Application using Docker

1. Start the services
   
   ```bash
      chmod +x ./start-kraft.sh
   ```

   ```bash
      ./start-kraft.sh
   ```

2. Verify services

   ```bash
     docker-compose ps
   ```

### Using the Application

1. Recording the points individually

   ```bash
   curl -X POST "http://localhost:8090/api/game/point?player=A"
   ```
   ```bash
   curl -X POST "http://localhost:8090/api/game/point?player=B"

2. Getting current score

   ```bash
   curl "http://localhost:8090/api/game/score"

3. Processing a sequence of points

  ```bash
   curl -X POST "http://localhost:8090/api/game/process-sequence?sequence=ABABAA"
   ```
  ```bash
   curl -X POST "http://localhost:8090/api/game/process-sequence-sync?sequence=ABABAA"
   ```
4. Resetting the Game
   ```bash
   curl -X POST "http://localhost:8090/api/game/reset"
   ```

## Notes
- Player values must correspond to the defined enum.
- KafkaProducer handles async point submissions.
- Sync processing returns immediate score results.
