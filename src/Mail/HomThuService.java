package Mail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import item.Item;
import network.Message;
import player.Player;
import server.Client;
import services.InventoryService;
import services.ItemService;
import services.NpcService;
import services.Service;

/**
 *
 * @author RAIS
 */
public class HomThuService {

    private static HomThuService ins;

    public static HomThuService gI() {
        if (ins == null) {
            ins = new HomThuService();
        }
        return ins;
    }
    private final short maxMail = 50;
    public static final int XOA_THU = 0;
    public static final int XOA_DA_DOC = 1;
    public static final int XOA_ALL = 2;

    public void readMsg(Message msg, Player player) {
        try {
            int type = msg.reader().readInt();
            switch (type) {
                case 1 ->
                    sendListMail(player);
                case 3 ->
                    seenMail(player, msg.reader().readInt());
                case 4 ->
                    receiveItemThu(player, msg.reader().readInt());
                case 5 ->
                    remove(player, msg.reader().readInt());
                case 6 ->
                    receiveAllItems(player);
                default -> {
                }
            }
        } catch (IOException e) {
//            e.printStackTrace();
        }
    }

    private boolean isValidIndex(Player player, int index) {
        return index >= 0 && index < player.homThu.size();
    }

    public void remove(Player player, int index) {
        if (!isValidIndex(player, index)) {
            return;
        }
        player.selectMail = index;
        Thu selectedMail = player.homThu.get(index);
        boolean hasItems = !selectedMail.listItem.isEmpty();
        boolean isNotReceived = !selectedMail.isNhan;

        StringBuilder message = new StringBuilder("Bạn có muốn xóa thư hay không?\n");
        if (hasItems && isNotReceived) {
            message.append("Bạn sẽ bị mất vật phẩm trong thư");
        }
        NpcService.gI().createMenuConMeo(player, 1102025, -1, message.toString(), "Xóa thư", "Xóa thư\nđã đọc", "Xóa All", "Không");
    }

    public void removeMail(Player player, int option) {
        if (player.homThu.isEmpty()) {
            Service.gI().sendThongBao(player, "Hòm thư của bạn đang trống");
            return;
        }
        switch (option) {
            case XOA_THU -> {
                if (player.selectMail >= 0 && player.selectMail < player.homThu.size()) {
                    player.homThu.remove(player.selectMail);
                    Service.gI().sendThongBao(player, "Xóa thư thành công!");
                } else {
                    Service.gI().sendThongBao(player, "Thư không hợp lệ!");
                }
            }
            case XOA_DA_DOC -> {
                player.homThu.removeIf(mail -> mail.isSeen && mail.listItem.isEmpty() || mail.isNhan);
                Service.gI().sendThongBao(player, "Đã xóa tất cả thư đã đọc (trừ thư đã nhận)");
            }
            case XOA_ALL -> {
                player.homThu.clear();
                Service.gI().sendThongBao(player, "Đã xóa tất cả thư");
            }

            default -> {
                sendListMail(player);
                return;
            }
        }
        sendListMail(player);
    }

    public void receiveItemThu(Player player, int index) {
        index = player.homThu.size() - index - 1;
        if (!isValidIndex(player, index)) {
            return;
        }

        if (InventoryService.gI().getCountEmptyBag(player) < player.homThu.get(index).listItem.size()) {
            Service.gI().sendThongBao(player, "Hành trang đầy");
            return;
        }

        Thu mail = player.homThu.get(index);
        if (mail.isNhan) {
            Service.gI().sendThongBao(player, "Đã nhận thư này rồi");
            return;
        }

        for (Item item : mail.listItem) {
            InventoryService.gI().addItemBag(player, item,999999);
        }

        mail.isNhan = true;
        Service.gI().sendThongBao(player, "Nhận thành công");

        sendListMail(player);
        InventoryService.gI().sendItemBag(player);
    }

