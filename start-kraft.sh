#!/bin/bash
set -euo pipefail

readonly IMAGE="confluentinc/cp-kafka:7.5.0"
readonly VOLUME_NAME="tennis-score-kata_kafka-data"

# Ensure the Docker volume exists
if ! docker volume inspect "$VOLUME_NAME" >/dev/null 2>&1; then
    docker volume create "$VOLUME_NAME"
fi

echo "Generating cluster ID..."
CLUSTER_ID=$(docker run --rm "$IMAGE" kafka-storage random-uuid)
echo "Cluster ID: $CLUSTER_ID"

echo "Formatting storage volume..."
docker run --rm \
  -v "$VOLUME_NAME:/var/lib/kafka/data" \
  "$IMAGE" sh -c '
    cat > /tmp/server.properties << "EOF"
node.id=1
process.roles=broker,controller
controller.quorum.voters=1@kafka:9093
listener.security.protocol.map=CONTROLLER:PLAINTEXT,PLAINTEXT:PLAINTEXT,PLAINTEXT_HOST:PLAINTEXT
inter.broker.listener.name=PLAINTEXT
controller.listener.names=CONTROLLER
advertised.listeners=PLAINTEXT://kafka:9092,PLAINTEXT_HOST://localhost:29092
listeners=PLAINTEXT://0.0.0.0:9092,CONTROLLER://0.0.0.0:9093,PLAINTEXT_HOST://0.0.0.0:29092
log.dirs=/var/lib/kafka/data
EOF

    kafka-storage format --ignore-formatted --cluster-id '"$CLUSTER_ID"' --config /tmp/server.properties
  '

echo "Starting services with cluster ID..."
export CLUSTER_ID
docker-compose up -d --build