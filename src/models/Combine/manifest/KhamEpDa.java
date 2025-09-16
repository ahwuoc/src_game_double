package models.Combine.manifest;

import consts.ConstFont;
import consts.ConstNpc;
import item.Item;
import models.Combine.CombineService;
import player.Player;
import services.InventoryService;
import services.Service;

/**
 *
 * @author bemeo
 */
public class KhamEpDa {

    public static void showInfoCombine(Player player) {
        if (player.combine.itemsCombine.size() != 3) {
            Service.gI().sendDialogMessage(player, "Cần 1 trang bị có lỗ sao pha lê và 1 loại ngọc để ép vào.");
            return;
        }
        Item trangBi = null;
        Item daPhaLe = null;
        Item zenNiNe = null;
        int requiredZenni = 5000;
        for (Item item : player.combine.itemsCombine) {
            if (item.isTrangBiKham()) {
                trangBi = item;
            } else if (item.isDaKham()) {
                daPhaLe = item;
            } else if (item.template.id == 457) {
                zenNiNe = item;
            }
        }
        if (trangBi == null || !trangBi.isNotNullItem() || daPhaLe == null || !daPhaLe.isNotNullItem()
                || zenNiNe == null || !zenNiNe.isNotNullItem()) {
            Service.gI().sendDialogMessage(player, "Cần 1 trang bị có lỗ sao pha lê,Đá Khảm và 1 loại ngọc để ép vào.");
            return;
        }
        long star = trangBi.getOptionParam(102);
        long starEmpty = trangBi.getOptionParam(107);
        long cuongHoa = trangBi.getOptionParam(228);
        if (star >= starEmpty) {
            Service.gI().sendDialogMessage(player, "Cần 1 trang bị có lỗ sao pha lê và 1 loại ngọc để ép vào.");
            return;
        }
        StringBuilder text = new StringBuilder();
        text.append(ConstFont.BOLD_BLUE).append(trangBi.template.name).append("\n");
        text.append(ConstFont.BOLD_DARK)
                .append(star >= 7 ? trangBi.getOptionInfoCuongHoa(daPhaLe) : trangBi.getOptionInfo(daPhaLe))
                .append("\n");
        text.append((zenNiNe == null || zenNiNe.quantity < requiredZenni) ? ConstFont.BOLD_RED : ConstFont.BOLD_BLUE)
                .append("Cần 5.000 Zenni");

        if (zenNiNe == null || zenNiNe.quantity < requiredZenni) {
            CombineService.gI().baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, text.toString(),
                    "Còn thiếu\n" + (zenNiNe == null ? requiredZenni : (requiredZenni - zenNiNe.quantity))
                            + " Zenni");
            return;
        }

        CombineService.gI().baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, text.toString(),
                "Nâng cấp", "Từ chối");
    }

    public static void KhamEpDa(Player player) {
        if (player.combine.itemsCombine.size() != 3) {
            return;
        }
        Item trangBi = null;
        Item daPhaLe = null;
        Item zenNiNe = null;
        int requiredZenni = 5000;
        for (Item item : player.combine.itemsCombine) {
            if (item.isTrangBiKham()) {
                trangBi = item;
            } else if (item.isDaKham()) {
                daPhaLe = item;
            } else if (item.template.id == 457) {
                zenNiNe = item;
            }
        }
        if (trangBi == null || !trangBi.isNotNullItem()  || daPhaLe == null || !daPhaLe.isNotNullItem()|| zenNiNe == null || !zenNiNe.isNotNullItem()) {
            return;
        }
        long star = trangBi.getOptionParam(102);
        long starEmpty = trangBi.getOptionParam(107);
        long cuongHoa = trangBi.getOptionParam(228);
        trangBi.addOptionParam(102, 1);
        if (star >= 7) {
            if (star == 7) {
                trangBi.itemOptions.add(new Item.ItemOption(218, 0));
            }
            trangBi.itemOptions.add(new Item.ItemOption(daPhaLe.getOptionDaPhaLe().optionTemplate.id,
                    daPhaLe.getOptionDaPhaLe().param));
        } else {
            trangBi.addOptionParam(daPhaLe.getOptionDaPhaLe().optionTemplate.id, daPhaLe.getOptionDaPhaLe().param);
        }
        zenNiNe.quantity -= requiredZenni;
        InventoryService.gI().subQuantityItemsBag(player, daPhaLe, 1);
        CombineService.gI().sendEffectSuccessCombine(player);
        InventoryService.gI().sendItemBag(player);
        Service.gI().sendMoney(player);
        CombineService.gI().reOpenItemCombine(player);
    }
}

