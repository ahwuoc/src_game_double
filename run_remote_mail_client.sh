
echo "Starting Remote Mail Client..."
echo "Connecting to VPS: 14.225.219.221:14446"

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
