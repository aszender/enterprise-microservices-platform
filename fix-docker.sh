#!/bin/bash
echo "🚨 Emergency Docker restart..."

# Kill all processes
sudo killall -9 Docker com.docker.backend com.docker.hyperkit dockerd 2>/dev/null

# Clean sockets
sudo rm -f /var/run/docker.sock 2>/dev/null
rm -f ~/Library/Group\ Containers/group.com.docker/settings.json.lock 2>/dev/null

# Wait
sleep 3

# Restart
open -a Docker

echo "✅ Docker restarting... wait 10 seconds"
sleep 10

# Check status
docker ps 2>/dev/null && echo "✅ Docker is working!" || echo "❌ Not responding yet, wait 10 more seconds"