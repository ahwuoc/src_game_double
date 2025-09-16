package mob;

import services.InventoryService;
import services.Service;
import services.TaskService;
import services.ItemMapService;
import consts.ConstMap;
import consts.ConstMob;
import consts.ConstTask;
import event.EventManager;
import item.Item;
import map.ItemMap;

import java.util.List;

import map.Zone;
import player.Location;
import player.Pet;
import player.Player;
import network.Message;

import java.io.IOException;

import server.Maintenance;
import server.Manager;
import utils.Util;

import java.util.ArrayList;

import models.Achievement.AchievementService;
import models.Training.TrainingService;
import server.ServerNotify;
import services.ItemService;
import services.MapService;
import skill.Skill;
import utils.TimeUtil;

public class Mob {

    public int id;
    public Zone zone;
    public int tempId;
    public String name;
    public byte level;

    public List<Player> temporaryEnemies = new ArrayList<>();

    public MobPoint point;
    public MobEffectSkill effectSkill;
    public Location location;

    public byte pDame;
    public int pTiemNang;
    private long maxTiemNang;

    public long lastTimeDie;
    public int lvMob = 0;
    public int status = 5;
    public int type = 1;

    private long lastTimeAttackPlayer;
    private long timeAttack = 2000;
    public long lastTimePhucHoi = System.currentTimeMillis();
    public long lastTimeSendEffect = System.currentTimeMillis();

    public Mob(Mob mob) {
        this.point = new MobPoint(this);
        this.effectSkill = new MobEffectSkill(this);
        this.location = new Location();
        this.id = mob.id;
        this.tempId = mob.tempId;
        this.level = mob.level;
        this.point.setHpFull(mob.point.getHpFull());
        this.point.sethp(this.point.getHpFull());
        this.location.x = mob.location.x;
        this.location.y = mob.location.y;
        this.pDame = mob.pDame;
        this.pTiemNang = mob.pTiemNang;
        this.type = mob.type;
        this.setTiemNang();
    }

    public Mob() {
        this.point = new MobPoint(this);
        this.effectSkill = new MobEffectSkill(this);
        this.location = new Location();
    }

    public void setTiemNang() {
        this.maxTiemNang = (long) this.point.getHpFull() * (long) (this.pTiemNang + Util.nextInt(-2, 2)) / 100L;
    }

    public boolean isDie() {
        return this.point.gethp() <= 0;
    }

    public void setDie() {
        this.lastTimePhucHoi = System.currentTimeMillis();
        this.lastTimeDie = System.currentTimeMillis();
    }

    public void addTemporaryEnemies(Player pl) {
        if (pl != null && !temporaryEnemies.contains(pl)) {
            temporaryEnemies.add(pl);
        }
    }

    public void injured(Player plAtt, double damage, boolean dieWhenHpFull) {
        if (!this.isDie()) {
            if (damage >= this.point.hp) {
                damage = this.point.hp;
            }
            if (!dieWhenHpFull) {
                if (this.point.hp == this.point.maxHp && damage >= this.point.hp) {
                    damage = this.point.hp - 1;
                }
                if ((this.tempId == ConstMob.MOC_NHAN || this.tempId == ConstMob.BU_NHIN_MA_QUAI) && damage > this.point.maxHp / 10) {
                    damage = this.point.maxHp / 10;
                }
            }
            if (MapService.gI().isMapKhiGasHuyDiet(this.zone.map.mapId)) {
                boolean mob76Die = true;
                for (Mob mob : this.zone.mobs) {
                    if (!mob.isDie() && mob.tempId == ConstMob.CO_MAY_HUY_DIET) {
                        mob76Die = false;
                        break;
                    }
                }
                if (!mob76Die && plAtt != null && plAtt.playerSkill != null && plAtt.playerSkill.skillSelect != null) {
                    switch (plAtt.playerSkill.skillSelect.template.id) {
                        case Skill.LIEN_HOAN, Skill.ANTOMIC, Skill.MASENKO, Skill.KAMEJOKO ->
                            damage = 1;
                    }
                }
            }
            if (!dieWhenHpFull && !isBigBoss() && !MapService.gI().isMapPhoBan(this.zone.map.mapId) && this.lvMob > 0 && plAtt != null && plAtt.charms.tdOaiHung < System.currentTimeMillis()) {
                damage = (long) ((this.point.maxHp <= 20000000 ? this.point.maxHp * 10 : 2000000000) * (10.0 / 100));
                this.mobAttackPlayer(plAtt);
            }
            if (plAtt != null && plAtt.isBoss && this.tempId > 0 && Util.isTrue(1, 2) && Util.canDoWithTime(lastTimeAttackPlayer, 2500)) {
                this.mobAttackPlayer(plAtt);
                lastTimeAttackPlayer = System.currentTimeMillis();
            }

            this.point.hp -= damage;
            addTemporaryEnemies(plAtt);
            if (this.isDie()) {
                this.status = 0;
                this.setDie();
                this.temporaryEnemies.clear();
                if (plAtt != null) {
                    this.sendMobDieAffterAttacked(plAtt, damage);
                    TaskService.gI().checkDoneTaskKillMob(plAtt, this);
                    TaskService.gI().checkDoneSideTaskKillMob(plAtt, this);
                    TaskService.gI().checkDoneClanTaskKillMob(plAtt, this);
                    AchievementService.gI().checkDoneTaskKillMob(plAtt, this);
                }
                if (this.id == 13) {
                    this.zone.isbulon1Alive = false;
                }
                if (this.id == 14) {
                    this.zone.isbulon2Alive = false;
                }
            } else {
                this.sendMobStillAliveAffterAttacked(damage, plAtt != null ? (plAtt.nPoint != null && plAtt.nPoint.isCrit) : false);
            }
            if (plAtt != null) {
                if (plAtt.isPl() && plAtt.satellite != null && plAtt.satellite.isDefend) {
                    plAtt.satellite.isDefend = false;
                }
                Service.gI().addSMTN(plAtt, (byte) 2, getTiemNangForPlayer(plAtt, damage), true);
                TrainingService.gI().tangTnsmLuyenTap(plAtt, getTiemNangForPlayer(plAtt, damage));
            }
        }
    }

