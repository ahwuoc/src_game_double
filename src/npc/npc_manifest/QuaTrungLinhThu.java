package npc.npc_manifest;

import consts.ConstMenu;
import consts.ConstNpc;
import item.Item;
import models.Combine.CombineService;
import npc.Npc;
import player.Player;
import services.InventoryService;
import services.ItemService;
import services.Service;

public class QuaTrungLinhThu extends Npc {

    public QuaTrungLinhThu(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            createOtherMenu(player, ConstNpc.BASE_MENU,
                    "Ngươi cần gì ở ta?",
                    "Mua Trứng\nLinh Thú Thường",
                    "Mua Trứng\nLinh Thú VIP");
        }
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (!canOpenNpc(player)) return;

        if (player.iDMark.isBaseMenu()) {
            switch (select) {
                case 0, 1 -> giveBeastEgg(player, select == 1);
                // Đã bỏ phần phân rã linh thú, nên không xử lý case 2 nữa
            }
        }
        // Không còn xử lý menu phân rã linh thú nữa
    }

    private void giveBeastEgg(Player player, boolean isVip) {
        short requiredItemId = 1150;                         // ID item để đổi
        int requiredAmount = isVip ? 9999 : 999;             // 9999 cho VIP, 999 cho thường
        short rewardItemId = isVip ? (short) 1915 : (short) 1914; // ID phần thưởng
        String itemName = isVip ? "Trứng Linh Thú VIP" : "Trứng Linh Thú Thường";

        try {
            // Kiểm tra đủ item
            if (!hasEnoughItems(player, requiredItemId, requiredAmount)) {
                Service.gI().sendThongBao(player, "Bạn không đủ vật phẩm (ID: " + requiredItemId + ") để đổi!");
                return;
            }

            // Trừ item
            if (!removeItems(player, requiredItemId, requiredAmount)) {
                Service.gI().sendThongBao(player, "Lỗi khi trừ vật phẩm!");
                return;
            }

            // Tạo và thêm item phần thưởng
            Item reward = ItemService.gI().createNewItem(rewardItemId);
            InventoryService.gI().addItemBag(player, reward, 1);
            InventoryService.gI().sendItemBag(player);

            Service.gI().sendThongBao(player, "Bạn nhận được: " + itemName);
        } catch (Exception e) {
            Service.gI().sendThongBao(player, "Đã xảy ra lỗi khi đổi vật phẩm!");
            e.printStackTrace();
        }
    }

    // Kiểm tra player có đủ số lượng item cần thiết không
    private boolean hasEnoughItems(Player player, short itemId, int requiredAmount) {
        int total = 0;
        for (Item item : player.inventory.itemsBag) {
            if (item != null && item.template != null && item.template.id == itemId) {
                total += item.quantity;
                if (total >= requiredAmount) {
                    return true;
                }
            }
        }
        return false;
    }

    // Trừ dần số lượng item trong từng slot, xóa slot nếu số lượng = 0
    private boolean removeItems(Player player, short itemId, int amount) {
        int toRemove = amount;
        for (int i = 0; i < player.inventory.itemsBag.size(); i++) {
            Item item = player.inventory.itemsBag.get(i);
            if (item != null && item.template.id == itemId) {
                if (item.quantity > toRemove) {
                    item.quantity -= toRemove;
                    return true;
                } else {
                    toRemove -= item.quantity;
                    InventoryService.gI().removeItemBag(player, i);
                    if (toRemove == 0) {
                        return true;
                    }
                    i--; // lùi index vì mảng bị xê dịch sau khi remove
                }
            }
        }
        return false;
    }
}
