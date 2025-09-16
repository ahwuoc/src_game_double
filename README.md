# DragonBoy Double Server

A Dragon Ball themed game server built with Java.

## 🚀 Quick Start

### Prerequisites

- Java 17+
- Docker & Docker Compose
- Apache Ant (for building)

### 1. Clone Repository

```bash
git clone https://github.com/ahwuoc/src_game_double.git
cd src_game_double
```

### 2. Database Setup with Docker

#### Start MariaDB Database

```bash
# Start MariaDB 10.4 (compatible with XAMPP exports)
docker-compose up -d
```

#### Import Database

```bash
# Wait for database to be ready (about 15 seconds)
sleep 15

# Import the SQL file (43 tables)
docker exec -i dragonboy_mysql mysql -u root -proot123 dragonboy < thaodragon.sql

# Verify import (should show 43 tables)
docker exec dragonboy_mysql mysql -u root -proot123 dragonboy -e "SHOW TABLES;" | wc -l
```

#### Access phpMyAdmin

- **URL:** http://localhost:8080
- **Username:** `root`
- **Password:** `root123`

### 3. Build and Run Server

```bash
# Build the project
ant clean compile jar

# Run the server
java -cp "lib/*:dist/DragonBoy.jar" server.ServerManager
```

## 📊 Database Information

- **Host:** localhost
- **Port:** 3306
- **Database:** dragonboy
- **Username:** dragonboy_user
- **Password:** dragonboy_pass

## 🐳 Docker Commands

```bash
# Start services
docker-compose up -d

# Stop services
docker-compose down

# View logs
docker-compose logs -f mysql

# Restart services
docker-compose restart

# Remove everything (including data)
docker-compose down -v
```

## 📁 Project Structure

```
DragonBoyDoubleServer/
├── src/                    # Java source code
├── lib/                    # JAR dependencies
├── data/                   # Game data files
│   └── config/            # Configuration files
├── nbproject/             # NetBeans project files
├── docker-compose.yml     # Docker configuration
├── thaodragon.sql         # Main database schema (43 tables)
└── dragonballsaga.sql     # Additional database data
```

## 🔧 Configuration

### Database Configuration

Edit `data/config/config.properties`:

```properties
#DATABASE
database.driver=com.mysql.cj.jdbc.Driver
database.host=127.0.0.1
database.port=3306
database.name=dragonboy
database.user=dragonboy_user
database.pass=dragonboy_pass
```

### Server Configuration

Edit `data/config/config.properties`:

```properties
#SERVER
server.name=Thảo Dragon
server.port=789
server.maxplayer=10000
```

## 🛠️ Troubleshooting

### Database Import Issues

**Problem:** Only partial tables imported
**Solution:** 
```bash
# Drop and recreate database
docker exec dragonboy_mysql mysql -u root -proot123 -e "DROP DATABASE IF EXISTS dragonboy; CREATE DATABASE dragonboy CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"

# Re-import
docker exec -i dragonboy_mysql mysql -u root -proot123 dragonboy < thaodragon.sql
```

**Problem:** MySQL 8.0 compatibility issues
**Solution:** Use MariaDB 10.4 (already configured in docker-compose.yml)

### Server Connection Issues

**Problem:** "Access denied for user"
**Solution:** Check database credentials in `data/config/config.properties`

**Problem:** "Table doesn't exist"
**Solution:** Verify all 43 tables are imported:
```bash
docker exec dragonboy_mysql mysql -u root -proot123 dragonboy -e "SHOW TABLES;" | wc -l
```

### Build Issues

**Problem:** Java version mismatch
**Solution:** Ensure Java 17 is installed:
```bash
java -version
javac -version
```

## 📋 Database Tables

The server uses 43 database tables including:

- `account` - User accounts
- `player` - Player data
- `item_template` - Item definitions
- `mob_template` - Monster definitions
- `npc_template` - NPC definitions
- `map_template` - Map definitions
- `skill_template` - Skill definitions
- `shop` - Shop data
- `clan` - Guild data
- And 35 more tables...

## 🎮 Game Features

- Character system with Dragon Ball characters
- Item and equipment system
- Skill and combat system
- Guild (clan) system
- Shop and trading system
- Map and NPC system
- Achievement system
- Event system

## 📝 Development

### Building from Source

```bash
# Clean and compile
ant clean compile

# Create JAR
ant jar

# Run tests
ant test
```

### IDE Setup

This project uses NetBeans project structure. Import the project folder into NetBeans IDE.

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## 📄 License

This project is for educational purposes.

## 🆘 Support

If you encounter issues:

1. Check the troubleshooting section
2. Verify all prerequisites are installed
3. Check Docker logs: `docker-compose logs mysql`
4. Ensure database has all 43 tables imported

---

**Note:** This server requires a compatible Dragon Ball game client to connect.
