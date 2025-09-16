package npc.npc_manifest;

/**
 *
 */
import consts.ConstNpc;
import consts.ConstTask;
import consts.ConstTaskBadges;
import item.Item;

import java.util.ArrayList;
import java.util.List;

import jdbc.daos.PlayerDAO;
import npc.Npc;
import player.Player;
import services.InventoryService;
import services.ItemService;
import services.NpcService;
import services.PetService;
import services.Service;
import services.TaskService;
import services.func.Input;
import static services.func.Input.doico4la;
import shop.ShopService;
import task.Badges.BadgesTaskService;
import utils.Util;

public class OngGohan extends Npc {

    public OngGohan(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    int costNapVang = 1;

    int[][] napVang = {{20000, 400}, {50000, 1050}, {100000, 2500}, {500000, 15000}, {1000000, 31000}, {2000000, 65000}, {5000000, 170000}};

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                this.createOtherMenu(player, ConstNpc.BASE_MENU,
                        "|7|Xin Chào Anh Chị Đến Với Saga\n"
                        + "|4|Bên Em Phục Vụ Từ AZ Cực Rẻ\n"
                        + "Wesite:https://Dragonballsaga.vn",
                        "GiftCode",
                        "Đổi\nTàiNguyên",
                        "Đổi mật khẩu",
                        "Nhận Đệ",
                        "Nạp Tiền",
                        "Next Nv");
            }
        }
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            if (player.iDMark.isBaseMenu()) {
                switch (select) {
                    case 0: // mã quà tặng
                        Input.gI().createFormGiftCode(player);
                        break;
                    case 1: // nạp tiền
                        String npcSay = "|7|Số dư Vnd: " + Util.numberToText(player.getSession().vnd) + " VND\n dùng để Đổi Vàng Ngọc\n"
                                + "Kho Cất giữ " + Util.numberToText(player.getSession().goldBar) + " Zenni";
                        createOtherMenu(player, ConstNpc.NAP_TIEN, npcSay,
                                "Đổi Zenni",
                                "Nhận\nZenni",
                                "Nhận\nNgọc Xanh",
                                "Đổi\nRuby",
                                "Đổi\nCỏ 4 Lá",
                                "Đóng");
                        break;
                    case 2:
                        Input.gI().createFormChangePassword(player);
                        break;

                    case 3:
                        if (player.pet == null) {
                            PetService.gI().createNormalPet(player);
                            Service.gI().sendThongBao(player, "Bạn vừa nhận được đệ tử");
                        } else {
                            this.npcChat(player, "Bạn đã có rồi");
                        }
                        break;

                    case 4:
                        NpcService.gI().createBigMessage(player, avartar, "Ấn Mở Vào Web Nạp Tự Động\b"
                                + "|7|Số Dư " + Util.numberToText(player.getSession().vnd) + " VND",
                                (byte) 1, "Mở", "https://Dragonballsaga.vn");
                        break;

                    case 5:
                        if (player.playerTask.taskMain.id < 20) {
                            player.playerTask.taskMain.id = 20;
                            TaskService.gI().sendNextTaskMain(player);
                              } else {
                            this.npcChat(player, "Mày Không Thể Next quá Nv kuku");
                        }
                        break;

                }
            } else if (player.iDMark.getIndexMenu() == ConstNpc.NAP_TIEN) {
                switch (select) {
                    case 0:
                        List<String> menu = new ArrayList<>();
                        for (int i = 0; i < napVang.length; i++) {
                            menu.add(i, Util.numberToText(napVang[i][0]) + "\n" + Util.numberToText(napVang[i][1] * costNapVang) + " Zenni");
                        }
                        String[] menus = menu.toArray(new String[0]);
                        createOtherMenu(player, ConstNpc.NAP_VANG, "Ta sẽ giữ giúp con\n"
                                + "Nếu con cần dùng tới hãy quay lại đây gặp ta!", menus);
                        break;
                    case 1:
                        if (player.getSession().goldBar > 0) {
                            List<Item> listItem = new ArrayList<>();
                            Item thoiVang = ItemService.gI().createNewItem((short) 457, player.getSession().goldBar);
                            listItem.add(thoiVang);
                            if (InventoryService.gI().getCountEmptyBag(player) < listItem.size()) {
                                Service.gI().sendThongBao(player, "Cần ít nhất " + listItem.size() + " ô trống trong hành trang");
                            }
                            for (Item it : listItem) {
                                InventoryService.gI().addItemBag(player, it, 999999);
                                InventoryService.gI().sendItemBag(player);
                            }
                            Service.gI().sendThongBao(player, "Bạn đã nhận được " + player.getSession().goldBar + " Zenni");
                            PlayerDAO.subGoldBar(player, -(napVang[select][1] * costNapVang));
                            PlayerDAO.subGoldBar(player, player.getSession().goldBar);
                        }
                        break;
                    case 2:
                        Input.gI().createFormQDngocxanh(player);
                        break;
                    case 3:
                        Input.gI().createFormQDhongngoc(player);
                        break;
                     case 4:
                        Input.gI().createFormQDco4la(player);
                        break;   
                        
                        

                }
            } else if (player.iDMark.getIndexMenu() == ConstNpc.NAP_VANG) {
                if (player.getSession().vnd >= napVang[select][0]) {
                    List<Item> listItem = new ArrayList<>();
                    if (InventoryService.gI().getCountEmptyBag(player) < listItem.size()) {
                        Service.gI().sendThongBao(player, "Cần ít nhất " + listItem.size() + " ô trống trong hành trang");
                    }
                    for (Item it : listItem) {
                        InventoryService.gI().addItemBag(player, it, 9999999);

                    }
                    InventoryService.gI().sendItemBag(player);
                    PlayerDAO.subcash(player, napVang[select][0]);
                    BadgesTaskService.updateCountBagesTask(player, ConstTaskBadges.DAI_GIA_MOI_NHU, napVang[select][0]);
                    PlayerDAO.subGoldBar(player, -(napVang[select][1] * costNapVang));
                    Service.gI().sendThongBao(player, "Bạn có thêm " + Util.numberToText(napVang[select][1] * costNapVang) + " Zenni");
                } else {
                    Service.gI().sendThongBao(player, "Không đủ số dư");
                }
            }
        }
    }
}