    public double getTiemNangForPlayer(Player pl, double dame) {
        int levelPlayer = Service.gI().getCurrLevel(pl);
        int n = levelPlayer - this.level;
        if (pl.zone != null && MapService.gI().isMapBanDoKhoBau(pl.zone.map.mapId)) {
            n = 0;
        }
        if (pl.nPoint != null && pl.nPoint.power < 40_000_000_0000L) {
            n = 0;
        }
        double pDameHit = dame * 100 / point.getHpFull();
        double tiemNang = pDameHit * maxTiemNang / 100;
        if (tiemNang <= 0) {
            tiemNang = 1;
        }
        if (n >= 0) {
            for (int i = 0; i < n; i++) {
                double sub = tiemNang * 10 / 100;
                if (sub <= 0) {
                    sub = 1;
                }
                tiemNang -= sub;
            }
        } else {
            for (int i = 0; i < -n; i++) {
                double add = tiemNang * 10 / 100;
                if (add <= 0) {
                    add = 1;
                }
                tiemNang += add;
            }
        }
        if (tiemNang <= 0) {
            tiemNang = 1;
        }
        if (pl.nPoint != null) {
            tiemNang = pl.nPoint.calSucManhTiemNang(tiemNang);
        } else {
            return 0;
        }
        if (pl.zone.map.mapId == 122 || pl.zone.map.mapId == 123 || pl.zone.map.mapId == 124) {
            tiemNang *= 2;
        }
        return tiemNang;
    }

    public void update() {
        if (zone.isGoldenFriezaAlive && TimeUtil.is21H()) {
            if (!isDie()) {
                startDie();
                return;
            }
        }
        if (!this.isDie() && this.tempId == ConstMob.CO_MAY_HUY_DIET && Util.canDoWithTime(lastTimeSendEffect, 1000)) {
            sendEffect(55);
            lastTimeSendEffect = System.currentTimeMillis();
        }

        if (this.isDie() && !Maintenance.isRunning && !isBigBoss()) {
            switch (zone.map.type) {
                case ConstMap.MAP_DOANH_TRAI:
                    if (this.tempId == ConstMob.BULON && this.zone.isTUTAlive && Util.canDoWithTime(lastTimeDie, 10000)) {
                        this.hoiSinh();
                        this.hoiSinhMobPhoBan();
                        if (this.id == 13) {
                            this.zone.isbulon1Alive = true;
                        }
                        if (this.id == 14) {
                            this.zone.isbulon2Alive = true;
                        }
                    }
                    break;
                case ConstMap.MAP_BAN_DO_KHO_BAU:
                    break;
                case ConstMap.MAP_CON_DUONG_RAN_DOC:
                    break;
                case ConstMap.MAP_KHI_GAS_HUY_DIET:
                    break;
                case ConstMap.MAP_TAY_KARIN:
                    break;
                default:
                    if (this.zone.isGoldenFriezaAlive && TimeUtil.is21H()) {
                        return;
                    }
                    if (Util.canDoWithTime(lastTimeDie, 5000)) {
                        this.hoiSinh();
                        this.sendMobHoiSinh();
                    }
                    if (Util.canDoWithTime(lastTimePhucHoi, 30000) && !isDie()) {
                        lastTimePhucHoi = System.currentTimeMillis();
                        double hpMax = this.point.maxHp;
                        if (this.point.hp < hpMax) {
                            hoi_hp(hpMax / 10);
                        } else {
                            this.sendMobHoiSinh();
                        }
                    }
            }
        }

        effectSkill.update();
        attack();
    }

    public boolean isBigBoss() {
        return (this.tempId == ConstMob.HIRUDEGARN
                || this.tempId == ConstMob.VUA_BACH_TUOC
                || this.tempId == ConstMob.ROBOT_BAO_VE
                || this.tempId == ConstMob.GAU_TUONG_CUOP
                || this.tempId == ConstMob.VOI_CHIN_NGA
                || this.tempId == ConstMob.GA_CHIN_CUA
                || this.tempId == ConstMob.NGUA_CHIN_LMAO
                || this.tempId == ConstMob.PIANO);
    }

    public void attack() {
        Player player = getPlayerCanAttack();
        if (!isDie() && !effectSkill.isHaveEffectSkill() && tempId != ConstMob.MOC_NHAN && tempId != ConstMob.BU_NHIN_MA_QUAI && tempId != ConstMob.CO_MAY_HUY_DIET && !this.isBigBoss() && (this.lvMob < 1 || MapService.gI().isMapPhoBan(this.zone.map.mapId)) && Util.canDoWithTime(lastTimeAttackPlayer, timeAttack)) {
            if (player != null) {
                this.mobAttackPlayer(player);
            }
            this.lastTimeAttackPlayer = System.currentTimeMillis();
        }
    }

