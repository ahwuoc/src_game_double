package services;

/*
 *
 *

 */
import ChisoAn.ChisoHuyenAo;
import ChisoAn.ChisoVIP;
import models.Template;
import models.Template.ItemOptionTemplate;
import item.Item;
import map.ItemMap;
import player.Player;
import shop.ItemShop;
import server.Manager;
import utils.TimeUtil;
import utils.Util;
import item.Item.ItemOption;

import java.util.*;
import java.util.stream.Collectors;

import consts.ConstItemOption;
import map.Zone;
import models.Combine.CombineService;
import shop.Shop;
import shop.TabShop;

public class ItemService {

    private static ItemService i;

    public static ItemService gI() {
        if (i == null) {
            i = new ItemService();
        }
        return i;
    }

    public short getItemIdByIcon(short IconID) {
        for (int i = 0; i < Manager.ITEM_TEMPLATES.size(); i++) {
            if (Manager.ITEM_TEMPLATES.get(i).iconID == IconID) {
                return Manager.ITEM_TEMPLATES.get(i).id;
            }
        }
        return -1;
    }

    public Item createItemNull() {
        Item item = new Item();
        return item;
    }

    public Item createItemFromItemShop(ItemShop itemShop) {
        Item item = new Item();
        item.template = itemShop.temp;
        item.quantity = 1;
        item.content = item.getContent();
        item.info = item.getInfo();
        for (Item.ItemOption io : itemShop.options) {
            item.itemOptions.add(new Item.ItemOption(io));
        }
        return item;
    }

    public Item copyItem(Item item) {
        Item it = new Item();
        it.itemOptions = new ArrayList<>();
        it.template = item.template;
        it.info = item.info;
        it.content = item.content;

        it.quantity = item.quantity;
        it.createTime = item.createTime;
        for (Item.ItemOption io : item.itemOptions) {
            it.itemOptions.add(new Item.ItemOption(io));
        }
        return it;
    }

    public Item createNewItem(int tempId) {
        return createNewItem(tempId, 1);
    }

    public Item otpts(short tempId) {
        return otpts(tempId, 1);
    }

    public Item createNewItem(int tempId, int quantity) {
        Item item = new Item();
        item.template = getTemplate(tempId);
        item.quantity = quantity;
        item.createTime = System.currentTimeMillis();

        item.content = item.getContent();
        item.info = item.getInfo();
        return item;
    }

    public Item otpts(short tempId, int quantity) {
        Item item = new Item();
        item.template = getTemplate(tempId);
        item.quantity = quantity;
        item.createTime = System.currentTimeMillis();
        if (item.template.type == 0) {
            item.itemOptions.add(new ItemOption(21, 80));
            item.itemOptions.add(new ItemOption(47, Util.nextInt(2000, 2500)));
        }
        if (item.template.type == 1) {
            item.itemOptions.add(new ItemOption(21, 80));
            item.itemOptions.add(new ItemOption(22, Util.nextInt(150, 200)));
        }
        if (item.template.type == 2) {
            item.itemOptions.add(new ItemOption(21, 80));
            item.itemOptions.add(new ItemOption(0, Util.nextInt(18000, 20000)));
        }
        if (item.template.type == 3) {
            item.itemOptions.add(new ItemOption(21, 80));
            item.itemOptions.add(new ItemOption(23, Util.nextInt(150, 200)));
        }
        if (item.template.type == 4) {
            item.itemOptions.add(new ItemOption(21, 80));
            item.itemOptions.add(new ItemOption(14, Util.nextInt(20, 25)));
        }
        item.content = item.getContent();
        item.info = item.getInfo();
        return item;
    }

    public Item createItemSetKichHoat(int tempId, int quantity) {
        Item item = new Item();
        item.template = getTemplate(tempId);
        item.quantity = quantity;
        item.itemOptions = createItemNull().itemOptions;
        item.createTime = System.currentTimeMillis();
        item.content = item.getContent();
        item.info = item.getInfo();
        return item;
    }

    public Item createItemDoHuyDiet(int tempId, int quantity) {
        Item item = new Item();
        item.template = getTemplate(tempId);
        item.quantity = quantity;
        item.itemOptions = createItemNull().itemOptions;
        item.createTime = System.currentTimeMillis();
        item.content = item.getContent();
        item.info = item.getInfo();
        return item;
    }

    public Item createItemFromItemMap(ItemMap itemMap) {
        Item item = createNewItem(itemMap.itemTemplate.id, itemMap.quantity);
        item.itemOptions = itemMap.options;
        return item;
    }

    public ItemOptionTemplate getItemOptionTemplate(int id) {
        return Manager.ITEM_OPTION_TEMPLATES.get(id);
    }

    public Template.ItemTemplate getTemplate(int id) {
        return Manager.ITEM_TEMPLATES.get(id);
    }

    public boolean isItemActivation(Item item) {
        return false;
    }

    public int getPercentTrainArmor(Item item) {
        if (item != null) {
            switch (item.template.id) {
                case 529:
                case 534:
                    return 10;
                case 530:
                case 535:
                    return 20;
                case 531:
                case 536:
                    return 30;
                case 1407:
                    return 40;
                default:
                    return 0;
            }
        } else {
            return 0;
        }
    }