    public void receiveAllItems(Player player) {
        if (player.homThu.isEmpty()) {
            Service.gI().sendThongBao(player, "Hòm thư của bạn đang trống");
            return;
        }
        for (Thu mail : player.homThu) {
            if (mail.isNhan) {
                continue;
            }
            if (InventoryService.gI().getCountEmptyBag(player) < mail.listItem.size()) {
                Service.gI().sendThongBao(player, "Hành trang không đủ không gian để nhận tất cả vật phẩm!");
                return;
            } else {
                for (Item item : mail.listItem) {
                    InventoryService.gI().addItemBag(player, item,999999);
                    InventoryService.gI().sendItemBag(player);
                    Service.gI().sendThongBao(player, "Nhận vật phẩm thành công!");
                }
                mail.isNhan = true;
                mail.isSeen = true;
                sendListMail(player);
            }
        }
    }

    public boolean isMailboxFull(Player player) {
        return player.homThu.size() >= maxMail;
    }

    public void seenMail(Player player, int select) {
        select = player.homThu.size() - select - 1;
        if (!isValidIndex(player, select)) {
            return;
        }

        player.homThu.get(select).isSeen = true;
        sendListMail(player);
    }

    public void sendListMail(Player player) {
        try {
            // Tạo tin nhắn gửi đi
            Message msg = new Message(-120);
            msg.writer().writeInt(1); // Loại tin nhắn
            int mailSize = player.homThu.size();
            msg.writer().writeInt(mailSize); // Số lượng thư
            msg.writer().writeShort(maxMail); // Giới hạn số thư

            // Duyệt qua danh sách thư
            for (int i = mailSize - 1; i >= 0; i--) {
                Thu mail = player.homThu.get(i); // Lấy thư tại chỉ số i

                // Ghi thông tin cơ bản của thư vào Message
                msg.writer().writeUTF(mail.title != null ? mail.title : ""); // Tiêu đề
                msg.writer().writeUTF(mail.getTime()); // Thời gian
                msg.writer().writeUTF(mail.title2 != null ? mail.title2 : ""); // Tiêu đề phụ
                msg.writer().writeUTF(mail.note != null ? mail.note : ""); // Ghi chú
                msg.writer().writeUTF(mail.content != null ? mail.content : ""); // Nội dung
                msg.writer().writeBoolean(mail.isNhan); // Trạng thái nhận
                msg.writer().writeBoolean(mail.isSeen); // Trạng thái đã đọc

                // Lấy danh sách vật phẩm trong thư
                int itemSize = mail.listItem.size();
                msg.writer().writeInt(itemSize); // Số lượng vật phẩm

                // Duyệt qua danh sách vật phẩm trong thư
                for (int j = 0; j < itemSize; j++) {
                    Item item = mail.listItem.get(j); // Lấy vật phẩm tại chỉ số j

                    // Ghi thông tin vật phẩm
                    msg.writer().writeInt( item.template.iconID); // Icon ID
                    msg.writer().writeInt(item.quantity); // Số lượng
                }
            }

            // Gửi tin nhắn cho người chơi
            player.sendMessage(msg);

        } catch (IOException e) {
            e.printStackTrace(); // Log lỗi nếu có
        }
    }

    public void sendMail(Player player, Thu thu) {
        try {
            Message msg = new Message(-120);
            msg.writer().writeInt(2);

            player.sendMessage(msg);
        } catch (Exception e) {

        }
    }

    public void createMail(Player player, String mailName, String tieuDe, String noiDung) {
        Player rais = Client.gI().getPlayer(player.id);
        Thu thu = new Thu(mailName, tieuDe, "Được gửi bởi hệ thống", noiDung,
                System.currentTimeMillis(), false, false);
        int itemId = 0, quantity = 0;
        List<Item.ItemOption> options = new ArrayList<>();
        Item item = ItemService.gI().createNewItem((short) itemId);
        item.quantity = quantity;
        item.itemOptions = options;
        thu.listItem.add(item);
        rais.homThu.add(thu);
    }
}
