package npc.npc_manifest;

/**
 *
 *
 */
import consts.ConstNpc;
import item.Item;
import item.Item.ItemOption;
import npc.Npc;
import player.Player;
import server.Manager;
import services.InventoryService;
import services.ItemService;
import services.Service;
import services.func.ChangeMapService;
import services.func.UseItem;
import utils.Util;

public class hatsu extends Npc {

    public hatsu(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            switch (mapId) {
                case 0 -> {
                    createOtherMenu(player, ConstNpc.BASE_MENU, "Ta Sẽ giúp Ngươi Thống Trị Thế Gioi\nHãy Luyện Tập Nào",
                            "Đến Map\nNgũ Hành", "Đến Map\nPhòngTập\nTgian", "Top\nNgũ Hành", "Top\nPhòng Tập");
                }
                case 49 -> {
                    createOtherMenu(player, ConstNpc.BASE_MENU, "Ta Rất Thất Vọng Về Ngươi\n"
                            + "|7|" + "Điểm Tập Luyện: " + Util.FormatNumber(player.point_event) + " Điểmღ\n",
                            "Về Làng Aru", "Nhận Cải Trang\nUp Ngọc");

                }
                default ->
                    super.openBaseMenu(player);
            }
        }
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            if (player.iDMark.isBaseMenu()) {
                switch (mapId) {
                    case 0 -> {
                        if (select == 0) {
                            ChangeMapService.gI().changeMapNonSpaceship(player, 123, 191, 384);
                        }
                        if (select == 1) {
                            ChangeMapService.gI().changeMapNonSpaceship(player, 49, 50, 384);
                        }
                        if (select == 2) {
                            Service.gI().showListTop(player, Manager.TopNGUHANH);

                        }

                        if (select == 3) {
                            Service.gI().showListTop(player, Manager.TopPhongtap);

                        }
                    }

                    case 49 -> {
                        if (select == 0) {
                            ChangeMapService.gI().changeMapNonSpaceship(player, 0, Util.nextInt(700, 800), 432);
                        }
                        if (select == 1) {
                            if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                                int evPoint = player.point_event;
                                if (evPoint >= 7999) {
                                    Item HopQua = ItemService.gI().createNewItem((short) 1834, 1);
                                    HopQua.itemOptions.add(new ItemOption(239, 0));
                                    HopQua.itemOptions.add(new ItemOption(93, Util.nextInt(1, 3)));
                                    player.point_event -= 7999;
                                    InventoryService.gI().addItemBag(player, HopQua, 1);
                                    InventoryService.gI().sendItemBag(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được Cải Trang");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Cần 7999 điểm để đổi");
                                }
                            } else {
                                Service.getInstance().sendThongBao(player, "Hành trang đầy.");
                            }
                            break;
                        }

                    }
                }
            }
        }
    }

}
