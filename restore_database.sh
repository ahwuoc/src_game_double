#!/bin/bash

# Script restore database DragonBoy
# Sử dụng: ./restore_database.sh [backup_file]

if [ $# -eq 0 ]; then
    echo "❌ Vui lòng chỉ định file backup!"
    echo "📖 Cách sử dụng: ./restore_database.sh backups/dragonboy_backup_YYYYMMDD_HHMMSS.sql"
    echo ""
    echo "📁 Các file backup có sẵn:"
    ls -la backups/dragonboy_backup_*.sql 2>/dev/null || echo "Không có file backup nào!"
    exit 1
fi

BACKUP_FILE="$1"

if [ ! -f "$BACKUP_FILE" ]; then
    echo "❌ File backup không tồn tại: $BACKUP_FILE"
    exit 1
fi

echo "🔄 Đang restore database từ: $BACKUP_FILE"

# Tạo lại database và user
docker exec dragonboy_mysql mysql -u root -proot123 -e "DROP DATABASE IF EXISTS dragonboy; CREATE DATABASE dragonboy; CREATE USER IF NOT EXISTS 'dragonboy_user'@'%' IDENTIFIED BY 'dragonboy_pass'; GRANT ALL PRIVILEGES ON dragonboy.* TO 'dragonboy_user'@'%'; FLUSH PRIVILEGES;"

# Restore database
docker exec -i dragonboy_mysql mysql -u dragonboy_user -pdragonboy_pass dragonboy < "$BACKUP_FILE"

if [ $? -eq 0 ]; then
    echo "✅ Restore thành công!"
    
    # Kiểm tra số lượng tables
    TABLE_COUNT=$(docker exec dragonboy_mysql mysql -u dragonboy_user -pdragonboy_pass dragonboy -e "SHOW TABLES;" | wc -l)
    echo "📊 Số lượng tables: $((TABLE_COUNT - 1))"
else
    echo "❌ Restore thất bại!"
    exit 1
fi

echo "🎉 Hoàn thành restore!"