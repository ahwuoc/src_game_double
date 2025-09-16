package models.Combine.manifest;

import consts.ConstNpc;
import item.Item;
import models.Combine.CombineService;
import player.Player;
import services.InventoryService;
import services.ItemService;
import services.Service;
import utils.Util;

public class PhanRaX1 {

    public static boolean isLinhThu(Item item) {
        return item.template.id >= 1351 && item.template.id <= 1367;
    }

    public static void showInfoCombine(Player player) {
        if (player.combine != null && player.combine.itemsCombine != null && player.combine.itemsCombine.size() == 1) {
            Item trangBi = null;
            for (Item item : player.combine.itemsCombine) {
                if (isLinhThu(item)) {
                    trangBi = item;
                    break;
                }
            }
            if (trangBi != null) {
                CombineService.gI().quatrunglinhthu.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                        "Phân Rã ngay để nhận lại Cỏ 4 lá", "Phân rã");
                System.out.println("[DEBUG] showInfoCombine: Linh Thú được chọn: " + trangBi.template.name);
            } else {
                Service.gI().sendThongBaoOK(player, "Chỉ có thể phân rã Linh Thú!");
                System.out.println("[DEBUG] showInfoCombine: Không phải Linh Thú");
            }
        } else {
            Service.gI().sendThongBaoOK(player, "Vui lòng chọn một Linh Thú để phân rã!");
            System.out.println("[DEBUG] showInfoCombine: Không đủ item hoặc player.combine null");
        }
    }

    public static void startCombine(Player player) {
        System.out.println("[DEBUG] startCombine: Bắt đầu phân rã");
        if (player.combine != null && player.combine.itemsCombine != null && player.combine.itemsCombine.size() == 1) {
            Item trangBi = player.combine.itemsCombine.get(0);
            System.out.println("[DEBUG] startCombine: Item lấy ra là: " + (trangBi == null ? "null" : trangBi.template.name));
            if (isLinhThu(trangBi)) {
                System.out.println("[DEBUG] startCombine: Item là Linh Thú, chuẩn bị phân rã");
                // Xóa món đồ được phân rã
                InventoryService.gI().subQuantityItemsBag(player, trangBi, 1);

                int randomPercent = Util.nextInt(1, 101); // 1 - 100
                int soLuongThoiVang;
                if (randomPercent <= 50) {
                    soLuongThoiVang = 99; // 50%
                } else if (randomPercent <= 80) {
                    soLuongThoiVang = 199; // 30%
                } else if (randomPercent <= 95) {
                    soLuongThoiVang = 299; // 15%
                } else {
                    soLuongThoiVang = 399; // 5%
                }
                System.out.println("[DEBUG] startCombine: randomPercent = " + randomPercent + ", soLuongThoiVang = " + soLuongThoiVang);

                Item thoiVang = ItemService.gI().createNewItem((short) 1150, soLuongThoiVang);
                if (thoiVang == null) {
                    System.out.println("[DEBUG] startCombine: Tạo item Cỏ 4 lá thất bại!");
                    Service.gI().sendThongBaoOK(player, "Lỗi khi tạo item Cỏ 4 lá, vui lòng thử lại sau!");
                    return;
                }
                boolean added = InventoryService.gI().addItemBag(player, thoiVang, 1.0);
                System.out.println("[DEBUG] startCombine: addItemBag trả về: " + added);

                CombineService.gI().reOpenItemCombine(player);
                InventoryService.gI().sendItemBag(player);
                Service.gI().sendThongBaoOK(player,
                        "Bạn đã phân rã 1 Linh Thú và nhận được x" + soLuongThoiVang + " Cỏ 4 lá!");
            } else {
                Service.gI().sendThongBaoOK(player, "Chỉ có thể phân rã Linh Thú!");
                System.out.println("[DEBUG] startCombine: Item không phải Linh Thú");
            }
        } else {
            Service.gI().sendThongBaoOK(player, "Vui lòng chọn một Linh Thú để phân rã!");
            System.out.println("[DEBUG] startCombine: Không đủ item hoặc player.combine null");
        }
    }
}
