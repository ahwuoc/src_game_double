/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models.Combine.manifest;

import consts.ConstFont;
import consts.ConstNpc;
import item.Item;
import models.Combine.CombineService;
import player.Player;
import server.ServerNotify;
import services.InventoryService;
import services.Service;
import utils.Util;

/**
 *
 * @author bemeo
 */
public class DucLoKham {

    private static float getRatio(long star) {
        if (star == 0) {
            return 8f;
        } else if (star == 1) {
            return 5f;
        } else if (star == 2) {
            return 2f;
        } else if (star == 3) {
            return 1f;
        } else if (star == 4) {
            return 0.1f;
        } else if (star == 5) {
            return 0.2f;
        } else if (star == 6) {
            return 0.1f;
        } else if (star == 7) {
            return 0.01f;
        } else if (star == 8) {
            return 0.01f;
        } else {
            return 0f;
        }
    }

    private static String getRatioStr(long star) {
        int ratio = (int) getRatio(star);
        if (ratio < 1) {
            ratio = 1;
        }
        return String.valueOf(ratio);
    }

    public static void showInfoCombine(Player player) {
        if (player.combine.itemsCombine.size() != 3) {
            Service.gI().sendDialogMessage(player, "Thiếu nguyên liệu.");
            return;
        }
        Item coNe = null;
        Item zenNiNe = null;
        Item trangBiKham = null;

        for (Item item : player.combine.itemsCombine) {
            if (item == null || !item.isNotNullItem()) continue;

            if (item.template.id == 457) {
                coNe = item;
            } else if (item.template.id == 1150) {
                zenNiNe = item;
            } else if (item.isTrangBiKham()) {
                trangBiKham = item;
            }
        }

        if (coNe == null || zenNiNe == null || trangBiKham == null) {
            Service.gI().sendDialogMessage(player, "Thiếu nguyên liệu hoặc trang bị không hợp lệ");
            return;
        }
        long star = trangBiKham.getOptionParam(107);
        if (star >= CombineService.MAX_STAR_ITEM) {
            Service.gI().sendDialogMessage(player, "Đã đạt số pha lê tối đa");
            return;
        }

        int required = 999;
        StringBuilder text = new StringBuilder();
        text.append(ConstFont.BOLD_BLUE).append(trangBiKham.template.name).append("\n");
        text.append(ConstFont.BOLD_DARK).append(trangBiKham.getOptionInfo()).append("\n");
        text.append(ConstFont.BOLD_GREEN).append(star + 1).append(" ô Sao Pha Lê\n");
     //  text.append(ConstFont.BOLD_BLUE).append("Tỉ lệ thành công: ").append(getRatioStr(star)).append("%\n");
        text.append(coNe.quantity < required ? ConstFont.BOLD_RED : ConstFont.BOLD_BLUE)
                .append("Cần ").append(required).append(" Zenni\n");
        text.append(zenNiNe.quantity < required ? ConstFont.BOLD_RED : ConstFont.BOLD_BLUE)
                .append("Cần ").append(required).append(" Cỏ 4 Lá");

        CombineService.gI().baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, text.toString(),
                "Nâng cấp x" + required, "Từ chối");
    }

    public static void DucLoKham(Player player, int... numm) {
        int n = (numm.length > 0) ? numm[0] : 1;
        Item coNe = null;
        Item zenNiNe = null;
        Item trangBiKham = null;
        for (Item item : player.combine.itemsCombine) {
            if (item == null || !item.isNotNullItem()) continue;

            if (item.template.id == 457) {
                coNe = item;
            } else if (item.template.id == 1150) {
                zenNiNe = item;
            } else if (item.isTrangBiKham()) {
                trangBiKham = item;
            }
        }

        if (coNe == null || zenNiNe == null || trangBiKham == null) return;
        if (trangBiKham.isHaveOption(93)) return;

        long star = trangBiKham.getOptionParam(107);
        if (star >= CombineService.MAX_STAR_ITEM) return;

        int required = 999;
        boolean success = false;
        int i;

        for (i = 0; i < n; i++) {
            if (coNe.quantity < required) {
                Service.gI().sendServerMessage(player, "Thiếu Zenni sau " + i + " lần thử.");
                break;
            }
            if (zenNiNe.quantity < required) {
                Service.gI().sendServerMessage(player, "Thiếu Cỏ 4 Lá sau " + i + " lần thử.");
                break;
            }

            coNe.quantity -= required;
            zenNiNe.quantity -= required;

            if (Util.isTrue(getRatio(star), 100)) {
                success = true;
                break;
            }
        }
        if (success) {
            trangBiKham.addOptionParam(107, 1);
            if (star > 4) {
                ServerNotify.gI().notify("Chúc mừng " + player.name + " vừa pha lê hóa "
                        + "thành công " + trangBiKham.template.name + " lên " + (star + 1) + " sao pha lê");
            }
            CombineService.gI().sendEffectSuccessCombine(player);
        } else {
            CombineService.gI().sendEffectFailCombine(player);
        }
        InventoryService.gI().sendItemBag(player);
        Service.gI().sendMoney(player);
        CombineService.gI().reOpenItemCombine(player);
    }
}