    public boolean isTrainArmor(Item item) {
        if (item != null) {
            switch (item.template.id) {
                case 529:
                case 534:
                case 530:
                case 535:
                case 531:
                case 536:
                case 1407:
                    return true;
                default:
                    return false;
            }
        } else {
            return false;
        }
    }

    public boolean isOutOfDateTime(Item item) {
        if (item != null) {
            for (Item.ItemOption io : item.itemOptions) {
                if (io.optionTemplate.id == 93) {
                    int dayPass = (int) TimeUtil.diffDate(new Date(), new Date(item.createTime), TimeUtil.DAY);
                    if (dayPass != 0) {
                        io.param -= dayPass;
                        if (io.param <= 0) {
                            return true;
                        } else {
                            item.createTime = System.currentTimeMillis();
                        }
                    }
                }
            }
        }
        return false;
    }

    public int randomSKHId(byte gender) {
        if (gender == 3) {
            gender = 2;
        }
        int[][] options = {{128, 129, 127}, {131, 132, 130}, {133, 135, 134}};
        int skhv1 = 25;
        int skhv2 = 35;
        int skhc = 40;
        int skhId = -1;
        int rd = Util.nextInt(1, 100);
        if (rd <= skhv1) {
            skhId = 0;
        } else if (rd <= skhv1 + skhv2) {
            skhId = 1;
        } else if (rd <= skhv1 + skhv2 + skhc) {
            skhId = 2;
        }
        if (gender == 0 && skhId == 0 && Util.isTrue(50, 100)) {
            return 214;
        }
        return options[gender][skhId];
    }

