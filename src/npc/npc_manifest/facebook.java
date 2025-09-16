package npc.npc_manifest;

/**
 *
 */
import boss.BossManager;
import consts.ConstNpc;
import item.Item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import npc.Npc;
import player.Player;
import server.Manager;
import services.InventoryService;
import services.ItemService;
import services.Service;
import services.func.Input;
import shop.ShopService;

public class facebook extends Npc {

    public facebook(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            List<String> menu = new ArrayList<>(Arrays.asList(
                    "Tặng Mảnh\n Tàn Hồn",
                    "Xem Danh Sách \nBoss",
                    "Top Vĩ Thú",
                    "Top Săn Boss"
            ));

            if (!player.inventory.itemsDaBan.isEmpty()) {
                menu.add(4, "Mua lại\nvật phẩm\nđã bán [" + player.inventory.itemsDaBan.size() + "/20]");
            }

            String[] menus = menu.toArray(new String[0]);

            createOtherMenu(player, ConstNpc.BASE_MENU,
                    "|7|SỰ KIỆN SĂN BẮT\n"
                    + "|4|Tham Gia Săn Bắt Vĩ Thú SSS Tại Các Map Bên Fide\n"
                    + "|6|Tiêu Diệt Boss Nhặt Tàn Hồn Thú Đủ 99 Cái Tặng Cho Ta\n"
                    + "Ta Sẽ Cho Con Ngẫu Nhiên 1 quả Trứng Thần Thú SSS Cực Ngon\n"
                    + "Mở Ra Ngẫu Nhiên Nhận 1 Pét Vĩ Thú Ramdom 1-99% SD Có Tỉ Lệ Vĩnh Viễn\n"
                            + "|7|Điểm Vĩ Thú: " + player.point_vithu + " Điểm",
                    menus);
        }
    }

     @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            if (player.iDMark.isBaseMenu()) {
                switch (mapId) {
                    case 5 -> {
                        if (select == 0) {
                            Item manhtanhon = null;
                            Item thoivang = null;
                         
                            try {
                                manhtanhon = InventoryService.gI().findItemBag(player, 1204);
                                thoivang = InventoryService.gI().findItemBag(player, 457);
                              
                            } catch (Exception e) {
                            }
                            if (manhtanhon == null
                                    || thoivang == null
                                    || manhtanhon.quantity < 99
                                    || thoivang.quantity < 1000) {
                                this.npcChat(player, "Bạn Cần x99 Mảnh Tàn Hồn  + 1000 zenni");
                            } else if (InventoryService.gI().getCountEmptyBag(player) == 7) {
                                this.npcChat(player, "Hành Trang Của Bạn Không Đủ Chỗ Trống");

                            } else {
                                InventoryService.gI().subQuantityItemsBag(player, manhtanhon, 99);
                                InventoryService.gI().subQuantityItemsBag(player, thoivang, 1000);

                                Item trungthanthu = ItemService.gI().createNewItem((short) 1205);
                                trungthanthu.itemOptions.add(new Item.ItemOption(244, 2024));
                                InventoryService.gI().addItemBag(player, trungthanthu, 99999999);
                                InventoryService.gI().sendItemBag(player);
                                Service.getInstance().sendThongBao(player, "Bạn nhận được " + trungthanthu.template.name);
                            }
                            break;

                        }
                        if (select == 1) {
                        BossManager.gI().showListBoss(player);
                        }
                        
                        if (select == 2 ){
                         Service.gI().showListTop(player, Manager.Topvithu);
                            
                        }
                        if (select == 3){
                         Service.gI().showListTop(player, Manager.Topsanboss);
                            
                        }
                        {
                           
                        }

                    }
                }
            }
        }
    }
}
