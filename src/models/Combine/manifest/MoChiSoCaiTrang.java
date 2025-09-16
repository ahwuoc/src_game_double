/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models.Combine.manifest;

import consts.ConstNpc;
import item.Item;
import item.Item.ItemOption;
import models.Combine.CombineService;
import player.Player;
import services.InventoryService;
import services.Service;
import utils.Util;

/**
 *
 * @author Administrator
 */
public class MoChiSoCaiTrang {

    public static void showCombine(Player player) {
        if (player.combine.itemsCombine.size() == 0) {
            CombineService.gI().baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    "Hãy đưa em Đồ chưa kích hoạt chỉ số !",
                    "Đóng");
            return;
        }

        if (player.combine.itemsCombine.size() == 2) {
            if (player.combine.itemsCombine.stream().filter(
                    item -> item.isNotNullItem() && (item.template.type == 5))
                    .count() < 1) {
                CombineService.gI().baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu cải trang kích hoạt", "Đóng");
                return;
            }
            if (player.combine.itemsCombine.stream()
                    .filter(item -> item.isNotNullItem() && item.template.id == 1010)
                    .count() < 1) {
                CombineService.gI().baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu hỏa băng nhóc", "Đóng");
                return;
            }

            String npcSay = "mày có muốn mở chỉ số Cải Trang Vip Này Ramdom 1-150% !";

            CombineService.gI().baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                    npcSay, "Nâng cấp", "Từ chối");
        } else {
            if (player.combine.itemsCombine.size() > 2) {
                CombineService.gI().baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Nguyên liệu không phù hợp",
                        "Đóng");
                return;
            }
            CombineService.gI().baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    "Còn thiếu nguyên liệu để nâng cấp hãy quay lại sau", "Đóng");
        }
    }

    public static void MoChiSoCaiTrang(Player player) {
        if (player.combine.itemsCombine.size() != 2) {
            Service.getInstance().sendThongBao(player, "Thiếu nguyên liệu");
            return;
        }
        if (player.combine.itemsCombine.stream()
                .filter(item -> item.isNotNullItem() && (item.template.type == 5))
                .count() != 1) {
            Service.getInstance().sendThongBao(player, "Thiếu cải trang kích hoạt Hiếm");
            return;
        }
        if (player.combine.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 1010)
                .count() != 1) {
            Service.getInstance().sendThongBao(player, "Thiếu hỏa băng");
            return;
        }
        ///

        Item caiTrang = null;
        Item longDen = null;
        int checkOption = 0;
        for (Item item : player.combine.itemsCombine) {
            if (item.template.type == 5) {
                caiTrang = item;
            } else if (item.template.id == 1010) {
                longDen = item;
            }
        }
        for (ItemOption io : caiTrang.itemOptions) {
            if (io.optionTemplate.id == 241) {
                checkOption++;
            } else if (io.optionTemplate.id == 242) {
                checkOption = 0;
            }
        }
        if (checkOption == 0) {
            Service.getInstance().sendThongBao(player, "Yêu cầu Cải Trang chưa kích hoạt ! ");
            return;
        }

        if (caiTrang != null && longDen != null && longDen.quantity >= 100) {
            InventoryService.gI().subQuantityItemsBag(player, longDen, 100);
            caiTrang.itemOptions.clear();
            caiTrang.itemOptions.add(new ItemOption(50, Util.nextInt(60, 120)));
            caiTrang.itemOptions.add(new ItemOption(77, Util.nextInt(60, 120)));
            caiTrang.itemOptions.add(new ItemOption(103, Util.nextInt(60, 120)));
            caiTrang.itemOptions.add(new ItemOption(100, Util.nextInt(1000, 2000)));
            caiTrang.itemOptions.add(new ItemOption(94, Util.nextInt(1, 10)));
            caiTrang.itemOptions.add(new ItemOption(5, Util.nextInt(60, 80)));
            caiTrang.itemOptions.add(new ItemOption(237, Util.nextInt(1, 7)));
            caiTrang.itemOptions.add(new ItemOption(238, 1));
            caiTrang.itemOptions.add(new ItemOption(72, Util.nextInt(1, 7)));
            caiTrang.itemOptions.add(new ItemOption(244, Util.nextInt(1, 20)));
            if (Util.isTrue(999, 1000)) {
                caiTrang.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
                CombineService.gI().sendEffectSuccessCombine(player);
                InventoryService.gI().sendItemBag(player);
                Service.getInstance().sendMoney(player);
                CombineService.gI().reOpenItemCombine(player);
            } else {
                Service.getInstance().sendThongBao(player, "Không đủ nguyên liệu nâng cấp!");
                CombineService.gI().reOpenItemCombine(player);
            }

            ///
        }
    }
}
