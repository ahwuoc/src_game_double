package services.func;

import boss.BossManager;
import consts.ConstItem;
import models.Combine.CombineService;
import models.ShenronEvent.ShenronEventService;
import models.Card.Card;
import models.Card.RadarService;
import models.Card.RadarCard;
import consts.ConstMap;
import item.Item;
import consts.ConstNpc;
import consts.ConstPlayer;
import item.Item.ItemOption;
import static java.awt.SystemColor.text;
import map.Zone;
import player.Inventory;
import services.*;
import player.Player;
import skill.Skill;
import network.Message;
import utils.SkillUtil;
import utils.TimeUtil;
import utils.Util;
import server.io.MySession;
import utils.Logger;
import java.util.Random;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import jdbc.daos.PlayerDAO;
import server.Client;

public class UseItem {

    private static final int ITEM_BOX_TO_BODY_OR_BAG = 0;
    private static final int ITEM_BAG_TO_BOX = 1;
    private static final int ITEM_BODY_TO_BOX = 3;
    private static final int ITEM_BAG_TO_BODY = 4;
    private static final int ITEM_BODY_TO_BAG = 5;
    private static final int ITEM_BAG_TO_PET_BODY = 6;
    private static final int ITEM_BODY_PET_TO_BAG = 7;

    private static final byte DO_USE_ITEM = 0;
    private static final byte DO_THROW_ITEM = 1;
    private static final byte ACCEPT_THROW_ITEM = 2;
    private static final byte ACCEPT_USE_ITEM = 3;

    private static UseItem instance;

    private int randClothes(int level) {
        return ConstItem.LIST_ITEM_CLOTHES[Util.nextInt(0, 2)][Util.nextInt(0, 4)][level - 1];
    }

    private UseItem() {

    }

    public static UseItem gI() {
        if (instance == null) {
            instance = new UseItem();
        }
        return instance;
    }

    public void getItem(MySession session, Message msg) {
        Player player = session.player;
        if (player == null) {
            return;
        }
        TransactionService.gI().cancelTrade(player);
        try {
            int type = msg.reader().readByte();
            int index = msg.reader().readByte();
            if (index == -1) {
                return;
            }
            switch (type) {
                case ITEM_BOX_TO_BODY_OR_BAG:
                    InventoryService.gI().itemBoxToBodyOrBag(player, index);
                    TaskService.gI().checkDoneTaskGetItemBox(player);
                    break;
                case ITEM_BAG_TO_BOX:
                    InventoryService.gI().itemBagToBox(player, index);
                    break;
                case ITEM_BODY_TO_BOX:
                    InventoryService.gI().itemBodyToBox(player, index);
                    break;
                case ITEM_BAG_TO_BODY:
                    InventoryService.gI().itemBagToBody(player, index);
                    break;
                case ITEM_BODY_TO_BAG:
                    InventoryService.gI().itemBodyToBag(player, index);
                    break;
                case ITEM_BAG_TO_PET_BODY:
                    InventoryService.gI().itemBagToPetBody(player, index);
                    break;
                case ITEM_BODY_PET_TO_BAG:
                    InventoryService.gI().itemPetBodyToBag(player, index);
                    break;
            }
            if (player.setClothes != null) {
                player.setClothes.setup();
            }
            if (player.pet != null) {
                player.pet.setClothes.setup();
            }
            player.setClanMember();
            Service.gI().sendFlagBag(player);
            Service.gI().point(player);
            Service.gI().sendSpeedPlayer(player, -1);
        } catch (Exception e) {
            Logger.logException(UseItem.class, e);

        }
    }

    public Item finditem(Player player, int iditem) {
        for (Item item : player.inventory.itemsBag) {
            if (item.isNotNullItem() && item.template.id == iditem) {
                return item;
            }
        }
        return null;
    }

