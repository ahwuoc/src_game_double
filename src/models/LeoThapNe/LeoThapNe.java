package models.LeoThapNe;

import boss.Boss;
import boss.BossData;
import boss.BossManager;
import consts.ConstPlayer;
import map.Map;
import map.Zone;
import player.Player;
import services.MapService;
import services.Service;
import services.func.ChangeMapService;
import skill.Skill;
import utils.Util;

/**
 *
 * @author bemeo
 */
public class LeoThapNe {

    static double getDameBoss(int levelThap, int tangThap) {
        double baseDamage = 1000; // Sát thương gốc cho level 1 tầng 1
        double levelMultiplier = 2; // Tăng 50% cho mỗi level
        double floorMultiplier = 3; // Tăng gấp đôi cho mỗi tầng

        // Tính sát thương cho boss
        double damage = baseDamage * Math.pow(levelMultiplier, levelThap - 1) * Math.pow(floorMultiplier, tangThap - 1);

        // Giới hạn sát thương tối đa để tránh NaN
        if (damage > Double.MAX_VALUE) {
            return Double.MAX_VALUE; // Trả về giá trị tối đa của double nếu quá lớn
        }

        return damage;
    }

    static double getHpBoss(int levelThap, int tangThap) {
        double baseHp = 10000000l; // HP gốc cho level 1 tầng 1
        double levelMultiplier = 2; // Tăng 50% cho mỗi level
        double floorMultiplier = 3; // Tăng gấp đôi cho mỗi tầng

        // Tính HP cho boss
        double hp = baseHp * Math.pow(levelMultiplier, levelThap - 1) * Math.pow(floorMultiplier, tangThap - 1);

        // Giới hạn HP tối đa để tránh NaN
        if (hp > Double.MAX_VALUE) {
            return Double.MAX_VALUE; // Trả về giá trị tối đa của double nếu quá lớn
        }

        return hp;
    }

    static short[] getOutfitBoss() {
        short[][] of = {{1910,	1911	,1912, -1, -1, -1}, {1910	,1911,	1912, -1, -1, -1}};
        return of[Util.nextInt(of.length - 1)];
    }

    public static Zone getMapLeoThap(Map m) {
        return m.zones.stream().filter(z -> z.getHumanoids().isEmpty()).findAny().orElse(null);
    }

    public static void joinMapLeoThap(Player pl) {
        Map map = MapService.gI().getMapById(145);
        if (getMapLeoThap(map) == null) {
            Zone z = new Zone(MapService.gI().getMapById(145), map.zones.size(), 1);
            map.zones.add(z);
            ChangeMapService.gI().changeMapBySpaceShipBoss(pl, z, 693);
        } else {
            ChangeMapService.gI().changeMapBySpaceShipBoss(pl, getMapLeoThap(map), 693);
        }
        callBoss(pl);
    }

    public static synchronized void callBoss(Player pl) {
        if (pl.levelThap == 0) {
            pl.levelThap = 1;
        }
        if (pl.tangThap == 0) {
            pl.tangThap = 1;
        }
        Boss b = null;
        try {
            b = new BossLeoThap(pl.levelThap, pl.tangThap);
        } catch (Exception e) {
        }
        if (b != null) {
            b.zone = pl.zone;
        }
    }

    public static class BossLeoThap extends Boss {

        public BossLeoThap(int level, int tang) throws Exception {
            super(Util.nextInt(1_000_000, 100_000_000), new BossData("Boss Bí Cảnh Tầng " + tang + " [Level " + level + "]", //name
                    ConstPlayer.XAYDA, //gender
                    getOutfitBoss(), //outfit
                    getDameBoss(level, tang), //dame
                    new double[]{getHpBoss(level, tang)}, //hp
                    new int[]{3, 4, 5, 6, 27, 28, 29, 30,//traidat
                        9, 11, 12, 13, 10, 34, 33, 32, 31,//namec
                        16, 17, 18, 19, 20, 37, 38, 36, 35,//xayda
                        24, 25, 26}, //map join
                    new int[][]{ //skill
                        {Skill.KAMEJOKO, 1, 10}, {Skill.LIEN_HOAN, 2, 20}, {Skill.MASENKO, 7, 20}, {Skill.TAI_TAO_NANG_LUONG, 7, 90000}, {Skill.TAI_TAO_NANG_LUONG, 5, 50000}, {Skill.DE_TRUNG, 7, 20000},
                        {Skill.ANTOMIC, 1, 70}},
                    new String[]{"|-1|Xem bản lĩnh của ngươi như nào đã", "|-1|Các ngươi tới số mới gặp phải ta"},
                    new String[]{"|-1|Ác quỷ biến hình, hêy aaa......."},
                    new String[]{"|-1|Bái bai"}, 0));
        }

        @Override
        public void update() {
            super.update();
            if (this.zone.getPlayers().isEmpty()) {
                this.leaveMap();
            }
        }

        @Override
        public void reward(Player pl) {

            pl.pointThap++;
            pl.levelThap++;
            int tang = pl.tangThap;
            if (pl.levelThap == 99) {
                pl.levelThap = 1;
                pl.tangThap++;
            }
            if (pl.tangThap > tang) {
                pl.nPoint.calPoint();
            }
            Service.gI().sendThongBaoBenDuoi("Player " + pl.name + " Bí Cảnh tháp đến tầng " + pl.tangThap + " level " + pl.levelThap);
            callBoss(pl);
        }

        @Override
        public void checkPlayerDie(Player pl) {

        }

        @Override
        public void leaveMap() {
            super.leaveMap();
            BossManager.gI().removeBoss(this);
        }

        @Override
        public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
            double dame = 0;
            if (this.isDie()) {
                return dame;
            } else {
                if (Util.isTrue(1, 5) && plAtt != null) {
                    switch (plAtt.playerSkill.skillSelect.template.id) {
                        case Skill.TU_SAT, Skill.QUA_CAU_KENH_KHI, Skill.MAKANKOSAPPO -> {
                        }
                        default -> {
                            return 0;
                        }
                    }
                }
                dame = super.injured(plAtt, damage, piercing, isMobAttack);
                if (this.isDie()) {
                    this.setDie(plAtt);
                    die(plAtt);
                    leaveMap();
                }
                return dame;
            }
        }

    }

}
