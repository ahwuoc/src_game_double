package boss.boss_manifest.Broly;

import boss.Boss;
import boss.BossData;
import boss.BossID;
import boss.BossManager;
import boss.BossStatus;
import boss.BrolyManager;


import consts.ConstPlayer;
import player.Player;
import server.ServerNotify;
import services.SkillService;
import services.func.ChangeMapService;
import skill.Skill;
import utils.SkillUtil;
import utils.Util;

public class broly1 extends Boss {

    public broly1(double... hpz) throws Exception {

        super( BossID.BROLY, new BossData(
                "broly1", // name
                ConstPlayer.XAYDA, // gender
                new short[] { 291, 292, 293, -1, -1, -1 }, // outfit {head, body, leg, bag, aura, eff}
                100, // dame
                new double[] { 1000 }, // hp
                new int[] { 5, 13, 20, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38 }, // map join
                new int[][] {
                        { Skill.TAI_TAO_NANG_LUONG, 1, 1000 }, { Skill.TAI_TAO_NANG_LUONG, 2, 1000 },
                        { Skill.TAI_TAO_NANG_LUONG, 3, 1000 }, { Skill.TAI_TAO_NANG_LUONG, 4, 1000 },
                        { Skill.TAI_TAO_NANG_LUONG, 5, 1000 }, { Skill.TAI_TAO_NANG_LUONG, 6, 1000 },
                        { Skill.TAI_TAO_NANG_LUONG, 7, 1000 },
                        { Skill.DRAGON, 1, 1000 }, { Skill.DRAGON, 2, 1000 }, { Skill.DRAGON, 3, 1000 },
                        { Skill.DRAGON, 4, 1000 }, { Skill.DRAGON, 5, 1000 }, { Skill.DRAGON, 6, 1000 },
                        { Skill.DRAGON, 7, 1000 },
                        { Skill.DEMON, 1, 1000 }, { Skill.DEMON, 2, 1000 }, { Skill.DEMON, 3, 1000 },
                        { Skill.DEMON, 4, 1000 }, { Skill.DEMON, 5, 1000 }, { Skill.DEMON, 6, 1000 },
                        { Skill.DEMON, 7, 1000 },
                        { Skill.GALICK, 1, 1000 }, { Skill.GALICK, 2, 1000 }, { Skill.GALICK, 3, 1000 },
                        { Skill.GALICK, 4, 1000 }, { Skill.GALICK, 5, 1000 }, { Skill.GALICK, 6, 1000 },
                        { Skill.GALICK, 7, 1000 },
                        { Skill.KAMEJOKO, 1, 1000 }, { Skill.KAMEJOKO, 2, 1000 }, { Skill.KAMEJOKO, 3, 1000 },
                        { Skill.KAMEJOKO, 4, 1000 }, { Skill.KAMEJOKO, 5, 1000 }, { Skill.KAMEJOKO, 6, 1000 },
                        { Skill.KAMEJOKO, 7, 1000 },
                        { Skill.MASENKO, 1, 1000 }, { Skill.MASENKO, 2, 1000 }, { Skill.MASENKO, 3, 1000 },
                        { Skill.MASENKO, 4, 1000 }, { Skill.MASENKO, 5, 1000 }, { Skill.MASENKO, 6, 1000 },
                        { Skill.MASENKO, 7, 1000 },
                        { Skill.ANTOMIC, 1, 1000 }, { Skill.ANTOMIC, 2, 1000 }, { Skill.ANTOMIC, 3, 1000 },
                        { Skill.ANTOMIC, 4, 1000 }, { Skill.ANTOMIC, 5, 1000 }, { Skill.ANTOMIC, 6, 1000 },
                        { Skill.ANTOMIC, 7, 1000 }, }, // skill
                new String[] {}, // text chat 1
                new String[] { "|-1|Haha! ta sẽ giết hết các ngươi",
                        "|-1|Sức mạnh của ta là tuyệt đối",
                        "|-1|Vào hết đây!!!", }, // text chat 2
                new String[] { "|-1|Các ngươi giỏi lắm. Ta sẽ quay lại." }, // text chat 3
                600// type appear
        ));
        if (hpz.length > 1) {
            this.nPoint.hpMax = hpz[0];
        }
    }
    public long lastTimeNoti = 0;
    @Override
    public void active() {
        if(System.currentTimeMillis() - lastTimeNoti >= 300000){
            lastTimeNoti = System.currentTimeMillis();
            ServerNotify.gI().notify("BOSS " + this.name + " vừa xuất hiện tại " + this.zone.map.mapName);
        }
        super.active();
    }

    @Override
    public void joinMap() {
        this.name = "Broly " + Util.nextInt(10, 100);
        this.nPoint.hpMax = Util.nextInt(100, 10000);
        this.nPoint.hp = this.nPoint.hpMax;
        this.nPoint.dame = this.nPoint.hpMax / 100;
        this.nPoint.crit = Util.nextInt(50);
        this.joinMap2(); // To change body of generated methods, choose Tools | Templates.
        st = System.currentTimeMillis();
    }

