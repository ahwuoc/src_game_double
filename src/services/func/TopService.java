package services.func;

import Top.TOPDAME;
import Top.TopEventManager;
import Top.TopPowerManager;
import Top.TopTaskManager;
import Top.TopVnd;
import consts.ConstSQL;

import java.io.IOException;

import jdbc.DBConnecter;
import player.Player;
import server.Manager;
import network.Message;
import utils.Logger;

import java.sql.Connection;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import jdbc.NDVDB;
import jdbc.daos.NDVSqlFetcher;

import matches.TOP;
import services.TaskService;
import utils.Util;

public class TopService {

    private static TopService instance;

    public static TopService gI() {
        if (instance == null) {
            instance = new TopService();
        }
        return instance;
    }

    public void updateTop() {
        if (Manager.timeRealTop + (10 * 60 * 1000) < System.currentTimeMillis()) {
            Manager.timeRealTop = System.currentTimeMillis();
            try (Connection con = DBConnecter.getConnectionServer()) {
                Manager.topNV = Manager.realTop(ConstSQL.TOP_NV, con);
                Manager.topDC = Manager.realTop(ConstSQL.TOP_DC, con);
                Manager.topVDST = Manager.realTop(ConstSQL.TOP_VDST, con);
                Manager.topWHIS = Manager.realTop(ConstSQL.TOP_WHIS, con);
            } catch (Exception ignored) {
                Logger.error("Lỗi đọc top");
            }
        }
    }

