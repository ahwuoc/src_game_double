package Mail;

import jdbc.DBConnecter;
import jdbc.daos.NDVSqlFetcher;
import jdbc.daos.PlayerDAO;
import player.Player;
import server.Client;
import Mail.HomThuService;
import services.ItemService;
import services.Service;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import item.Item;
import item.Item.ItemOption;

public class RemoteMailServer {

    private static final int REMOTE_MAIL_PORT = 14446; // Port khác với game server
    private ServerSocket serverSocket;
    private boolean running = false;

    public void start() {
        try {
            serverSocket = new ServerSocket(REMOTE_MAIL_PORT);
            running = true;
            System.out.println("Remote Mail Server started on port " + REMOTE_MAIL_PORT);

            while (running) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new RemoteMailHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            System.err.println("Error starting Remote Mail Server: " + e.getMessage());
        }
    }

    public void stop() {
        running = false;
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            System.err.println("Error stopping Remote Mail Server: " + e.getMessage());
        }
    }

    private static class RemoteMailHandler implements Runnable {
        private Socket clientSocket;
        private DataInputStream in;
        private DataOutputStream out;

        public RemoteMailHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try {
                in = new DataInputStream(clientSocket.getInputStream());
                out = new DataOutputStream(clientSocket.getOutputStream());

                String command = in.readUTF();

                switch (command) {
                    case "LOGIN":
                        handleLogin();
                        break;
                    case "SEND_MAIL":
                        handleSendMail();
                        break;
                    case "DISCONNECT":
                        break;
                    default:
                        out.writeUTF("UNKNOWN_COMMAND");
                        break;
                }

            } catch (Exception e) {
                System.err.println("Error handling client: " + e.getMessage());
            } finally {
                try {
                    if (in != null)
                        in.close();
                    if (out != null)
                        out.close();
                    if (clientSocket != null)
                        clientSocket.close();
                } catch (IOException e) {
                    // Ignore
                }
            }
        }

        private void handleLogin() throws IOException {
            String username = in.readUTF();
            String password = in.readUTF();

            try {
                // Kiểm tra tài khoản admin
                boolean isValidAdmin = checkAdminAccount(username, password);
                if (isValidAdmin) {
                    out.writeUTF("LOGIN_SUCCESS");

                    // Gửi danh sách items
                    List<String> items = getItemList();
                    out.writeInt(items.size());
                    for (String item : items) {
                        String[] parts = item.split(" - ", 2);
                        out.writeInt(Integer.parseInt(parts[0]));
                        out.writeUTF(parts[1]);
                    }

                    // Gửi danh sách options
                    List<String> options = getOptionList();
                    out.writeInt(options.size());
                    for (String option : options) {
                        String[] parts = option.split(" - ", 2);
                        out.writeInt(Integer.parseInt(parts[0]));
                        out.writeUTF(parts[1]);
                    }
                } else {
                    out.writeUTF("LOGIN_FAILED");
                }
            } catch (Exception e) {
                out.writeUTF("LOGIN_ERROR: " + e.getMessage());
            }
        }

        private boolean checkAdminAccount(String username, String password) {
            try (Connection con = DBConnecter.getConnectionServer();
                    PreparedStatement ps = con
                            .prepareStatement("SELECT is_admin FROM account WHERE username = ? AND password = ?")) {

                ps.setString(1, username);
                ps.setString(2, password);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getBoolean("is_admin");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        private void handleSendMail() throws IOException {
            try {
                String recipient = in.readUTF();
                String title = in.readUTF();
                String title2 = in.readUTF();
                String content = in.readUTF();

                // Đọc danh sách vật phẩm
                int itemCount = in.readInt();
                List<Item> items = new ArrayList<>();

                for (int i = 0; i < itemCount; i++) {
                    short itemId = in.readShort();
                    int quantity = in.readInt();
                    int optionCount = in.readInt();

                    Item item = ItemService.gI().createNewItem(itemId);
                    item.quantity = quantity;

                    for (int j = 0; j < optionCount; j++) {
                        int optionId = in.readInt();
                        long param = in.readLong();
                        item.itemOptions.add(new ItemOption(optionId, param));
                    }

                    items.add(item);
                }

                // Gửi mail
                boolean success = sendMailToPlayer(recipient, title, title2, content, items);

                if (success) {
                    out.writeUTF("MAIL_SENT");
                } else {
                    out.writeUTF("MAIL_FAILED");
                }

            } catch (Exception e) {
                out.writeUTF("MAIL_ERROR: " + e.getMessage());
            }
        }

        private boolean sendMailToPlayer(String recipient, String title, String title2, String content,
                List<Item> items) {
            try {
                // Kiểm tra người chơi online
                Player player = Client.gI().getPlayer(recipient);
                if (player != null) {
                    // Người chơi đang online
                    Thu thu = new Thu(title, title2, "Được gửi bởi hệ thống", content, System.currentTimeMillis(),
                            false, false);
                    thu.listItem.addAll(items);
                    player.homThu.add(thu);
                    HomThuService.gI().sendListMail(player);
                    Service.gI().sendThongBaoFromAdmin(player, "Bạn nhận được thư mới");
                    return true;
                } else {
                    // Người chơi offline
                    Player offlinePlayer = NDVSqlFetcher.loadPlayerByName(recipient);
                    if (offlinePlayer != null) {
                        Thu thu = new Thu(title, title2, "Được gửi bởi hệ thống", content, System.currentTimeMillis(),
                                false, false);
                        thu.listItem.addAll(items);
                        offlinePlayer.homThu.add(thu);
                        PlayerDAO.updatePlayer(offlinePlayer);
                        return true;
                    }
                }
                return false;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        private List<String> getItemList() {
            List<String> itemList = new ArrayList<>();
            try (Connection con = DBConnecter.getConnectionServer();
                    PreparedStatement ps = con.prepareStatement("SELECT id, name FROM item_template");
                    ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    itemList.add(id + " - " + name);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return itemList;
        }

        private List<String> getOptionList() {
            List<String> optionList = new ArrayList<>();
            try (Connection con = DBConnecter.getConnectionServer();
                    PreparedStatement ps = con.prepareStatement("SELECT id, name FROM item_option_template");
                    ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    optionList.add(id + " - " + name);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return optionList;
        }
    }

    public static void main(String[] args) {
        RemoteMailServer server = new RemoteMailServer();
        server.start();
    }
}
