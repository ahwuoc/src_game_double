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

public class PhaLeHoaTrangBi {

    private static float getRatio(long star) {
        if (star == 0) {
            return 80f;
        } else if (star == 1) {
            return 50f;
        } else if (star == 2) {
            return 20f;
        } else if (star == 3) {
            return 10f;
        } else if (star == 4) {
            return 1f;
        } else if (star == 5) {
            return 0.2f;
        } else if (star == 6) {
            return 0.1f;
        } else if (star == 7) {
            return 0.01f;
        } else if (star == 8) {
            return 0.01f;
        } else if (star == 9) {
            return 0.01f;
        } else if (star == 10) {
            return 0.01f;
        } else if (star == 11) {
            return 0.01f;
        } else if (star == 12) {
            return 0.01f;
        } else {
            return 0f;
        }
    }

    private static String getRatioStr(long star) {
        float ratio = getRatio(star);
        if (ratio < 1) {
            ratio = 1;
        }
        return String.valueOf(ratio);
    }

    private static long getGold(long star) {
        if (star == 0) {
            return 5_000_000;
        } else if (star == 1) {
            return 10_000_000;
        } else if (star == 2) {
            return 20_000_000;
        } else if (star == 3) {
            return 40_000_000;
        } else if (star == 4) {
            return 100_000_000;
        } else if (star == 5) {
            return 500_000_000;
        } else if (star == 6) {
            return 1000_000_000;
        } else if (star == 7) {
            return 1200_000_000;
        } else if (star == 8) {
            return 1400_000_000l;
        } else if (star == 9) {
            return 1600_000_000l;
        } else if (star == 10) {
            return 1800_000_000l;
        } else if (star == 11) {
            return 1900_000_000l;
        } else if (star == 12) {
            return 2000_000_000l;
        } else {
            return 0;
        }
    }

    private static int getGem(long star) {
        if (star == 0) {
            return 100;
        } else if (star == 1) {
            return 200;
        } else if (star == 2) {
            return 300;
        } else if (star == 3) {
            return 400;
        } else if (star == 4) {
            return 500;
        } else if (star == 5) {
            return 600;
        } else if (star == 6) {
            return 700;
        } else if (star == 7) {
            return 800;
        } else if (star == 8) {
            return 900;
            } else if (star == 9) {
            return 1000;
            } else if (star == 10) {
            return 1100;
            } else if (star == 11) {
            return 1200;
            } else if (star == 12) {
            return 1300;
        } else {
            return 0;
        }
    }

    public static void showInfoCombine(Player player) {
        if (player.combine.itemsCombine.size() != 1) {
            Service.gI().sendDialogMessage(player, "Trang bị không phù hợp");
            return;//clm mấy ở nhà khó làm vc lm k quen
        }
        Item item = player.combine.itemsCombine.get(0);
        if (item == null || !item.isNotNullItem()) {
            return;
        }
        if (item.isHaveOption(93)) {
            Service.gI().sendDialogMessage(player, "Trang bị có hạn sử dụng, không thể thực hiện");
            return;
        }
        if (!item.canPhaLeHoa()) {
            Service.gI().sendDialogMessage(player, "Trang bị không phù hợp");
            return;
        }
        long star = item.getOptionParam(107);
        int gem = getGem(star);
        long gold = getGold(star);
        if (star >= CombineService.MAX_STAR_ITEM) {
            Service.gI().sendDialogMessage(player, "Đã đạt số pha lê tối đa");
            return;
        }
        StringBuilder text = new StringBuilder();
        text.append(ConstFont.BOLD_BLUE).append(item.template.name).append("\n");
        text.append(ConstFont.BOLD_DARK).append(item.getOptionInfo()).append("\n");
        text.append(ConstFont.BOLD_GREEN).append(star + 1).append(" ô Sao Pha Lê\n");
        // text.append(ConstFont.BOLD_BLUE).append("Tỉ lệ thành công: ").append(getRatioStr(star)).append("%\n");
        text.append(player.inventory.gold < gold ? ConstFont.BOLD_RED : ConstFont.BOLD_BLUE).append("Cần ").append(Util.numberToMoney(gold)).append(" vàng");
        if (player.inventory.gold < gold) {
            CombineService.gI().baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, text.toString(),
                    "Còn thiếu\n" + Util.numberToMoney(gold - player.inventory.gold) + " vàng");
            return;
        }
        CombineService.gI().baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, text.toString(),
                "Nâng cấp\n" + gem + " ngọc\nx100 lần", "Nâng cấp\n" + gem + " ngọc\nx10 lần", "Nâng cấp\n" + gem + " ngọc", "Từ chối");
    }

    public static void phaLeHoa(Player player, int... numm) {
        int n = 1;
        if (numm.length > 0) {
            n = numm[0];
        }
        if (!player.combine.itemsCombine.isEmpty()) {
            Item item = player.combine.itemsCombine.get(0);
            if (item == null || !item.isNotNullItem() || item.isHaveOption(93) || !item.canPhaLeHoa()) {
                return;
            }
            long star = item.getOptionParam(107);
            if (star >= CombineService.MAX_STAR_ITEM) {
                return;
            }
            long gold = getGold(star);
            int gem = getGem(star);
            if (n == 1) {
                if (player.inventory.gold < gold) {
                    return;
                } else if (player.inventory.getGemAndRuby() < gem) {
                    Service.gI().sendServerMessage(player, "Bạn không đủ ngọc, còn thiếu " + (gem - player.inventory.getGemAndRuby()) + " ngọc nữa");
                    return;
                }
            }
            int num = 0;
            boolean success = false;
            for (int i = 0; i < n; i++) {
                num = i + 1;
                if (player.inventory.getGemAndRuby() < gem) {
                    Service.gI().sendServerMessage(player, "Sau " + i + " lần nâng cấp thất bại, bạn không đủ ngọc để tiếp tục.");
                    break;
                }
                if (player.inventory.gold < gold) {
                    Service.gI().sendServerMessage(player, "Sau " + i + " lần nâng cấp thất bại, bạn không đủ vàng để tiếp tục.");
                    break;
                }
                player.inventory.gold -= gold;
                player.inventory.subGemAndRuby(gem);
                if (Util.isTrue(getRatio(star), 100)) {
                    success = true;
                    break;
                }
            }
            if (success) {
                item.addOptionParam(107, 1);
                if (star > 4 && !player.isAdmin()) {
                    ServerNotify.gI().notify("Chúc mừng " + player.name + " vừa pha lê hóa "
                            + "thành công " + item.template.name + " lên " + (star + 1) + " sao pha lê");
                }
                if (n > 1) {
                    Service.gI().sendServerMessage(player, "Thành công sau " + num + " lần nâng cấp.");
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

}
