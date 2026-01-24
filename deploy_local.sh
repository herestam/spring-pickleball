#!/bin/bash

SERVER="tam@192.168.1.12"
APP_DIR="/home/tam/Documents/project/spring-pickleball"
JAR_NAME="app.jar"

echo "🚀 Connecting to server..."

ssh $SERVER << 'ENDSSH'

echo "📂 Go to project folder"
cd /home/tam/Documents/project/spring-pickleball || exit

echo "🔄 Pull latest code"
git pull origin HEAD

echo "🛠 Build project"
make restart

echo "✅ Deploy finished!"
ENDSSH

echo "🎉 Done!"
