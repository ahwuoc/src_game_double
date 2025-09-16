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
import shop.ShopService;
import utils.Util;

public class SUKIEN365 extends Npc {

    public SUKIEN365(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            switch (mapId) {
                case 5 -> {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "|7|SỰ KIỆN\n"
                            + "|6|GIẢI PHÓNG 30/4\n"
                            + "Truy tìm Boss Xe Tăng 390 Tiêu Diệ Nhận Mảnh Cờ\n"
                            + "Up quái Nhẫu Nhiên Thu Thập Được Mũ Cối Việt Nam\n"
                            + "|7|Thu Thập Đủ 999 Mảnh + 500 MũCối + 500 zenni\n"
                            + "|4|Tiến Hành Ghép Và Cắm Cờ Giải Phóng Tại [ NPC SỰ KIỆN ]\n"
                            + "Khi Cắm Cờ [Tặng 1 Điểm SK]+[1 Cải Trang + Huy Hiệu]\n"
                            + "[Shop Huy Hiệu Cho Đổi Pét Xe Tăng Vĩnh Viễn] + 1 Số Vp\n"
                            + "Trong Tgian Tham Gia có Thể Nhận Được Cải Trang Vĩnh Viễn\n"
                            + "|7|Điểm Sự Kiện " + Util.FormatNumber(player.sukien) + " Điểm",
                            "Ghép Mảnh Cờ",
                            "Cắm Cờ\nGiải Phóng",
                            "Cửa Hàng\nNguyên Liệu",
                            "Cửa Hàng\nHuy Hiệu",
                            "Nhận CảiTrang\n Sự Kiện",
                            "Top Sự Kiện"
                    );

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
                    case 5 -> {
                        if (select == 0) {

                            Item manhco = null;
                            Item thoivang = null;
                            Item mucoi = null;
                            try {
                                manhco = InventoryService.gI().findItemBag(player, 1015);
                                thoivang = InventoryService.gI().findItemBag(player, 457);
                                mucoi = InventoryService.gI().findItemBag(player, 1017);
                            } catch (Exception e) {
                            }
                            if (manhco == null
                                    || thoivang == null
                                    || mucoi == null
                                    || manhco.quantity < 999
                                    || mucoi.quantity < 500
                                    || thoivang.quantity < 500) {
                                this.npcChat(player, "Bạn Cần x999 Mảnh Cờ Giai Phóng + 500 Mũ Cối + 500 zenni");
                            } else if (InventoryService.gI().getCountEmptyBag(player) == 7) {
                                this.npcChat(player, "Hành Trang Của Bạn Không Đủ Chỗ Trống");

                            } else {
                                InventoryService.gI().subQuantityItemsBag(player, manhco, 999);
                                InventoryService.gI().subQuantityItemsBag(player, mucoi, 500);
                                InventoryService.gI().subQuantityItemsBag(player, thoivang, 500);

                                Item cogiaiphong = ItemService.gI().createNewItem((short) 1016);
                                cogiaiphong.itemOptions.add(new ItemOption(244, 2024));
                                InventoryService.gI().addItemBag(player, cogiaiphong, 99999999);
                                InventoryService.gI().sendItemBag(player);
                                Service.getInstance().sendThongBao(player, "Bạn nhận được " + cogiaiphong.template.name);
                            }
                            break;

                        }
                        if (select == 1) {

                            Item cogiaiphong = null;
                            Item thoivang = null;
                            try {
                                cogiaiphong = InventoryService.gI().findItemBag(player, 1016);
                                thoivang = InventoryService.gI().findItemBag(player, 457);
                            } catch (Exception e) {
                            }
                            if (cogiaiphong == null
                                    || thoivang == null
                                    || cogiaiphong.quantity < 1
                                    || thoivang.quantity < 500) {
                                this.npcChat(player, "Bạn Cần x1 Cờ Giai Phóng + 500 zenni");
                            } else if (InventoryService.gI().getCountEmptyBag(player) == 7) {
                                this.npcChat(player, "Hành Trang Của Bạn Không Đủ Chỗ Trống");

                            } else {
                                InventoryService.gI().subQuantityItemsBag(player, cogiaiphong, 1);
                                InventoryService.gI().subQuantityItemsBag(player, thoivang, 500);
                                
                                Item huyhieu = ItemService.gI().createNewItem((short) 1018);
                                huyhieu.itemOptions.add(new ItemOption(244, 0));
                                huyhieu.itemOptions.add(new ItemOption(72, Util.nextInt(1, 10)));
                                InventoryService.gI().addItemBag(player, huyhieu, 99999999);
                                InventoryService.gI().sendItemBag(player);
                                Service.getInstance().sendThongBao(player, "Bạn nhận được " + huyhieu.template.name);
                                

                                Item caitrang = ItemService.gI().createNewItem((short) 1014);
                                caitrang.itemOptions.add(new ItemOption(50, Util.nextInt(50, 120)));
                                caitrang.itemOptions.add(new ItemOption(77, Util.nextInt(50, 129)));
                                caitrang.itemOptions.add(new ItemOption(103, Util.nextInt(50, 120)));
                                caitrang.itemOptions.add(new ItemOption(5, Util.nextInt(5, 100)));
                                caitrang.itemOptions.add(new ItemOption(237, Util.nextInt(1, 5)));
                                caitrang.itemOptions.add(new ItemOption(72, Util.nextInt(1, 10)));
                                if (Util.isTrue(99, 100)) {
                                    caitrang.itemOptions.add(new ItemOption(93, Util.nextInt(1, 10)));
                                }
                                InventoryService.gI().addItemBag(player, caitrang, 99999999);
                                InventoryService.gI().sendItemBag(player);
                                Service.getInstance().sendThongBao(player, "Bạn nhận được " + caitrang.template.name);

                                player.sukien += 1;
                                Service.getInstance().sendThongBao(player, "Bạn Nhận Được 1 Điểm Sự Kiện");
                            }

                            break;

                        }
                        if (select == 2) {
                          ShopService.gI().opendShop(player, "NGUYENLIEU", false);

                        }
                        if (select == 3) {
                          ShopService.gI().opendShop(player, "HUYHIEU", false);
                        }
                        if (select == 4) {
                              
                            Item thoivang = null;
                            try {
                               
                                thoivang = InventoryService.gI().findItemBag(player, 457);
                            } catch (Exception e) {
                            }
                            if (thoivang == null
                                    || thoivang.quantity < 1000) {
                                this.npcChat(player, "Bạn Cần x1 ZENNI");
                            } else if (InventoryService.gI().getCountEmptyBag(player) == 7) {
                                this.npcChat(player, "Hành Trang Của Bạn Không Đủ Chỗ Trống");

                            } else {
                               
                                InventoryService.gI().subQuantityItemsBag(player, thoivang, 1000);

                                Item caitrang = ItemService.gI().createNewItem((short) 1014);
                                caitrang.itemOptions.add(new ItemOption(50, Util.nextInt(50, 120)));
                                caitrang.itemOptions.add(new ItemOption(77, Util.nextInt(50, 120)));
                                caitrang.itemOptions.add(new ItemOption(103, Util.nextInt(50, 120)));
                                caitrang.itemOptions.add(new ItemOption(5, Util.nextInt(50, 120)));
                                caitrang.itemOptions.add(new ItemOption(237, Util.nextInt(1, 8)));
                                caitrang.itemOptions.add(new ItemOption(72, Util.nextInt(1, 10)));
                                caitrang.itemOptions.add(new ItemOption(93, 1));
                             
                                InventoryService.gI().addItemBag(player, caitrang, 99999999);
                                InventoryService.gI().sendItemBag(player);
                                Service.getInstance().sendThongBao(player, "Bạn nhận được " + caitrang.template.name);
                            
                            }

                            break;
                        }
                        if (select == 5) {
                          Service.gI().showListTop(player, Manager.Topsk);

                        }
                    }

                   
                    }
                }
            }
        }
    }


