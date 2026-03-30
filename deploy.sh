#!/bin/bash

APP_NAME=identity
IMAGE_NAME=identity-app
PORT=8081

echo "🛑 Stop old container (if exists)..."
docker stop $APP_NAME 2>/dev/null
docker rm $APP_NAME 2>/dev/null

echo "🧱 Building Docker image..."
docker build -t $IMAGE_NAME .

echo "🚀 Starting container..."
docker run -d \
  --name $APP_NAME \
  -p $PORT:$PORT \
  --restart unless-stopped \
  $IMAGE_NAME

echo "✅ Deployment done!"
docker ps | grep $APP_NAME
