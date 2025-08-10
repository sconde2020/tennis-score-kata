#!/bin/bash

echo "1. Score reaches 40-40 (Deuce):"
curl -X POST "http://localhost:8090/api/game/process-sequence-sync?sequence=ABABAB"
echo -e "\n"

echo "2. Advantage scenario (Player A):"
curl -X POST "http://localhost:8090/api/game/process-sequence-sync?sequence=ABABABA"
echo -e "\n"

echo "3. Advantage scenario (Player B):"
curl -X POST "http://localhost:8090/api/game/process-sequence-sync?sequence=ABABABAB"
echo -e "\n"

echo "4. Player with advantage wins game (Player A):"
curl -X POST "http://localhost:8090/api/game/process-sequence-sync?sequence=ABABABA"
echo -e "\n"

echo "5. Player with advantage loses advantage (back to deuce):"
curl -X POST "http://localhost:8090/api/game/process-sequence-sync?sequence=ABABABAB"
echo -e "\n"

echo "6. Invalid characters in input sequence:"
curl -X POST "http://localhost:8090/api/game/process-sequence-sync?sequence=ABXAB"
echo -e "\n"

echo "7. Player B wins directly without deuce:"
curl -X POST "http://localhost:8090/api/game/process-sequence-sync?sequence=BBBB"
echo -e "\n"

echo "8. Player A wins directly without deuce:"
curl -X POST "http://localhost:8090/api/game/process-sequence-sync?sequence=AAAA"
echo -e "\n"