    public void doItem(Player player, Message _msg) {
        TransactionService.gI().cancelTrade(player);
        Message msg = null;
        byte type;
        try {
            type = _msg.reader().readByte();
            int where = _msg.reader().readByte();
            int index = _msg.reader().readByte();
            switch (type) {
                case DO_USE_ITEM:
                    if (player != null && player.inventory != null) {
                        if (index != -1) {
                            if (index < 0) {
                                return;
                            }
                            Item item = player.inventory.itemsBag.get(index);
                            if (item.isNotNullItem()) {
                                if (item.template.type == 7) {
                                    msg = new Message(-43);
                                    msg.writer().writeByte(type);
                                    msg.writer().writeByte(where);
                                    msg.writer().writeByte(index);
                                    msg.writer().writeUTF("Bạn chắc chắn học "
                                            + player.inventory.itemsBag.get(index).template.name + "?");
                                    player.sendMessage(msg);
                                } else if (item.template.id == 570) {
                                    // if (!Util.isAfterMidnight(player.lastTimeRewardWoodChest)) {
                                    // Service.gI().sendThongBao(player, "Hãy chờ đến ngày mai");
                                    // return;
                                    // }
                                    msg = new Message(-43);
                                    msg.writer().writeByte(type);
                                    msg.writer().writeByte(where);
                                    msg.writer().writeByte(index);
                                    msg.writer().writeUTF("Bạn chắc muốn mở\n"
                                            + player.inventory.itemsBag.get(index).template.name + " ?");
                                    player.sendMessage(msg);

                                } else if (item.template.type == 22) {
                                    if (player.zone.items.stream()
                                            .filter(it -> it != null && it.itemTemplate.type == 22).count() > 2) {
                                        Service.gI().sendThongBaoOK(player, "Mỗi map chỉ đặt được 3 Vệ Tinh");
                                        return;
                                    }
                                    msg = new Message(-43);
                                    msg.writer().writeByte(type);
                                    msg.writer().writeByte(where);
                                    msg.writer().writeByte(index);
                                    msg.writer().writeUTF("Bạn chắc muốn dùng\n"
                                            + player.inventory.itemsBag.get(index).template.name + " ?");
                                    player.sendMessage(msg);

                                } else if (item.template.id == 401 || item.template.id == 722 || item.template.id == 1212 || item.template.id == 1213 || item.template.id == 1214
                                        || item.template.id == 1215 || item.template.id == 1885) {
                                    msg = new Message(-43);
                                    msg.writer().writeByte(type);
                                    msg.writer().writeByte(where);
                                    msg.writer().writeByte(index);
                                    msg.writer().writeUTF("Mày Lưu Ý ( Trước Khi Đổi Đệ Tử Mới Hãy Tháo Trang Bị) Ngu Thì Đừng Trách");
                                    player.sendMessage(msg);
                                } else {
                                    UseItem.gI().useItem(player, item, index);
                                }
                            }
                        } else {
                            int iditem = _msg.reader().readShort();
                            Item item = finditem(player, iditem);
                            UseItem.gI().useItem(player, item, index);
                        }
                    }
                    break;
                case DO_THROW_ITEM:
                    if (!(player.zone.map.mapId == 21 || player.zone.map.mapId == 22 || player.zone.map.mapId == 23)) {
                        Item item = null;
                        if (index < 0) {
                            return;
                        }
                        if (where == 0) {
                            item = player.inventory.itemsBody.get(index);
                        } else {
                            item = player.inventory.itemsBag.get(index);
                        }

                        if (item.isNotNullItem() && item.template.id == 570) {
                            Service.gI().sendThongBao(player, "Không thể bỏ vật phẩm này.");
                            return;
                        }
                        if (!item.isNotNullItem()) {
                            return;
                        }
                        msg = new Message(-43);
                        msg.writer().writeByte(type);
                        msg.writer().writeByte(where);
                        msg.writer().writeByte(index);
                        msg.writer().writeUTF("Bạn chắc chắn muốn vứt " + item.template.name + "?");
                        player.sendMessage(msg);
                    } else {
                        Service.gI().sendThongBao(player, "Không thể thực hiện");
                    }
                    break;
                case ACCEPT_THROW_ITEM:
                    InventoryService.gI().throwItem(player, where, index);
                    Service.gI().point(player);
                    InventoryService.gI().sendItemBag(player);
                    break;
                case ACCEPT_USE_ITEM:
                    UseItem.gI().useItem(player, player.inventory.itemsBag.get(index), index);
                    break;
            }
        } catch (Exception e) {
            Logger.logException(UseItem.class, e);
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    private void useItem(Player pl, Item item, int indexBag) {
        if (item != null && item.isNotNullItem()) {
            if (item.template.id == 570) {
                int time = (int) TimeUtil.diffDate(new Date(), new Date(item.createTime), TimeUtil.DAY);
                if (time == 0) {
                    Service.gI().sendThongBao(pl, "Hãy chờ đến ngày mai");
                } else {
                    openRuongGo(pl, item);
                }
                return;
            }
            if (item.template.strRequire <= pl.nPoint.power) {
                switch (item.template.type) {
                    case 33: // card
                        UseCard(pl, item);
                        break;
                    case 7: // sách học, nâng skill
                        learnSkill(pl, item);
                        break;
                    case 6: // đậu thần
                        this.eatPea(pl);
                        break;
                    case 12: // ngọc rồng các loại
                        controllerCallRongThan(pl, item);
                        break;
                    case 21:
                    case 23: // thú cưỡi mới
                    case 24: // thú cưỡi cũ
                    case 77: // Sách tuyệt kĩ
                        InventoryService.gI().itemBagToBody(pl, indexBag);
                        break; // adu:))
                    case 11: // item bag
                        InventoryService.gI().itemBagToBody(pl, indexBag);
                        Service.gI().sendFlagBag(pl);
                        break;//baotri ddck
//                    case 75:
//                        InventoryService.gI().itemBagToBody(pl, indexBag);
//                        Service.gI().sendchienlinh(pl, (short) (item.template.iconID - 1));
//                        break;
                    case 72: {
                        InventoryService.gI().itemBagToBody(pl, indexBag);
                        Service.gI().sendPetFollow(pl, (short) (item.template.iconID - 1));
                        break;
                    }
                    case 98: {
                        InventoryService.gI().itemBagToBody(pl, indexBag);
                        Service.gI().sendEffPlayer(pl);
                        break;
                    }
                    case 99: {
                        InventoryService.gI().itemBagToBody(pl, indexBag);
                        Service.gI().sendEffPlayer(pl);
                        break;
                    }
                    case 82:
                        InventoryService.gI().itemBagToBody(pl, indexBag);
                        Service.getInstance().sendFoot(pl, item.template.id);
                        break;
                    case 83:
                        InventoryService.gI().itemBagToBody(pl, indexBag);
                        Service.gI().sendEffPlayer(pl);
                        break;
                    case 84, 85, 86: {
                        InventoryService.gI().itemBagToBody(pl, indexBag);
                        break;
                    }
                    default:
                        switch (item.template.id) {

                            case 1000:
                                if (pl.diemfam >= 1000) {
                                    pl.diemfam -= 1000;
                                } else {
                                    Service.getInstance().sendThongBaoFromAdmin(pl,
                                            "Anh Zai Không có điểm Để Mở Lần Này \nHãy Up Điểm Ngũ Hành Sơn Để Mở Cải Trang Vinh Viễn" + "\nCần " + (1000 - pl.diemfam) + " Điểm Nữa");
                                    return;
                                }
                                usecaitrangbase(pl, item);
                                break;
                            case 1001:
                                if (pl.point_event >= 2000) {
                                    pl.point_event -= 2000;
                                } else {
                                    Service.getInstance().sendThongBaoFromAdmin(pl,
                                            "Anh Zai Không có điểm Để Mở Lần Này \nHãy Up Điểm Map Phòng Luyện Tập Thời Gian\n Để Mở Lấy 99 Nro 7s" + "\nCần " + (2000 - pl.point_event) + " Điểm Nữa");
                                    return;
                                }
                                usengocrong7sao(pl, item);
                                break;
                            case 1002:
                                hopQuaTanThu(pl, item);
                                break;
                            case 457:
                                useThoiVang(pl, item);
                                break;

                            case 1044, 1260:
                                useGokuDay(pl, item);
                                break;
                            case 1261:
                                useGokuDayVip(pl, item);
                                break;
                            case 992: // Nhan thoi khong
                                pl.type = 2;
                                pl.maxTime = 5;
                                Service.gI().Transport(pl);
                                break;
                            case 361:
                                pl.idGo = (short) Util.nextInt(0, 6);
                                NgocRongNamecService.gI().menuCheckTeleNamekBall(pl);
                                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                                InventoryService.gI().sendItemBag(pl);
                                break;
                            case 942:
                                InventoryService.gI().itemBagToBody(pl, indexBag);
                                PetService.Pet2(pl, 966, 967, 968);
                                Service.gI().point(pl);
                                break;
                            case 943:
                                InventoryService.gI().itemBagToBody(pl, indexBag);
                                PetService.Pet2(pl, 969, 970, 971);
                                Service.gI().point(pl);
                                break;
                            case 944:
                                InventoryService.gI().itemBagToBody(pl, indexBag);
                                PetService.Pet2(pl, 972, 973, 974);
                                Service.gI().point(pl);
                                break;
//                            case 1019:
//                                InventoryService.gI().itemBagToBody(pl, indexBag);
//                                PetService.Pet2(pl, 1536, 1537, 1538);
//                                Service.gI().point(pl);
//                                break;      
//                                

                            case 967:
                                InventoryService.gI().itemBagToBody(pl, indexBag);
                                PetService.Pet2(pl, 1050, 1051, 1052);
                                Service.gI().point(pl);
                                break;
                            case 1107:
                                InventoryService.gI().itemBagToBody(pl, indexBag);
                                PetService.Pet2(pl, 1183, 1184, 1185);
                                Service.gI().point(pl);
                                break;

                            case 211: // nho tím
                            case 212: // nho xanh
                                eatGrapes(pl, item);
                                break;
                            case 342:
                            case 343:
                            case 344:
                            case 345:
                                if (pl.zone.items.stream().filter(it -> it != null && it.itemTemplate.type == 22)
                                        .count() < 3) {
                                    Service.gI().dropSatellite(pl, item, pl.zone, pl.location.x, pl.location.y);
                                    InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                                } else {
                                    Service.gI().sendThongBaoOK(pl, "Mỗi map chỉ đặt được 3 Vệ Tinh");
                                }
                                break;
                            case 380: // cskb
                                openCSKB(pl, item);
                                break;
                            case 381: // cuồng nộ
                            case 382: // bổ huyết
                            case 383: // bổ khí
                            case 384: // giáp xên
                            case 385: // ẩn danh
                            case 379: // máy dò capsule
                            case 638: // commeson
                            case 2075: // rocket
                            case 2160: // Nồi cơm điện
                            case 579:
                            case 1045: // đuôi khỉ
                            case 663: // bánh pudding
                            case 664: // xúc xíc
                            case 665: // kem dâu
                            case 666: // mì ly
                            case 667: // sushi
                            case 1099:
                            case 1100:
                            case 1101:
                            case 1102:
                            case 1103:
                            case 1216:
                                case 1233:
                                useItemTime(pl, item);
                                break;
                            case 1914:
                                OpenLinhThu(pl, item);
                                break;
                            case 1915:
                                OpenLinhThuVip(pl, item);
                                break;
                            case 880:
                            case 881:
                            case 882:
                                if (pl.itemTime.isEatMeal2) {
                                    Service.gI().sendThongBao(pl, "Chỉ được sử dụng 1 cái");
                                    break;
                                }
                                useItemTime(pl, item);
                                break;
                            case 521: // tdlt
                                useTDLT(pl, item);
                                break;
                            case 454: // bông tai
                                UseItem.gI().usePorata(pl);
                                break;
                            case 1405: // bông tai
                                UseItem.gI().usePorata3(pl);
                                break;
                            case 1406: // bông tai
                                UseItem.gI().usePorata4(pl);
                                break;
                            case 1108:
                                changeBerus(pl, item);
                                break;
                            case 722: //đổi đệ tử
                                changexencon(pl, item);
                                break;
                            case 1158: //đổi đệ tử
                                changeblack(pl, item);
                                break;
                            case 1159: //đổi đệ tử
                                changeblackrose(pl, item);
                                break;
                            case 1214: //đổi đệ tử
                                changezamasu(pl, item);
                                break;

                            case 1215: //đổi đệ tử
                                changecumber(pl, item);
                                break;

                            case 1885: //đổi đệ tử
                                changebroly(pl, item);
                                break;

                            case 1857: //đổi đệ tử
                                quatop1(pl, item);
                                break;
                            case 1858: //đổi đệ tử
                                quatop2(pl, item);
                                break;
                            case 1859: //đổi đệ tử
                                quatop3(pl, item);
                                break;
                            case 1860: //đổi đệ tử
                                quatop4(pl, item);
                                break;
                            case 1861: //đổi đệ tử
                                quatop5(pl, item);
                                break;
                            case 1862: //đổi đệ tử
                                quatop6(pl, item);
                                break;
                            case 1863: //đổi đệ tử
                                quatop7(pl, item);
                                break;

                            case 921: // bông tai
                                UseItem.gI().usePorata2(pl);
                                break;
                            case 193: // gói 10 viên capsule
                                openCapsuleUI(pl);
                                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                            case 194: // capsule đặc biệt
                                openCapsuleUI(pl);
                                break;
                            case 401: // đổi đệ tử
                                changePet(pl, item);
                                break;
                            case 402: // sách nâng chiêu 1 đệ tử
                            case 403: // sách nâng chiêu 2 đệ tử
                            case 404: // sách nâng chiêu 3 đệ tử
                            case 759: // sách nâng chiêu 4 đệ tử
                                upSkillPet(pl, item);
                                break;
                            case 726:
                                UseItem.gI().ItemManhGiay(pl, item);
                                break;
                            case 727:
                            case 728:
                                UseItem.gI().ItemSieuThanThuy(pl, item);
                                break;
                            case 648:
                                ItemService.gI().OpenItem648(pl, item);
                                break;
                            case 1865:
                                UseItem.gI().hopquaramdomthoivang(pl, item);
                                break;
                            case 1033:
                                UseItem.gI().hopquanoitai(pl, item);
                                break;
                            case 1034:
                                UseItem.gI().hopquadangusac(pl, item);
                                break;
                            case 1035:
                                UseItem.gI().hopquadanangcap(pl, item);
                                break;
                            case 1036:
                                UseItem.gI().hopquadabaove(pl, item);
                                break;
                            case 1037:
                                UseItem.gI().hopqualinhthu(pl, item);
                            case 1105:
                                UseItem.gI().setkichhoatthiensu(pl, item);
                                break;
                            case 1819:
                                UseItem.gI().mobitcoin(pl, item);
                                break;
                            case 1038:
                                UseItem.gI().hopquaphukien(pl, item);
                                break;
                            case 1458:
                                UseItem.gI().hopquacaocap(pl, item);
                                break;
                            case 1148:
                                Input.gI().ChatAll(pl);
                                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                                break;
                            case 1149:
                                Input.gI().Chatallplayer(pl);
                                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                                break;

                            case 1882:
                                UseItem.gI().goidau9(pl, item);
                                break;
                            case 1884:
                                UseItem.gI().setkichhoat(pl, item);
                                break;

                            case 1205:
                                UseItem.gI().usevithu(pl);
                                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                                break;
                            case 1160:
                                NpcService.gI().createMenuConMeo(pl, ConstNpc.ghepmanhtrung, -1,
                                        "Xin Chào Chủ Nhân\n"
                                        + "|8|Chức Năng Ghép Trứng Đệ\n"
                                        + "Yêu Cầu Mỗi Lần Ghép Mất 999 Mảnh\n"
                                        + "|7|Hiện Đang Có: " + (InventoryService.gI().findItemBag(pl, (short) 1160).quantity) + " Mảnh\n",
                                        "Ghép Nhanh"
                                );
                                break;

                            case 1169:
                                NpcService.gI().createMenuConMeo(pl, ConstNpc.ghepsachchiendau, -1,
                                        "|7|Xin Chào Chủ Nhân\n"
                                        + "|8|Chức Năng Ghép Sách Chiến Đấu\n"
                                        + "|6|Yêu Cầu Mỗi Lần Ghép Mất 999 Mảnh\n"
                                        + "|4|Chỉ Số Ramdom Sách Thường 1-40% sd\n"
                                        + "|6|Chỉ Số Ramdom Sách Cao Cấp 1-70%\n"
                                        + "|7|Hiện Đang Có: " + (InventoryService.gI().findItemBag(pl, (short) 1169).quantity) + " Mảnh\n",
                                        "Ghép Nhanh"
                                );
                                break;

                            case 1179:
                                NpcService.gI().createMenuConMeo(pl, ConstNpc.ghepmatgiatoc, -1,
                                        "|7|Xin Chào Chủ Nhân\n"
                                        + "|8|Chức Năng Ghép Mắt gia tộc\n"
                                        + "|6|Yêu Cầu Mỗi Lần Ghép Mất 999 Mảnh\n"
                                        + "|4|Chỉ Số Ramdom Sách Thường 1-40% sd\n"
                                        + "|6|Chỉ Số Ramdom Sách Cao Cấp 1-70%\n"
                                        + "|7|Hiện Đang Có: " + (InventoryService.gI().findItemBag(pl, (short) 1179).quantity) + " Mảnh\n",
                                        "Ghép Nhanh"
                                );
                                break;

                            case 1881:
                                BossManager.gI().showListBoss(pl);
                                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                                break;
                            case 1194:
                                NpcService.gI().createMenuConMeo(pl, ConstNpc.PKALL, -1,
                                        "Xin Chào Kiếm Sĩ Số 1 Võ Lâm\n"
                                        + "|8|Minh Chủ Lệnh\n"
                                        + "|7|Quyền Đại Khai Sát Gioi Võ Lâm\n",
                                        "Bật Tắt\n Đồ Sát"
                                );
                                break;
                            case 1866:
                                UseItem.gI().hopquaramdomngoc(pl, item);
                                break;
                            case 1867:
                                UseItem.gI().hopquaramdomcaitrangtho(pl, item);
                                break;
                            case 1856:
                                UseItem.gI().ramdomitem(pl, item);
                                break;

                            case 736:
                                ItemService.gI().OpenItem736(pl, item);
                                break;
                            case 987:
                                Service.gI().sendThongBao(pl, "Bảo vệ trang bị không bị rớt cấp"); // đá bảo vệ
                                break;
                            case 2006:
                                Input.gI().createFormChangeNameByItem(pl);
                                break;
                            case 1623:
                                TaskService.gI().sendNextTaskMain(pl);
                                break;
                            case 1228:
                                NpcService.gI().createMenuConMeo(pl, ConstNpc.HOP_QUA_THAN_LINH, -1,
                                        "Chọn hành tinh của đồ thần linh muốn nhận.",
                                        "Trái đất", "Namek", "Xayda");
                                break;
                            case 1626: {
                                int[] listItem = {856, 943, 942};
                                if (InventoryService.gI().getCountEmptyBag(pl) == 0) {
                                    Service.gI().sendThongBaoOK(pl, "Cần 1 ô hành trang để mở");
                                    return;
                                }
                                Item phuKien = ItemService.gI().createNewItem((short) listItem[Util.nextInt(2)]);
                                if (phuKien.template.id == 856) {
                                    phuKien.itemOptions.add(new Item.ItemOption(50, 10));
                                    phuKien.itemOptions.add(new Item.ItemOption(77, 10));
                                    phuKien.itemOptions.add(new Item.ItemOption(103, 10));
                                } else if (phuKien.template.id == 943) {
                                    phuKien.itemOptions.add(new Item.ItemOption(50, 10));
                                } else if (phuKien.template.id == 942) {
                                    phuKien.itemOptions.add(new Item.ItemOption(77, 10));
                                    phuKien.itemOptions.add(new Item.ItemOption(103, 10));
                                }
                                if (Util.isTrue(95, 100)) {
                                    phuKien.itemOptions.add(new Item.ItemOption(93, Util.nextInt(1, 5)));
                                }
                                InventoryService.gI().addItemBag(pl, phuKien, 999999);
                                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                                InventoryService.gI().sendItemBag(pl);
                                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + phuKien.template.name);
                            }
                            break;
                            case 1628: {
                                Player player = pl;
                                if (player.pet != null) {
                                    if (player.pet.playerSkill.skills.get(1).skillId != -1) {
                                        player.pet.openSkill2();
                                    } else {
                                        Service.gI().sendThongBao(player, "Ít nhất đệ tử ngươi phải có chiêu 2 chứ!");
                                        return;
                                    }
                                } else {
                                    Service.gI().sendThongBao(player, "Ngươi làm gì có đệ tử?");
                                    return;
                                }
                            }
                            break;
                            case 1629: {
                                Player player = pl;
                                if (player.pet != null) {
                                    if (player.pet.playerSkill.skills.get(2).skillId != -1) {
                                        player.pet.openSkill3();
                                    } else {
                                        Service.gI().sendThongBao(player, "Ít nhất đệ tử ngươi phải có chiêu 3 chứ!");
                                        return;
                                    }
                                } else {
                                    Service.gI().sendThongBao(player, "Ngươi làm gì có đệ tử?");
                                    return;
                                }
                            }
                            break;
                            case 1630: {
                                Player player = pl;
                                if (player.pet != null) {
                                    if (player.pet.playerSkill.skills.get(3).skillId != -1) {
                                        player.pet.openSkill4();
                                    } else {
                                        Service.gI().sendThongBao(player, "Ít nhất đệ tử ngươi phải có chiêu 4 chứ!");
                                        return;
                                    }
                                } else {
                                    Service.gI().sendThongBao(player, "Ngươi làm gì có đệ tử?");
                                    return;
                                }
                            }
                            break;
                        }
                        break;
                }
                TaskService.gI().checkDoneTaskUseItem(pl, item);
                InventoryService.gI().sendItemBag(pl);
            } else {
                Service.gI().sendThongBaoOK(pl, "Sức mạnh không đủ yêu cầu");
            }
        }
    }

    public void openRuongGo(Player pl, Item item) {
        List<String> textRuongGo = new ArrayList<>();
        int time = (int) TimeUtil.diffDate(new Date(), new Date(item.createTime), TimeUtil.DAY);
        if (time != 0) {
            Item itemReward = null;
            long param = item.itemOptions.get(0).param;
            int gold = 0;
            int[] listItem = {441, 442, 443, 444, 445, 446, 447, 220, 221, 222, 223, 224, 225};
            int[] listClothesReward;
            int[] listItemReward;
            String text = "Bạn nhận được\n";
            if (param < 8) {
                gold = (int) (100000 * param);
                listClothesReward = new int[]{randClothes((int) param)};
                listItemReward = Util.pickNRandInArr(listItem, 3);
            } else if (param < 10) {
                gold = (int) (250000 * param);
                listClothesReward = new int[]{randClothes((int) param), randClothes((int) param)};
                listItemReward = Util.pickNRandInArr(listItem, 4);
            } else {
                gold = (int) (500000 * param);
                listClothesReward = new int[]{randClothes((int) param), randClothes((int) param), randClothes((int) param)};
                listItemReward = Util.pickNRandInArr(listItem, 5);
                int ruby = Util.nextInt(1, 5);
                pl.inventory.ruby += ruby;
                textRuongGo.add(text + "|1| " + ruby + " Hồng Ngọc");
            }
            for (var i : listClothesReward) {
                itemReward = ItemService.gI().createNewItem((short) i);
                RewardService.gI().initBaseOptionClothes(itemReward.template.id, itemReward.template.type,
                        itemReward.itemOptions);
                RewardService.gI().initStarOption(itemReward, new RewardService.RatioStar[]{
                    new RewardService.RatioStar((byte) 1, 1, 2), new RewardService.RatioStar((byte) 2, 1, 3),
                    new RewardService.RatioStar((byte) 3, 1, 4), new RewardService.RatioStar((byte) 4, 1, 5),});
                InventoryService.gI().addItemBag(pl, itemReward, 999999);
                textRuongGo.add(text + itemReward.info);
            }
            for (var i : listItemReward) {
                itemReward = ItemService.gI().createNewItem((short) i);
                RewardService.gI().initBaseOptionSaoPhaLe(itemReward);
                itemReward.quantity = Util.nextInt(1, 5);
                InventoryService.gI().addItemBag(pl, itemReward, 999999);
                textRuongGo.add(text + itemReward.info);
            }
            if (param == 11) {
                itemReward = ItemService.gI().createNewItem((short) ConstItem.MANH_NHAN);
                itemReward.quantity = Util.nextInt(1, 3);
                InventoryService.gI().addItemBag(pl, itemReward, 999999);
                textRuongGo.add(text + itemReward.info);
            }
            NpcService.gI().createMenuConMeo(pl, ConstNpc.RUONG_GO, -1,
                    "Bạn nhận được\n|1|+" + Util.numberToMoney(gold) + " vàng", "OK [" + textRuongGo.size() + "]");
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            pl.inventory.addGold(gold);
            InventoryService.gI().sendItemBag(pl);
            PlayerService.gI().sendInfoHpMpMoney(pl);
        }
    }

    private void changePet(Player player, Item item) {
        if (player.pet != null) {
            int gender = player.pet.gender + 1;
            if (gender > 2) {
                gender = 0;
            }
            PetService.gI().changeNormalPet(player, gender);
            InventoryService.gI().subQuantityItemsBag(player, item, 1);
        } else {
            Service.gI().sendThongBao(player, "Không thể thực hiện");
        }
    }

    private void eatGrapes(Player pl, Item item) {
        int percentCurrentStatima = pl.nPoint.stamina * 100 / pl.nPoint.maxStamina;
        if (percentCurrentStatima > 50) {
            Service.gI().sendThongBao(pl, "Thể lực vẫn còn trên 50%");
            return;
        } else if (item.template.id == 211) {
            pl.nPoint.stamina = pl.nPoint.maxStamina;
            Service.gI().sendThongBao(pl, "Thể lực của bạn đã được hồi phục 100%");
        } else if (item.template.id == 212) {
            pl.nPoint.stamina += (pl.nPoint.maxStamina * 20 / 100);
            Service.gI().sendThongBao(pl, "Thể lực của bạn đã được hồi phục 20%");
        }
        InventoryService.gI().subQuantityItemsBag(pl, item, 1);
        InventoryService.gI().sendItemBag(pl);
        PlayerService.gI().sendCurrentStamina(pl);
    }

    private void openCSKB(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short[] temp = {76, 188, 189, 190, 381, 382, 383, 384, 385};
            int[][] gold = {{5000, 20000}};
            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            if (index <= 3) {
                pl.inventory.gold += Util.nextInt(gold[0][0], gold[0][1]);
                if (pl.inventory.gold > Inventory.LIMIT_GOLD) {
                    pl.inventory.gold = Inventory.LIMIT_GOLD;
                }// trùm
                PlayerService.gI().sendInfoHpMpMoney(pl);
                icon[1] = 930;
            } else {
                Item it = ItemService.gI().createNewItem(temp[index]);
                it.itemOptions.add(new ItemOption(73, 0));
                InventoryService.gI().addItemBag(pl, it, 999999);
                icon[1] = it.template.iconID;
            }
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBag(pl);

            CombineService.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.gI().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void useItemTime(Player pl, Item item) {
        switch (item.template.id) {
            case 1216:
                pl.itemTime.lastTimeItemTest = System.currentTimeMillis();
                pl.itemTime.isItemTest = true;
                break;
            case 382: // bổ huyết
                pl.itemTime.lastTimeBoHuyet = System.currentTimeMillis();
                pl.itemTime.isUseBoHuyet = true;
                break;
            case 383: // bổ khí
                pl.itemTime.lastTimeBoKhi = System.currentTimeMillis();
                pl.itemTime.isUseBoKhi = true;
                break;
            case 384: // giáp xên
                pl.itemTime.lastTimeGiapXen = System.currentTimeMillis();
                pl.itemTime.isUseGiapXen = true;
                break;
            case 381: // cuồng nộ
                pl.itemTime.lastTimeCuongNo = System.currentTimeMillis();
                pl.itemTime.isUseCuongNo = true;
                Service.gI().point(pl);
                break;
            case 385: // ẩn danh
                pl.itemTime.lastTimeAnDanh = System.currentTimeMillis();
                pl.itemTime.isUseAnDanh = true;
                break;
            case 379: // máy dò capsule
                pl.itemTime.lastTimeUseMayDo = System.currentTimeMillis();
                pl.itemTime.isUseMayDo = true;
                break;
                 case 1233: // máy dò capsule
                pl.itemTime.lastTimeUsebinhtnsm = System.currentTimeMillis();
                pl.itemTime.binhtnsm= true;
                break;
            case 1099:// cn
                pl.itemTime.lastTimeCuongNo2 = System.currentTimeMillis();
                pl.itemTime.isUseCuongNo2 = true;
                Service.gI().point(pl);

                break;
            case 1100:// bo huyet
                pl.itemTime.lastTimeBoHuyet2 = System.currentTimeMillis();
                pl.itemTime.isUseBoHuyet2 = true;
                break;
            case 1101:// bo khi
                pl.itemTime.lastTimeBoKhi2 = System.currentTimeMillis();
                pl.itemTime.isUseBoKhi2 = true;
                break;
            case 1102:// gx
                pl.itemTime.lastTimeGiapXen2 = System.currentTimeMillis();
                pl.itemTime.isUseGiapXen2 = true;
                break;
            case 1103:// an danh
                pl.itemTime.lastTimeAnDanh2 = System.currentTimeMillis();
                pl.itemTime.isUseAnDanh2 = true;
                break;
            case 638: // Commeson
                pl.itemTime.lastTimeUseCMS = System.currentTimeMillis();
                pl.itemTime.isUseCMS = true;
                break;
            case 2160: // Nồi cơm điện
                pl.itemTime.lastTimeUseNCD = System.currentTimeMillis();
                pl.itemTime.isUseNCD = true;
                break;
            case 579:
            case 1045: // Đuôi khỉ
                pl.itemTime.lastTimeUseDK = System.currentTimeMillis();
                pl.itemTime.isUseDK = true;
                break;
            case 663: // bánh pudding
            case 664: // xúc xíc
            case 665: // kem dâu
            case 666: // mì ly
            case 667: // sushi
                pl.itemTime.lastTimeEatMeal = System.currentTimeMillis();
                pl.itemTime.isEatMeal = true;
                ItemTimeService.gI().removeItemTime(pl, pl.itemTime.iconMeal);
                pl.itemTime.iconMeal = item.template.iconID;
                break;
            case 880:
            case 881:
            case 882:
                pl.itemTime.lastTimeEatMeal2 = System.currentTimeMillis();
                pl.itemTime.isEatMeal2 = true;
                ItemTimeService.gI().removeItemTime(pl, pl.itemTime.iconMeal2);
                pl.itemTime.iconMeal2 = item.template.iconID;
                break;
            case 1109: // máy dò đồ
                pl.itemTime.lastTimeUseMayDo2 = System.currentTimeMillis();
                pl.itemTime.isUseMayDo2 = true;
                break;
        }
        Service.gI().point(pl);
        ItemTimeService.gI().sendAllItemTime(pl);
        InventoryService.gI().subQuantityItemsBag(pl, item, 1);
        InventoryService.gI().sendItemBag(pl);
    }

    private void controllerCallRongThan(Player pl, Item item) {
        int tempId = item.template.id;
        if (tempId >= SummonDragon.NGOC_RONG_1_SAO && tempId <= SummonDragon.NGOC_RONG_7_SAO) {
            switch (tempId) {
                case SummonDragon.NGOC_RONG_1_SAO:
                case SummonDragon.NGOC_RONG_2_SAO:
                case SummonDragon.NGOC_RONG_3_SAO:
                    SummonDragon.gI().openMenuSummonShenron(pl, (byte) (tempId - 13), SummonDragon.DRAGON_SHENRON);
                    break;
                default:
                    NpcService.gI().createMenuConMeo(pl, ConstNpc.TUTORIAL_SUMMON_DRAGON, -1, "Bạn chỉ có thể gọi rồng từ ngọc 3 sao, 2 sao, 1 sao", "Hướng\ndẫn thêm\n(mới)", "OK");
                    break;
            }
//        } else if (tempId == SummonDragon.NGOC_RONG_SIEU_CAP) {
//            SummonDragon.gI().openMenuSummonShenron(pl, (byte) 1018, SummonDragon.DRAGON_BLACK_SHENRON);
        } else if (tempId >= SummonDragon.NGOC_RONG_BANG[0] && tempId <= SummonDragon.NGOC_RONG_BANG[6]) {
            switch (tempId) {
                case 925:
                    SummonDragon.gI().openMenuSummonShenron(pl, (byte) 925, SummonDragon.DRAGON_ICE_SHENRON);
                    break;
                default:
                    Service.getInstance().sendThongBao(pl, "Bạn chỉ có thể gọi rồng băng từ ngọc 1 sao");
                    break;
            }
        }
    }

    private void learnSkill(Player pl, Item item) {
        Message msg;
        try {
            if (item.template.id >= 1334 && item.template.id <= 1351) {
                learnSkillSuperNew(pl, item);
            } else {
                if (item.template.gender == pl.gender || item.template.gender == 3) {
                    String[] subName = item.template.name.split("");
                    byte level = Byte.parseByte(subName[subName.length - 1]);
                    Skill curSkill = SkillUtil.getSkillByItemID(pl, item.template.id);
                    if (curSkill.template.id >= 17 && pl.nPoint.power < 150_000_000) {
                        Service.gI().sendThongBao(pl, "Yêu cầu đạt 150tr sức mạnh để học " + curSkill.template.name);
                        return;
                    }
                    if (curSkill.point == 7) {
                        Service.gI().sendThongBao(pl, "Kỹ năng đã đạt tối đa!");
                    } else {
                        if (curSkill.point == 0) {
                            if (level == 1) {
                                curSkill = SkillUtil.createSkill(SkillUtil.getTempSkillSkillByItemID(item.template.id),
                                        level);
                                SkillUtil.setSkill(pl, curSkill);
                                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                                msg = Service.gI().messageSubCommand((byte) 23);
                                msg.writer().writeShort(curSkill.skillId);
                                pl.sendMessage(msg);
                                msg.cleanup();
                            } else {
                                Skill skillNeed = SkillUtil
                                        .createSkill(SkillUtil.getTempSkillSkillByItemID(item.template.id), level);
                                Service.gI().sendThongBao(pl, "Vui lòng học " + skillNeed.template.name + " cấp "
                                        + skillNeed.point + " trước!");
                            }
                        } else {
                            if (curSkill.point + 1 == level) {
                                curSkill = SkillUtil.createSkill(SkillUtil.getTempSkillSkillByItemID(item.template.id),
                                        level);
                                // System.out.println(curSkill.template.name + " - " + curSkill.point);
                                SkillUtil.setSkill(pl, curSkill);
                                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                                msg = Service.gI().messageSubCommand((byte) 62);
                                msg.writer().writeShort(curSkill.skillId);
                                pl.sendMessage(msg);
                                msg.cleanup();
                            } else {
                                Service.gI().sendThongBao(pl, "Vui lòng học " + curSkill.template.name + " cấp "
                                        + (curSkill.point + 1) + " trước!");
                            }
                        }
                        InventoryService.gI().sendItemBag(pl);
                    }
                } else {
                    Service.gI().sendThongBao(pl, "Không thể thực hiện");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
//    private void learnSkillNew2(Player pl, Item item) {
//        Message msg;
//        try {
//            if (item.template.gender == pl.gender || item.template.gender == 3) {
//                byte level = SkillUtil.getLevelSkillByItemID(item.template.id);
//                Skill curSkill = SkillUtil.getSkillByItemID(pl, item.template.id);
//                if (curSkill == null) {
//                    SkillService.gI().learSkillSpecial(pl,
//                            (byte) SkillUtil.getSkillByItemID(pl, item.template.id).skillId);
//                    InventoryService.gI().subQuantityItemsBag(pl, item, 1);
//                    return;
//                } else {
//                    if (curSkill.point == 7) {
//                        Service.gI().sendThongBao(pl, "Kỹ năng đã đạt tối đa!");
//                    } else {
//                        if (curSkill.point == 0) {
//                            if (level == 1) {
//                                curSkill = SkillUtil.createSkill(SkillUtil.getTempSkillSkillByItemID(item.template.id),
//                                        level);
//                                SkillUtil.setSkill(pl, curSkill);
//                                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
//                                msg = Service.gI().messageSubCommand((byte) 23);
//                                msg.writer().writeShort(curSkill.skillId);
//                                pl.sendMessage(msg);
//                                msg.cleanup();
//                                if (curSkill.template.id == Skill.SUPER_NAMEC
//                                        || curSkill.template.id == Skill.SUPER_SAIYAN
//                                        || curSkill.template.id == Skill.SUPER_TRAI_DAT) {
//                                    curSkill = SkillUtil.createSkill(Skill.GONG, level);
//                                    SkillUtil.setSkill(pl, curSkill);
//                                    InventoryService.gI().subQuantityItemsBag(pl, item, 1);
//                                    msg = Service.gI().messageSubCommand((byte) 23);
//                                    msg.writer().writeShort(curSkill.skillId);
//                                    pl.sendMessage(msg);
//                                    msg.cleanup();
//                                }
//                            } else {
//                                Skill skillNeed = SkillUtil
//                                        .createSkill(SkillUtil.getTempSkillSkillByItemID(item.template.id), level);
//                                Service.gI().sendThongBao(pl, "Vui lòng học " + skillNeed.template.name + " cấp "
//                                        + skillNeed.point + " trước!");
//                            }
//                        } else {
//                            if (curSkill.point + 1 == level) {
//                                curSkill = SkillUtil.createSkill(SkillUtil.getTempSkillSkillByItemID(item.template.id),
//                                        level);
//                                // System.out.println(curSkill.template.name + " - " + curSkill.point);
//                                SkillUtil.setSkill(pl, curSkill);
//                                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
//                                msg = Service.gI().messageSubCommand((byte) 62);
//                                msg.writer().writeShort(curSkill.skillId);
//                                pl.sendMessage(msg);
//                                msg.cleanup();
//                            } else {
//                                Service.gI().sendThongBao(pl, "Vui lòng học " + curSkill.template.name + " cấp "
//                                        + (curSkill.point + 1) + " trước!");
//                            }
//                        }
//                        InventoryService.gI().sendItemBag(pl);
//                    }
//                }
//            } else {
//                Service.gI().sendThongBao(pl, "Không thể thực hiện");
//            }
//        } catch (Exception e) {
//
//        }
//    }
    private void learnSkillSuperNew(Player pl, Item item) {
        Message msg;
        try {
            if (item.template.gender == pl.gender || item.template.gender == 3) {
                byte level = SkillUtil.getLevelSkillByItemID(item.template.id);
                Skill curSkill = SkillUtil.getSkillByItemID(pl, item.template.id);
                if (curSkill.point == 6) {
                    Service.gI().sendThongBao(pl, "Kỹ năng đã đạt tối đa!");
                } else {
                    if (curSkill.point == 0) {
                        if (level == 1) {
                            curSkill = SkillUtil.createSkill(SkillUtil.getTempSkillSkillByItemID(item.template.id),
                                    level);
                            SkillUtil.setSkill(pl, curSkill);
                            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                            msg = Service.gI().messageSubCommand((byte) 23);
                            msg.writer().writeShort(curSkill.skillId);
                            pl.sendMessage(msg);
                            msg.cleanup();
                            SkillService.gI().learSkillSpecial(pl, (byte) 30);
                        } else {
                            Skill skillNeed = SkillUtil
                                    .createSkill(SkillUtil.getTempSkillSkillByItemID(item.template.id), level);
                            if (level > 1) {
                                Item itemNew = ItemService.gI().createNewItem((short) (item.template.id - 1));
                                String name = itemNew.template.name;
                                String desiredName = name.substring(5);
                                Service.gI().sendThongBao(pl, "Vui lòng học " + desiredName + " trước!");
                            } else {
                                Service.gI().sendThongBao(pl, "Vui lòng học " + skillNeed.template.name + " trước!");
                            }
                        }
                    } else {
                        if (curSkill.point + 1 == level) {
                            curSkill = SkillUtil.createSkill(SkillUtil.getTempSkillSkillByItemID(item.template.id),
                                    level);
                            SkillUtil.setSkill(pl, curSkill);
                            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                            msg = Service.gI().messageSubCommand((byte) 62);
                            msg.writer().writeShort(curSkill.skillId);
                            pl.sendMessage(msg);
                            msg.cleanup();
                        } else {
                            if (level > 1) {
                                Item itemNew = ItemService.gI().createNewItem((short) (item.template.id - 1));
                                String name = itemNew.template.name;
                                String desiredName = name.substring(5);
                                Service.gI().sendThongBao(pl, "Vui lòng học " + desiredName + " trước!");
                            } else {
                                Service.gI().sendThongBao(pl, "Vui lòng học " + curSkill.template.name + " trước!");
                            }
                        }
                    }
                    InventoryService.gI().sendItemBag(pl);
                }
            } else {
                Service.gI().sendThongBao(pl, "Không thể thực hiện");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void changexencon(Player player, Item item) {
        if (player.pet != null) {
            int gender = player.pet.gender;
            PetService.gI().changeXencon(player, gender);
            InventoryService.gI().subQuantityItemsBag(player, item, 1);
        } else {
            Service.getInstance().sendThongBao(player, "Không thể thực hiện");
        }
    }

    private void changebroly(Player player, Item item) {
        if (player.pet != null) {
            int gender = player.pet.gender;
            PetService.gI().changeBroly(player, gender);
            InventoryService.gI().subQuantityItemsBag(player, item, 1);
        } else {
            Service.getInstance().sendThongBao(player, "Không thể thực hiện");
        }
    }

    private void changeBerus(Player player, Item item) {
        if (player.pet != null) {
            int gender = player.pet.gender;
            PetService.gI().changeBeerusPet(player, gender);
            InventoryService.gI().subQuantityItemsBag(player, item, 1);
        } else {
            Service.getInstance().sendThongBao(player, "Không thể thực hiện");
        }
    }

    private void changeblack(Player player, Item item) {
        if (player.pet != null) {
            int gender = player.pet.gender;
            PetService.gI().changeblackgoku(player, gender);
            InventoryService.gI().subQuantityItemsBag(player, item, 1);
        } else {
            Service.getInstance().sendThongBao(player, "Không thể thực hiện");
        }
    }

    private void changeblackrose(Player player, Item item) {
        if (player.pet != null) {
            int gender = player.pet.gender;
            PetService.gI().changeblackgokurose(player, gender);
            InventoryService.gI().subQuantityItemsBag(player, item, 1);
        } else {
            Service.getInstance().sendThongBao(player, "Không thể thực hiện");
        }
    }

    private void changezamasu(Player player, Item item) {
        if (player.pet != null) {
            int gender = player.pet.gender;
            PetService.gI().changezamasu(player, gender);
            InventoryService.gI().subQuantityItemsBag(player, item, 1);
        } else {
            Service.getInstance().sendThongBao(player, "Không thể thực hiện");
        }
    }

    private void changecumber(Player player, Item item) {
        if (player.pet != null) {
            int gender = player.pet.gender;
            PetService.gI().changecumber(player, gender);
            InventoryService.gI().subQuantityItemsBag(player, item, 1);
        } else {
            Service.getInstance().sendThongBao(player, "Không thể thực hiện");
        }
    }

    private void useTDLT(Player pl, Item item) {
        if (pl.itemTime.isUseTDLT) {
            ItemTimeService.gI().turnOffTDLT(pl, item);
        } else {
            ItemTimeService.gI().turnOnTDLT(pl, item);
        }
    }

    private void usePorata2(Player pl) {
        if (pl.pet == null || pl.fusion.typeFusion == 4) {
            Service.gI().sendThongBao(pl, "Không thể thực hiện");
        } else {
            if (pl.fusion.typeFusion == ConstPlayer.NON_FUSION) {
                pl.pet.hopthebongtai(true, ConstPlayer.HOP_THE_PORATA2);
            } else {
                pl.pet.unFusion();
            }
        }
    }

    private void usePorata3(Player pl) {
        if (pl.pet == null || pl.fusion.typeFusion == 4) {
            Service.gI().sendThongBao(pl, "Không thể thực hiện");
        } else {
            if (pl.fusion.typeFusion == ConstPlayer.NON_FUSION) {
                pl.pet.hopthebongtai(true, ConstPlayer.HOP_THE_PORATA3);
            } else {
                pl.pet.unFusion();
            }
        }
    }

    private void usePorata4(Player pl) {
        if (pl.pet == null || pl.fusion.typeFusion == 4) {
            Service.gI().sendThongBao(pl, "Không thể thực hiện");
        } else {
            if (pl.fusion.typeFusion == ConstPlayer.NON_FUSION) {
                pl.pet.hopthebongtai(true, ConstPlayer.HOP_THE_PORATA4);

            } else {
                pl.pet.unFusion();
            }
        }
    }

    private void hopquaramdomcaitrangtho(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            Item it = ItemService.gI().createNewItem((short) 464, 1);
            it.itemOptions.add(new ItemOption(50, 100));
            it.itemOptions.add(new ItemOption(77, 100));
            it.itemOptions.add(new ItemOption(103, 100));
            it.itemOptions.add(new ItemOption(117, 30));
            it.itemOptions.add(new ItemOption(72, 5));
            it.itemOptions.add(new ItemOption(210, 5));
            it.itemOptions.add(new ItemOption(30, 1));
            if (Util.isTrue(99, 100)) {
                it.itemOptions.add(new ItemOption(93, Util.nextInt(5, 10)));
            }
            InventoryService.gI().addItemBag(pl, it, 999999);
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBag(pl);
        } else {
            Service.getInstance().sendThongBao(pl, "Hãy chừa 1 ô trống để mở.");
        }

    }

    private void hopqualinhthu(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            Item it = ItemService.gI().createNewItem((short) Util.nextInt(1351, 1367), 1);
            it.itemOptions.add(new ItemOption(50, Util.nextInt(50, 100)));
            it.itemOptions.add(new ItemOption(77, Util.nextInt(50, 100)));
            it.itemOptions.add(new ItemOption(103, Util.nextInt(50, 100)));
            it.itemOptions.add(new ItemOption(78, Util.nextInt(1, 20)));

            it.itemOptions.add(new ItemOption(30, 1));
            if (Util.isTrue(99, 100)) {
                it.itemOptions.add(new ItemOption(93, Util.nextInt(5, 10)));
            }
            InventoryService.gI().addItemBag(pl, it, 999999);
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBag(pl);
        } else {
            Service.getInstance().sendThongBao(pl, "Hãy chừa 1 ô trống để mở.");
        }

    }

    private void hopquacaocap(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            Item it = ItemService.gI().createNewItem((short) Util.nextInt(1351, 1367), 1);
            it.itemOptions.add(new ItemOption(50, Util.nextInt(50, 85)));
            it.itemOptions.add(new ItemOption(77, Util.nextInt(50, 85)));
            it.itemOptions.add(new ItemOption(103, Util.nextInt(50, 85)));
            it.itemOptions.add(new ItemOption(78, Util.nextInt(1, 20)));
            it.itemOptions.add(new ItemOption(30, 1));
            if (Util.isTrue(99, 100)) {
                it.itemOptions.add(new ItemOption(93, Util.nextInt(5, 10)));
            }
            InventoryService.gI().addItemBag(pl, it, 999999);
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBag(pl);
        } else {
            Service.getInstance().sendThongBao(pl, "Hãy chừa 1 ô trống để mở.");
        }

    }

    private void hopquaphukien(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            Item it = ItemService.gI().createNewItem((short) Util.nextInt(814, 817), 1);
            it.itemOptions.add(new ItemOption(50, Util.nextInt(50, 100)));
            it.itemOptions.add(new ItemOption(77, Util.nextInt(50, 100)));
            it.itemOptions.add(new ItemOption(103, Util.nextInt(50, 100)));
            it.itemOptions.add(new ItemOption(14, Util.nextInt(5, 20)));
            it.itemOptions.add(new ItemOption(30, 1));
            if (Util.isTrue(99, 100)) {
                it.itemOptions.add(new ItemOption(93, Util.nextInt(10, 100)));
            }
            InventoryService.gI().addItemBag(pl, it, 999999);
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBag(pl);
        } else {
            Service.getInstance().sendThongBao(pl, "Hãy chừa 1 ô trống để mở.");
        }

    }

    public void ghepmanhtrung(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            Item trungThuong = ItemService.gI().createNewItem((short) 1158, 1);
            Item trungVip = ItemService.gI().createNewItem((short) 1159, 1);
            trungThuong.itemOptions.add(new ItemOption(244, 1));
            trungVip.itemOptions.add(new ItemOption(244, 1));
            if (Util.isTrue(98, 100)) {
                InventoryService.gI().addItemBag(pl, trungThuong, 999999);

            } else {
                InventoryService.gI().addItemBag(pl, trungVip, 999999);
            }
            InventoryService.gI().subQuantityItemsBag(pl, item, 999);
            InventoryService.gI().sendItemBag(pl);
        } else {
            Service.getInstance().sendThongBao(pl, "Hãy chừa 1 ô trống để mở.");
        }

    }

    private void goidau9(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            Item it = ItemService.gI().createNewItem((short) 523, 100);
            InventoryService.gI().addItemBag(pl, it, 99999999);
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBag(pl);
        } else {
            Service.getInstance().sendThongBao(pl, "Hãy chừa 1 ô trống để mở.");
        }

    }

    private void OpenLinhThu(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            int[][] gokuday = {
                {1351, 1352, 1353, 1354, 1355, 1356, 1357, 1358, 1359}, // Goku Thường
                {1361, 1360, 1362, 1363, 1364}, // Goku Xịn
                {1365, 1366, 1367} // Goku Vip
            };

            int[][] OPTION = {
                {0, 6, 7, 5, 27, 28}, {500, 5000, 5000, 55, 5000, 5000}, // Goku Thường
                {0, 6, 7, 5, 27, 28}, {800, 8000, 8000, 90, 8000, 8000}, // Goku Xịn
                {0, 6, 7, 5, 27, 28, 163, 181, 161, 190}, {1200, 12000, 12000, 150, 12000, 12000, 50, 20, 50, 20} // Goku Vip
            };

            short id = 0;
            Random random = new Random();
            int rand = random.nextInt(100);

            // Tỷ lệ drop cố định (không phụ thuộc VIP)
            int normalRate = 80;
            int premiumRate = 15;
            int vipRate = 5;

            int premiumThreshold = normalRate + premiumRate;

            short[] icon = new short[2];
            icon[0] = item.template.iconID;

            if (rand < normalRate) {
                // Goku Thường
                id = (short) gokuday[0][Util.nextInt(0, gokuday[0].length - 1)];
                int x = Util.nextInt(0, OPTION[0].length - 1);

                Item it = ItemService.gI().createNewItem(id);
                it.itemOptions.add(new ItemOption(OPTION[0][x], Util.nextInt(1, OPTION[1][x])));
                it.itemOptions.add(new ItemOption(50, Util.nextInt(45, 60)));
                it.itemOptions.add(new ItemOption(77, Util.nextInt(45, 60)));
                it.itemOptions.add(new ItemOption(103, Util.nextInt(45, 60)));
                it.itemOptions.add(new ItemOption(72, 3));
                InventoryService.gI().addItemBag(pl, it, 1.0);
                icon[1] = it.template.iconID;

            } else if (rand < premiumThreshold) {
                // Goku Xịn
                id = (short) gokuday[1][Util.nextInt(0, gokuday[1].length - 1)];
                int x = Util.nextInt(0, OPTION[2].length - 1);
                int y;
                do {
                    y = Util.nextInt(0, OPTION[2].length - 1);
                } while (y == x);

                Item it = ItemService.gI().createNewItem(id);
                it.itemOptions.add(new ItemOption(OPTION[2][x], Util.nextInt(1, OPTION[3][x])));
                it.itemOptions.add(new ItemOption(OPTION[2][y], Util.nextInt(1, OPTION[3][y])));
                it.itemOptions.add(new ItemOption(50, Util.nextInt(60, 80)));
                it.itemOptions.add(new ItemOption(77, Util.nextInt(60, 80)));
                it.itemOptions.add(new ItemOption(103, Util.nextInt(60, 80)));
                it.itemOptions.add(new ItemOption(5, Util.nextInt(5, 10)));
                it.itemOptions.add(new ItemOption(72, 4));
                InventoryService.gI().addItemBag(pl, it, 1.0);
                icon[1] = it.template.iconID;

            } else {
                // Goku Vip
                id = (short) gokuday[2][Util.nextInt(0, gokuday[2].length - 1)];
                int x = Util.nextInt(0, OPTION[4].length - 1);
                int y, z;
                do {
                    y = Util.nextInt(0, OPTION[4].length - 1);
                } while (y == x);
                do {
                    z = Util.nextInt(0, OPTION[4].length - 1);
                } while (z == x || z == y);

                Item it = ItemService.gI().createNewItem(id);
                it.itemOptions.add(new ItemOption(OPTION[4][x], Util.nextInt(1, OPTION[5][x])));
                it.itemOptions.add(new ItemOption(OPTION[4][y], Util.nextInt(1, OPTION[5][y])));
                it.itemOptions.add(new ItemOption(OPTION[4][z], Util.nextInt(1, OPTION[5][z])));
                it.itemOptions.add(new ItemOption(50, Util.nextInt(80, 110)));
                it.itemOptions.add(new ItemOption(77, Util.nextInt(80, 110)));
                it.itemOptions.add(new ItemOption(103, Util.nextInt(80, 110)));
                it.itemOptions.add(new ItemOption(5, Util.nextInt(80, 110)));
                it.itemOptions.add(new ItemOption(72, 5));
                InventoryService.gI().addItemBag(pl, it, 1.0);
                icon[1] = it.template.iconID;
            }

            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBag(pl);
            CombineService.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.gI().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void OpenLinhThuVip(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            int[][] gokuday = {
                {1351, 1352, 1353, 1354, 1355, 1356, 1357, 1358, 1359}, // Goku Thường
                {1361, 1360, 1362, 1363, 1364}, // Goku Xịn
                {1365, 1366, 1367} // Goku Vip
            };

            int[][] OPTION = {
                {0, 6, 7, 5, 27, 28}, {500, 5000, 5000, 60, 5000, 5000}, // Goku Thường
                {0, 6, 7, 5, 27, 28}, {800, 8000, 8000, 80, 8000, 8000}, // Goku Xịn
                {0, 6, 7, 5, 27, 28, 163, 181, 161, 190}, {1200, 12000, 12000, 100, 12000, 12000, 50, 20, 50, 20} // Goku Vip// tự thay các option khác vào đây biết chưa? 
            };

            short id = 0;
            Random random = new Random();
            int rand = random.nextInt(100);

            // Tỷ lệ drop cố định (không phụ thuộc VIP)
            int normalRate = 75;
            int premiumRate = 15;
            int vipRate = 10;

            int premiumThreshold = normalRate + premiumRate;

            short[] icon = new short[2];
            icon[0] = item.template.iconID;

            if (rand < normalRate) {
                // Goku Thường
                id = (short) gokuday[0][Util.nextInt(0, gokuday[0].length - 1)];
                int x = Util.nextInt(0, OPTION[0].length - 1);

                Item it = ItemService.gI().createNewItem(id);
                it.itemOptions.add(new ItemOption(OPTION[0][x], Util.nextInt(1, OPTION[1][x])));
                it.itemOptions.add(new ItemOption(50, Util.nextInt(40, 80)));
                it.itemOptions.add(new ItemOption(77, Util.nextInt(40, 80)));
                it.itemOptions.add(new ItemOption(103, Util.nextInt(40, 80)));
                it.itemOptions.add(new ItemOption(72, 3));
                InventoryService.gI().addItemBag(pl, it, 1.0);
                icon[1] = it.template.iconID;

            } else if (rand < premiumThreshold) {
                // Goku Xịn
                id = (short) gokuday[1][Util.nextInt(0, gokuday[1].length - 1)];
                int x = Util.nextInt(0, OPTION[2].length - 1);
                int y;
                do {
                    y = Util.nextInt(0, OPTION[2].length - 1);
                } while (y == x);

                Item it = ItemService.gI().createNewItem(id);
                it.itemOptions.add(new ItemOption(OPTION[2][x], Util.nextInt(1, OPTION[3][x])));
                it.itemOptions.add(new ItemOption(OPTION[2][y], Util.nextInt(1, OPTION[3][y])));
                it.itemOptions.add(new ItemOption(50, Util.nextInt(50, 111)));
                it.itemOptions.add(new ItemOption(77, Util.nextInt(50, 111)));
                it.itemOptions.add(new ItemOption(103, Util.nextInt(50, 111)));
                it.itemOptions.add(new ItemOption(5, Util.nextInt(50, 111)));
                it.itemOptions.add(new ItemOption(72, 4));
                InventoryService.gI().addItemBag(pl, it, 1.0);
                icon[1] = it.template.iconID;

            } else {
                // Goku Vip
                id = (short) gokuday[2][Util.nextInt(0, gokuday[2].length - 1)];
                int x = Util.nextInt(0, OPTION[4].length - 1);
                int y, z;
                do {
                    y = Util.nextInt(0, OPTION[4].length - 1);
                } while (y == x);
                do {
                    z = Util.nextInt(0, OPTION[4].length - 1);
                } while (z == x || z == y);

                Item it = ItemService.gI().createNewItem(id);
                it.itemOptions.add(new ItemOption(OPTION[4][x], Util.nextInt(1, OPTION[5][x])));
                it.itemOptions.add(new ItemOption(OPTION[4][y], Util.nextInt(1, OPTION[5][y])));
                it.itemOptions.add(new ItemOption(OPTION[4][z], Util.nextInt(1, OPTION[5][z])));
                it.itemOptions.add(new ItemOption(50, Util.nextInt(50, 100)));
                it.itemOptions.add(new ItemOption(77, Util.nextInt(50, 100)));
                it.itemOptions.add(new ItemOption(103, Util.nextInt(50, 100)));
                it.itemOptions.add(new ItemOption(5, Util.nextInt(50, 100)));
                it.itemOptions.add(new ItemOption(72, 5));
                InventoryService.gI().addItemBag(pl, it, 1.0);
                icon[1] = it.template.iconID;
            }
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBag(pl);
            CombineService.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.gI().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void hopquaramdomthoivang(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            Item it = ItemService.gI().createNewItem((short) 457, Util.nextInt(1, 200));
            if (it.template != null) {
                it.itemOptions.add(new ItemOption(30, 1));
                InventoryService.gI().addItemBag(pl, it, 999999);
                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                InventoryService.gI().sendItemBag(pl);
                short icon1 = item.template != null ? item.template.iconID : -1;
                short icon2 = it.template.iconID;
                CombineService.gI().sendEffectOpenItem(pl, icon1, icon2);
            } else {
                Service.getInstance().sendThongBao(pl, "Item template is missing!");
            }
        } else {
            Service.getInstance().sendThongBao(pl, "Hãy chừa 1 ô trống để mở.");
        }
    }

    private void hopquanoitai(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            Item it = ItemService.gI().createNewItem((short) 1825, Util.nextInt(1, 10000));
            if (it.template != null) {
                it.itemOptions.add(new ItemOption(30, 1));
                InventoryService.gI().addItemBag(pl, it, 99999999);
                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                InventoryService.gI().sendItemBag(pl);
                short icon1 = item.template != null ? item.template.iconID : -1;
                short icon2 = it.template.iconID;
                CombineService.gI().sendEffectOpenItem(pl, icon1, icon2);
            } else {
                Service.getInstance().sendThongBao(pl, "Item template is missing!");
            }
        } else {
            Service.getInstance().sendThongBao(pl, "Hãy chừa 1 ô trống để mở.");
        }
    }

    private void ramdomitem(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short[] temp = {663, 664, 665, 666, 667, 457};
            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
            int Soluong = Util.nextInt(1, 9);
            int Soluongmacdinh = 500;
            Item it = ItemService.gI().createNewItem(temp[index], Soluong);
            //
            if (it.template.type == 27) { // cải trang
                //  it.itemOptions.add(new ItemOption(30, Util.nextInt(1, 2025)));
            }
            if (it.template.type == 29) { // cải trang
                //  it.itemOptions.add(new ItemOption(30, Util.nextInt(1, 2025)));
            }
            InventoryService.gI().addItemBag(pl, it, 999999999);
            icon[1] = it.template.iconID;
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            //   pl.point_moruong += 1;
            InventoryService.gI().sendItemBag(pl);
            CombineService.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");

        }
    }

    private void hopquadangusac(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            Item it = ItemService.gI().createNewItem((short) 674, Util.nextInt(1, 500));
            if (it.template != null) {
                it.itemOptions.add(new ItemOption(30, 1));
                InventoryService.gI().addItemBag(pl, it, 99999999);
                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                InventoryService.gI().sendItemBag(pl);
                short icon1 = item.template != null ? item.template.iconID : -1;
                short icon2 = it.template.iconID;
                CombineService.gI().sendEffectOpenItem(pl, icon1, icon2);
            } else {
                Service.getInstance().sendThongBao(pl, "Item template is missing!");
            }
        } else {
            Service.getInstance().sendThongBao(pl, "Hãy chừa 1 ô trống để mở.");
        }
    }

    private void hopquadanangcap(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            Item it = ItemService.gI().createNewItem((short) Util.nextInt(220, 224), Util.nextInt(1, 500));
            if (it.template != null) {
                it.itemOptions.add(new ItemOption(30, 1));
                InventoryService.gI().addItemBag(pl, it, 99999999);
                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                InventoryService.gI().sendItemBag(pl);
                short icon1 = item.template != null ? item.template.iconID : -1;
                short icon2 = it.template.iconID;
                CombineService.gI().sendEffectOpenItem(pl, icon1, icon2);
            } else {
                Service.getInstance().sendThongBao(pl, "Item template is missing!");
            }
        } else {
            Service.getInstance().sendThongBao(pl, "Hãy chừa 1 ô trống để mở.");
        }
    }