    public Player getPlayerCanAttack() {
        Player plAttack = getFirstPlayerCanAttack();
        if (plAttack != null) {
            return plAttack;
        }
        int distance = 100;
        try {
            List<Player> players = this.zone.getNotBosses();
            for (Player pl : players) {
                if (!pl.isDie() && !pl.isBoss && !pl.isNewPet && (pl.satellite == null || !pl.satellite.isDefend) && (pl.effectSkin == null || !pl.effectSkin.isVoHinh) && (this.tempId > 18 || (this.tempId > 9 && this.type == 4)) || isBigBoss()) {
                    int dis = Util.getDistance(pl, this);
                    if (dis <= distance || isBigBoss()) {
                        plAttack = pl;
                        distance = dis;
                    }
                }
            }
            this.timeAttack = 2000;
        } catch (Exception e) {

        }
        return plAttack;
    }

    private Player getFirstPlayerCanAttack() {
        Player plAtt = null;
        try {
            List<Player> playersMap = zone.getHumanoids();
            int dis = 300;
            if (playersMap != null) {
                for (Player plAttt : playersMap) {
                    if (plAttt.isDie() || plAttt.isBoss || (plAttt.satellite != null && plAttt.satellite.isDefend) || (plAttt.effectSkin != null && plAttt.effectSkin.isVoHinh) || !this.temporaryEnemies.contains(plAttt)) {
                        continue;
                    }
                    int d = Util.getDistance(plAttt, this);
                    if (d <= dis) {
                        dis = d;
                        plAtt = plAttt;
                    }
                }
            }
            this.timeAttack = 1000;
        } catch (Exception e) {

        }
        return plAtt;
    }

    private void mobAttackPlayer(Player player) {
        double dameMob = this.point.getDameAttack();
        if (player.charms != null && player.charms.tdDaTrau > System.currentTimeMillis()) {
            dameMob /= 2;
        }
        if (player.isPet && ((Pet) player).master.charms != null && ((Pet) player).master.charms.tdDeTu > System.currentTimeMillis()) {
            dameMob /= 2;
        }
        if (this.lvMob > 0 && !MapService.gI().isMapPhoBan(this.zone.map.mapId)) {
            dameMob = (long) (player.nPoint.hpMax * (10.0 / 100));
        }
        if (player.satellite != null && player.satellite.isDefend) {
            dameMob -= dameMob / 5;
        }
        if (player.itemTime != null && player.itemTime.isUseCMS) {
            dameMob = Math.round(dameMob * 0.1);
        }
        if (this.lvMob > 0 && player.charms.tdOaiHung > System.currentTimeMillis()) {
            dameMob = 0;
        }
        double dame = player.injured(null, dameMob, false, true);

        this.sendMobAttackMe(player, dame);
        this.sendMobAttackPlayer(player);
        this.phanSatThuong(player, dame);
    }

    private void sendMobAttackMe(Player player, double dame) {
        if (!player.isPet && !player.isNewPet) {
            Message msg;
            try {
                msg = new Message(-11);
                msg.writer().writeByte(this.id);
                if (player.isClientDoubleMessage()) {
                    msg.writer().writeDouble(dame); //dame
                } else {
                    msg.writer().writeInt(Util.maxInt(dame)); //dame
                }
                player.sendMessage(msg);
                msg.cleanup();
            } catch (Exception e) {
            }
        }
    }

    private void sendMobAttackPlayer(Player player) {
        Message msg;
        try {
            for (Player plMap : this.zone.getPlayers()) {
                msg = new Message(-10);
                msg.writer().writeByte(this.id);
                msg.writer().writeInt((int) player.id);
                if (plMap.isClientDoubleMessage()) {
                    msg.writer().writeDouble(player.nPoint.hp);
                } else {
                    msg.writer().writeInt(Util.maxInt(player.nPoint.hp));
                }
                plMap.sendMessage(msg);
                msg.cleanup();
            }
        } catch (Exception e) {
        }
    }

    public void hoiSinh() {
        this.status = 5;
        this.point.hp = this.point.maxHp;
        this.setTiemNang();
    }

    public int lvMob() {
        for (Mob mobMap : this.zone.mobs) {
            if (mobMap.lvMob > 0) {
                return 0;
            }
        }
        this.lvMob = this.tempId > 18 && !isBigBoss() ? Util.isTrue(10, 100) ? 1 : 0 : 0;
        this.point.hp = this.lvMob > 0 ? this.point.maxHp <= 20000000 ? this.point.maxHp * 10 : 2000000000 : this.point.maxHp;
        return this.lvMob;
    }

