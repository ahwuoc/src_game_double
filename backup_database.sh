#!/bin/bash

# Script backup database DragonBoy
# Sử dụng: ./backup_database.sh

echo "🔄 Đang backup database DragonBoy..."

# Tạo thư mục backup nếu chưa có
mkdir -p ./backups

# Tạo file backup với timestamp
BACKUP_FILE="./backups/dragonboy_backup_$(date +%Y%m%d_%H%M%S).sql"

# Backup database
docker exec dragonboy_mysql mysqldump -u dragonboy_user -pdragonboy_pass dragonboy > "$BACKUP_FILE"

if [ $? -eq 0 ]; then
    echo "✅ Backup thành công: $BACKUP_FILE"
    echo "📁 Kích thước file: $(du -h "$BACKUP_FILE" | cut -f1)"
else
    echo "❌ Backup thất bại!"
    exit 1
fi

# Giữ lại chỉ 5 file backup gần nhất
cd backups
ls -t dragonboy_backup_*.sql | tail -n +6 | xargs -r rm
cd ..

echo "🎉 Hoàn thành backup!"