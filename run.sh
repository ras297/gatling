#!/bin/bash
set -e

DB_CONTAINER=postgres
APP_CONTAINER=NextTick-app
DB_USER=myuser
DB_NAME=mydb
SEED_FILE=scripts/seed.sql

echo "Running seed.sql against Postgres..."
docker exec -i $DB_CONTAINER psql -U $DB_USER -d $DB_NAME < $SEED_FILE

echo "Restarting app container..."
docker restart $APP_CONTAINER

echo "Waiting 15 seconds for app to be ready..."
sleep 15

echo "Running Gatling tests..."
./gradlew gatlingRun --no-configuration-cache

echo "Done!"
