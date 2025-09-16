package mob;

import map.Zone;
import player.Player;
import utils.SkillUtil;
import services.Service;
import utils.Util;
import network.Message;

public final class MobMe extends Mob {

    private Player player;
    private final long lastTimeSpawn;
    private final int timeSurvive;

    public MobMe(Player player) {
        super();
        this.player = player;
        this.id = (int) player.id;
        int level = player.playerSkill.getSkillbyId(12).point;
        this.tempId = SkillUtil.getTempMobMe(level);
        this.point.maxHp = SkillUtil.getHPMobMe(player.nPoint.hpMax, level);
        this.point.dame = SkillUtil.getHPMobMe(player.nPoint.getDameAttack(false), level);
        this.point.hp = this.point.maxHp;
        this.zone = player.zone;
        this.lastTimeSpawn = System.currentTimeMillis();
        this.timeSurvive = SkillUtil.getTimeSurviveMobMe(level);
        spawn();
    }

    @Override
    public void update() {
        if (Util.canDoWithTime(lastTimeSpawn, timeSurvive) && this.player.setClothes.pikkoroDaimao != 5) {
            this.mobMeDie();
            this.dispose();
        }
    }

    public void attack(Player pl, Mob mob, boolean miss) {
        Message msg;
        try {
            if (pl != null) {
                double dame = !miss ? this.point.dame : 0;
                if ((pl.nPoint.hp > dame && pl.nPoint.hp > pl.nPoint.hpMax * 0.05) || this.player.setClothes.pikkoroDaimao == 5) {
                    double dameHit = pl.injured(this.player, dame, true, true);
                    for (Player plMap : this.zone.getPlayers()) {
                        msg = new Message(-95);
                        msg.writer().writeByte(2);
                        msg.writer().writeInt(this.id);
                        msg.writer().writeInt((int) pl.id);
                        if (plMap.isClientDoubleMessage()) {
                            msg.writer().writeDouble(dameHit);
                            msg.writer().writeDouble(pl.nPoint.hp);
                        } else {
                            msg.writer().writeDouble((dameHit));
                            msg.writer().writeDouble((pl.nPoint.hp));
                        }
                        plMap.sendMessage(msg);
                        msg.cleanup();
                    }
                }
            }

            if (mob != null) {
                if (mob.point.gethp() > this.point.dame) {
                    double tnsm = mob.getTiemNangForPlayer(this.player, this.point.dame);
                    for (Player plMap : this.zone.getPlayers()) {
                        msg = new Message(-95);
                        msg.writer().writeByte(3);
                        msg.writer().writeInt(this.id);
                        msg.writer().writeInt(mob.id);
                        if (plMap.isClientDoubleMessage()) {
                            msg.writer().writeDouble(mob.point.hp);
                            msg.writer().writeDouble(tnsm);
                        } else {
                            msg.writer().writeInt(Util.maxInt(mob.point.hp));
                            msg.writer().writeInt(Util.maxInt(tnsm));
                        }
                        plMap.sendMessage(msg);
                        msg.cleanup();
                    }
                    Service.gI().addSMTN(player, (byte) 2, tnsm, true);
                }
            }
        } catch (Exception e) {
        }
    }

    //tạo mobme
    public void spawn() {
        Message msg;
        try {
            for (Player plMap : this.zone.getPlayers()) {
                msg = new Message(-95);
                msg.writer().writeByte(0);//type
                msg.writer().writeInt((int) player.id);
                msg.writer().writeShort(this.tempId);
                if (plMap.isClientDoubleMessage()) {
                    msg.writer().writeDouble(this.point.hp);// hp mob
                } else {
                    msg.writer().writeInt(Util.maxInt(this.point.hp));// hp mob
                }
                plMap.sendMessage(msg);
                msg.cleanup();
            }
        } catch (Exception e) {
        }
    }

    public void goToMap(Zone zone) {
        if (zone != null) {
            this.removeMobInMap();
            this.zone = zone;
        }
    }

    //xóa mobme khỏi map
    private void removeMobInMap() {
        Message msg;
        try {
            msg = new Message(-95);
            msg.writer().writeByte(7);//type
            msg.writer().writeInt((int) player.id);
            Service.gI().sendMessAllPlayerInMap(this.player, msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void mobMeDie() {
        Message msg;
        try {
            msg = new Message(-95);
            msg.writer().writeByte(6);//type
            msg.writer().writeInt((int) player.id);
            Service.gI().sendMessAllPlayerInMap(this.player, msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    @Override
    public synchronized void injured(Player plAtt, double damage, boolean dieWhenHpFull) {
        Message msg;
        try {
            if (damage > point.maxHp / 20) {
                damage = point.maxHp / 20;
            }
            point.hp -= damage;
            msg = new Message(-95);
            msg.writer().writeByte(5);//type
            msg.writer().writeInt((int) plAtt.id);
            msg.writer().writeByte(plAtt.playerSkill.skillSelect.template.id); // id skill
            msg.writer().writeInt(id); //mob id
            msg.writer().writeDouble(damage);
            msg.writer().writeDouble(point.hp);
            Service.gI().sendMessAllPlayerInMap(this.player, msg);
            msg.cleanup();
            if (isDie()) {
                mobMeDie();
                dispose();
            }
        } catch (Exception e) {
        }
    }

    public void dispose() {
        player.mobMe = null;
        this.player = null;
    }
}
