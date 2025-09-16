/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AutoGiftTop;

import item.Item;
import item.Item.ItemOption;
import java.sql.Timestamp;
import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import jdbc.DBConnecter;
import server.Manager;
import services.InventoryService;
import services.ItemService;
import utils.Logger;
import utils.TimeUtil;
import utils.Util;

/**
 *
 * @author Son
 */
public class AutoGiftTopService {

    public static final ArrayList<AutoGiftTop> listTopAuto = new ArrayList<>();
    public static boolean isRunning;
    public static StringBuilder textInfo = new StringBuilder();

    private static AutoGiftTopService I;

    public static AutoGiftTopService gI() {
        if (I == null) {
            I = new AutoGiftTopService();
            I.activeAutoGiftTop();
        }
        return I;
    }

    public AutoGiftTop getTop(int id) {
        return listTopAuto.get(id);
    }

    public static void loadListAutoTop(Connection con) {
        try (PreparedStatement ps = con.prepareStatement("SELECT * FROM auto_gift_top"); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                AutoGiftTop autoTop = new AutoGiftTop();
                autoTop.id = rs.getInt("id_top");
                autoTop.name = rs.getString("name_top");
                autoTop.timeReward = rs.getTimestamp("date_time");
                autoTop.isReceive = Byte.parseByte(rs.getString("is_receive")) != 0;
                JSONArray players = (JSONArray) JSONValue.parse(rs.getString("user_receive"));
                if (players != null) {
                    for (int i = 0; i < players.size(); i++) {
                        String arrPlayer[] = players.get(i).toString().split("#");
                        autoTop.listReceive.put(Integer.valueOf(arrPlayer[0].replace("pId:", "")), arrPlayer[1].replace("tLog:", ""));
                    }
                    players.clear();
                }
                listTopAuto.add(autoTop);
            }
            for (int topId = 0; topId < listTopAuto.size(); topId++) {
                for (int i = 0; i < 10; i++) {
                    addItemTop(topId, i);
                }
                //print info item gift
                getInfoItemGift(topId);
            }
        } catch (SQLException e) {
            Logger.logException(AutoGiftTopService.class, e);
        }
        Logger.logln("AUTOGIFTTOP [" + listTopAuto.size() + "]");
    }

    public static void getInfoItemGift(int topId) {
        //print info item gift
        textInfo.append("|7|>>>>>>>>>>>>>>[").append(listTopAuto.get(topId).name).append("]<<<<<<<<<<<<<<<\n").append("|3|Ngày kết thúc: ").append(TimeUtil.formatTime(listTopAuto.get(topId).timeReward, "dd/MM/yyyy HH:mm:ss")).append("\n");
        for (Integer top : listTopAuto.get(topId).items.keySet()) {
            textInfo.append("|-1|TOP ").append(top + 1).append("\n|3|");
            for (int j = 0; j < listTopAuto.get(topId).items.get(top).size(); j++) {
                textInfo.append(listTopAuto.get(topId).items.get(top).get(j).template.name).append(" x").append(Util.format(listTopAuto.get(topId).items.get(top).get(j).quantity)).append("\n");
            }
        }
        textInfo.append("\n");
    }

    private void activeAutoGiftTop() {
        new Thread(() -> {
            new Timer("Auto Gift Item Top").schedule(new TimerTask() {
                @Override
                public void run() {
                    boolean isLoadTop = false;
                    isRunning = true;
                    for (AutoGiftTop autoTop : listTopAuto) {
                        if (autoTop.isToDate() && !autoTop.isReceive && !autoTop.items.isEmpty()) {
                            if (!isLoadTop) {
                                Manager.reloadtop();
                                isLoadTop = true;
                            }
                            System.err.println("Update Auto Trao Quà " + autoTop.name + " Running.........");
                            try {
                                //top 1-10
                                for (int i = 0; i < 10; i++) {
                                    switch (autoTop.id) {
                                        case 0 -> {
                                            try {
                                                if (autoTop.isNonReceive(Manager.Topvithu.get(i).getId_player())) {
                                                    InventoryService.gI().addItemMail(i, autoTop, Manager.Topvithu.get(i).getId_player());
                                                }
                                            } catch (Exception e) {
                                            }
                                        }
                                        case 1 -> {
                                            try {
                                                if (autoTop.isNonReceive(Manager.Topsanboss.get(i).getId_player())) {
                                                    InventoryService.gI().addItemMail(i, autoTop, Manager.Topsanboss.get(i).getId_player());
                                                }
                                            } catch (Exception e) {
                                            }
                                        }

                                    }
                                }
                                autoTop.update();
                                System.err.println("Update Auto Trao Quà " + autoTop.name + " Completed.........");
                            } catch (Exception e) {
                                Logger.logException(AutoGiftTopService.class, e);
                            }
                        }
                    }
                    isRunning = false;
                }
            }, listTopAuto.get(0).timeReward);
            System.err.println("Thread Update Auto Gift Item Top Running.........");
        }).start();
    }

    private static void addItemTop(int topId, int top) {
        ArrayList<Item> items = new ArrayList<>();
        Item item;
        //Top Nạp
        switch (topId) {
            case 0 -> {
                switch (top) {
                    case 0 -> {
                        //Thỏi vàng
                        item = ItemService.gI().createNewItem(1198, 1);
                        item.itemOptions.add(new ItemOption(50,99));
                        item.itemOptions.add(new ItemOption(77, 99));
                        item.itemOptions.add(new ItemOption(103, 99));
                        item.itemOptions.add(new ItemOption(237, 5));
                        item.itemOptions.add(new ItemOption(30, 1));
                        items.add(item);
                        //Item
                        item = ItemService.gI().createNewItem(1217,99);
                        item.itemOptions.add(new ItemOption(244, 1));
                        items.add(item);
                        
                      
                    }
                    case 1 -> {
                         item = ItemService.gI().createNewItem(1198, 1);
                        item.itemOptions.add(new ItemOption(50,88));
                        item.itemOptions.add(new ItemOption(77, 88));
                        item.itemOptions.add(new ItemOption(103, 88));
                        item.itemOptions.add(new ItemOption(237, 5));
                        item.itemOptions.add(new ItemOption(30, 1));
                        items.add(item);
                        //Item
                        item = ItemService.gI().createNewItem(1217,88);
                        item.itemOptions.add(new ItemOption(244, 1));
                        items.add(item);
                    }
                    case 2 -> {
                         item = ItemService.gI().createNewItem(1198, 1);
                        item.itemOptions.add(new ItemOption(50,77));
                        item.itemOptions.add(new ItemOption(77, 77));
                        item.itemOptions.add(new ItemOption(103, 77));
                        item.itemOptions.add(new ItemOption(237, 5));
                        item.itemOptions.add(new ItemOption(30, 1));
                        items.add(item);
                        //Item
                        item = ItemService.gI().createNewItem(1217,66);
                        item.itemOptions.add(new ItemOption(244, 1));
                        items.add(item);
                    }
                    case 3 -> {
                         item = ItemService.gI().createNewItem(1198, 1);
                        item.itemOptions.add(new ItemOption(50,66));
                        item.itemOptions.add(new ItemOption(77, 66));
                        item.itemOptions.add(new ItemOption(103, 66));
                        item.itemOptions.add(new ItemOption(237, 5));
                        item.itemOptions.add(new ItemOption(30, 1));
                        items.add(item);
                        //Item
                        item = ItemService.gI().createNewItem(1217,55);
                        item.itemOptions.add(new ItemOption(244, 1));
                        items.add(item);
                    }
                    case 4 -> {
                      
                        //Item
                        item = ItemService.gI().createNewItem(1150,200000);
                        item.itemOptions.add(new ItemOption(244, 1));
                        items.add(item);
                    }
                    case 5 -> {
                       item = ItemService.gI().createNewItem(1150,200000);
                        item.itemOptions.add(new ItemOption(244, 1));
                        items.add(item);
                    }
                    case 6 -> {
                        item = ItemService.gI().createNewItem(1150,200000);
                        item.itemOptions.add(new ItemOption(244, 1));
                        items.add(item);
                    }
                    case 7 -> {
                       item = ItemService.gI().createNewItem(1150,200000);
                        item.itemOptions.add(new ItemOption(244, 1));
                        items.add(item);
                    }
                    case 8 -> {
                       item = ItemService.gI().createNewItem(1150,200000);
                        item.itemOptions.add(new ItemOption(244, 1));
                        items.add(item);
                    }
                    case 9 -> {
                        item = ItemService.gI().createNewItem(1150,200000);
                        item.itemOptions.add(new ItemOption(244, 1));
                        items.add(item);
                    }
                    default -> {
                    }
                }
                listTopAuto.get(topId).items.put(top, new ArrayList<>(items));
                items.clear();
            }
            case 1 -> {
                //Top Sức Mạnh
                switch (top) {
                    case 0 -> {
                        item = ItemService.gI().createNewItem(1012, 1);
                        item.itemOptions.add(new ItemOption(50,90));
                        item.itemOptions.add(new ItemOption(77, 90));
                        item.itemOptions.add(new ItemOption(103, 90));
                        item.itemOptions.add(new ItemOption(204, 80));
                        item.itemOptions.add(new ItemOption(237, 5));
                        item.itemOptions.add(new ItemOption(30, 1));
                        items.add(item);
                        //Item
                        item = ItemService.gI().createNewItem(457,55000);
                        item.itemOptions.add(new ItemOption(244, 1));
                        items.add(item);
                    }
                    case 1 -> {
                      item = ItemService.gI().createNewItem(1012, 1);
                        item.itemOptions.add(new ItemOption(50,70));
                        item.itemOptions.add(new ItemOption(77, 70));
                        item.itemOptions.add(new ItemOption(103, 70));
                        item.itemOptions.add(new ItemOption(204, 60));
                        item.itemOptions.add(new ItemOption(237, 5));
                        item.itemOptions.add(new ItemOption(30, 1));
                        items.add(item);
                        //Item
                        item = ItemService.gI().createNewItem(457,55000);
                        item.itemOptions.add(new ItemOption(244, 1));
                        items.add(item);
                    }
                    case 2 -> {
                       item = ItemService.gI().createNewItem(1012, 1);
                        item.itemOptions.add(new ItemOption(50,50));
                        item.itemOptions.add(new ItemOption(77, 50));
                        item.itemOptions.add(new ItemOption(103, 50));
                        item.itemOptions.add(new ItemOption(204, 40));
                        item.itemOptions.add(new ItemOption(237, 5));
                        item.itemOptions.add(new ItemOption(30, 1));
                        items.add(item);
                        //Item
                        item = ItemService.gI().createNewItem(457,55000);
                        item.itemOptions.add(new ItemOption(244, 1));
                        items.add(item);
                    }
                    case 3 -> {
                      item = ItemService.gI().createNewItem(1012, 1);
                        item.itemOptions.add(new ItemOption(50,30));
                        item.itemOptions.add(new ItemOption(77, 30));
                        item.itemOptions.add(new ItemOption(103, 30));
                        item.itemOptions.add(new ItemOption(204, 20));
                        item.itemOptions.add(new ItemOption(237, 5));
                        item.itemOptions.add(new ItemOption(30, 1));
                        items.add(item);
                        //Item
                        item = ItemService.gI().createNewItem(457,55000);
                        item.itemOptions.add(new ItemOption(244, 1));
                        items.add(item);
                    }
                    case 4 -> {
                       
                        item = ItemService.gI().createNewItem(1150, 200000);
                        item.itemOptions.add(new ItemOption(244, 1));
                        items.add(item);
                    }
                    case 5 -> {
                           item = ItemService.gI().createNewItem(1150, 200000);
                        item.itemOptions.add(new ItemOption(244, 1));
                        items.add(item);
                    }
                    case 6 -> {
                           item = ItemService.gI().createNewItem(1150, 200000);
                        item.itemOptions.add(new ItemOption(244, 1));
                        items.add(item);
                    }
                    case 7 -> {
                        item = ItemService.gI().createNewItem(1150, 200000);
                        item.itemOptions.add(new ItemOption(244, 1));
                        items.add(item);
                    }
                    case 8 -> {
                          item = ItemService.gI().createNewItem(1150, 200000);
                        item.itemOptions.add(new ItemOption(244, 1));
                        items.add(item);
                    }
                    case 9 -> {
                         item = ItemService.gI().createNewItem(1150, 200000);
                        item.itemOptions.add(new ItemOption(244, 1));
                        items.add(item);
                    }
                    default -> {
                    }
                }
                listTopAuto.get(topId).items.put(top, new ArrayList<>(items));
                items.clear();
            }

        }
    }

    public static class AutoGiftTop {

        public int id;
        public String name;
        public boolean isReceive;
        public HashMap<Integer, ArrayList<Item>> items = new HashMap<>();
        public HashMap<Integer, String> listReceive = new HashMap<>();
        public Timestamp timeReward;

        public boolean isToDate() {
            return System.currentTimeMillis() > timeReward.getTime();
        }

        private boolean isNonReceive(int id) {
            return listReceive.get(id) == null;
        }

        public void addPlayerReceive(int id) {
            listReceive.put(id, TimeUtil.getTimeNow("HH:mm:ss") + " (" + TimeUtil.getTimeNow("dd-MM-yyyy") + ")");
            JSONArray dataArray = new JSONArray();
            for (Integer i : this.listReceive.keySet()) {
                dataArray.add("pId:" + i + "#tLog:" + this.listReceive.get(i));
            }
            try {
                DBConnecter.executeUpdate("UPDATE auto_gift_top SET user_receive = ? WHERE name_top = ?", dataArray.toJSONString(), name);
            } catch (Exception e) {
                Logger.logException(AutoGiftTopService.class, e);
            } finally {
                dataArray.clear();
            }
        }

        public void update() {
            this.isReceive = true;
            try {
                DBConnecter.executeUpdate("UPDATE auto_gift_top SET is_receive = 1 WHERE name_top = ?", name);
            } catch (Exception e) {
                Logger.logException(AutoGiftTopService.class, e);
            }
        }
    }
}
