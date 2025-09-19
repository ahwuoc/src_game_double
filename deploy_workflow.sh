#!/bin/bash

# Workflow để deploy Remote Mail Server lên VPS
echo "=== Deploy Remote Mail Server to VPS ==="
echo ""

# 1. Push code lên GitHub
echo "1. Pushing code to GitHub..."
git add .
git commit -m "Update Remote Mail Server for VPS deployment"
git push origin master

echo "✅ Code pushed to GitHub"
echo ""

# 2. Hướng dẫn deploy trên VPS
echo "2. Deploy on VPS:"
echo "   SSH vào VPS: ssh user@14.225.219.221"
echo "   cd /path/to/DragonBoyDoubleServer"
echo "   git pull origin master"
echo "   ant compile jar"
echo "   # Restart server để load Remote Mail Server"
echo ""

# 3. Kiểm tra port
echo "3. Check if port 14446 is open:"
echo "   nc -znv 14.225.219.221 14446"
echo ""

# 4. Test connection
echo "4. Test Remote Mail Client:"
echo "   ./run_remote_mail_client.sh"
echo "   # Nhập: IP=14.225.219.221, Port=14446"
echo ""

echo "=== Workflow Complete ==="