    public void joinMap2() {
        if (this.zone == null) {
            if (this.parentBoss != null) {
                this.zone = parentBoss.zone;
            } else if (this.lastZone == null) {
                this.zone = getMapJoin();
            } else {
                this.zone = this.lastZone;
            }
        }
        if (this.zone != null) {
            try {

                int zoneid = Util.nextInt(2, this.zone.map.zones.size() - 1);

                while (zoneid < this.zone.map.zones.size() && this.zone.map.zones.get(zoneid).getBosses().size() > 0) {
                    zoneid++;
                }

                if (zoneid < this.zone.map.zones.size()) {
                    this.zone = this.zone.map.zones.get(zoneid);
                } else {
                    if (this.id == BossID.broly1) {
                        this.changeStatus(BossStatus.DIE);
                        return;
                    }
                    this.zone = this.zone.map.zones.get(Util.nextInt(2, this.zone.map.zones.size() - 1));
                }

                if (this.zone.zoneId < 2) {
                    this.leaveMap();
                }

                ChangeMapService.gI().changeMap(this, this.zone, -1, -1);
                this.changeStatus(BossStatus.CHAT_S);
            } catch (Exception e) {
                this.changeStatus(BossStatus.REST);
            }
        } else {
            this.changeStatus(BossStatus.RESPAWN);
        }
    }

    private long st;

    @Override
    public synchronized double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 1000)) {
                this.chat("Xí hụt");
                return 0;
            }
            if (Util.isTrue(1, 30)) {
                this.playerSkill.skillSelect = this.playerSkill.skills.get(Util.nextInt(0, 6));
                this.tangChiSo();
                SkillService.gI().useSkill(this, null, null, -1, null);
            }
            damage = this.nPoint.subDameInjureWithDeff(damage);
            if (!piercing && plAtt.playerSkill.skillSelect.template.id != Skill.TU_SAT
                    && damage > this.nPoint.hpMax / 100) {
                damage = this.nPoint.hpMax / 100;
            }
            this.nPoint.subHP(damage);
            if (isDie()) {
                this.setDie(plAtt);
                die(plAtt);
            }
            return damage;
        } else {
            return 0;
        }
    }

    private long lastTimeAttack;

    @Override
    public void attack() {
        if (Util.canDoWithTime(this.lastTimeAttack, 100) && this.typePk == ConstPlayer.PK_ALL) {
            this.lastTimeAttack = System.currentTimeMillis();
            try {
                Player pl = getPlayerAttack();
                if (pl == null || pl.isDie()) {
                    return;
                }
                this.playerSkill.skillSelect = this.playerSkill.skills
                        .get(Util.nextInt(7, this.playerSkill.skills.size() - 1));
                if (Util.getDistance(this, pl) <= this.getRangeCanAttackWithSkillSelect()) {
                    if (Util.isTrue(5, 20)) {
                        if (SkillUtil.isUseSkillChuong(this)) {
                            this.moveTo(pl.location.x + (Util.getOne(-1, 1) * Util.nextInt(20, 200)),
                                    Util.nextInt(10) % 2 == 0 ? pl.location.y : pl.location.y - Util.nextInt(0, 70));
                        } else {
                            this.moveTo(pl.location.x + (Util.getOne(-1, 1) * Util.nextInt(10, 40)),
                                    Util.nextInt(10) % 2 == 0 ? pl.location.y : pl.location.y - Util.nextInt(0, 50));
                        }
                    }
                    if (Util.isTrue(20, 100)) {
                        this.playerSkill.skillSelect = this.playerSkill.skills.get(Util.nextInt(0, 6));
                        this.tangChiSo();
                    }

                    SkillService.gI().useSkill(this, pl, null, -1, null);
                    checkPlayerDie(pl);
                } else {
                    if (Util.isTrue(1, 2)) {
                        this.moveToPlayer(pl);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void die(Player plKill) {
        this.changeStatus(BossStatus.DIE);
        try {
            if (this.nPoint.hpMax >= 5_000_000) {
                BossManager.gI().createBoss(BossID.SUPER_BROLY);
            } else {
                BossManager.gI().createBossBroly(BossID.BROLY, this.nPoint.hpMax * 15 / 10);
            }

        } catch (Exception ex) {
        }
    }

    @Override
    public void reward(Player plKill) {

        super.reward(plKill);
    }

    private void tangChiSo() {
        if(this.nPoint.hpMax >= 100_000_000) return;
        double hpMax = this.nPoint.hpMax;
        int rand = Util.nextInt(5, 10);
        hpMax += hpMax * rand/100;;
        this.nPoint.hpMax = hpMax;
        this.nPoint.dame = hpMax / 10;
    }

    @Override
    public void leaveMap() {
        ChangeMapService.gI().exitMap(this);
        this.lastZone = null;
        this.lastTimeRest = System.currentTimeMillis();
        this.changeStatus(BossStatus.REST);
        BrolyManager.gI().removeBoss(this);
        this.dispose();
    }
}
