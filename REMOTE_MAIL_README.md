# Remote Mail Client - Ứng Dụng Gửi Thư Từ Xa

## Mô tả
Ứng dụng Remote Mail Client cho phép bạn gửi thư và vật phẩm cho người chơi từ xa mà không cần chạy cùng server game. Bạn có thể kết nối từ máy tính khác và sử dụng tài khoản admin để gửi thư.

## Tính năng
- ✅ Kết nối từ xa đến server game
- ✅ Đăng nhập bằng tài khoản admin
- ✅ Gửi thư với vật phẩm và options
- ✅ Giao diện thân thiện với người dùng
- ✅ Hỗ trợ người chơi online và offline

## Cách sử dụng

### 1. Khởi động Server
Trước tiên, đảm bảo server game đã được khởi động. Remote Mail Server sẽ tự động chạy trên port 14446.

### 2. Chạy Remote Mail Client
```bash
# Cách 1: Sử dụng script
./run_remote_mail_client.sh

# Cách 2: Chạy trực tiếp
java -cp "dist/DragonBoy.jar:lib/*" Mail.RemoteMailClient
```

### 3. Kết nối đến Server
1. Nhập thông tin kết nối:
   - **Server IP**: 14.225.219.221 (IP VPS)
   - **Port**: 14446 (Remote Mail Server port)
   - **Username**: Tên tài khoản admin
   - **Password**: Mật khẩu tài khoản admin

2. Nhấn nút "Kết Nối"

### 4. Gửi Thư
1. Nhập thông tin thư:
   - **Tên người nhận**: Tên người chơi sẽ nhận thư
   - **Tên thư**: Tên của thư
   - **Tiêu đề thư**: Tiêu đề của thư
   - **Nội dung**: Nội dung thư

2. Thêm vật phẩm (tùy chọn):
   - Chọn vật phẩm từ dropdown
   - Nhập số lượng
   - Thêm options cho vật phẩm (nếu cần)
   - Nhấn "Thêm Vật phẩm"

3. Nhấn "Gửi Thư"

## Cấu trúc File

```
src/Mail/
├── RemoteMailClient.java    # Client ứng dụng
├── RemoteMailServer.java   # Server handler
└── sendMail.java          # Form gửi thư gốc (tích hợp trong server)

run_remote_mail_client.sh   # Script chạy client
```

## Yêu cầu
- Java 8 trở lên
- Tài khoản admin trên server game
- Kết nối mạng đến server game

## Lưu ý
- Remote Mail Server chạy trên port 14446 (khác với game server port 14445)
- Chỉ tài khoản admin mới có thể sử dụng tính năng này
- Đảm bảo firewall cho phép kết nối đến port 14446
- Server game phải đang chạy để có thể gửi thư

## Troubleshooting

### Lỗi kết nối
- Kiểm tra IP và port có đúng không
- Đảm bảo server game đang chạy
- Kiểm tra firewall

### Lỗi đăng nhập
- Kiểm tra username và password
- Đảm bảo tài khoản có quyền admin
- Kiểm tra database connection

### Lỗi gửi thư
- Kiểm tra tên người chơi có tồn tại không
- Đảm bảo thông tin thư đã đầy đủ
- Kiểm tra kết nối đến server