    public void OpenItem736(Player player, Item itemUse) {
        try {
            if (InventoryService.gI().getCountEmptyBag(player) <= 1) {
                Service.gI().sendThongBao(player, "Bạn phải có ít nhất 2 ô trống hành trang");
                return;
            }
            short[] icon = new short[2];
            int rd = Util.nextInt(1, 100);
            int rac = 50;
            int ruby = 20;
            int dbv = 10;
            int vb = 10;
            int bh = 5;
            int ct = 5;
            Item item = randomRac();
            if (rd <= rac) {
                item = randomRac();
            } else if (rd <= rac + ruby) {
                item = createItemSetKichHoat(861, 1);
            } else if (rd <= rac + ruby + dbv) {
                item = daBaoVe();
            } else if (rd <= rac + ruby + dbv + vb) {
                item = vanBay2011(true);
            } else if (rd <= rac + ruby + dbv + vb + bh) {
                item = phuKien2011(true);
            } else if (rd <= rac + ruby + dbv + vb + bh + ct) {
                item = caitrang2011(true);
            }
            if (item.template.id == 861) {
                item.quantity = Util.nextInt(10, 30);
            }
            icon[0] = itemUse.template.iconID;
            icon[1] = item.template.iconID;
            InventoryService.gI().subQuantityItemsBag(player, itemUse, 1);
            InventoryService.gI().addItemBag(player, item,999999);
            InventoryService.gI().sendItemBag(player);
            player.inventory.event++;
            Service.gI().sendThongBao(player, "Bạn đã nhận được " + item.template.name);
            CombineService.gI().sendEffectOpenItem(player, icon[0], icon[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void OpenItem648(Player player, Item itemUse) {
        try {
            if (InventoryService.gI().getCountEmptyBag(player) <= 1) {
                Service.gI().sendThongBao(player, "Bạn phải có ít nhất 2 ô trống hành trang");
                return;
            }
            short[] icon = new short[2];
            int rd = Util.nextInt(1, 100);
            int rac = 50;
            int ruby = 20;
            int dbv = 10;
            int vb = 10;
            int bh = 5;
            int ct = 5;
            Item item = randomRac();
            if (rd <= rac) {
                item = randomRac2();
            } else if (rd <= rac + ruby) {
                item = createItemSetKichHoat(861, 1);
            } else if (rd <= rac + ruby + dbv) {
                item = vatphamsk(true);
            } else if (rd <= rac + ruby + dbv + vb) {
                item = vanBayChrimas(true);
            } else if (rd <= rac + ruby + dbv + vb + bh) {
                item = phuKienChristmas(true);
            } else if (rd <= rac + ruby + dbv + vb + bh + ct) {
                item = caitrangChristmas(true);
            }
            if (item.template.id == 861) {
                item.quantity = Util.nextInt(10, 30);
            }
            icon[0] = itemUse.template.iconID;
            icon[1] = item.template.iconID;
            InventoryService.gI().subQuantityItemsBag(player, itemUse, 1);
            InventoryService.gI().addItemBag(player, item,999999);
            InventoryService.gI().sendItemBag(player);
            player.inventory.event++;
            Service.gI().sendThongBao(player, "Bạn đã nhận được " + item.template.name);
            CombineService.gI().sendEffectOpenItem(player, icon[0], icon[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Item itemSKH(int itemId, int skhId) {
        Item item = createItemSetKichHoat(itemId, 1);
        if (item != null) {
            item.itemOptions.addAll(ItemService.gI().getListOptionItemShop((short) itemId));
            item.itemOptions.add(new Item.ItemOption(skhId, 1));
            item.itemOptions.add(new Item.ItemOption(optionIdSKH(skhId), 1));
            item.itemOptions.add(new Item.ItemOption(30, 1));
        }
        return item;
    }

    public int optionItemSKH(int typeItem) {
        switch (typeItem) {
            case 0:
                return 47;
            case 1:
                return 6;
            case 2:
                return 0;
            case 3:
                return 7;
            default:
                return 14;
        }
    }

    public int pagramItemSKH(int typeItem) {
        switch (typeItem) {
            case 0:
            case 2:
                return Util.nextInt(5);
            case 1:
            case 3:
                return Util.nextInt(20, 30);
            default:
                return Util.nextInt(3);
        }
    }

    public int optionIdSKH(int skhId) {
        switch (skhId) {
            case 127: //Set Taiyoken
                return 139;
            case 128: //Set Genki
                return 140;
            case 129: //Set Kamejoko
                return 141;
            case 130: //Set KI
                return 142;
            case 131: //Set Dame
                return 143;
            case 132: //Set Summon
                return 144;
            case 133: //Set Galick
                return 136;
            case 134: //Set Monkey
                return 137;
            case 135: //Set HP
                return 138;
            case 214: //Set Kaioken
                return 215;
        }
        return 0;
    }

    public Item itemDHD(int itemId, int dhdId) {
        Item item = createItemSetKichHoat(itemId, 1);
        if (item != null) {
            item.itemOptions.add(new Item.ItemOption(dhdId, 1));
            item.itemOptions.add(new Item.ItemOption(optionIdDHD(dhdId), 1));
            item.itemOptions.add(new Item.ItemOption(30, 1));
        }
        return item;
    }

    public int optionIdDHD(int skhId) {
        switch (skhId) {
            case 127: //Set Taiyoken
                return 139;
            case 128: //Set Genki
                return 140;
            case 129: //Set Kamejoko
                return 141;
            case 130: //Set KI
                return 142;
            case 131: //Set Dame
                return 143;
            case 132: //Set Summon
                return 144;
            case 133: //Set Galick
                return 136;
            case 134: //Set Monkey
                return 137;
            case 135: //Set HP
                return 138;
        }
        return 0;
    }

    public Item randomCS_DHD(int itemId, int gender) {
        Item it = createItemSetKichHoat(itemId, 1);
        List<Integer> ao = Arrays.asList(650, 652, 654);
        List<Integer> quan = Arrays.asList(651, 653, 655);
        List<Integer> gang = Arrays.asList(657, 659, 661);
        List<Integer> giay = Arrays.asList(658, 660, 662);
        int nhd = 656;
        if (ao.contains(itemId)) {
            it.itemOptions.add(new Item.ItemOption(47, Util.highlightsItem(gender == 2, new Random().nextInt(1001) + 1800))); // áo từ 1800-2800 giáp
        }
        if (quan.contains(itemId)) {
            it.itemOptions.add(new Item.ItemOption(22, Util.highlightsItem(gender == 0, new Random().nextInt(16) + 85))); // hp 85-100k
        }
        if (gang.contains(itemId)) {
            it.itemOptions.add(new Item.ItemOption(0, Util.highlightsItem(gender == 2, new Random().nextInt(150) + 8500))); // 8500-10000
        }
        if (giay.contains(itemId)) {
            it.itemOptions.add(new Item.ItemOption(23, Util.highlightsItem(gender == 1, new Random().nextInt(11) + 80))); // ki 80-90k
        }
        if (nhd == itemId) {
            it.itemOptions.add(new Item.ItemOption(14, new Random().nextInt(3) + 17)); //chí mạng 17-19%
        }
        it.itemOptions.add(new Item.ItemOption(21, 80));// yêu cầu sm 80 tỉ
        it.itemOptions.add(new Item.ItemOption(30, 1));// ko the gd
        return it;
    }

    //Cải trang sự kiện 20/11
    public Item caitrang2011(boolean rating) {
        Item item = createItemSetKichHoat(680, 1);
        item.itemOptions.add(new Item.ItemOption(76, 1));//VIP
        item.itemOptions.add(new Item.ItemOption(77, 28));//hp 28%
        item.itemOptions.add(new Item.ItemOption(103, 25));//ki 25%
        item.itemOptions.add(new Item.ItemOption(147, 24));//sd 26%
        if (Util.isTrue(995, 1000) && rating) {// tỉ lệ ra hsd
            item.itemOptions.add(new Item.ItemOption(93, new Random().nextInt(3) + 1));//hsd
        }
        return item;
    }
    
   private void hopquaramdomthoivang(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            Item it = ItemService.gI().createNewItem((short) 457, Util.nextInt(1, 200));
            it.itemOptions.add(new ItemOption(30, Util.nextInt(1, 5)));
            InventoryService.gI().addItemBag(pl, it,999999);
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBag(pl);
            short icon1 = item.template.iconID;
            short icon2 = it.template.iconID;
            CombineService.gI().sendEffectOpenItem(pl, icon1, icon2);
        } else {
            Service.getInstance().sendThongBao(pl, "Hãy chừa 1 ô trống để mở.");
        }

    }
    

    //Cải trang sự kiện giáng sinh
    public Item caitrangChristmas(boolean rating) {
        Item item = createItemSetKichHoat(Util.nextInt(386, 394), 1);
        item.itemOptions.add(new Item.ItemOption(77, Util.nextInt(15, 51)));
        item.itemOptions.add(new Item.ItemOption(103, Util.nextInt(15, 51)));
        item.itemOptions.add(new Item.ItemOption(147, Util.nextInt(15, 20)));
        item.itemOptions.add(new Item.ItemOption(95, Util.nextInt(15, 51)));
        item.itemOptions.add(new Item.ItemOption(5, Util.nextInt(1, 30)));
        item.itemOptions.add(new Item.ItemOption(106, 0));//sd 26%
        if (Util.isTrue(995, 1000) && rating) {// tỉ lệ ra hsd
            item.itemOptions.add(new Item.ItemOption(93, new Random().nextInt(3) + 1));//hsd
        }
        return item;
    }

    //610 - bong hoa
    //Phụ kiện bó hoa 20/11
    public Item phuKien2011(boolean rating) {
        Item item = createItemSetKichHoat(954, 1);
        item.itemOptions.add(new Item.ItemOption(77, new Random().nextInt(5) + 5));
        item.itemOptions.add(new Item.ItemOption(103, new Random().nextInt(5) + 5));
        item.itemOptions.add(new Item.ItemOption(147, new Random().nextInt(5) + 5));
        if (Util.isTrue(1, 100)) {
            item.itemOptions.get(Util.nextInt(item.itemOptions.size() - 1)).param = 10;
        }
        item.itemOptions.add(new Item.ItemOption(30, 1));//ko the gd
        if (Util.isTrue(995, 1000) && rating) {// tỉ lệ ra hsd
            item.itemOptions.add(new Item.ItemOption(93, new Random().nextInt(3) + 1));//hsd
        }
        return item;
    }

    public Item phuKienChristmas(boolean rating) {
        Item item = createItemSetKichHoat(745, 1);
        item.itemOptions.add(new Item.ItemOption(77, new Random().nextInt(25) + 5));
        item.itemOptions.add(new Item.ItemOption(103, new Random().nextInt(25) + 5));
        item.itemOptions.add(new Item.ItemOption(147, new Random().nextInt(25) + 5));
        if (Util.isTrue(1, 100)) {
            item.itemOptions.get(Util.nextInt(item.itemOptions.size() - 1)).param = 10;
        }
        item.itemOptions.add(new Item.ItemOption(30, 1));//ko the gd
        if (Util.isTrue(995, 1000) && rating) {// tỉ lệ ra hsd
            item.itemOptions.add(new Item.ItemOption(93, new Random().nextInt(3) + 1));//hsd
        }
        return item;
    }

    public Item vanBay2011(boolean rating) {
        Item item = createItemSetKichHoat(795, 1);
        item.itemOptions.add(new Item.ItemOption(89, 1));
        item.itemOptions.add(new Item.ItemOption(30, 1));//ko the gd
        if (Util.isTrue(950, 1000) && rating) {// tỉ lệ ra hsd
            item.itemOptions.add(new Item.ItemOption(93, new Random().nextInt(3) + 1));//hsd
        }
        return item;
    }

    public Item daBaoVe() {
        Item item = createItemSetKichHoat(987, 1);
        item.itemOptions.add(new Item.ItemOption(30, 1));//ko the gd
        return item;
    }

    public Item randomRac() {
        short[] racs = {20, 19, 18, 17};
        Item item = createItemSetKichHoat(racs[Util.nextInt(racs.length - 1)], 1);
        if (optionRac(item.template.id) != 0) {
            item.itemOptions.add(new Item.ItemOption(optionRac(item.template.id), 1));
        }
        return item;
    }

    public Item randomRac2() {
        short[] racs = {585, 704, 2048, 379, 384, 385, 381, 828, 829, 830, 831, 832, 833, 834, 835, 836, 837, 838, 839, 840, 841, 842, 934, 935};
        int idItem = racs[Util.nextInt(racs.length - 1)];
        if (Util.isTrue(1, 100)) {
            idItem = 956;
        }
        Item item = createItemSetKichHoat(idItem, 1);
        if (optionRac(item.template.id) != 0) {
            item.itemOptions.add(new Item.ItemOption(optionRac(item.template.id), 1));
        }
        return item;
    }

    public Item vanBayChrimas(boolean rating) {
        Item item = createItemSetKichHoat(746, 1);
        item.itemOptions.add(new Item.ItemOption(89, 1));
        item.itemOptions.add(new Item.ItemOption(30, 1));//ko the gd
        if (Util.isTrue(950, 1000) && rating) {// tỉ lệ ra hsd
            item.itemOptions.add(new Item.ItemOption(93, new Random().nextInt(3) + 1));//hsd
        }
        return item;
    }

    public byte optionRac(short itemId) {
        switch (itemId) {
            case 220:
                return 71;
            case 221:
                return 70;
            case 222:
                return 69;
            case 224:
                return 67;
            case 223:
                return 68;
            default:
                return 0;
        }
    }

    public Item vatphamsk(boolean hsd) {
        int[] itemId = {2025, 2026, 2036, 2037, 2038, 2039, 2040, 2019, 2020, 2021, 2022, 2023, 2024, 954, 955, 952, 953, 924, 860, 742};
        byte[] option = {77, 80, 81, 103, 50, 94, 5};
        byte[] option_v2 = {14, 16, 17, 19, 27, 28, 47, 87}; //77 %hp // 80 //81 //103 //50 //94 //5 % sdcm
        byte optionid = 0;
        byte optionid_v2 = 0;
        byte param = 0;
        Item lt = ItemService.gI().createNewItem((short) itemId[Util.nextInt(itemId.length)]);
        lt.itemOptions.clear();
        optionid = option[Util.nextInt(0, 6)];
        param = (byte) Util.nextInt(5, 15);
        lt.itemOptions.add(new Item.ItemOption(optionid, param));
        if (Util.isTrue(1, 100)) {
            optionid_v2 = option_v2[Util.nextInt(option_v2.length)];
            lt.itemOptions.add(new Item.ItemOption(optionid_v2, param));
        }
        if (Util.isTrue(999, 1000) && hsd) {
            lt.itemOptions.add(new Item.ItemOption(93, Util.nextInt(1, 7)));
        }
        lt.itemOptions.add(new Item.ItemOption(30, 0));
        return lt;
    }

    public void openBoxVip(Player player) {
        if (InventoryService.gI().getCountEmptyBag(player) <= 1) {
            Service.gI().sendThongBao(player, "Bạn phải có ít nhất 2 ô trống hành trang");
            return;
        }
        if (player.inventory.event < 3000) {
            Service.gI().sendThongBao(player, "Bạn không đủ bông...");
            return;
        }
        Item item;
        if (Util.isTrue(45, 100)) {
            item = caitrang2011(false);
        } else {
            item = phuKien2011(false);
        }
        short[] icon = new short[2];
        icon[0] = 6983;
        icon[1] = item.template.iconID;
        InventoryService.gI().addItemBag(player, item,999999);
        InventoryService.gI().sendItemBag(player);
        player.inventory.event -= 3000;
        Service.gI().sendThongBao(player, "Bạn đã nhận được " + item.template.name);
        CombineService.gI().sendEffectOpenItem(player, icon[0], icon[1]);
    }

    public void giaobong(Player player, int quantity) {
        if (quantity > 10000) {
            return;
        }
        try {
            Item itemUse = InventoryService.gI().findItem(player.inventory.itemsBag, 610);
            if (itemUse.quantity < quantity) {
                Service.gI().sendThongBao(player, "Bạn không đủ bông...");
                return;
            }
            InventoryService.gI().subQuantityItemsBag(player, itemUse, quantity);
            Item item = createItemSetKichHoat(736, (quantity / 100));
            item.itemOptions.add(new Item.ItemOption(30, 1));//ko the gd
            InventoryService.gI().addItemBag(player, item,999999);
            InventoryService.gI().sendItemBag(player);
            Service.gI().sendThongBao(player, "Bạn đã nhận được x" + (quantity / 100) + " " + item.template.name);
        } catch (Exception e) {
            Service.gI().sendThongBao(player, "Bạn không đủ bông...");
        }
    }

    public Item PK_WC(int itemId) {
        Item phukien = createItemSetKichHoat(itemId, 1);
        int co = 983;
        int cup = 982;
        int bong = 966;
        if (cup == itemId) {
            phukien.itemOptions.add(new Item.ItemOption(77, new Random().nextInt(6) + 5)); // hp 5-10%
        }
        if (co == itemId) {
            phukien.itemOptions.add(new Item.ItemOption(103, new Random().nextInt(6) + 5)); // ki 5-10%
        }
        if (bong == itemId) {
            phukien.itemOptions.add(new Item.ItemOption(50, new Random().nextInt(6) + 5)); // sd 5- 10%
        }
        phukien.itemOptions.add(new Item.ItemOption(192, 1));//WORLDCUP
        phukien.itemOptions.add(new Item.ItemOption(193, 1));//(2 món kích hoạt ....)
        if (Util.isTrue(99, 100)) {// tỉ lệ ra hsd
            phukien.itemOptions.add(new Item.ItemOption(93, new Random().nextInt(2) + 1));//hsd
        }
        return phukien;
    }

    //Cải trang Gohan WC
    public Item CT_WC(boolean rating) {
        Item caitrang = createItemSetKichHoat(883, 1);
        caitrang.itemOptions.add(new Item.ItemOption(77, 30));// hp 30%
        caitrang.itemOptions.add(new Item.ItemOption(103, 15));// ki 15%
        caitrang.itemOptions.add(new Item.ItemOption(50, 20));// sd 20%
        caitrang.itemOptions.add(new Item.ItemOption(192, 1));//WORLDCUP
        caitrang.itemOptions.add(new Item.ItemOption(193, 1));//(2 món kích hoạt ....)
        if (Util.isTrue(99, 100) && rating) {// tỉ lệ ra hsd
            caitrang.itemOptions.add(new Item.ItemOption(93, new Random().nextInt(2) + 1));//hsd
        }
        return caitrang;
    }

    public void openDTS(Player player) {
        //check sl đồ tl, đồ hd
        if (player.combine.itemsCombine.stream().filter(item -> item.template.id >= 555 && item.template.id <= 567).count() < 1) {
            Service.gI().sendThongBao(player, "Thiếu đồ thần linh");
            return;
        }
        if (player.combine.itemsCombine.stream().filter(item -> item.template.id >= 650 && item.template.id <= 662).count() < 2) {
            Service.gI().sendThongBao(player, "Thiếu đồ hủy diệt");
            return;
        }
        if (player.combine.itemsCombine.size() != 3) {
            Service.gI().sendThongBao(player, "Thiếu đồ");
            return;
        }
        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
            Item itemTL = player.combine.itemsCombine.stream().filter(item -> item.template.id >= 555 && item.template.id <= 567).findFirst().get();
            List<Item> itemHDs = player.combine.itemsCombine.stream().filter(item -> item.template.id >= 650 && item.template.id <= 662).collect(Collectors.toList());
            short[][] itemIds = {{1048, 1051, 1054, 1057, 1060}, {1049, 1052, 1055, 1058, 1061}, {1050, 1053, 1056, 1059, 1062}}; // thứ tự td - 0,nm - 1, xd - 2

            Item itemTS = DoThienSu(itemIds[player.gender][itemTL.template.type], player.gender);
            InventoryService.gI().addItemBag(player, itemTS,999999);

            InventoryService.gI().subQuantityItemsBag(player, itemTL, 1);
            itemHDs.forEach(item -> InventoryService.gI().subQuantityItemsBag(player, item, 1));

            InventoryService.gI().sendItemBag(player);
            Service.gI().sendThongBao(player, "Bạn đã nhận được " + itemTS.template.name);
        } else {
            Service.gI().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
        }
    }

    public Item DoThienSu(int itemId, int gender) {
        Item dots = createItemSetKichHoat(itemId, 1);
        List<Integer> ao = Arrays.asList(1048, 1049, 1050);
        List<Integer> quan = Arrays.asList(1051, 1052, 1053);
        List<Integer> gang = Arrays.asList(1054, 1055, 1056);
        List<Integer> giay = Arrays.asList(1057, 1058, 1059);
        List<Integer> nhan = Arrays.asList(1060, 1061, 1062);
        //áo
        if (ao.contains(itemId)) {
            dots.itemOptions.add(new Item.ItemOption(47, Util.highlightsItem(gender == 2, new Random().nextInt(1201) + 2800))); // áo từ 2800-4000 giáp
        }
        //quần
        if (Util.isTrue(80, 100)) {
            if (quan.contains(itemId)) {
                dots.itemOptions.add(new Item.ItemOption(22, Util.highlightsItem(gender == 0, new Random().nextInt(11) + 120))); // hp 120k-130k
            }
        } else {
            if (quan.contains(itemId)) {
                dots.itemOptions.add(new Item.ItemOption(22, Util.highlightsItem(gender == 0, new Random().nextInt(21) + 130))); // hp 130-150k 15%
            }
        }
        //găng
        if (Util.isTrue(80, 100)) {
            if (gang.contains(itemId)) {
                dots.itemOptions.add(new Item.ItemOption(0, Util.highlightsItem(gender == 2, new Random().nextInt(651) + 9350))); // 9350-10000
            }
        } else {
            if (gang.contains(itemId)) {
                dots.itemOptions.add(new Item.ItemOption(0, Util.highlightsItem(gender == 2, new Random().nextInt(1001) + 10000))); // gang 15% 10-11k -xayda 12k1
            }
        }
        //giày
        if (Util.isTrue(80, 100)) {
            if (giay.contains(itemId)) {
                dots.itemOptions.add(new Item.ItemOption(23, Util.highlightsItem(gender == 1, new Random().nextInt(21) + 90))); // ki 90-110k
            }
        } else {
            if (giay.contains(itemId)) {
                dots.itemOptions.add(new Item.ItemOption(23, Util.highlightsItem(gender == 1, new Random().nextInt(21) + 110))); // ki 110-130k
            }
        }

        if (nhan.contains(itemId)) {
            dots.itemOptions.add(new Item.ItemOption(14, Util.highlightsItem(gender == 1, new Random().nextInt(3) + 18))); // nhẫn 18-20%
        }
        dots.itemOptions.add(new Item.ItemOption(21, 120));
        dots.itemOptions.add(new Item.ItemOption(30, 1));
        return dots;
    }

    public List<Item.ItemOption> getListOptionItemShop(short id) {
        List<Item.ItemOption> list = new ArrayList<>();
        Manager.SHOPS.forEach(shop -> shop.tabShops.forEach(tabShop -> tabShop.itemShops.forEach(itemShop -> {
            if (itemShop.temp.id == id && list.isEmpty()) {
                list.addAll(itemShop.options);
            }
        })));
        return list;
    }

    public int randTempItemKichHoat(int gender) {
        int[][][] items = {{{0, 33}, {1, 41}, {2, 49}}, {{6, 35}, {7, 43}, {8, 51}}, {{27, 30}, {28, 47}, {29, 55}}, {{21, 24}, {22, 46}, {23, 53}}, {{12, 57}, {12, 57}, {12, 57}}};
        // a w j g rd
        int type;
        if (Util.isTrue(20, 100)) {
            type = 4; // rada
        } else if (Util.isTrue(30, 100)) {
            type = 3; // gang
        } else if (Util.isTrue(40, 100)) {
            type = 1; // quan
        } else if (Util.isTrue(50, 100)) {
            type = 0; // ao
        } else {
            type = 2; // giay
        }

        return items[type][gender][Util.nextInt(1)];
    }

  public int randTempItemKichHoatthanlinh(int gender) {
        int[][][] items = {{{555, 557,559}, {556, 558,560}, {562, 564,566}}, {{563, 565,567}, {561, 561,561}}};
        // a w j g rd
        int type;
        if (Util.isTrue(20, 100)) {
            type = 4; // rada
        } else if (Util.isTrue(30, 100)) {
            type = 3; // gang
        } else if (Util.isTrue(40, 100)) {
            type = 1; // quan
        } else if (Util.isTrue(50, 100)) {
            type = 0; // ao
        } else {
            type = 2; // giay
        }

        return items[type][gender][Util.nextInt(1)];
    }   
    
    
    
    
    public int[] randOptionItemKichHoat(int gender) {
        int op1;
        int op2;
        switch (gender) {
            case 0 -> {
                if (Util.isTrue(50, 100)) {
                    op1 = 128;
                    op2 = 140;
                } else if (Util.isTrue(50, 100)) {
                    op1 = 127;
                    op2 = 139;
                } else {
                    op1 = 129;
                    op2 = 141;
                } 
            }
            case 1 -> {
                if (Util.isTrue(50, 100)) {
                    op1 = 130;
                    op2 = 142;
                } else if (Util.isTrue(25, 100)) {
                    op1 = 131;
                    op2 = 143;
                } else {
                    op1 = 132;
                    op2 = 144;
                }
            }
            default -> {
                if (Util.isTrue(50, 100)) {
                    op1 = 134;
                    op2 = 137;
                } else if (Util.isTrue(25, 100)) {
                    op1 = 135;
                    op2 = 138;
                } else {
                    op1 = 133;
                    op2 = 136;
                }
            }
        }
        int[] options = {op1, op2};
        return options;
    }

    public ItemMap randDoTL(Zone zone, int quantity, int x, int y, long id) {
        short idTempTL, type;
        short[] ao = {555, 557, 559};
        short[] quan = {556, 558, 560};
        short[] gang = {562, 564, 566};
        short[] giay = {563, 565, 567};
        short[] nhan = {561};
        short[] options = {30, 34, 35, 36, 86, 87, 208};
        if (Util.isTrue(10, 100)) {
            idTempTL = nhan[0];
            type = 4; // rada
        } else if (Util.isTrue(30, 100)) {
            idTempTL = gang[Util.nextInt(3)];
            type = 2; // gang
        } else if (Util.isTrue(50, 100)) {
            idTempTL = quan[Util.nextInt(3)];
            type = 1; // quan
        } else if (Util.isTrue(70, 100)) {
            idTempTL = ao[Util.nextInt(3)];
            type = 0; // ao
        } else {
            idTempTL = giay[Util.nextInt(3)];
            type = 3; // giay
        }
        int tiLe = Util.nextInt(100, 115);
        List<ItemOption> itemoptions = new ArrayList<>();
        switch (type) {
            case 0 ->
                itemoptions.add(new ItemOption(47, Util.nextInt(800, 900) * tiLe / 100));
            case 1 -> {
                int chiso = Util.nextInt(46000, 49000) * tiLe / 100;
                itemoptions.add(new ItemOption(22, chiso / 1000));
                itemoptions.add(new ItemOption(27, chiso * 125 / 1000));
            }
            case 2 ->
                itemoptions.add(new ItemOption(0, Util.nextInt(4300, 4500) * tiLe / 100));
            case 3 -> {
                int chiso = Util.nextInt(46000, 49000) * tiLe / 100;
                itemoptions.add(new ItemOption(23, chiso / 1000));
                itemoptions.add(new ItemOption(28, chiso * 125 / 1000));
            }
            case 4 ->
                itemoptions.add(new ItemOption(14, Util.nextInt(14, 17) * tiLe / 100));
        }
        if (Util.isTrue(90, 100)) {
            itemoptions.add(new ItemOption(options[Util.nextInt(options.length)], 0));
        }
        itemoptions.add(new ItemOption(21, Util.nextInt(15, 40)));
        ItemMap it = new ItemMap(zone, idTempTL, quantity, x, y, id);
        it.options.clear();
        it.options.addAll(itemoptions);
        return it;
    }

    public long getOptionParamItemShop(short id, int optionId) {
        for (Shop shop : Manager.SHOPS) {
            for (TabShop tabShop : shop.tabShops) {
                for (ItemShop itemShop : tabShop.itemShops) {
                    if (itemShop.temp.id != id) {
                        continue;
                    }
                    for (ItemOption itemOption : itemShop.options) {
                        if (itemOption.optionTemplate.id == optionId) {
                            return itemOption.param;
                        }
                    }
                }
            }
        }
        return -1;
    }
    public Item randomChiSoAn(Item item){
        List<Integer> list = new ArrayList<>();
        for(int i = 0;i < item.itemOptions.size();i++){
            ItemOption io = item.itemOptions.get(i);
            list.add(io.optionTemplate.id);
        }
        int count = 0;
        for(int i = 0;i < item.itemOptions.size();i++){
            ItemOption io = item.itemOptions.get(i);
            if(io.optionTemplate.id == 210){
                item.itemOptions.remove(i);
                long param = io.param;
                int size = ConstItemOption.OPTION_CHI_SO_AN.length;
                while(param > 0 && count < 1000){
                    count++;
                    int id = Util.nextInt(size);
                    if(!list.contains(ConstItemOption.OPTION_CHI_SO_AN[id][0])){
                        int[] tmp = ConstItemOption.OPTION_CHI_SO_AN[id];
                        list.add(ConstItemOption.OPTION_CHI_SO_AN[id][0]);
                        item.addOptionParam(tmp[0], Util.nextInt(tmp[1],tmp[2]),i);
                        param--;
                    }
                }
                break;
            }
        }
        return item;
    }
    public Item randomChiSoAnVIP(Item item){
        List<Integer> list = new ArrayList<>();
        for(int i = 0;i < item.itemOptions.size();i++){
            ItemOption io = item.itemOptions.get(i);
            list.add(io.optionTemplate.id);
        }
        int count = 0;
        for(int i = 0;i < item.itemOptions.size();i++){
            ItemOption io = item.itemOptions.get(i);
            if(io.optionTemplate.id == 237){
                item.itemOptions.remove(i);
                long param = io.param;
                int size = ChisoVIP.OPTION_CHI_SO_vip.length;
                while(param > 0 && count < 1000){
                    count++;
                    int id = Util.nextInt(size);
                    if(!list.contains(ChisoVIP.OPTION_CHI_SO_vip[id][0])){
                        int[] tmp = ChisoVIP.OPTION_CHI_SO_vip[id];
                        list.add(ChisoVIP.OPTION_CHI_SO_vip[id][0]);
                        item.addOptionParam(tmp[0], Util.nextInt(tmp[1],tmp[2]),i);
                        param--;
                    }
                }
                break;
            }
        }
        return item;
    } 
    public Item randomChiSoAnHuyenao(Item item){
        List<Integer> list = new ArrayList<>();
        for(int i = 0;i < item.itemOptions.size();i++){
            ItemOption io = item.itemOptions.get(i);
            list.add(io.optionTemplate.id);
        }
        int count = 0;
        for(int i = 0;i < item.itemOptions.size();i++){
            ItemOption io = item.itemOptions.get(i);
            if(io.optionTemplate.id == 238){
                item.itemOptions.remove(i);
                long param = io.param;
                int size = ChisoHuyenAo.OPTION_CHI_SO_HUYENAO.length;
                while(param > 0 && count < 1000){
                    count++;
                    int id = Util.nextInt(size);
                    if(!list.contains(ChisoHuyenAo.OPTION_CHI_SO_HUYENAO[id][0])){
                        int[] tmp = ChisoHuyenAo.OPTION_CHI_SO_HUYENAO[id];
                        list.add(ChisoHuyenAo.OPTION_CHI_SO_HUYENAO[id][0]);
                        item.addOptionParam(tmp[0], Util.nextInt(tmp[1],tmp[2]),i);
                        param--;
                    }
                }
                break;
            }
        }
        return item;
    }  
    
    
    
    public List<Item.ItemOption> getListOptionItemShop(int level, int gender, int type) {
        List<Item.ItemOption> list = new ArrayList<>();
        Manager.SHOPS.forEach(shop -> shop.tabShops.forEach(tabShop -> tabShop.itemShops.forEach(itemShop -> {
            if (itemShop.temp.level == level && itemShop.temp.gender == gender && itemShop.temp.type == type && list.isEmpty()) {
                for (ItemOption io : itemShop.options) {
                    list.add(new ItemOption(io.optionTemplate.id, io.param));
                }
            }
        })));
        return list;
    }

    public Item getAngelItem(int gender, int type) {
        int tempId = 1048 + type * 3 + gender;
        Item angelItem = createNewItem((short) tempId);
        for (Item.ItemOption io : getListOptionItemShop(14, type == 4 ? 3 : gender, type)) {
            if (io.isOptionCanUpgrade()) {
                int param = (int) (io.param * 1.2);
                angelItem.itemOptions.add(new ItemOption(io.optionTemplate.id, param));
            }
        }
        int param = switch (type) {
            case 0 ->
                62;
            case 1 ->
                66;
            case 2 ->
                70;
            case 3 ->
                64;
            default ->
                68;
        };
        angelItem.itemOptions.add(new ItemOption(21, param));
        angelItem.itemOptions.add(new ItemOption(30, 0));
        return angelItem;
    }

}