    public void sendMobHoiSinh() {
        Message msg = null;
        try {
            for (Player plMap : this.zone.getPlayers()) {
                msg = new Message(-13);
                msg.writer().writeByte(this.id);
                msg.writer().writeByte(tempId);
                msg.writer().writeByte(lvMob);
                if (plMap.isClientDoubleMessage()) {
                    msg.writer().writeDouble(point.hp);
                } else {
                    msg.writer().writeInt(Util.maxInt(point.hp));
                }
                plMap.sendMessage(msg);
                msg.cleanup();
            }
//            this.sendMobMaxHp(this.point.hp);
        } catch (Exception e) {
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    public void hoi_hp(double hp) {
        Message msg = null;
        try {
            this.point.sethp(this.point.gethp() + hp);
            double hpz = hp > 0 ? 1 : Math.abs(hp);
            for (Player pl : this.zone.getPlayers()) {
                if (pl.isClientDoubleMessage()) {
                    msg = new Message(-9);
                    msg.writer().writeByte(this.id);
                    msg.writer().writeDouble(this.point.gethp());
                    msg.writer().writeDouble(hpz);
                    msg.writer().writeBoolean(false);
                    msg.writer().writeByte(-1);
                } else {
                    msg = new Message(-9);
                    msg.writer().writeByte(this.id);
                    msg.writer().writeDouble((this.point.gethp()));
                    msg.writer().writeInt(Util.maxInt(hpz));
                    msg.writer().writeBoolean(false);
                    msg.writer().writeByte(-1);
                }
                pl.sendMessage(msg);
                msg.cleanup();
            }
        } catch (Exception e) {
        } finally {
            if (msg != null) {
                msg.cleanup();
                msg = null;
            }
        }
    }

    public void sendEffect(int Effect) {
        Message msg = null;
        try {
            for (Player pl : this.zone.getPlayers()) {
                if (pl.isClientDoubleMessage()) {
                    msg = new Message(-9);
                    msg.writer().writeByte(this.id);
                    msg.writer().writeDouble(this.point.gethp());
                    msg.writer().writeDouble(this.point.gethp());
                    msg.writer().writeBoolean(false);
                    msg.writer().writeByte(Effect);
                } else {
                    msg = new Message(-9);
                    msg.writer().writeByte(this.id);
                    msg.writer().writeDouble((this.point.gethp()));
                    msg.writer().writeDouble((this.point.gethp()));
                    msg.writer().writeBoolean(false);
                    msg.writer().writeByte(Effect);
                }
                pl.sendMessage(msg);
                msg.cleanup();
            }
        } catch (Exception e) {
        } finally {
            if (msg != null) {
                msg.cleanup();
                msg = null;
            }
        }
    }

    private void sendMobDieAffterAttacked(Player plKill, double dameHit) {
        Message msg;
        try {
            for (Player plMap : this.zone.getPlayers()) {
                msg = new Message(-12);
                msg.writer().writeByte(this.id);
                if (plMap.isClientDoubleMessage()) {
                    msg.writer().writeDouble(dameHit);
                } else {
                    msg.writer().writeDouble((dameHit));
                }
                msg.writer().writeBoolean(plKill.nPoint.isCrit); // crit
                plMap.sendMessage(msg);
                msg.cleanup();
            }
            getItemMobReward(plKill, this.location.x + Util.nextInt(-10, 10), this.zone.map.yPhysicInTop(this.location.x, this.location.y)).forEach(itemMap -> {
                Service.gI().dropItemMap(this.zone, itemMap);
                hutItem(plKill, itemMap);
            });
        } catch (Exception e) {
        }
    }

    private void hutItem(Player player, ItemMap itemMap) {
        if (!player.isPet && !player.isNewPet) {
            if (player.charms.tdThuHut > System.currentTimeMillis()) {
                ItemMapService.gI().pickItem(player, itemMap.itemMapId, true);
            }
        } else {
            if (((Pet) player).master.charms.tdThuHut > System.currentTimeMillis()) {
                ItemMapService.gI().pickItem(((Pet) player).master, itemMap.itemMapId, true);
            }
        }
    }

    private List<ItemMap> mobReward(Player player, ItemMap itemTask, Message msg) {
        List<ItemMap> itemReward = new ArrayList<>();
        try {
            itemReward = this.getItemMobReward(player, this.location.x + Util.nextInt(-10, 10),
                    this.zone.map.yPhysicInTop(this.location.x, this.location.y));
            if (itemTask != null) {
                itemReward.add(itemTask);
            }
            msg.writer().writeByte(itemReward.size()); //sl item roi
            for (ItemMap itemMap : itemReward) {
                msg.writer().writeShort(itemMap.itemMapId);// itemmapid
                msg.writer().writeShort(itemMap.itemTemplate.id); // id item
                msg.writer().writeShort(itemMap.x); // xend item
                msg.writer().writeShort(itemMap.y); // yend item
                msg.writer().writeInt((int) itemMap.playerId); // id nhan vat
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return itemReward;
    }

    public List<ItemMap> getItemMobReward(Player player, int x, int yEnd) {
        List<ItemMap> list = new ArrayList<>();
        if (player.isBoss) {
            return list;
        }

        if (player.isPl() && Util.isTrue(1, 10000) && this.tempId == 0) {
            short itTemp = (short) ItemService.gI().randTempItemKichHoat(player.gender);
            ItemMap it = new ItemMap(zone, itTemp, 1, x, yEnd, player.id);
            List<Item.ItemOption> ops = ItemService.gI().getListOptionItemShop(itTemp);
            if (!ops.isEmpty()) {
                it.options = ops;
            }
            it.options.add(new Item.ItemOption(210, 0));
            it.options.add(new Item.ItemOption(216, 0));
            it.options.add(new Item.ItemOption(30, 0));
            list.add(it);
        }
        if (this.tempId == 0) {
            return list;
        }
        int mapid = player.zone.map.mapId;

        if (EventManager.CHRISTMAS) {
            Player pl = player;
            if (pl.isPet) {
                pl = ((Pet) pl).master;
            }
            if (pl.isPet) {
                pl = ((Pet) pl).master;
            }
            if (Util.isTrue(1, 50)) {
                if (pl.itemEvent != null && pl.itemEvent.canDropTatVoGiangSinh(100)) {
                    list.add(new ItemMap(zone, 649, 1, x, yEnd, player.id));
                }
            }
        }
        if (mapid == 5 || mapid == 13) {
            Player pl = player;
            if (pl.isPet) {
                pl = ((Pet) pl).master;
            }
            if (pl.isPet) {
                pl = ((Pet) pl).master;
            }
            if (Util.isTrue(1, 500)) {
                if (pl.itemEvent != null && pl.itemEvent.canDropBinhNuoc(100)) {
                    list.add(new ItemMap(zone, 456, 1, x, yEnd, pl.id));
                }
            }
        }
        if (EventManager.INTERNATIONAL_WOMANS_DAY) {
            Player pl = player;
            if (pl.isPet) {
                pl = ((Pet) pl).master;
            }
            if (pl.isPet) {
                pl = ((Pet) pl).master;
            }
            if (Util.isTrue(1, 50)) {
                if (pl.itemEvent != null && pl.itemEvent.canDropHoaHong(100)) {
                    list.add(new ItemMap(zone, 610, 1, x, yEnd, player.id));
                }
            }
        }
        if (Util.isTrue(1, 100)) {
            list.add(new ItemMap(zone, 18, 1, x, yEnd, player.id));
        }
        if (Util.isTrue(1, 100)) {
            list.add(new ItemMap(zone, 19, 1, x, yEnd, player.id));
        }
        if (Util.isTrue(1, 100)) {
            list.add(new ItemMap(zone, 20, 1, x, yEnd, player.id));
        }
        if (EventManager.HALLOWEEN) {
            if (MapService.gI().isMapEventHalloween(mapid)) {
                if (Util.isTrue(1, 50)) {
                    list.add(new ItemMap(zone, 707, 1, x, yEnd, player.id));
                } else if (Util.isTrue(1, 50)) {
                    list.add(new ItemMap(zone, 708, 1, x, yEnd, player.id));
                }
            }
        }
        if (EventManager.GIAI_CUU_NHAN_GIOI) {
            if (Util.isTrue(1, 100)) {
                list.add(new ItemMap(zone, 1536, 1, x, yEnd, player.id));
            } else if (Util.isTrue(1, 50)) {
                list.add(new ItemMap(zone, 1525, 1, x, yEnd, player.id));
            }
        }

        if (player.itemTime.isUseMayDo && (Util.isTrue(15, 100) || (player.actived() && Util.isTrue(1, 10))) && this.tempId > 57 && this.tempId < 66) {
            list.add(new ItemMap(zone, 380, 1, x, yEnd, player.id));
        }
        if (player.itemTime.isUseMayDo2 && Util.isTrue(15, 100) && this.tempId > 80 && this.tempId < 81) {
            list.add(new ItemMap(zone, 1110, 1, x, yEnd, player.id));
        }

        if (player.isPl() && TaskService.gI().getIdTask(player) == ConstTask.TASK_8_1) {
            if (player.gender == 0 && this.tempId == 11 || player.gender == 1 && this.tempId == 12 || player.gender == 2 && this.tempId == 10) {
                list.add(new ItemMap(zone, 20, 1, x, yEnd, player.id));
            }
        }

        if (MapService.gI().isMapPhoBan(mapid) && this.tempId != 22) {

        }

        if (MapService.gI().isMapUpPorata(mapid)) {
            if (Util.isTrue(70, 100)) {
                ItemMap it = new ItemMap(zone, 934, Util.nextInt(1, 10), x, yEnd, player.id);
                it.options.add(new Item.ItemOption(30, 0));
                list.add(it);
            } else if (Util.isTrue(150, 200)) {
                ItemMap it = new ItemMap(zone, 935, Util.nextInt(1, 10), x, yEnd, player.id);
                it.options.add(new Item.ItemOption(30, 0));
                list.add(it);
            } else if (Util.isTrue(8, 10)) {
                ItemMap it = new ItemMap(zone, 933, 1, x, yEnd, player.id);
                it.options.add(new Item.ItemOption(31, Util.nextInt(1, 100)));
                it.options.add(new Item.ItemOption(30, 0));
                list.add(it);
            }
        }

        // Vang roi
        if (Util.isTrue(10, 100) || (Manager.TEST && Util.isTrue(1, 10)) || (player.actived() && Util.isTrue(1, 15))) {
            int vang = Util.nextInt(10000, 1000000);
            if (vang < 10000) {
                list.add(new ItemMap(zone, 189, vang, x, yEnd, player.id));
            } else if (vang < 200000) {
                list.add(new ItemMap(zone, 188, vang, x, yEnd, player.id));
            } else {
                list.add(new ItemMap(zone, 190, vang, x, yEnd, player.id));
            }
        }

        //Set kich hoat
        if (((player.actived() && Util.isTrue(1, 1000) && player.isNewMember) || (Manager.TEST && Util.isTrue(5, 100000)) || Util.isTrue(1, 1000)) && MapService.gI().isMapUpSKH(mapid)) {
            short itTemp = (short) ItemService.gI().randTempItemKichHoat(player.gender);
            ItemMap it = new ItemMap(zone, itTemp, 1, x, yEnd, player.id);
            List<Item.ItemOption> ops = ItemService.gI().getListOptionItemShop(itTemp);
            if (!ops.isEmpty()) {
                it.options = ops;
            }
            int[] opsrand = ItemService.gI().randOptionItemKichHoat(player.gender);
            it.options.add(new Item.ItemOption(opsrand[0], 0));
           // it.options.add(new Item.ItemOption(opsrand[1], 0));
            it.options.add(new Item.ItemOption(30, 0));
            list.add(it);
        }

     
        
        
        
        
        
        
        //Sao pha le
        if (Util.isTrue(1, 500) || (player.nPoint.isDoSPL && Util.isTrue(5, 100)) || (player.actived() && Util.isTrue(1, 200))) {
            int rand = Util.nextInt(0, 6);
            ItemMap it = new ItemMap(zone, 441 + rand, 1, x, yEnd, player.id);
            it.options.add(new Item.ItemOption(95 + rand, (rand == 3 || rand == 4) ? 3 : 5));
            list.add(it);
        }

        if (player.zone.map.mapId >= 122 && player.zone.map.mapId <= 124 ) {
                    if (Util.isTrue(30, 100)) {
                        player.diemfam += Util.nextInt(1, 5);
                        Service.getInstance().sendThongBao(player, "Bạn nhận được Điểm Fam\n Ramdom 1-5 Điểm");
                    }
                } 
        
         if (player.zone.map.mapId >= 135 && player.zone.map.mapId <= 140 ) {
                    if (Util.isTrue(7, 100)) {
                       ItemMap it = new ItemMap(zone, 77, Util.nextInt(1, 100), x, yEnd, player.id);
            it.options.add(new Item.ItemOption(30, 0));
            list.add(it);
                 //       Service.getInstance().sendThongBao(player, "Bạn nhận được " + it.itemTemplate.name + " Số Lượng" + it.quantity);
                    }
                } 
        
          if (player.zone.map.mapId == 49  ) {
                    if (Util.isTrue(1, 100)) {
                       ItemMap it = new ItemMap(zone, 611, Util.nextInt(1, 2), x, yEnd, player.id);
            it.options.add(new Item.ItemOption(30, 0));
            list.add(it);
                      //  Service.getInstance().sendThongBao(player, "Bạn nhận được " + it.itemTemplate.name + " Số Lượng" + it.quantity);
                    }
                } 
        
        
          if (player.zone.map.mapId == 49  ) {
                    if (Util.isTrue(30, 100)) {
                        player.point_event += Util.nextInt(1, 5);
                        Service.getInstance().sendThongBao(player, "Bạn nhận được Điểm Phòng Tập\n Ramdom 1-5 Điểm");
                    }
                } 
          
     // Chỉ áp dụng cho map ID 49
if (player.zone.map.mapId == 49) {
    // Kiểm tra người chơi có đang mặc cải trang ID 1371 không
    Item caiTrang = player.inventory.itemsBody.get(5); // Thường slot 10 là cải trang
    if (caiTrang != null && caiTrang.template.id == 1834) {
        // Nếu có mặc thì mới xét tỉ lệ rơi
        if (Util.isTrue(30,100)) { // 1% tỉ lệ rơi
            ItemMap drop = new ItemMap(zone, 77, Util.nextInt(1, 2), x, yEnd, player.id);
            drop.options.add(new Item.ItemOption(30, 0));
            list.add(drop);

            // Gửi thông báo nếu bạn cần
            // Service.getInstance().sendThongBao(player, "Bạn nhận được " + drop.itemTemplate.name + " số lượng: " + drop.quantity);
        }
    }
}

 if (player.zone.map.mapId == 49) {
    // Kiểm tra người chơi có đang mặc cải trang ID 1371 không
    Item caiTrang = player.inventory.itemsBody.get(5); // Thường slot 10 là cải trang
    if (caiTrang != null && caiTrang.template.id == 1883) {
        // Nếu có mặc thì mới xét tỉ lệ rơi
        if (Util.isTrue(80, 100)) { // 1% tỉ lệ rơi
            ItemMap drop = new ItemMap(zone, 861, 1, x, yEnd, player.id);
            drop.options.add(new Item.ItemOption(30, 0));
            list.add(drop);

            // Gửi thông báo nếu bạn cần
            // Service.getInstance().sendThongBao(player, "Bạn nhận được " + drop.itemTemplate.name + " số lượng: " + drop.quantity);
        }
    }
}
  if (player.zone.map.mapId == 49) {
    // Kiểm tra người chơi có đang mặc cải trang ID 1371 không
    Item caiTrang = player.inventory.itemsBody.get(5); // Thường slot 10 là cải trang
    if (caiTrang != null && caiTrang.template.id == 1883) {
        // Nếu có mặc thì mới xét tỉ lệ rơi
        if (Util.isTrue(80, 100)) { // 1% tỉ lệ rơi
            ItemMap drop = new ItemMap(zone, 861, 1, x, yEnd, player.id);
            drop.options.add(new Item.ItemOption(30, 0));
            list.add(drop);

            // Gửi thông báo nếu bạn cần
            // Service.getInstance().sendThongBao(player, "Bạn nhận được " + drop.itemTemplate.name + " số lượng: " + drop.quantity);
        }
    }
}
 
 
 
 
       
   if (player.zone.map.mapId >= 0) {
    // Kiểm tra người chơi có đang mặc cải trang ID 1371 không
    Item caiTrang = player.inventory.itemsBody.get(5); // Thường slot 10 là cải trang
    if (caiTrang != null && caiTrang.template.id == 1014) {
        // Nếu có mặc thì mới xét tỉ lệ rơi
        if (Util.isTrue(30, 100)) { // 1% tỉ lệ rơi
            ItemMap drop = new ItemMap(zone, 1017, 1, x, yEnd, player.id);
            drop.options.add(new Item.ItemOption(244, 0));
            list.add(drop);

            // Gửi thông báo nếu bạn cần
            // Service.getInstance().sendThongBao(player, "Bạn nhận được " + drop.itemTemplate.name + " số lượng: " + drop.quantity);
        }
    }
}
              
          
        
        
        
        //Da nang cap
        if (Util.isTrue(1, 500) || (Util.isTrue(5, 100) && MapService.gI().isMapTuongLai(mapid)) || (player.actived() && Util.isTrue(1, 200))) {
            int rand = Util.nextInt(0, 4);
            ItemMap it = new ItemMap(zone, 220 + rand, 1, x, yEnd, player.id);
            it.options.add(new Item.ItemOption(71 - rand, 0));
            list.add(it);
        }

        if (MapService.gI().isMapCold(mapid)) {
            if (player.isPet) {
                player = ((Pet) player).master;
            }
            if (player.isPet) {
                player = ((Pet) player).master;
            }
            if (Util.isTrue(1, 50000)) {
                ItemMap it = ItemService.gI().randDoTL(this.zone, 1, x, yEnd, player.id);
                list.add(it);
                ServerNotify.gI().notify(player.name + " vừa nhặt được " + it.itemTemplate.name + " tại " + this.zone.map.mapName + " khu " + this.zone.zoneId);
            }
            if (Util.isTrue(2, 100) && InventoryService.gI().fullSetThan(player)) {
                ItemMap it = new ItemMap(zone, Util.nextInt(663, 667), 1, x, yEnd, player.id);
                it.options.add(new Item.ItemOption(30, 0));
                list.add(it);
            }
        }

        if (MapService.gI().isMapTuongLai(mapid) && ((Util.isTrue(1, 1000) || (player.actived() && Util.isTrue(1, 150)))) && InventoryService.gI().fullSetThan(player)) {
            ItemMap it = new ItemMap(zone, Util.nextInt(663, 667), 1, x, yEnd, player.id);
            it.options.add(new Item.ItemOption(30, 0));
            list.add(it);
        }

        if (Util.isTrue(1, 100000) || (player.actived() && Util.isTrue(1, 50000))) {
            list.add(new ItemMap(zone, 457, 1, x, yEnd, player.id));
        }

        // Manh thien su
        if(MapService.gI().isMapHanhTinhThucVat(mapid)){
            if ((Util.isTrue(1, 100000) || (player.actived() && Util.isTrue(1, 100))) && InventoryService.gI().findItemNTK(player)) {
                list.add(new ItemMap(zone, Util.nextInt(1066, 1070), 1, x, yEnd, player.id));
            }
            if((Util.isTrue(1, 1000) || (player.actived() && Util.isTrue(10, 100))) ){
                list.add(new ItemMap(zone, 1393, 1, x, yEnd, player.id));
            }
        }
        
        
       if(MapService.gI().isMapHanhTinhConCac(mapid)){
            if ((Util.isTrue(1, 100) || (player.actived() && Util.isTrue(1,100)))) {// tỉ lệ sửa ở đây tự sửa
                list.add(new ItemMap(zone, 1150, 1, x, yEnd, player.id));// 1150 la id item 1 la so luong
                 list.add(new ItemMap(zone, 674, 1, x, yEnd, player.id));// 1150 la id item 1 la so luong
            }
        }
       
        if (Util.isTrue(1, 5000) || (player.actived() && Util.isTrue(1, 100))) {
            list.add(new ItemMap(zone, 861, 1, x, yEnd, player.id));
        }
        ItemMap itTask = dropItemTask(player);
        if(itTask != null){
        list.add(itTask);

        }
        return list;
    }

    private ItemMap dropItemTask(Player player) {
        ItemMap itemMap = null;
        switch (tempId) {
            case ConstMob.KHUNG_LONG:
            case ConstMob.LON_LOI:
            case ConstMob.QUY_DAT:
                if (TaskService.gI().getIdTask(player) == ConstTask.TASK_2_0) {
                    itemMap = new ItemMap(zone, 73, 1, location.x, location.y, player.id);
                }
                break;
            case ConstMob.THAN_LAN_ME:
                if (TaskService.gI().getIdTask(player) == ConstTask.TASK_8_1) {
                    if (Util.isTrue(1, 3)) {
                        itemMap = new ItemMap(zone, 20, 1, location.x, location.y, player.id);
                    } else {
                        Service.gI().sendThongBao(player, "Con thằn lằn mẹ này không giữ ngọc, hãy tìm con thằn lằn mẹ khác");
                    }
                }
            case ConstMob.OC_MUON_HON:
                if (TaskService.gI().getIdTask(player) == ConstTask.TASK_14_1) {
                    if (Util.isTrue(1, 3)) {
                        itemMap = new ItemMap(zone, 85, 1, location.x, location.y, player.id);
                    } else {
                        Service.gI().sendThongBao(player, "Con ốc mượn hồn này không giữ truyện tranh, hãy thử tìm con ốc mượn hồn khác");
                    }
                }
            case ConstMob.HEO_XAYDA_ME:
                if (TaskService.gI().getIdTask(player) == ConstTask.TASK_14_1) {
                    if (Util.isTrue(1, 3)) {
                        itemMap = new ItemMap(zone, 85, 1, location.x, location.y, player.id);
                    } else {
                        Service.gI().sendThongBao(player, "Con heo xayda mẹ này không giữ truyện tranh, hãy thử tìm con heo xayda mẹ khác");
                    }
                }
            case ConstMob.OC_SEN:
                if (TaskService.gI().getIdTask(player) == ConstTask.TASK_14_1) {
                    if (Util.isTrue(1, 3)) {
                        itemMap = new ItemMap(zone, 85, 1, location.x, location.y, player.id);
                    } else {
                        Service.gI().sendThongBao(player, "Con ốc xên này không giữ truyện tranh, hãy thử tìm con ốc xên khác");
                    }
                }
        }
        if (itemMap != null) {
            return itemMap;
        }
        return null;
    }

    private void sendMobStillAliveAffterAttacked(double dameHit, boolean crit) {
        Message msg;
        try {
            for (Player plMap : this.zone.getPlayers()) {
                msg = new Message(-9);
                msg.writer().writeByte(this.id);
                if (plMap.isClientDoubleMessage()) {
                    msg.writer().writeDouble(this.point.gethp());
                    msg.writer().writeDouble(dameHit);
                } else {
                    msg.writer().writeDouble((this.point.gethp()));
                    msg.writer().writeDouble((dameHit));
                }
                msg.writer().writeBoolean(crit); // chí mạng
                msg.writer().writeInt(-1);
                plMap.sendMessage(msg);
                msg.cleanup();
            }
        } catch (Exception e) {
        }
    }

    public void hoiSinhMobPhoBan() {
        this.point.hp = this.point.maxHp;
        this.setTiemNang();
        Message msg;
        try {
            msg = new Message(-13);
            msg.writer().writeByte(this.id);
            msg.writer().writeByte(this.tempId);
            msg.writer().writeByte(this.lvMob); //level mob
            msg.writer().writeDouble(this.point.hp);
            Service.gI().sendMessAllPlayerInMap(this.zone, msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void hoiSinhMobTayKarin() {
        this.point.hp = this.point.maxHp;
        this.maxTiemNang = 1;
        Message msg;
        try {
            msg = new Message(-13);
            msg.writer().writeByte(this.id);
            msg.writer().writeByte(this.tempId);
            msg.writer().writeByte(this.lvMob); //level mob
            msg.writer().writeDouble(this.point.hp);
            Service.gI().sendMessAllPlayerInMap(this.zone, msg);
            msg.cleanup();
        } catch (IOException e) {
        }
    }

    public void sendSieuQuai(int type) {
        Message msg;
        try {
            msg = new Message(-75);
            msg.writer().writeByte(this.id);
            msg.writer().writeByte(type);
            Service.gI().sendMessAllPlayerInMap(this.zone, msg);
            msg.cleanup();
        } catch (IOException e) {
        }
    }

    public void sendDisable(boolean bool) {
        Message msg;
        try {
            msg = new Message(81);
            msg.writer().writeByte(this.id);
            msg.writer().writeBoolean(bool);
            Service.gI().sendMessAllPlayerInMap(this.zone, msg);
            msg.cleanup();
        } catch (IOException e) {
        }
    }

    public void sendDoneMove(boolean bool) {
        Message msg;
        try {
            msg = new Message(82);
            msg.writer().writeByte(this.id);
            msg.writer().writeBoolean(bool);
            Service.gI().sendMessAllPlayerInMap(this.zone, msg);
            msg.cleanup();
        } catch (IOException e) {
        }
    }

    public void sendFire(boolean bool) {
        Message msg;
        try {
            msg = new Message(85);
            msg.writer().writeByte(this.id);
            msg.writer().writeBoolean(bool);
            Service.gI().sendMessAllPlayerInMap(this.zone, msg);
            msg.cleanup();
        } catch (IOException e) {
        }
    }

    public void sendIce(boolean bool) {
        Message msg;
        try {
            msg = new Message(86);
            msg.writer().writeByte(this.id);
            msg.writer().writeBoolean(bool);
            Service.gI().sendMessAllPlayerInMap(this.zone, msg);
            msg.cleanup();
        } catch (IOException e) {
        }
    }

    public void sendWind(boolean bool) {
        Message msg;
        try {
            msg = new Message(87);
            msg.writer().writeByte(this.id);
            msg.writer().writeBoolean(bool);
            Service.gI().sendMessAllPlayerInMap(this.zone, msg);
            msg.cleanup();
        } catch (IOException e) {
        }
    }

    public void sendMobMaxHp(long maxHp) {
        Message msg;
        try {
            for (Player pl : this.zone.getPlayers()) {
                if (pl.isClientDoubleMessage()) {
                    msg = new Message(87);
                    msg.writer().writeByte(this.id);
                    msg.writer().writeLong(maxHp); //fix -> bool
                } else {
                    msg = new Message(87);
                    msg.writer().writeByte(this.id);
                    msg.writer().writeInt(Util.maxInt(maxHp)); //fix -> bool
                }
                pl.sendMessage(msg);
                msg.cleanup();
            }
        } catch (IOException e) {
        }
    }

    private void phanSatThuong(Player plTarget, double dame) {
        if (plTarget.nPoint == null) {
            return;
        }
        int percentPST = plTarget.nPoint.tlPST;
        if (percentPST != 0) {
            double damePST = (dame * percentPST / 100L);
            if (damePST >= this.point.hp) {
                damePST = this.point.hp - 1;
            }
            injured(null, damePST, true);
            double hpMob = this.point.hp;
            damePST = hpMob - this.point.hp;
            Message msg;
            try {
                for (Player pl : plTarget.zone.getPlayers()) {
                    if (pl.isClientDoubleMessage()) {
                        msg = new Message(-9);
                        msg.writer().writeByte(this.id);
                        msg.writer().writeDouble(this.point.hp);
                        msg.writer().writeDouble(damePST);
                        msg.writer().writeBoolean(false);
                        msg.writer().writeByte(36);
                    } else {
                        msg = new Message(-9);
                        msg.writer().writeByte(this.id);
                        msg.writer().writeInt(Util.maxInt(this.point.hp));
                        msg.writer().writeInt(Util.maxInt(damePST));
                        msg.writer().writeBoolean(false);
                        msg.writer().writeByte(36);
                    }
                    pl.sendMessage(msg);
                    msg.cleanup();
                }
            } catch (IOException e) {
            }
        }
    }

    public void startDie() {
        Message msg;
        try {
            setDie();
            this.point.hp = -1;
            this.status = 0;
            msg = new Message(-12);
            msg.writer().writeByte(this.id);
            Service.gI().sendMessAllPlayerInMap(this.zone, msg);
            msg.cleanup();
        } catch (IOException e) {
        }
    }
}