    private void hopquadabaove(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            Item it = ItemService.gI().createNewItem((short) 987, Util.nextInt(1, 5));
            if (it.template != null) {
                it.itemOptions.add(new ItemOption(30, 1));
                InventoryService.gI().addItemBag(pl, it, 99999999);
                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                InventoryService.gI().sendItemBag(pl);
                short icon1 = item.template != null ? item.template.iconID : -1;
                short icon2 = it.template.iconID;
                CombineService.gI().sendEffectOpenItem(pl, icon1, icon2);
            } else {
                Service.getInstance().sendThongBao(pl, "Item template is missing!");
            }
        } else {
            Service.getInstance().sendThongBao(pl, "Hãy chừa 1 ô trống để mở.");
        }
    }

    public void nhancaitrangupngoc(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            Item it = ItemService.gI().createNewItem((short) 1834, 1);
            if (it.template != null) {
                it.itemOptions.add(new ItemOption(239, 1));
                it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 3)));
                InventoryService.gI().addItemBag(pl, it, 999999);
                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                InventoryService.gI().sendItemBag(pl);
                short icon1 = item.template != null ? item.template.iconID : -1;
                short icon2 = it.template.iconID;
                CombineService.gI().sendEffectOpenItem(pl, icon1, icon2);
            } else {
                Service.getInstance().sendThongBao(pl, "Item template is missing!");
            }
        } else {
            Service.getInstance().sendThongBao(pl, "Hãy chừa 1 ô trống để mở.");
        }
    }

    private void hopquaramdomngoc(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short[] temp = {77, 861};
            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item it = ItemService.gI().createNewItem(temp[index], 5000);

            InventoryService.gI().addItemBag(pl, it, 0);
            icon[1] = it.template.iconID;
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBag(pl);
            CombineService.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");

        }
    }

    public void useThoiVang(Player pl, Item it) {
        if (pl.inventory.gold > Inventory.LIMIT_GOLD) {
            Service.gI().sendThongBao(pl, "Vàng sau khi sử dụng vượt quá giới hạn!");
            return;
        }
        pl.inventory.gold += 100000000;
        Service.gI().sendThongBao(pl, "Bạn nhận được 100M vàng");
        InventoryService.gI().subQuantityItemsBag(pl, it, 1);
        InventoryService.gI().sendItemBag(pl);
        Service.gI().sendMoney(pl);
    }

    private void usePorata(Player pl) {
        if (pl.pet == null || pl.fusion.typeFusion == 4) {
            Service.gI().sendThongBao(pl, "Không thể thực hiện");
        } else {
            if (pl.fusion.typeFusion == ConstPlayer.NON_FUSION) {
                pl.pet.fusion(true);
            } else {
                pl.pet.unFusion();
            }
        }
    }

    private void openCapsuleUI(Player pl) {
        pl.iDMark.setTypeChangeMap(ConstMap.CHANGE_CAPSULE);
        ChangeMapService.gI().openChangeMapTab(pl);
    }

    public void choseMapCapsule(Player pl, int index) {

        if (pl.idNRNM != -1) {
            Service.gI().sendThongBao(pl, "Không thể mang ngọc rồng này lên Phi thuyền");
            Service.gI().hideWaitDialog(pl);
            return;
        }

        int zoneId = -1;
        if (index > pl.mapCapsule.size() - 1 || index < 0) {
            Service.gI().sendThongBao(pl, "Không thể thực hiện");
            Service.gI().hideWaitDialog(pl);
            return;
        }
        Zone zoneChose = pl.mapCapsule.get(index);
        // Kiểm tra số lượng người trong khu

        if (zoneChose.getNumOfPlayers() > 25
                || MapService.gI().isMapDoanhTrai(zoneChose.map.mapId)
                || MapService.gI().isMapMaBu(zoneChose.map.mapId)
                || MapService.gI().isMapHuyDiet(zoneChose.map.mapId)) {
            Service.gI().sendThongBao(pl, "Hiện tại không thể vào được khu!");
            return;
        }
        if (index != 0 || zoneChose.map.mapId == 21
                || zoneChose.map.mapId == 22
                || zoneChose.map.mapId == 23) {
            pl.mapBeforeCapsule = pl.zone;
        } else {
            zoneId = pl.mapBeforeCapsule != null ? pl.mapBeforeCapsule.zoneId : -1;
            pl.mapBeforeCapsule = null;
        }
        pl.changeMapVIP = true;
        ChangeMapService.gI().changeMapBySpaceShip(pl, pl.mapCapsule.get(index).map.mapId, zoneId, -1);
    }

    public void eatPea(Player player) {
        if (!Util.canDoWithTime(player.lastTimeEatPea, 1000)) {
            return;
        }
        player.lastTimeEatPea = System.currentTimeMillis();
        Item pea = null;
        for (Item item : player.inventory.itemsBag) {
            if (item.isNotNullItem() && item.template.type == 6) {
                pea = item;
                break;
            }
        }
        if (pea != null) {
            long hpKiHoiPhuc = 0;
            int lvPea = Integer.parseInt(pea.template.name.substring(13));
            for (Item.ItemOption io : pea.itemOptions) {
                if (io.optionTemplate.id == 2) {
                    hpKiHoiPhuc = io.param * 1000;
                    break;
                }
                if (io.optionTemplate.id == 48) {
                    hpKiHoiPhuc = io.param;
                    break;
                }
            }
            player.nPoint.setHp(player.nPoint.hp + hpKiHoiPhuc);
            player.nPoint.setMp(player.nPoint.mp + hpKiHoiPhuc);
            PlayerService.gI().sendInfoHpMp(player);
            Service.gI().sendInfoPlayerEatPea(player);
            if (player.pet != null && player.zone.equals(player.pet.zone) && !player.pet.isDie()) {
                int statima = 100 * lvPea;
                player.pet.nPoint.stamina += statima;
                if (player.pet.nPoint.stamina > player.pet.nPoint.maxStamina) {
                    player.pet.nPoint.stamina = player.pet.nPoint.maxStamina;
                }
                player.pet.nPoint.setHp(player.pet.nPoint.hp + hpKiHoiPhuc);
                player.pet.nPoint.setMp(player.pet.nPoint.mp + hpKiHoiPhuc);
                Service.gI().sendInfoPlayerEatPea(player.pet);
                Service.gI().chatJustForMe(player, player.pet, "Cám ơn sư phụ");
            }

            InventoryService.gI().subQuantityItemsBag(player, pea, 1);
            InventoryService.gI().sendItemBag(player);
        }
    }

    private void setkichhoat(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short[] temp = {230, 231, 232, 233, 234, 235, 236, 237, 238, 239, 240, 241, 242, 243, 244, 245, 246, 247, 248, 249, 250, 251, 252, 253, 254, 255, 256, 257, 258, 259, 260, 261, 262, 263, 264, 265, 266, 267, 268, 269, 270, 271, 272, 273, 274, 275, 276, 277, 278, 279, 280, 281};
            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item it = ItemService.gI().createNewItem(temp[index]);

            // Thêm chỉ số dựa trên loại item
            switch (it.template.type) {
                case 0: // Áo
                    it.itemOptions.add(new ItemOption(47, Util.nextInt(5, 50000))); // Chỉ số ngẫu nhiên

                    break;
                case 1: // Quần
                    it.itemOptions.add(new ItemOption(6, Util.nextInt(5, 150000)));

                    break;

                case 2: // Găng
                    it.itemOptions.add(new ItemOption(0, Util.nextInt(10, 75000)));

                    break;
                case 3: // Giày
                    it.itemOptions.add(new ItemOption(7, Util.nextInt(20, 1500000)));

                    break;
                case 4: // Rada
                    it.itemOptions.add(new ItemOption(14, Util.nextInt(1, 30)));

                    break;
            }
            if (Util.isTrue(100, 100)) {
                if (it.template.gender == 0) {
                    it.itemOptions.add(new ItemOption(Util.nextInt(127, 129), 1));
                    it.itemOptions.add(new ItemOption(30, 1)); // Chỉ số đặc biệt 
                }
                if (it.template.gender == 1) {
                    it.itemOptions.add(new ItemOption(Util.nextInt(130, 132), 1)); // Chỉ số đặc biệt
                    it.itemOptions.add(new ItemOption(30, 1)); // Chỉ số đặc biệt 
                }
                if (it.template.gender == 2) {
                    it.itemOptions.add(new ItemOption(Util.nextInt(133, 135), 1)); // Chỉ số đặc biệt
                    it.itemOptions.add(new ItemOption(30, 1)); // Chỉ số đặc biệt 

                }
                if (it.template.gender == 3) {
                    it.itemOptions.add(new ItemOption(Util.nextInt(127, 135), 1)); // Chỉ số đặc biệt
                    it.itemOptions.add(new ItemOption(30, 1)); // Chỉ số đặc biệt 

                }
//                it.itemOptions.add(new ItemOption(233,100));s

            }
            // Thêm vào túi đồ
            InventoryService.gI().addItemBag(pl, it, 0);
            icon[1] = it.template.iconID;
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBag(pl);
            CombineService.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void mobitcoin(Player player, Item item) {
        if (player != null && item != null) {
            int randomRate = Util.nextInt(1, 100);
            int rewardVND;
            if (Util.isTrue(99, 100)) { // tỉ lệ
                rewardVND = Util.nextInt(5000, 15000); // ramdom tiền
            } else if (Util.isTrue(10, 100)) {
                rewardVND = Util.nextInt(15000, 30000);
            } else if (Util.isTrue(5, 100)) {
                rewardVND = Util.nextInt(30000, 70000);
            } else if (Util.isTrue(1, 100)) {
                rewardVND = Util.nextInt(70000, 120000);
            } else {
                rewardVND = Util.nextInt(1, 500);
            }
            player.getSession().vnd += rewardVND;
            PlayerDAO.updateVND(player);
            InventoryService.gI().subQuantityItemsBag(player, item, 1);
            InventoryService.gI().sendItemBag(player);
            Service.getInstance().sendThongBao(player, "Bạn : " + player.name + " \nđã nhận được " + rewardVND + " VND \ntừ Lixi Lớn!");
        } else {
            Service.getInstance().sendThongBao(player, "Không thể mở vật phẩm, vui lòng thử lại.");
        }
    }

    private void setkichhoatthiensu(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short[] temp = {1048, 1049, 1050, 1051, 1052, 1053, 1054, 1055, 1056, 1057, 1058, 1059, 1060, 1061, 1062}; // id đồ thiên sứ
            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
            
            icon[0] = item.template.iconID;
            Item it = ItemService.gI().createNewItem(temp[index]);

            // Thêm chỉ số dựa trên loại item
            switch (it.template.type) {
                case 0: // Áo
                    it.itemOptions.add(new ItemOption(47, Util.nextInt(5, 500000))); // Chỉ số ngẫu nhiên

                    break;
                case 1: // Quần
                    it.itemOptions.add(new ItemOption(6, Util.nextInt(5, 150000000)));

                    break;

                case 2: // Găng
                    it.itemOptions.add(new ItemOption(0, Util.nextInt(10, 750000)));

                    break;
                case 3: // Giày
                    it.itemOptions.add(new ItemOption(7, Util.nextInt(20, 150000000)));

                    break;
                case 4: // Rada
                    it.itemOptions.add(new ItemOption(14, Util.nextInt(10, 30)));

                    break;
            }
            if (Util.isTrue(100, 100)) {
                if (it.template.gender == 0) {
                    it.itemOptions.add(new ItemOption(Util.nextInt(127, 129), 1)); // Chỉ số đặc biệt
                    it.itemOptions.add(new ItemOption(30, 1)); // Chỉ số đặc biệt 
                }
                if (it.template.gender == 1) {
                    it.itemOptions.add(new ItemOption(Util.nextInt(130, 132), 1)); // Chỉ số đặc biệt
                    it.itemOptions.add(new ItemOption(30, 1)); // Chỉ số đặc biệt 
                }
                if (it.template.gender == 2) {
                    it.itemOptions.add(new ItemOption(Util.nextInt(133, 135), 1)); // Chỉ số đặc biệt
                    it.itemOptions.add(new ItemOption(30, 1)); // Chỉ số đặc biệt 

                }
                if (it.template.gender == 3) {
                    it.itemOptions.add(new ItemOption(Util.nextInt(127, 135), 1)); // Chỉ số đặc biệt
                    it.itemOptions.add(new ItemOption(30, 1)); // Chỉ số đặc biệt 

                }

            }
            // Thêm vào túi đồ
            InventoryService.gI().addItemBag(pl, it, 0);
            icon[1] = it.template.iconID;
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBag(pl);
            CombineService.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void upSkillPet(Player pl, Item item) {
        if (pl.pet == null) {
            Service.gI().sendThongBao(pl, "Không thể thực hiện");
            return;
        }
        try {
            switch (item.template.id) {
                case 402: // skill 1
                    if (SkillUtil.upSkillPet(pl.pet.playerSkill.skills, 0)) {
                        Service.gI().chatJustForMe(pl, pl.pet, "Cám ơn sư phụ");
                        InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                    } else {
                        Service.gI().sendThongBao(pl, "Không thể thực hiện");
                    }
                    break;
                case 403: // skill 2
                    if (SkillUtil.upSkillPet(pl.pet.playerSkill.skills, 1)) {
                        Service.gI().chatJustForMe(pl, pl.pet, "Cám ơn sư phụ");
                        InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                    } else {
                        Service.gI().sendThongBao(pl, "Không thể thực hiện");
                    }
                    break;
                case 404: // skill 3
                    if (SkillUtil.upSkillPet(pl.pet.playerSkill.skills, 2)) {
                        Service.gI().chatJustForMe(pl, pl.pet, "Cám ơn sư phụ");
                        InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                    } else {
                        Service.gI().sendThongBao(pl, "Không thể thực hiện");
                    }
                    break;
                case 759: // skill 4
                    if (SkillUtil.upSkillPet(pl.pet.playerSkill.skills, 3)) {
                        Service.gI().chatJustForMe(pl, pl.pet, "Cám ơn sư phụ");
                        InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                    } else {
                        Service.gI().sendThongBao(pl, "Không thể thực hiện");
                    }
                    break;

            }

        } catch (Exception e) {
            Service.gI().sendThongBao(pl, "Không thể thực hiện");
        }
    }

    private void ItemManhGiay(Player pl, Item item) {
        if (pl.winSTT && !Util.isAfterMidnight(pl.lastTimeWinSTT)) {
            Service.gI().sendThongBao(pl, "Hãy gặp thần mèo Karin để sử dụng");
            return;
        } else if (pl.winSTT && Util.isAfterMidnight(pl.lastTimeWinSTT)) {
            pl.winSTT = false;
            pl.callBossPocolo = false;
            pl.zoneSieuThanhThuy = null;
        }
        NpcService.gI().createMenuConMeo(pl, item.template.id, 564,
                "Đây chính là dấu hiệu riêng của...\nĐại Ma Vương Pôcôlô\nĐó là một tên quỷ dữ đội lốt người, một kẻ đại gian ác\ncó sức mạnh vô địch và lòng tham không đáy...\nĐối phó với hắn không phải dễ\nCon có chắc chắn muốn tìm hắn không?",
                "Đồng ý", "Từ chối");
    }

    private void ItemSieuThanThuy(Player pl, Item item) {
        long tnsm = 5_000_000;
        int n = 0;
        switch (item.template.id) {
            case 727:
                n = 2;
                break;
            case 728:
                n = 10;
                break;
        }
        InventoryService.gI().subQuantityItemsBag(pl, item, 1);
        InventoryService.gI().sendItemBag(pl);
        if (Util.isTrue(50, 100)) {
            Service.gI().sendThongBao(pl, "Bạn đã bị chết vì độc của thuốc tăng lực siêu thần thủy.");
            pl.setDie();
        } else {
            for (int i = 0; i < n; i++) {
                Service.gI().addSMTN(pl, (byte) 2, tnsm, true);
            }
        }
    }

    public void UseCard(Player pl, Item item) {
        RadarCard radarTemplate = RadarService.gI().RADAR_TEMPLATE.stream().filter(c -> c.Id == item.template.id)
                .findFirst().orElse(null);
        if (radarTemplate == null) {
            return;
        }
        if (radarTemplate.Require != -1) {
            RadarCard radarRequireTemplate = RadarService.gI().RADAR_TEMPLATE.stream()
                    .filter(r -> r.Id == radarTemplate.Require).findFirst().orElse(null);
            if (radarRequireTemplate == null) {
                return;
            }
            Card cardRequire = pl.Cards.stream().filter(r -> r.Id == radarRequireTemplate.Id).findFirst().orElse(null);
            if (cardRequire == null || cardRequire.Level < radarTemplate.RequireLevel) {
                Service.gI().sendThongBao(pl, "Bạn cần sưu tầm " + radarRequireTemplate.Name + " ở cấp độ "
                        + radarTemplate.RequireLevel + " mới có thể sử dụng thẻ này");
                return;
            }
        }
        Card card = pl.Cards.stream().filter(r -> r.Id == item.template.id).findFirst().orElse(null);
        if (card == null) {
            Card newCard = new Card(item.template.id, (byte) 1, radarTemplate.Max, (byte) -1, radarTemplate.Options);
            if (pl.Cards.add(newCard)) {
                RadarService.gI().RadarSetAmount(pl, newCard.Id, newCard.Amount, newCard.MaxAmount);
                RadarService.gI().RadarSetLevel(pl, newCard.Id, newCard.Level);
                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                InventoryService.gI().sendItemBag(pl);
            }
        } else {
            if (card.Level >= 2) {
                Service.gI().sendThongBao(pl, "Thẻ này đã đạt cấp tối đa");
                return;
            }
            card.Amount++;
            if (card.Amount >= card.MaxAmount) {
                card.Amount = 0;
                if (card.Level == -1) {
                    card.Level = 1;
                } else {
                    card.Level++;
                }
                Service.gI().point(pl);
            }
            RadarService.gI().RadarSetAmount(pl, card.Id, card.Amount, card.MaxAmount);
            RadarService.gI().RadarSetLevel(pl, card.Id, card.Level);
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBag(pl);
        }
    }

    public void useGokuDay(Player pl, Item item) {
        short[] listItem = new short[]{904, 945, 951, 968, 1098, 1106, 1440};
        short idItem = listItem[Util.nextInt(listItem.length)];
        Item it = ItemService.gI().createNewItem(idItem, 1);

        int param = Util.nextInt(15, 23);

        it.addOptionParam(50, param);
        it.addOptionParam(77, param);
        it.addOptionParam(103, param);
        it.addOptionParam(210, Util.nextInt(1, 3));

        if (!Util.isTrue(1, 1000)) {
            it.addOptionParam(93, Util.nextInt(5, 15));
        }
        if (InventoryService.gI().addItemBag(pl, it, 999999)) {
            Service.gI().sendThongBao(pl, "Chúc mừng bạn đã nhận được " + it.template.name);
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBag(pl);
        } else {
            Service.gI().sendThongBao(pl, "Cần ít nhất 1 ô trống trong hành trang!");
        }
    }

    public void useGokuDayVip(Player pl, Item item) {
        short[] listItem = new short[]{1336, 1337, 1338, 1340, 1375, 1479, 1484, 1573, 1576, 1592, 1789, 1790};
        short idItem = listItem[Util.nextInt(listItem.length)];
        Item it = ItemService.gI().createNewItem(idItem, 1);

        int param = Util.nextInt(25, 33);

        it.addOptionParam(50, param);
        it.addOptionParam(77, param);
        it.addOptionParam(103, param);
        it.addOptionParam(210, Util.nextInt(5, 7));

        it.addOptionParam(106, 1);

        if (!Util.isTrue(1, 1000)) {
            it.addOptionParam(93, Util.nextInt(5, 15));
        }
        if (InventoryService.gI().addItemBag(pl, it, 999999)) {
            Service.gI().sendThongBao(pl, "Chúc mừng bạn đã nhận được " + it.template.name);
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBag(pl);
        } else {
            Service.gI().sendThongBao(pl, "Cần ít nhất 1 ô trống trong hành trang!");
        }
    }

    public void usecaitrangbase(Player pl, Item item) {
        short[] listItem = new short[]{575, 576, 577, 578, 448, 449, 450, 451, 452, 421, 422, 423, 424, 425, 426, 427, 428, 429, 430, 431, 432, 433};
        short idItem = listItem[Util.nextInt(listItem.length)];
        Item it = ItemService.gI().createNewItem(idItem, 1);

        int param = Util.nextInt(2, 40);
        it.addOptionParam(50, param);
        it.addOptionParam(77, param);
        it.addOptionParam(103, param);
        it.addOptionParam(101, Util.nextInt(1, 10));
        it.addOptionParam(210, Util.nextInt(1, 4));
        it.addOptionParam(72, Util.nextInt(1, 4));
        it.addOptionParam(106, 1);

        if (Util.isTrue(98, 100)) {
            it.addOptionParam(93, Util.nextInt(2, 7));
        }
        if (InventoryService.gI().addItemBag(pl, it, 999999)) {
            Service.gI().sendThongBao(pl, "Chúc mừng bạn đã nhận được " + it.template.name);
            //   InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBag(pl);
        } else {
            Service.gI().sendThongBao(pl, "Cần ít nhất 1 ô trống trong hành trang!");
        }
    }

    public void usevithu(Player pl) {
        short[] listItem = new short[]{1195, 1196, 1197, 1198, 1199, 1200, 1201, 1202, 1203};
        short idItem = listItem[Util.nextInt(listItem.length)];
        Item it = ItemService.gI().createNewItem(idItem, 1);

        int param = Util.nextInt(2, 99);
        it.addOptionParam(50, Util.nextInt(50, 99));
        it.addOptionParam(77, Util.nextInt(50, 99));
        it.addOptionParam(103, Util.nextInt(50, 99));
        it.addOptionParam(204, Util.nextInt(1, 20));
        it.addOptionParam(237, Util.nextInt(1, 5));
        it.addOptionParam(72, Util.nextInt(1, 4));
        if (Util.isTrue(995, 1000)) {
            it.addOptionParam(93, Util.nextInt(1, 5));
        }
        if (InventoryService.gI().addItemBag(pl, it, 999999)) {
            Service.gI().sendThongBao(pl, "Chúc mừng bạn đã nhận được " + it.template.name);
            //   InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBag(pl);
        } else {
            Service.gI().sendThongBao(pl, "Cần ít nhất 1 ô trống trong hành trang!");
        }
    }

    public void usengocrong7sao(Player pl, Item item) {
        short[] listItem = new short[]{20};
        short idItem = listItem[Util.nextInt(listItem.length)];
        Item it = ItemService.gI().createNewItem(idItem, 99);

        int param = Util.nextInt(2, 15);
//        it.addOptionParam(50, param);
//        it.addOptionParam(77, param);
//        it.addOptionParam(103, param);
//         it.addOptionParam(5, Util.nextInt(1, 10));
//        it.addOptionParam(237, Util.nextInt(1, 2));
        it.addOptionParam(72, Util.nextInt(1, 2));
        it.addOptionParam(30, 1);

        if (Util.isTrue(1, 100)) {
            it.addOptionParam(93, Util.nextInt(2, 7));
        }
        if (InventoryService.gI().addItemBag(pl, it, 999999)) {
            Service.gI().sendThongBao(pl, "Chúc mừng bạn đã nhận được " + it.template.name);
            //   InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBag(pl);
        } else {
            Service.gI().sendThongBao(pl, "Cần ít nhất 1 ô trống trong hành trang!");
        }
    }

    public void hopQuaTanThu(Player pl, Item it) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 20) {
            int gender = pl.gender;
            int[] id = {gender, 6 + gender, 21 + gender, 27 + gender, 12, 194, 441, 442, 443, 444, 445, 446, 447};
            int[] soluong = {1, 1, 1, 1, 1, 1, 10, 10, 10, 10, 10, 10, 10};
            int[] option = {0, 0, 0, 0, 0, 73, 95, 96, 97, 98, 99, 100, 101};
            int[] param = {0, 0, 0, 0, 0, 0, 5, 5, 5, 3, 3, 5, 5};
            int arrLength = id.length - 1;

            for (int i = 0; i < arrLength; i++) {
                if (i < 5) {
                    Item item = ItemService.gI().createNewItem((short) id[i]);
                    RewardService.gI().initBaseOptionClothes(item.template.id, item.template.type, item.itemOptions);
                    item.itemOptions.add(new ItemOption(107, 3));
                    InventoryService.gI().addItemBag(pl, item, 0);
                } else {
                    Item item = ItemService.gI().createNewItem((short) id[i]);
                    item.quantity = soluong[i];
                    item.itemOptions.add(new ItemOption(option[i], param[i]));
                    InventoryService.gI().addItemBag(pl, item, 0);
                }
            }
            int[] idpet = {1443};
            Item item = ItemService.gI().createNewItem((short) idpet[Util.nextInt(0, idpet.length - 1)]);
            item.itemOptions.add(new ItemOption(50, Util.nextInt(7, 50)));
            item.itemOptions.add(new ItemOption(77, Util.nextInt(9, 50)));
            item.itemOptions.add(new ItemOption(103, Util.nextInt(5, 50)));
            item.itemOptions.add(new ItemOption(72, Util.nextInt(1, 2)));
            item.itemOptions.add(new ItemOption(101, 20));
            item.itemOptions.add(new ItemOption(93, 30));
            InventoryService.gI().addItemBag(pl, item, 0);
            InventoryService.gI().subQuantityItemsBag(pl, it, 1);
            InventoryService.gI().sendItemBag(pl);
            Service.getInstance().sendThongBao(pl, "Chúc bạn chơi game vui vẻ");

        } else {
            Service.getInstance().sendThongBaoFromAdmin(pl, "Về Rương Nhà Lấy Ra 30 ô Hành Trang Để Mở");
        }
    }

    public void quatop1(Player pl, Item it) {
        // Danh sách ID pet đầu tiên (ví dụ có thể là thú cưỡi, thú cưng...)
        int[] idpet = {1159}; // thêm ID tại đây
        Item item = ItemService.gI().createNewItem((short) idpet[Util.nextInt(0, idpet.length - 1)]);
        item.itemOptions.add(new ItemOption(244, 3));
        InventoryService.gI().addItemBag(pl, item, 0);

        // Danh sách ID pet/thú/đồ đặc biệt thứ hai
        int[] idpet1 = {1540, 1541}; // thêm ID tại đây
        Item item1 = ItemService.gI().createNewItem((short) idpet1[Util.nextInt(0, idpet1.length - 1)]);
        item1.itemOptions.add(new ItemOption(50, 120));
        item1.itemOptions.add(new ItemOption(77, 120));
        item1.itemOptions.add(new ItemOption(103, 120));
        item1.itemOptions.add(new ItemOption(72, Util.nextInt(1, 2)));
        item1.itemOptions.add(new ItemOption(107, 8));
        item1.itemOptions.add(new ItemOption(210, 3));
        InventoryService.gI().addItemBag(pl, item1, 0);

        // Trừ vật phẩm mở hộp chỉ 1 lần duy nhất
        InventoryService.gI().subQuantityItemsBag(pl, it, 1);

        // Gửi lại túi đồ sau khi thêm item
        InventoryService.gI().sendItemBag(pl);

        // Thông báo
        Service.getInstance().sendThongBao(pl, "Bạn đã nhận được phần thưởng TOP!");
    }

    public void quatop2(Player pl, Item it) {
        // Danh sách ID pet đầu tiên (ví dụ có thể là thú cưỡi, thú cưng...)
        int[] idpet = {1158}; // thêm ID tại đây
        Item item = ItemService.gI().createNewItem((short) idpet[Util.nextInt(0, idpet.length - 1)]);
        item.itemOptions.add(new ItemOption(244, 3));
        InventoryService.gI().addItemBag(pl, item, 0);

        // Danh sách ID pet/thú/đồ đặc biệt thứ hai
        int[] idpet1 = {1540, 1541}; // thêm ID tại đây
        Item item1 = ItemService.gI().createNewItem((short) idpet1[Util.nextInt(0, idpet1.length - 1)]);
        item1.itemOptions.add(new ItemOption(50, 100));
        item1.itemOptions.add(new ItemOption(77, 100));
        item1.itemOptions.add(new ItemOption(103, 100));
        item1.itemOptions.add(new ItemOption(72, Util.nextInt(1, 2)));
        item1.itemOptions.add(new ItemOption(107, 8));
        item1.itemOptions.add(new ItemOption(210, 3));
        InventoryService.gI().addItemBag(pl, item1, 0);

        // Trừ vật phẩm mở hộp chỉ 1 lần duy nhất
        InventoryService.gI().subQuantityItemsBag(pl, it, 1);

        // Gửi lại túi đồ sau khi thêm item
        InventoryService.gI().sendItemBag(pl);

        // Thông báo
        Service.getInstance().sendThongBao(pl, "Bạn đã nhận được phần thưởng TOP!");
    }

    public void quatop3(Player pl, Item it) {
        // Danh sách ID pet đầu tiên (ví dụ có thể là thú cưỡi, thú cưng...)
        int[] idpet = {1214}; // thêm ID tại đây
        Item item = ItemService.gI().createNewItem((short) idpet[Util.nextInt(0, idpet.length - 1)]);
        item.itemOptions.add(new ItemOption(244, 3));
        InventoryService.gI().addItemBag(pl, item, 0);

        // Danh sách ID pet/thú/đồ đặc biệt thứ hai
        int[] idpet1 = {1540, 1541}; // thêm ID tại đây
        Item item1 = ItemService.gI().createNewItem((short) idpet1[Util.nextInt(0, idpet1.length - 1)]);
        item1.itemOptions.add(new ItemOption(50, 80));
        item1.itemOptions.add(new ItemOption(77, 80));
        item1.itemOptions.add(new ItemOption(103, 80));
        item1.itemOptions.add(new ItemOption(72, Util.nextInt(1, 2)));
        item1.itemOptions.add(new ItemOption(107, 8));
        item1.itemOptions.add(new ItemOption(210, 3));
        InventoryService.gI().addItemBag(pl, item1, 0);

        // Trừ vật phẩm mở hộp chỉ 1 lần duy nhất
        InventoryService.gI().subQuantityItemsBag(pl, it, 1);

        // Gửi lại túi đồ sau khi thêm item
        InventoryService.gI().sendItemBag(pl);

        // Thông báo
        Service.getInstance().sendThongBao(pl, "Bạn đã nhận được phần thưởng TOP!");
    }

    public void quatop4(Player pl, Item it) {
        // Danh sách ID pet đầu tiên (ví dụ có thể là thú cưỡi, thú cưng...)
        int[] idpet = {1883}; // thêm ID tại đây
        Item item = ItemService.gI().createNewItem((short) idpet[Util.nextInt(0, idpet.length - 1)]);
        item.itemOptions.add(new ItemOption(240, 3));
        InventoryService.gI().addItemBag(pl, item, 0);

        // Danh sách ID pet/thú/đồ đặc biệt thứ hai
        int[] idpet1 = {1817, 1818}; // thêm ID tại đây
        Item item1 = ItemService.gI().createNewItem((short) idpet1[Util.nextInt(0, idpet1.length - 1)]);
        item1.itemOptions.add(new ItemOption(50, 75));
        item1.itemOptions.add(new ItemOption(77, 75));
        item1.itemOptions.add(new ItemOption(103, 75));
        item1.itemOptions.add(new ItemOption(72, Util.nextInt(1, 2)));
        item1.itemOptions.add(new ItemOption(107, 8));
        item1.itemOptions.add(new ItemOption(210, 3));
        InventoryService.gI().addItemBag(pl, item1, 0);

        // Trừ vật phẩm mở hộp chỉ 1 lần duy nhất
        InventoryService.gI().subQuantityItemsBag(pl, it, 1);

        // Gửi lại túi đồ sau khi thêm item
        InventoryService.gI().sendItemBag(pl);

        // Thông báo
        Service.getInstance().sendThongBao(pl, "Bạn đã nhận được phần thưởng TOP!");
    }

    public void quatop5(Player pl, Item it) {
        // Danh sách ID pet đầu tiên (ví dụ có thể là thú cưỡi, thú cưng...)
        int[] idpet = {1883}; // thêm ID tại đây
        Item item = ItemService.gI().createNewItem((short) idpet[Util.nextInt(0, idpet.length - 1)]);
        item.itemOptions.add(new ItemOption(240, 3));
        item.itemOptions.add(new ItemOption(93, 90));
        InventoryService.gI().addItemBag(pl, item, 0);

        // Danh sách ID pet/thú/đồ đặc biệt thứ hai
        int[] idpet1 = {1817, 1818}; // thêm ID tại đây
        Item item1 = ItemService.gI().createNewItem((short) idpet1[Util.nextInt(0, idpet1.length - 1)]);
        item1.itemOptions.add(new ItemOption(50, 75));
        item1.itemOptions.add(new ItemOption(77, 75));
        item1.itemOptions.add(new ItemOption(103, 75));
        item1.itemOptions.add(new ItemOption(72, Util.nextInt(1, 2)));
        item1.itemOptions.add(new ItemOption(107, 8));
        item1.itemOptions.add(new ItemOption(210, 3));
        InventoryService.gI().addItemBag(pl, item1, 0);

        // Trừ vật phẩm mở hộp chỉ 1 lần duy nhất
        InventoryService.gI().subQuantityItemsBag(pl, it, 1);

        // Gửi lại túi đồ sau khi thêm item
        InventoryService.gI().sendItemBag(pl);

        // Thông báo
        Service.getInstance().sendThongBao(pl, "Bạn đã nhận được phần thưởng TOP!");
    }

    public void quatop6(Player pl, Item it) {
        // Danh sách ID pet đầu tiên (ví dụ có thể là thú cưỡi, thú cưng...)
        int[] idpet = {1883}; // thêm ID tại đây
        Item item = ItemService.gI().createNewItem((short) idpet[Util.nextInt(0, idpet.length - 1)]);
        item.itemOptions.add(new ItemOption(240, 3));
        item.itemOptions.add(new ItemOption(93, 30));
        InventoryService.gI().addItemBag(pl, item, 0);

        // Danh sách ID pet/thú/đồ đặc biệt thứ hai
        int[] idpet1 = {1817, 1818}; // thêm ID tại đây
        Item item1 = ItemService.gI().createNewItem((short) idpet1[Util.nextInt(0, idpet1.length - 1)]);
        item1.itemOptions.add(new ItemOption(50, 75));
        item1.itemOptions.add(new ItemOption(77, 75));
        item1.itemOptions.add(new ItemOption(103, 75));
        item1.itemOptions.add(new ItemOption(72, Util.nextInt(1, 2)));
        item1.itemOptions.add(new ItemOption(107, 8));
        item1.itemOptions.add(new ItemOption(210, 3));
        InventoryService.gI().addItemBag(pl, item1, 0);

        // Trừ vật phẩm mở hộp chỉ 1 lần duy nhất
        InventoryService.gI().subQuantityItemsBag(pl, it, 1);

        // Gửi lại túi đồ sau khi thêm item
        InventoryService.gI().sendItemBag(pl);

        // Thông báo
        Service.getInstance().sendThongBao(pl, "Bạn đã nhận được phần thưởng TOP!");
    }

    public void quatop7(Player pl, Item it) {
        // Danh sách ID pet đầu tiên (ví dụ có thể là thú cưỡi, thú cưng...)

        Item item = ItemService.gI().createNewItem((short) 1150, 20000);
        item.itemOptions.add(new ItemOption(240, 3));
        item.itemOptions.add(new ItemOption(93, 30));
        InventoryService.gI().addItemBag(pl, item, 0);
        InventoryService.gI().addItemBag(pl, item, 0);

        // Trừ vật phẩm mở hộp chỉ 1 lần duy nhất
        InventoryService.gI().subQuantityItemsBag(pl, it, 1);

        // Gửi lại túi đồ sau khi thêm item
        InventoryService.gI().sendItemBag(pl);

        // Thông báo
        Service.getInstance().sendThongBao(pl, "Bạn đã nhận được phần thưởng TOP!");
    }

    public void openRuongSPLVIP(Player pl, Item item) {
        int idtemp = 1263;
        if (Util.isTrue(50, 100)) {
            int[] lst = {1724, 1725, 1726, 1727, 1728, 1729, 1730, 1733, 1734, 1735, 1736, 1737, 1738, 1739};
            idtemp = lst[Util.nextInt(lst.length)];
        }
        Item it = ItemService.gI().createNewItem((short) idtemp);
        if (idtemp >= 1724 && idtemp <= 1730) {
            it.addOptionParam(idtemp - 1724 + 95, (idtemp == 1727 || idtemp == 1728) ? 3 : 5);
        } else if (idtemp >= 1733 && idtemp <= 1739) {
            it.addOptionParam(idtemp - 1733 + 95, (idtemp == 1736 || idtemp == 1737) ? 4 : 8);
        }
        it.addOptionParam(30, 1);
        if (!InventoryService.gI().addItemBag(pl, it, 999999)) {
            Service.gI().sendThongBao(pl, "Bạn không đủ ô trống trong hành trang");
            return;
        }
        InventoryService.gI().subQuantityItemsBag(pl, item, 1);
        Service.gI().sendThongBao(pl, "Bạn nhận được " + it.template.name);
        InventoryService.gI().sendItemBag(pl);

    }
}