    public void showListTopEvent(Player player) {
        TopEventManager.getInstance().load();
        List<Player> list = TopEventManager.getInstance().getList();
        Message msg = null;
        try {
            msg = new Message(-96);
            msg.writer().writeByte(0);
            msg.writer().writeUTF("Top 100 Sự kiện");
            msg.writer().writeInt(Math.min(100, list.size()));
            for (int i = 0; i < Math.min(100, list.size()); i++) {
                Player top = list.get(i);
                msg.writer().writeInt(i + 1);
                msg.writer().writeInt(i + 1);
                msg.writer().writeShort(top.getHead());
                if (player.getSession().version >= 214) {
                    msg.writer().writeShort(-1);
                }
                msg.writer().writeShort(top.getBody());
                msg.writer().writeShort(top.getLeg());
                msg.writer().writeUTF(top.name);
                msg.writer().writeUTF("Điểm: " + Util.numberFormat(top.inventory.event));
                msg.writer().writeUTF("Thằng này TOP " + (i + 1));
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (IOException e) {
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    public void showListTopPower(Player player) {
        TopPowerManager.getInstance().load();
        List<Player> list = TopPowerManager.getInstance().getList();
        Message msg = null;
        try {
            msg = new Message(-96);
            msg.writer().writeByte(0);
            msg.writer().writeUTF("Top 100");
            msg.writer().writeInt(Math.min(100, list.size()));
            for (int i = 0; i < Math.min(100, list.size()); i++) {
                Player top = list.get(i);
                msg.writer().writeInt(i + 1);
                msg.writer().writeInt(i + 1);
                msg.writer().writeShort(top.getHead());
                if (player.getSession().version >= 214) {
                    msg.writer().writeShort(-1);
                }
                msg.writer().writeShort(top.getBody());
                msg.writer().writeShort(top.getLeg());
                msg.writer().writeUTF(top.name);
                msg.writer().writeUTF("Sức mạnh: " + Util.powerToString(top.nPoint.power));
                msg.writer().writeUTF("Top sức mạnh");
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (IOException e) {
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    public static void showListTopVnd(Player player) {
        TopVnd.getInstance().load();
        List<Player> list = TopVnd.getInstance().getList();
        Message msg = null;
        try {
            msg = new Message(-96);
            msg.writer().writeByte(0);
            msg.writer().writeUTF("Top 100");
            msg.writer().writeInt(Math.min(100, list.size()));
            for (int i = 0; i < Math.min(100, list.size()); i++) {
                Player top = list.get(i);
                msg.writer().writeInt(i + 1);
                msg.writer().writeInt(i + 1);
                msg.writer().writeShort(top.getHead());
                if (player.getSession().version >= 214) {
                    msg.writer().writeShort(-1);
                }
                msg.writer().writeShort(top.getBody());
                msg.writer().writeShort(top.getLeg());
                msg.writer().writeUTF(top.name);
                msg.writer().writeUTF("Top Nạp: " + Util.numberToMoney(top.bktdeptrai));
                msg.writer().writeUTF("...");
            }
            player.sendMessage(msg);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    public void showListTopsucdanh(Player player) {
        TOPDAME.getInstance().load();
        List<Player> list = TOPDAME.getInstance().getList();
        Message msg = null;
        try {
            msg = new Message(-96);
            msg.writer().writeByte(0);
            msg.writer().writeUTF("Top 100");
            msg.writer().writeInt(Math.min(100, list.size()));
            for (int i = 0; i < Math.min(100, list.size()); i++) {
                Player top = list.get(i);
                msg.writer().writeInt(i + 1);
                msg.writer().writeInt(i + 1);
                msg.writer().writeShort(top.getHead());
                if (player.getSession().version >= 214) {
                    msg.writer().writeShort(-1);
                }
                msg.writer().writeShort(top.getBody());
                msg.writer().writeShort(top.getLeg());
                msg.writer().writeUTF(top.name);
                msg.writer().writeUTF("Dame: " + Util.numberFormat(top.nPoint.dame));
                msg.writer().writeUTF("Top Dame");
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (IOException e) {
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    public void showListTopphoban(Player player) {
        TopPowerManager.getInstance().load();
        List<Player> list = TopPowerManager.getInstance().getList();
        Message msg = null;
        try {
            msg = new Message(-96);
            msg.writer().writeByte(0);
            msg.writer().writeUTF("Top 100");
            msg.writer().writeInt(Math.min(100, list.size()));
            for (int i = 0; i < Math.min(100, list.size()); i++) {
                Player top = list.get(i);
                msg.writer().writeInt(i + 1);
                msg.writer().writeInt(i + 1);
                msg.writer().writeShort(top.getHead());
                if (player.getSession().version >= 214) {
                    msg.writer().writeShort(-1);
                }
                msg.writer().writeShort(top.getBody());
                msg.writer().writeShort(top.getLeg());
                msg.writer().writeUTF(top.name);
                msg.writer().writeUTF("Dame: " + Util.numberFormat(top.clan.KhiGasHuyDiet.id));
                msg.writer().writeUTF("Top Dame");
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (IOException e) {
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    public void showListTopTask(Player player) {
        TopTaskManager.getInstance().load();
        List<Player> list = TopTaskManager.getInstance().getList();
        Message msg = null;
        try {
            msg = new Message(-96);
            msg.writer().writeByte(0);
            msg.writer().writeUTF("Top 100");
            msg.writer().writeInt(Math.min(100, list.size()));
            for (int i = 0; i < Math.min(100, list.size()); i++) {
                Player top = list.get(i);
                Player pl = NDVSqlFetcher.loadById(top.id);
                msg.writer().writeInt(i + 1);
                msg.writer().writeInt(i + 1);
                msg.writer().writeShort(top.getHead());

                if (player.getSession().version >= 214) {
                    msg.writer().writeShort(-1);
                }
                msg.writer().writeShort(top.getBody());
                msg.writer().writeShort(top.getLeg());
                msg.writer().writeUTF(top.name);
                msg.writer().writeUTF("[" + pl.playerTask.taskMain.id + "]" + pl.playerTask.taskMain.name);
                Instant instant = Instant.ofEpochMilli(pl.playerTask.taskMain.lastTime);

                // Chuyển đổi sang đối tượng ZonedDateTime theo múi giờ
                ZonedDateTime dateTime = instant.atZone(ZoneId.systemDefault());

                // Định dạng ngày giờ
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

                // Hiển thị kết quả
                String formattedDateTime = dateTime.format(formatter);
                msg.writer().writeUTF("Thời gian hoàn thành: " + formattedDateTime);
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (IOException e) {
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    public static void showListTop(Player player, int select) {
        List<TOP> tops = new ArrayList<>();
        switch (select) {
            case 0 ->
                tops = Manager.topNV;
            case 1 ->
                tops = Manager.topDC;
            case 2 ->
                tops = Manager.topSM;
            case 3 ->
                tops = Manager.topWHIS;
        }
        Message msg = null;
        try {
            msg = new Message(-96);
            msg.writer().writeByte(0);
            msg.writer().writeUTF("Top 100");
            msg.writer().writeInt(tops.size());
            for (int i = 0; i < tops.size(); i++) {
                TOP top = tops.get(i);
                msg.writer().writeInt(i + 1);
                msg.writer().writeInt(i + 1);
                msg.writer().writeShort(top.getHead());
                if (player.getSession().version >= 214) {
                    msg.writer().writeShort(-1);
                }
                msg.writer().writeShort(top.getBody());
                msg.writer().writeShort(top.getLeg());
                msg.writer().writeUTF(top.getName());
                switch (select) {
                    case 0 -> {
                        msg.writer()
                                .writeUTF(TaskService.gI().getTaskMainById(player, top.getNv()).name.substring(0,
                                        TaskService.gI().getTaskMainById(player, top.getNv()).name.length() > 20 ? 20
                                        : TaskService.gI().getTaskMainById(player, top.getNv()).name.length())
                                        + "...");
                        msg.writer().writeUTF(
                                TaskService.gI().getTaskMainById(player, top.getNv()).subTasks.get(top.getSubnv()).name
                                + " - " + getTimeLeft(top.getLasttime()));
                    }
                    case 1 -> {
                        msg.writer().writeUTF("Chơi đồ " + top.getDicanh() + " lần");
                        msg.writer().writeUTF("Gia nhập juventus " + top.getJuventus() + " lần");
                    }
                    case 2 -> {
                        msg.writer().writeUTF(getTimeLeft(top.getLasttime()));
                        msg.writer().writeUTF("...");
                    }
                    case 3 -> {
                        msg.writer().writeUTF("LV:" + top.getLevel() + " với "
                                + Util.FormatNumber(top.getTime() / 1000d) + " giây");
                        msg.writer().writeUTF(getTimeLeft(top.getLasttime()));
                    }
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (IOException e) {
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    public static void showListTopdame(Player player, int select) {
        List<TOP> tops = new ArrayList<>();
        switch (select) {
            case 0 ->
                tops = Manager.topSD;
            case 1 ->
                tops = Manager.topHP;
            case 2 ->
                tops = Manager.topKI;
            case 3 ->
                tops = Manager.topVDST;
        }
        Message msg = null;
        try {
            msg = new Message(-96);
            msg.writer().writeByte(0);
            msg.writer().writeUTF("Top 100");
            msg.writer().writeInt(tops.size());
            for (int i = 0; i < tops.size(); i++) {
                TOP top = tops.get(i);
                msg.writer().writeInt(i + 1);
                msg.writer().writeInt(i + 1);
                msg.writer().writeShort(top.getHead());
                if (player.getSession().version >= 214) {
                    msg.writer().writeShort(-1);
                }
                msg.writer().writeShort(top.getBody());
                msg.writer().writeShort(top.getLeg());
                msg.writer().writeUTF(top.getName());
                switch (select) {
                    case 0 -> {
                        msg.writer()
                                .writeUTF(TaskService.gI().getTaskMainById(player, top.getNv()).name.substring(0,
                                        TaskService.gI().getTaskMainById(player, top.getNv()).name.length() > 20 ? 20
                                        : TaskService.gI().getTaskMainById(player, top.getNv()).name.length())
                                        + "...");
                        msg.writer().writeUTF(
                                TaskService.gI().getTaskMainById(player, top.getNv()).subTasks.get(top.getSubnv()).name
                                + " - " + getTimeLeft(top.getLasttime()));
                    }
                    case 1 -> {
                        msg.writer().writeUTF("Chơi đồ " + top.getDicanh() + " lần");
                        msg.writer().writeUTF("Gia nhập juventus " + top.getJuventus() + " lần");
                    }
                    case 2 -> {
                        msg.writer().writeUTF(getTimeLeft(top.getLasttime()));
                        msg.writer().writeUTF("...");
                    }
                    case 3 -> {
                        msg.writer().writeUTF("LV:" + top.getLevel() + " với "
                                + Util.roundToTwoDecimals(top.getTime() / 1000d) + " giây");
                        msg.writer().writeUTF(getTimeLeft(top.getLasttime()));
                    }
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (IOException e) {
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    public static String getTimeLeft(long lastTime) {
        int secondPassed = (int) ((System.currentTimeMillis() - lastTime) / 1000);
        return secondPassed > 86400 ? (secondPassed / 86400) + " ngày trước"
                : secondPassed > 3600 ? (secondPassed / 3600) + " giờ trước"
                        : secondPassed > 60 ? (secondPassed / 60) + " phút trước" : secondPassed + " giây trước";
    }

}
