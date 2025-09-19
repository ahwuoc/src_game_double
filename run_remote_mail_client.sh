#!/bin/bash

# Script để chạy Remote Mail Client độc lập
# Đảm bảo rằng bạn đã compile project trước khi chạy script này

echo "Starting Remote Mail Client..."

# Tìm file jar hoặc class files
if [ -f "dist/DragonBoy.jar" ]; then
    echo "Using DragonBoy.jar..."
    java -cp "dist/DragonBoy.jar:lib/*" Mail.RemoteMailClient
elif [ -d "build/classes" ]; then
    echo "Using compiled classes..."
    java -cp "build/classes:lib/*" Mail.RemoteMailClient
else
    echo "Error: No compiled classes found. Please compile the project first."
    echo "Run: ant compile"
    exit 1
fi
