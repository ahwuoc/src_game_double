package player;

import models.Card.Card;
import models.Card.OptionCard;
import consts.ConstPlayer;
import consts.ConstRatio;
import intrinsic.Intrinsic;
import item.Item;
import item.Item.ItemOption;
import player.badges.BagesTemplate;
import skill.Skill;
import server.Manager;
import services.EffectSkillService;
import services.ItemService;
import services.MapService;
import services.PlayerService;
import services.Service;
import services.TaskService;
import utils.Logger;
import utils.SkillUtil;
import utils.Util;

import java.util.ArrayList;
import java.util.List;

import jdbc.daos.EventDAO;
import lombok.Setter;
import mob.Mob;
import power.PowerLimit;
import power.PowerLimitManager;
import utils.TimeUtil;

public class NPoint {

    public static final byte MAX_LIMIT = 13;

    @Setter
    private Player player;
    private PetBonusCalculator petBonusCalculator;

    public NPoint(Player player) {
        this.player = player;
        this.petBonusCalculator = new PetBonusCalculator(player);
        this.tlHp = new ArrayList<>();
        this.tlMp = new ArrayList<>();
        this.tlDef = new ArrayList<>();
        this.tlDame = new ArrayList<>();
        this.tlDameAttMob = new ArrayList<>();
        this.tlTNSM = new ArrayList<>();
        this.tlDameCrit = new ArrayList<>();
    }

    public boolean isCrit;
    public boolean isCrit100;
    public boolean isCritTele;

    private Intrinsic intrinsic;
    private int percentDameIntrinsic;
    public double dameAfter;
    private PowerLimit powerLimit;
    /*-----------------------Chỉ số cơ bản------------------------------------*/
    public byte numAttack;
    public short stamina, maxStamina;

    public byte limitPower;
    public double power;
    public double tiemNang;

    public double hp, hpMax, hpg;
    public double mp, mpMax, mpg;
    public double dame, dameg;
    public double def, defg;
    public int crit, critg;
    public byte speed = 4;

    public boolean teleport;

    public boolean khangTDHS;

    public void initPowerLimit() {
        powerLimit = PowerLimitManager.getInstance().get(limitPower);
    }

    /**
     * Chỉ số cộng thêm
     */
    public double hpAdd, mpAdd, dameAdd, defAdd, critAdd, hpHoiAdd, mpHoiAdd, dameBoss;

    /**
     * //+#% sức đánh chí mạng
     */
    public List<Long> tlDameCrit;
    public int tlSDCM;

    /**
     * Tỉ lệ hp, mp cộng thêm
     */
    public List<Long> tlHp, tlMp;
    public int tlGiamst;
    /**
     * Tỉ lệ giáp cộng thêm
     */
    public List<Long> tlDef;

    /**
     * Tỉ lệ sức đánh/ sức đánh khi đánh quái
     */
    public List<Long> tlDame, tlDameAttMob;

    /**
     * Lượng hp, mp hồi mỗi 30s, mp hồi cho người khác
     */
    public double hpHoi, mpHoi, mpHoiCute;

    /**
     * Tỉ lệ hp, mp hồi cộng thêm
     */
    public short tlHpHoi, tlMpHoi;

    /**
     * Tỉ lệ hp, mp hồi bản thân và đồng đội cộng thêm
     */
    public short tlHpHoiBanThanVaDongDoi, tlMpHoiBanThanVaDongDoi;

    /**
     * Tỉ lệ hút hp, mp khi đánh, hp khi đánh quái
     */
    public long tlHutHp, tlHutMp, tlHutHpMob;

    /**
     * Tỉ lệ hút hp, mp xung quanh mỗi 5s
     */
    public short tlHutHpMpXQ;

    /**
     * Tỉ lệ phản sát thương
     */
    public short tlPST;

    /**
     * Tỉ lệ tiềm năng sức mạnh
     */
    public List<Long> tlTNSM;

    /**
     * Tỉ lệ vàng cộng thêm
     */
    public short tlGold;

    /**
     * Tỉ lệ né đòn
     */
    public short tlNeDon;

    public short tlBom;

    public short tlGiap;

    public short tlxgcc;

    public short tlxgc;

    public short tlchinhxac;

    public short tlTNSMPet;
    public short xChuong;

    public short setltdb;
    public short setTinhAn;
    public short setNhatAn;
    public short setNguyetAn;

    /**
     * Tỉ lệ sức đánh đẹp cộng thêm cho bản thân và người xung quanh
     */
    public long tlSexyDame;

    /**
     * Tỉ lệ giảm sức đánh
     */
    public short tlSubSD;

    public int voHieuChuong;

    /*------------------------Effect skin-------------------------------------*/
    public Item trainArmor;
    public boolean wearingTrainArmor;

    public boolean wearingVoHinh;
    public boolean isKhongLanh;
    public boolean islinhthuydanhbac;
    public boolean isTinhAn;
    public boolean isNhatAn;
    public boolean isNguyetAn;
    public boolean isTanHinh;
    public boolean isHoaDa;
    public boolean isLamCham;
    public boolean isDoSPL;
    public boolean isThoBulma;

    public short tlHpGiamODo;

    public boolean isGogeta;

    public int tlSpeed;

    public long levelBT;

    /*-------------------------------------------------------------------------*/
    /**
     * Tính toán mọi chỉ số sau khi có thay đổi
     */
    public void calPoint() {
        if (this.player.pet != null) {
            this.player.pet.nPoint.setPointWhenWearClothes();
        }
        this.setPointWhenWearClothes();
    }

    private void setPointWhenWearClothes() {
        resetPoint();
        if (this.player.rewardBlackBall.timeOutOfDateReward[2] > System.currentTimeMillis()) {
            tlHutHp += RewardBlackBall.R3S_1;
        }
        if (this.player.rewardBlackBall.timeOutOfDateReward[3] > System.currentTimeMillis()) {
            tlPST += RewardBlackBall.R4S_2;
        }
        if (this.player.rewardBlackBall.timeOutOfDateReward[4] > System.currentTimeMillis()) {
            tlDameCrit.add((long) RewardBlackBall.R5S_1);
            tlSDCM += RewardBlackBall.R5S_1;
        }
        if (this.player.rewardBlackBall.timeOutOfDateReward[6] > System.currentTimeMillis()) {
            tlNeDon += RewardBlackBall.R7S_1;
        }

        Card card = player.Cards.stream().filter(r -> r != null && r.Used == 1).findFirst().orElse(null);
        if (card != null) {
            for (OptionCard io : card.Options) {
                if (io.active == card.Level || (card.Level == -1 && io.active == 0)) {
                    switch (io.id) {
                        case 0: // Tấn công +#
                            this.dameAdd += io.param;
                            break;
                        case 2: // HP, KI+#000
                            this.hpAdd += io.param * 1000;
                            this.mpAdd += io.param * 1000;
                            break;
                        case 3:// vô hiệu chưởng
                            this.voHieuChuong += io.param;
                            break;
                        // case 5: // +#% sức đánh chí mạng
                        // this.tlDameCrit.add((long)io.param);
                        // this.tlSDCM += io.param;
                        // break;
                        case 6: // HP+#
                            this.hpAdd += io.param;
                            break;
                        case 7: // KI+#
                            this.mpAdd += io.param;
                            break;
                        case 8: // Hút #% HP, KI xung quanh mỗi 5 giây
                            this.tlHutHpMpXQ += io.param;
                            break;
                        case 14: // Chí mạng+#%
                            this.critAdd += io.param;
                            break;
                        case 16: // Speed
                        case 114:
                        case 148:
                            this.tlSpeed += io.param;
                            break;
                        case 18: // Chinh xac
                            this.tlchinhxac += io.param;
                            break;
                        case 19: // Tấn công+#% khi đánh quái
                            this.tlDameAttMob.add((long) io.param);
                            break;
                        case 22: // HP+#K
                            this.hpAdd += io.param * 1000;
                            break;
                        case 23: // MP+#K
                            this.mpAdd += io.param * 1000;
                            break;
                        case 27: // +# HP/30s
                            this.hpHoiAdd += io.param;
                            break;
                        case 28: // +# KI/30s
                            this.mpHoiAdd += io.param;
                            break;
                        case 33: // dịch chuyển tức thời
                            this.teleport = true;
                            break;
                        case 34:
                            this.setTinhAn += 1;
                            break;
                        case 35:
                            this.setNguyetAn += 1;
                            break;
                        case 36:
                            this.setNhatAn += 1;
                            break;
                        case 47: // Giáp+#
                            this.defAdd += io.param;
                            break;
                        case 48: // HP/KI+#
                            this.hpAdd += io.param;
                            this.mpAdd += io.param;
                            break;
                        case 49: // Tấn công+#%
                        case 50: // Sức đánh+#%
                            this.tlDame.add((long) io.param);
                            break;
                        case 249: // Sức đánh+#%
                            this.tlDame.add((long) io.param);
                            break;
                        case 250: // HP+#%
                            this.tlHp.add((long) io.param);
                            break;
                        case 251: // KI +#%
                            this.tlMp.add((long) io.param);
                            break;

                        case 252: // Giáp #%
                            this.tlGiap += io.param;
                            break;

                        case 77: // HP+#%
                            this.tlHp.add((long) io.param);
                            break;
                        case 80: // HP+#%/30s
                            this.tlHpHoi += io.param;
                            break;
                        case 81: // MP+#%/30s
                            this.tlMpHoi += io.param;
                            break;
                        case 88: // Cộng #% exp khi đánh quái
                            this.tlTNSM.add((long) io.param);
                            break;
                        case 94: // Giáp #%
                            this.tlGiap += io.param;
                            break;
                        case 95: // Biến #% tấn công thành HP
                            this.tlHutHp += io.param;
                            break;
                        case 96: // Biến #% tấn công thành MP
                            this.tlHutMp += io.param;
                            break;
                        case 97: // Phản #% sát thương
                            this.tlPST += io.param;
                            break;
                        case 98: // Xuyen giap chuong
                            this.tlxgc += io.param;
                            break;
                        case 99: // Xuyen giap can chien
                            this.tlxgcc += io.param;
                            break;
                        case 100: // +#% vàng từ quái
                            this.tlGold += io.param;
                            break;
                        case 101: // +#% TN,SM
                            this.tlTNSM.add((long) io.param);
                            break;
                        case 103: // KI +#%
                            this.tlMp.add((long) io.param);
                            break;
                        case 104: // Biến #% tấn công quái thành HP
                            this.tlHutHpMob += io.param;
                            break;
                        case 105: // Vô hình khi không đánh quái và boss
                            this.wearingVoHinh = true;
                            break;
                        case 106: // Không ảnh hưởng bởi cái lạnh
                            this.isKhongLanh = true;
                            break;
                        case 108: // #% Né đòn
                            this.tlNeDon += io.param;
                            break;
                        case 109: // Hôi, giảm #% HP
                            this.tlHpGiamODo += io.param;
                            break;
                        case 116: // Kháng thái dương hạ san
                            this.khangTDHS = true;
                            break;
                        case 117: // Đẹp +#% SĐ cho mình và người xung quanh
                            if (io.param > this.tlSexyDame) {
                                this.tlSexyDame = io.param;
                            }
                            break;
                        case 147: // +#% sức đánh
                            this.tlDame.add((long) io.param);
                            break;
                        case 156: // Giảm 50% sức đánh, HP, KI và +#% SM, TN, vàng từ quái
                            this.tlSubSD += 50;
                            this.tlTNSM.add((long) io.param);
                            this.tlGold += io.param;
                            break;
                        case 162: // Cute hồi #% KI/s bản thân và xung quanh
                            this.mpHoiCute += io.param;
                            break;
                        case 173: // Phục hồi #% HP và KI cho đồng đội
                            this.tlHpHoiBanThanVaDongDoi += io.param;
                            this.tlMpHoiBanThanVaDongDoi += io.param;
                            break;
                        case 153: // % phát nổ sau khi chết
                            this.tlBom += io.param;
                            break;
                    }
                }
            }
        }

        // Bông tai cấp 2
        if (this.player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2) {
            this.player.inventory.itemsBag.stream().filter(it -> it.isNotNullItem() && it.template.id == 921)
                    .findFirst().ifPresent(btc2 -> {
                        for (ItemOption io : btc2.itemOptions) {
                            addOption(io);
                            if (io.optionTemplate.id == 72) {
                                this.levelBT = io.param;
                            }
                        }
                    });
        }

        if (BagesTemplate.sendListItemOption(player) != null) {
            for (ItemOption io : BagesTemplate.sendListItemOption(player)) {
                addOption(io);
            }
        }

        this.player.setClothes.worldcup = 0;
        for (Item item : this.player.inventory.itemsBody) {
            if (item.isNotNullItem()) {
                switch (item.template.id) {
                    case 966:
                    case 982:
                    case 983:
                    case 883:
                    case 904:
                        player.setClothes.worldcup++;
                }
                if (item.template.id >= 592 && item.template.id <= 594) {
                    teleport = true;
                }
                for (ItemOption io : item.itemOptions) {
                    addOption(io);
                }
            }
        }
        setDameTrainArmor();
        setBasePoint();
        setOutfitFusion();
        setSpeed();
    }

    private void addOption(ItemOption io) {
        switch (io.optionTemplate.id) {
            case 0: // Tấn công +#
                this.dameAdd += io.param;
                break;
            case 2: // HP, KI+#000
                this.hpAdd += io.param * 1000;
                this.mpAdd += io.param * 1000;
                break;
            case 3:// vô hiệu chưởng
                this.voHieuChuong += io.param;
                break;
             case 5: // +#% sức đánh chí mạng
             this.tlDameCrit.add(io.param);
             this.tlSDCM += io.param;
             break;
            case 6: // HP+#
                this.hpAdd += io.param;
                break;
            case 7: // KI+#
                this.mpAdd += io.param;
                break;
            case 8: // Hút #% HP, KI xung quanh mỗi 5 giây
                this.tlHutHpMpXQ += io.param;
                break;
            case 14: // Chí mạng+#%
                this.critAdd += io.param;
                break;
            case 16: // Speed
            case 114:
            case 148:
                this.tlSpeed += io.param;
                break;
            case 18: // Chinh xac
                this.tlchinhxac += io.param;
                break;
            case 19: // Tấn công+#% khi đánh quái
                this.tlDameAttMob.add(io.param);
                break;
            case 22: // HP+#K
                this.hpAdd += io.param * 1000;
                break;
            case 23: // MP+#K
                this.mpAdd += io.param * 1000;
                break;
            case 24: // Làm chậm
                this.isLamCham = true;
                break;
            case 25: // Tàn hình
                this.isTanHinh = true;
                break;
            case 26: // Hóa đá
                this.isHoaDa = true;
                break;
            case 27: // +# HP/30s
                this.hpHoiAdd += io.param;
                break;
            case 28: // +# KI/30s
                this.mpHoiAdd += io.param;
                break;
            case 33: // dịch chuyển tức thời
                this.teleport = true;
                break;
            case 34:
                this.setTinhAn += 1;
                break;
            case 35:
                this.setNguyetAn += 1;
                break;
            case 36:
                this.setNhatAn += 1;
                break;
            case 47: // Giáp+#
                this.defAdd += io.param;
                break;
            case 48: // HP/KI+#
                this.hpAdd += io.param;
                this.mpAdd += io.param;
                break;
            case 49: // Tấn công+#%
            case 50: // Sức đánh+#%
                this.tlDame.add(io.param);
                break;

            case 249: // Sức đánh+#%
                this.tlDame.add((long) io.param);
                break;
            case 250: // HP+#%
                this.tlHp.add((long) io.param);
                break;
            case 251: // KI +#%
                this.tlMp.add((long) io.param);
                break;

            case 252: // Giáp #%
                this.tlGiap += io.param;
                break;
            case 77: // HP+#%
                this.tlHp.add(io.param);
                break;
            case 80: // HP+#%/30s
                this.tlHpHoi += io.param;
                break;
            case 81: // MP+#%/30s
                this.tlMpHoi += io.param;
                break;
            case 88: // Cộng #% exp khi đánh quái
                this.tlTNSM.add(io.param);
                break;
            case 94: // Giáp #%
                this.tlGiap += io.param;
                break;
            case 95: // Biến #% tấn công thành HP
                this.tlHutHp += io.param;
                break;
            case 96: // Biến #% tấn công thành MP
                this.tlHutMp += io.param;
                break;
            case 97: // Phản #% sát thương
                this.tlPST += io.param;
                break;
            case 98: // Xuyen giap chuong
                this.tlxgc += io.param;
                break;
            case 99: // Xuyen giap can chien
                this.tlxgcc += io.param;
                break;
            case 100: // +#% vàng từ quái
                this.tlGold += io.param;
                break;
            case 101: // +#% TN,SM
                this.tlTNSM.add(io.param);
                break;
            case 103: // KI +#%
                this.tlMp.add(io.param);
                break;
            case 104: // Biến #% tấn công quái thành HP
                this.tlHutHpMob += io.param;
                break;
            case 105: // Vô hình khi không đánh quái và boss
                this.wearingVoHinh = true;
                break;
            case 106: // Không ảnh hưởng bởi cái lạnh
                this.isKhongLanh = true;
                break;
            case 108: // #% Né đòn
                this.tlNeDon += io.param;
                break;
            case 109: // Hôi, giảm #% HP
                this.tlHpGiamODo += io.param;
                break;
            case 110: // Do spl
                this.isDoSPL = true;
                break;
            case 116: // Kháng thái dương hạ san
                this.khangTDHS = true;
                break;
            case 117: // Đẹp +#% SĐ cho mình và người xung quanh
                if (io.param > this.tlSexyDame) {
                    this.tlSexyDame = io.param;
                }
                break;
            case 147: // +#% sức đánh
                this.tlDame.add(io.param);
                break;
            case 156: // Giảm 50% sức đánh, HP, KI và +#% SM, TN, vàng từ quái
                this.tlSubSD += 50;
                this.tlTNSM.add(io.param);
                this.tlGold += io.param;
                break;
            case 162: // Cute hồi #% KI/s bản thân và xung quanh
                this.mpHoiCute += io.param;
                break;
            case 159: // x chưởng
                this.xChuong = (short) io.param;
                break;
            case 160: // TNSM PET;
                this.tlTNSMPet += io.param;
                break;
            case 173: // Phục hồi #% HP và KI cho đồng đội
                this.tlHpHoiBanThanVaDongDoi += io.param;
                this.tlMpHoiBanThanVaDongDoi += io.param;
                break;
            case 204:
                this.dameBoss += io.param;
                break;
            case 211:
                this.setltdb += 1;
                break;
            case 153: // % phát nổ sau khi chết
                this.tlBom += io.param;
                break;
        }
    }

    private void setSpeed() {
        if (player.isPl()) {
            speed = (byte) (6 + 6 * (tlSpeed / 100));
        }
    }

    private void setOutfitFusion() {
        if (this.player.inventory.itemsBody.size() < 6 || this.player.pet == null
                || this.player.pet.inventory.itemsBody.size() < 6) {
            return;
        }
        Item skin = this.player.inventory.itemsBody.get(5);
        Item pskin = this.player.pet.inventory.itemsBody.get(5);
        if (skin.isNotNullItem() && pskin.isNotNullItem()) {
            this.isGogeta = skin.template.id == 2133 && pskin.template.id == 2134
                    || skin.template.id == 2134 && pskin.template.id == 2133;
        } else {
            this.isGogeta = false;
        }
    }

    private void setDameTrainArmor() {
        if (!this.player.isPet && !this.player.isBoss) {
            if (this.player.inventory.itemsBody.size() < 7) {
                return;
            }
            try {
                Item gtl = this.player.inventory.itemsBody.get(6);
                if (gtl.isNotNullItem()) {
                    this.wearingTrainArmor = true;
                    this.player.inventory.trainArmor = gtl;
                    this.tlSubSD += ItemService.gI().getPercentTrainArmor(gtl);
                } else {
                    if (this.player.inventory.trainArmor == null) {
                        gtl = this.player.inventory.itemsBag.stream()
                                .filter(item -> item.isNotNullItem() && item.template.type == 32
                                        && item.itemOptions != null
                                        && item.itemOptions.stream()
                                                .filter(io -> io.optionTemplate.id == 9 && io.param > 0).findFirst()
                                                .orElse(null) != null)
                                .findFirst().orElse(null);
                        if (gtl == null) {
                            return;
                        }
                        this.player.inventory.trainArmor = gtl;
                    }
                    this.wearingTrainArmor = false;
                    for (Item.ItemOption io : this.player.inventory.trainArmor.itemOptions) {
                        if (io.optionTemplate.id == 9 && io.param > 0) {
                            this.tlDame.add(
                                    (long) ItemService.gI().getPercentTrainArmor(this.player.inventory.trainArmor));
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                Logger.error("Lỗi get giáp tập luyện " + this.player.name + "\n" + e + "\n");
            }
        }
    }

    public void setBasePoint() {
        setHpMax();
        setHp();
        setMpMax();
        setMp();
        setDame();
        setDef();
        setCrit();
        setHpHoi();
        setMpHoi();
        setLtdb();
        setThoBulma();
        setTinhNhatNguyetAn();
    }

    private void setLtdb() {
        this.islinhthuydanhbac = this.setltdb >= 5;
    }

    private void setThoBulma() {
        this.isThoBulma = (this.player.inventory != null && this.player.inventory.itemsBody != null
                && this.player.inventory.itemsBody.size() >= 5 && this.player.inventory.itemsBody.get(5).isNotNullItem()
                && this.player.inventory.itemsBody.get(5).template.id == 584);
    }

    private void setTinhNhatNguyetAn() {
        this.isTinhAn = this.setTinhAn >= 5;
        this.isNhatAn = this.setNhatAn >= 5;
        this.isNguyetAn = this.setNguyetAn >= 5;
    }

    private void setHpHoi() {
        this.hpHoi = this.hpMax / 100;
        this.hpHoi += this.hpHoiAdd;

        // Kiểm tra giá trị tlHpHoi không vượt quá giới hạn
        if (this.tlHpHoi > 100) {
            this.tlHpHoi = 100;
        } else if (this.tlHpHoi < 0) {
            this.tlHpHoi = 0;
        }

        this.hpHoi += ((long) this.hpMax * this.tlHpHoi / 100);

        // Kiểm tra giá trị tlHpHoiBanThanVaDongDoi không vượt quá giới hạn
        if (this.tlHpHoiBanThanVaDongDoi > 100) {
            this.tlHpHoiBanThanVaDongDoi = 100;
        } else if (this.tlHpHoiBanThanVaDongDoi < 0) {
            this.tlHpHoiBanThanVaDongDoi = 0;
        }

        this.hpHoi += ((long) this.hpMax * this.tlHpHoiBanThanVaDongDoi / 100);
    }

    private void setMpHoi() {
        this.mpHoi = this.mpMax / 100;
        this.mpHoi += this.mpHoiAdd;

        // Kiểm tra giá trị tlMpHoi không vượt quá giới hạn
        if (this.tlMpHoi > 100) {
            this.tlMpHoi = 100;
        } else if (this.tlMpHoi < 0) {
            this.tlMpHoi = 0;
        }

        this.mpHoi += ((long) this.mpMax * this.tlMpHoi / 100);

        // Kiểm tra giá trị tlMpHoiBanThanVaDongDoi không vượt quá giới hạn
        if (this.tlMpHoiBanThanVaDongDoi > 100) {
            this.tlMpHoiBanThanVaDongDoi = 100;
        } else if (this.tlMpHoiBanThanVaDongDoi < 0) {
            this.tlMpHoiBanThanVaDongDoi = 0;
        }

        this.mpHoi += ((long) this.mpMax * this.tlMpHoiBanThanVaDongDoi / 100);
    }

    private void setHpMax() {
        // Tính toán giới hạn hpMax
        double hpMax = this.hpg + this.hpAdd;

        // Áp dụng các yếu tố ảnh hưởng đến hpMax
        for (Long tl : this.tlHp) {
            hpMax += (hpMax * tl / 100L);
        }

        // Xử lý set nappa
        if (this.player.setClothes.nappa == 5) {
            hpMax += (hpMax * 80L / 100L);
        }
        if (this.player.effectSkill.isTranformation || this.player.effectSkill.isEvolution) {
            if (!this.player.isPet || (this.player.isPet
                    && ((Pet) this.player).status != Pet.FUSION)) {
                int percent = SkillUtil.getPercentDameMonkey((byte) player.isbienhinh);
                this.hpMax += ((long) this.hpMax * (percent * 10) / 100);
            }
        }

        // Xử lý set worldcup
        if (this.player.setClothes.worldcup == 2) {
            hpMax += (hpMax * 10 / 100L);
        }

        // Xử lý rồng xương
        if (player.itemTime != null && player.itemTime.isUseRX) {
            hpMax += (hpMax * 10L / 100L);
        }

        // Xử lý set nhật ấn
        if (this.isNhatAn) {
            hpMax += (hpMax * 15L / 100L);
        }

        // Xử lý ngọc rồng đen 2 sao
        if (this.player.rewardBlackBall.timeOutOfDateReward[1] > System.currentTimeMillis()) {
            hpMax += (hpMax * RewardBlackBall.R2S_1 / 100L);
        }

        // Xử lý khỉ
        if (this.player.effectSkill.isMonkey) {
            if (!this.player.isPet || (this.player.isPet && ((Pet) this.player).status != Pet.FUSION)) {
                int percent = SkillUtil.getPercentHpMonkey(player.effectSkill.levelMonkey);
                hpMax += (hpMax * percent / 100L);
            }
        }

        // Xử lý pet HP bonus
        hpMax += petBonusCalculator.calculatePetHpBonus(hpMax);

        // Xử lý phù
        if (this.player.zone != null && MapService.gI().isMapBlackBallWar(this.player.zone.map.mapId)) {
            hpMax *= this.player.effectSkin.xHPKI;
        }

        // Xử lý thức ăn 2
        if (this.player.itemTime != null && this.player.itemTime.isEatMeal2 && this.player.itemTime.iconMeal2 == 8062) {
            hpMax += (hpMax * 5 / 100L);
        }

        // Xử lý gogeta
        if (this.isGogeta) {
            hpMax += (hpMax * 10 / 100L);
        }

        // Phù map mabu
        if (this.player.isPhuHoMapMabu) {
            hpMax += 1_000_000;
        }

        // Xử lý +hp đệ
        if (this.player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            hpMax += this.player.pet.nPoint.hpMax;
        }

        if (this.player.itemTime != null && this.player.itemTime.isItemTest) {
            hpMax *= 2;
        }

        // Xử lý bổ huyết
        if (this.player.itemTime != null && this.player.itemTime.isUseBoHuyet && !this.player.itemTime.isUseBoHuyet2) {
            hpMax *= 2;
        }

        // Xử lý item sieu cap
        if (this.player.itemTime != null && this.player.itemTime.isUseBoHuyet2) {
            hpMax *= 2.2;
        }

        // Xử lý huýt sáo
        if (!this.player.isPet || (this.player.isPet && ((Pet) this.player).status != Pet.FUSION)) {
            if (this.player.effectSkill.tiLeHPHuytSao != 0) {
                hpMax += (hpMax * this.player.effectSkill.tiLeHPHuytSao / 100L);
            }
        }

        // Xử lý chibi
        if (this.player.effectSkill != null && this.player.effectSkill.isChibi && this.player.typeChibi == 3) {
            hpMax *= 2;
        }

        // Xử lý map lạnh
        if (this.player.zone != null && MapService.gI().isMapCold(this.player.zone.map) && !this.isKhongLanh) {
            hpMax /= 2;
        }

        if (!this.player.isBoss && !this.player.isNewPet
                && TimeUtil.checkTime(EventDAO.getRemainingTimeToIncreaseHP())) {
            hpMax += hpMax / 10;
        }

        this.hpMax = hpMax;
    }

    private void setHp() {
        // Giới hạn giá trị hp không vượt quá hpMax
        this.hp = Math.min(this.hp, this.hpMax);
    }

    private void setMpMax() {
        // Tính toán giới hạn mpMax
        double mpMax = this.mpg + this.mpAdd;

        // Áp dụng các yếu tố ảnh hưởng đến mpMax
        for (Long tl : this.tlMp) {
            mpMax += (mpMax * tl / 100L);
        }

        // Xử lý set picolo
        if (this.player.setClothes.picolo == 5) {
            mpMax *= 2;
        }

        // Xử lý set nguyệt ấn
        if (this.isNguyetAn) {
            mpMax += (mpMax * 15L / 100L);
        }
        if (this.player.effectSkill.isTranformation || this.player.effectSkill.isEvolution) {
            if (!this.player.isPet || (this.player.isPet
                    && ((Pet) this.player).status != Pet.FUSION)) {
                int percent = SkillUtil.getPercentDameMonkey((byte) player.isbienhinh);
                this.mpMax += ((long) this.mpMax * (percent * 10) / 100);
            }
        }

        // Xử lý ngọc rồng đen 6 sao
        if (this.player.rewardBlackBall.timeOutOfDateReward[5] > System.currentTimeMillis()) {
            mpMax += (mpMax * RewardBlackBall.R6S_1 / 100L);
        }

        // Xử lý set worldcup
        if (this.player.setClothes.worldcup == 2) {
            mpMax += (this.mpMax * 10 / 100L);
        }

        // Xử lý pet MP bonus
        mpMax += petBonusCalculator.calculatePetMpBonus(mpMax);

        // Xử lý phù
        if (this.player.zone != null && MapService.gI().isMapBlackBallWar(this.player.zone.map.mapId)) {
            mpMax *= this.player.effectSkin.xHPKI;
        }

        // Xử lý gogeta
        if (this.isGogeta) {
            mpMax += (mpMax * 10 / 100L);
        }

        // Phù map mabu
        if (this.player.isPhuHoMapMabu) {
            mpMax += 1_000_000;
        }

        // Xử lý rồng xương
        if (player.itemTime != null && player.itemTime.isUseRX) {
            mpMax += (mpMax * 10L / 100L);
        }

        // Xử lý hợp thể
        if (this.player.fusion.typeFusion != 0) {
            mpMax += this.player.pet.nPoint.mpMax;
        }

        // Xử lý bổ khí
        if (this.player.itemTime != null && this.player.itemTime.isUseBoKhi && !this.player.itemTime.isUseBoKhi2) {
            mpMax *= 2;
        }

        // Xử lý item sieu cap
        if (this.player.itemTime != null && this.player.itemTime.isUseBoKhi2) {
            mpMax *= 2.2;
        }

        if (!this.player.isBoss && !this.player.isNewPet
                && TimeUtil.checkTime(EventDAO.getRemainingTimeToIncreaseMP())) {
            mpMax += mpMax / 10;
        }

        this.mpMax = mpMax;
    }

    private void setMp() {
        // Giới hạn giá trị mp không vượt quá mpMax
        this.mp = Math.min(this.mp, this.mpMax);
    }

    public double getHP() {
        return this.hp <= this.hpMax ? this.hp : this.hpMax;
    }

    public void setHP(double hp) {
        if (hp > 0) {
            this.hp = (hp <= this.hpMax ? hp : this.hpMax);
        } else {
            player.setDie();
        }
    }

    public double getMP() {
        return this.mp <= this.mpMax ? this.mp : this.mpMax;
    }

    public void setMP(double mp) {
        if (mp > 0) {
            this.mp = (mp <= this.mpMax ? mp : this.mpMax);
        } else {
            this.mp = 0;
        }
    }

    private void setDame() {
        // Tính toán giới hạn dame
        double dame = this.dameg + this.dameAdd;

        // Áp dụng các yếu tố ảnh hưởng đến dame
        for (Long tl : this.tlDame) {
            dame += (dame * tl / 100L);
        }
        // Xử lý pet Dame bonus
        dame += petBonusCalculator.calculatePetDameBonus(dame);

        if (this.player.effectSkill.isTranformation || this.player.effectSkill.isEvolution) {
            if (petBonusCalculator.canApplySkillEffect()) {
                int percent = SkillUtil.getPercentDameMonkey((byte) player.isbienhinh);
                this.dame += ((long) this.dame * (percent * 10) / 100);
            }
        }

        // Xử lý set tinh ấn
        if (this.isTinhAn) {
            dame += (dame * 3L / 20);
        }

        // Xử lý thức ăn
        if (!this.player.isPet && this.player.itemTime != null && this.player.itemTime.isEatMeal
                || this.player.isPet && this.player.itemTime != null && ((Pet) this.player).master.itemTime.isEatMeal) {
            dame += (dame / 10L);
        }

        // Xử lý thức ăn 2
        if (this.player.itemTime != null && this.player.itemTime.isEatMeal2 && this.player.itemTime.iconMeal2 == 8060) {
            dame += (dame / 20L);
        }

        // Xử lý thức ăn 2
        if (this.player.itemTime != null && this.player.itemTime.isEatMeal2 && this.player.itemTime.iconMeal2 == 8061) {
            this.tlDameCrit.add((long) 5);
            this.tlSDCM += 5;
        }

        // Xử lý cuồng nộ
        if (this.player.itemTime != null && this.player.itemTime.isUseCuongNo && !this.player.itemTime.isUseCuongNo2) {
            dame *= 2;
        }
        if (this.player.itemTime != null && this.player.itemTime.isUseCuongNo2) {
            dame *= 2.2;
        }

        // Xử lý ngọc rồng đen 1 sao
        if (this.player.rewardBlackBall.timeOutOfDateReward[0] > System.currentTimeMillis()) {
            dame += (dame * RewardBlackBall.R1S_2 / 100L);
        }

        // Xử lý set worldcup
        if (this.player.setClothes.worldcup == 2) {
            dame += (dame / 10L);
        }

        // Xử lý gogeta
        if (this.isGogeta) {
            dame += (dame / 10L);
        }

        // Phù map mabu
        if (this.player.isPhuHoMapMabu) {
            dame += 10_000;
        }

        // Xử lý rồng xương
        if (player.itemTime != null && player.itemTime.isUseRX) {
            dame += (dame / 10L);
        }

        // Xử lý phù
        if (this.player.zone != null && MapService.gI().isMapBlackBallWar(this.player.zone.map.mapId)) {
            dame *= this.player.effectSkin.xDame;
        }

        // Xử lý hợp thể
        if (this.player.fusion.typeFusion != 0) {
            dame += this.player.pet.nPoint.dame;
        }

        // Xử lý khỉ
        if (this.player.effectSkill.isMonkey) {
            if (!this.player.isPet || (this.player.isPet && ((Pet) this.player).status != Pet.FUSION)) {
                int percent = SkillUtil.getPercentDameMonkey(player.effectSkill.levelMonkey);
                dame += (dame * percent / 100L);
            }
        }

        // Sức đánh đẹp
        dame += (dame * tlSexyDame / 100L);

        // Xử lý giảm dame
        dame -= (dame * tlSubSD / 100L);

        // Xử lý map cold
        if (this.player.zone != null && MapService.gI().isMapCold(this.player.zone.map) && !this.isKhongLanh) {
            dame /= 2;
        }

        if (!this.player.isBoss && !this.player.isNewPet
                && TimeUtil.checkTime(EventDAO.getRemainingTimeToIncreaseDame())) {
            dame += dame / 10;
        }

        this.dame = dame;
    }

    private void setDef() {
        this.def = this.defg * 4;
        this.def += this.defAdd;

    }

    private void setCrit() {
        this.crit = this.critg;
        this.crit += this.critAdd;
        // biến khỉ
        if (this.player.effectSkill.isMonkey) {
            this.crit = 110;
        }
    }

    private void resetPoint() {
        this.dameBoss = 0;
        this.voHieuChuong = 0;
        this.hpAdd = 0;
        this.mpAdd = 0;
        this.dameAdd = 0;
        this.defAdd = 0;
        this.critAdd = 0;
        this.tlGiamst = 0;

        this.tlHp.clear();
        this.tlMp.clear();
        this.tlDef.clear();
        this.tlDame.clear();
        this.tlDameCrit.clear();
        this.tlDameAttMob.clear();
        this.tlSDCM = 0;
        this.tlHpHoiBanThanVaDongDoi = 0;
        this.tlMpHoiBanThanVaDongDoi = 0;
        this.hpHoi = 0;
        this.mpHoi = 0;
        this.mpHoiCute = 0;
        this.tlHpHoi = 0;
        this.tlMpHoi = 0;
        this.tlHutHp = 0;
        this.tlHutMp = 0;
        this.tlHutHpMob = 0;
        this.tlHutHpMpXQ = 0;
        this.tlPST = 0;
        this.tlTNSM.clear();
        this.tlDameAttMob.clear();
        this.tlGold = 0;
        this.tlNeDon = 0;
        this.tlBom = 0;
        this.tlGiap = 0;
        this.tlxgcc = 0;
        this.tlxgc = 0;
        this.tlchinhxac = 0;
        this.tlTNSMPet = 0;
        this.xChuong = 0;
        this.setltdb = 0;
        this.setTinhAn = 0;
        this.setNhatAn = 0;
        this.setNguyetAn = 0;
        this.tlSexyDame = 0;
        this.tlSubSD = 0;
        this.tlHpGiamODo = 0;
        this.tlSpeed = 0;
        this.teleport = false;

        this.wearingVoHinh = false;
        this.isKhongLanh = false;
        this.khangTDHS = false;
        this.isTanHinh = false;
        this.isHoaDa = false;
        this.isLamCham = false;
        this.isDoSPL = false;
        this.isThoBulma = false;
    }

    public void addHp(double hp) {
        if (hp > 0) {
            double potentialHp = this.hp + hp;
            if (potentialHp > this.hpMax) {
                this.hp = this.hpMax;
            } else {
                this.hp = potentialHp;
            }
        }
    }

    public void addMp(double mp) {
        double potentialMp = this.mp + mp;

        if (potentialMp > this.mpMax) {
            this.mp = this.mpMax;
        } else if (potentialMp < 0) {
            this.mp = 0;
        } else {
            this.mp = potentialMp;
        }
    }

    public void setHp(double hp) {
        if (hp < 0) {
            this.hp = 0;
        } else {
            this.hp = hp;
        }
    }

    public void setMp(double mp) {
        if (mp < 0) {
            this.mp = 0;
        } else {
            this.mp = mp;
        }
    }

    private void setIsCrit() {
        if (intrinsic != null && intrinsic.id == 25 && this.getCurrPercentHP() <= intrinsic.param1) {
            isCrit = true;
        } else if (isCrit100) {
            isCrit100 = false;
            isCrit = true;
        } else {
            isCrit = Util.isTrue(this.crit, ConstRatio.PER100);
        }
    }

    public double getDameAttack(boolean isAttackMob) {
        setIsCrit();
        double dameAttack = this.dame;
        intrinsic = this.player.playerIntrinsic.intrinsic;
        percentDameIntrinsic = 0;
        double percentDameSkill = 0;
        double percentXDame = 0;
        Skill skillSelect = player.playerSkill.skillSelect;
        if (skillSelect.template.id != Skill.DICH_CHUYEN_TUC_THOI && isCritTele) {
            isCrit = true;
            isCritTele = false;
        }
        switch (skillSelect.template.id) {
            case Skill.DRAGON:
                if (intrinsic.id == 1) {
                    percentDameIntrinsic = intrinsic.param1;
                }
                percentDameSkill = skillSelect.damage;
                break;
            case Skill.KAMEJOKO:
                if (intrinsic.id == 2) {
                    percentDameIntrinsic = intrinsic.param1;
                }
                percentDameSkill = skillSelect.damage;
                if (this.player.setClothes.songoku == 5) {
                    percentXDame = 100;
                }
                break;
            case Skill.GALICK:
                if (intrinsic.id == 16) {
                    percentDameIntrinsic = intrinsic.param1;
                }
                percentDameSkill = skillSelect.damage;
                if (this.player.setClothes.kakarot == 5) {
                    percentXDame = 100;
                }
                break;
            case Skill.ANTOMIC:
                if (intrinsic.id == 17) {
                    percentDameIntrinsic = intrinsic.param1;
                }
                percentDameSkill = skillSelect.damage;
                break;
            case Skill.DEMON:
                if (intrinsic.id == 8) {
                    percentDameIntrinsic = intrinsic.param1;
                }
                percentDameSkill = skillSelect.damage;
                break;
            case Skill.MASENKO:
                if (intrinsic.id == 9) {
                    percentDameIntrinsic = intrinsic.param1;
                }
                percentDameSkill = skillSelect.damage;
                break;
            case Skill.LIEN_HOAN:
                if (intrinsic.id == 13) {
                    percentDameIntrinsic = intrinsic.param1;
                }
                percentDameSkill = skillSelect.damage;
                if (this.player.setClothes.ocTieu == 5) {
                    percentXDame = 100;
                }
                break;
            case Skill.DICH_CHUYEN_TUC_THOI:
                isCrit = true;
                isCritTele = true;
                dameAttack = Util.nextDouble(dameAttack - (dameAttack / 100 * 5), dameAttack + (dameAttack / 100 * 5));
                break;
            case Skill.MAKANKOSAPPO:
                percentDameSkill = skillSelect.damage;
                double dameSkill = this.mpMax / 100 * percentDameSkill;
                return dameSkill;
            case Skill.QUA_CAU_KENH_KHI:
                long hpmob = 0;
                long hppl = 0;

                for (Mob mob : this.player.zone.mobs) {
                    if (!mob.isDie() && Util.getDistance(this.player, mob) <= SkillUtil
                            .getRangeQCKK(this.player.playerSkill.skillSelect.point)) {
                        hpmob += mob.point.hp;
                    }
                }

                for (Player pl : this.player.zone.getHumanoids()) {
                    if (!pl.isDie() && this.player.id != pl.id && Util.getDistance(this.player, pl) <= SkillUtil
                            .getRangeQCKK(this.player.playerSkill.skillSelect.point)) {
                        hppl += pl.nPoint.hp;
                    }
                }

                double dameqckk = (hpmob / 10) + (hppl / 10) + this.dame * 10;

                if (this.player.setClothes.kirin == 5) {
                    dameqckk *= 2;
                }

                dameqckk = dameqckk + (Util.nextLong(-5, 5) * dameqckk / 100);

                return dameqckk;
            case Skill.DE_TRUNG:
                if (player.setClothes.pikkoroDaimao == 5) {
                    dameAttack *= 4;
                }
                return dameAttack;
        }

        if (intrinsic.id == 18 && this.player.effectSkill.isMonkey) {
            percentDameIntrinsic = intrinsic.param1;
        }

        if (percentDameSkill != 0) {
            dameAttack = dameAttack * percentDameSkill / 100;
        }

        dameAttack += (dameAttack * percentDameIntrinsic / 100);
        dameAttack += (dameAttack * dameAfter / 100);
        if (this.player.effectSkill != null && this.player.effectSkill.isDameBuff && tlSexyDame == 0) {
            double tiLeDame = this.player.effectSkill.tileDameBuff;
            dameAttack += (dameAttack * tiLeDame / 100L);
        }
        if (isAttackMob) {
            for (double tl : this.tlDameAttMob) {
                dameAttack += (dameAttack / 100 * tl);
            }
            if (this.player.isPet && ((Pet) this.player).master.charms.tdDeTu > System.currentTimeMillis()) {
                dameAttack *= 2;
            }
        }

        dameAfter = 0;

        if (isCrit) {
            dameAttack *= 2;
            dameAttack += (dameAttack * tlSDCM / 100);
        }

        dameAttack += (dameAttack * percentXDame / 100);

        double tempDameAttack = (dameAttack / 20L);
        if (tempDameAttack <= 0) {
            tempDameAttack = 1;
        }
        dameAttack += (Util.getOne(-1, 1) * (tempDameAttack) + 1);

        if (player.effectSkin != null && player.effectSkin.isXChuong
                && (player.playerSkill.skillSelect.template.id == Skill.KAMEJOKO
                        || player.playerSkill.skillSelect.template.id == Skill.ANTOMIC
                        || player.playerSkill.skillSelect.template.id == Skill.MASENKO)) {
            dameAttack *= xChuong;
            player.effectSkin.isXDame = true;
            player.effectSkin.isXChuong = false;
            player.effectSkin.lastTimeXChuong = System.currentTimeMillis();
        }

        return dameAttack;
    }

    public int getCurrPercentHP() {
        if (this.hpMax == 0) {
            return 100;
        }
        return (int) (this.hp * 1.0 / this.hpMax * 100);
    }

    public int getCurrPercentMP() {
        return (int) (this.mp * 1.0 / this.mpMax * 100);
    }

    public void setFullHpMp() {
        this.hp = this.hpMax;
        this.mp = this.mpMax;
    }

    public void subHP(double sub) {
        this.hp -= sub;
        if (this.hp <= 0) {
            this.hp = 0;
            this.setHp(0);
        }
    }

    public void subMP(double sub) {
        this.mp -= sub;
        if (this.mp <= 0) {
            this.mp = 0;
        }
    }

    public double calSucManhTiemNang(double tiemNang) {
        if (player.zone.map.type == 3) {
            return 0;
        }
        if (power < getPowerLimit()) {
            for (Long tl : this.tlTNSM) {
                tiemNang += ((long) tiemNang * tl / 100);
            }
            if (this.player.cFlag != 0) {
                if (this.player.cFlag == 8) {
                    tiemNang += ((long) tiemNang * 10 / 100);
                } else {
                    tiemNang += ((long) tiemNang * 5 / 100);
                }
            }
            double tn = tiemNang;
            if (this.player.charms.tdTriTue > System.currentTimeMillis()) {
                tiemNang += tn;
            }
            if (this.player.charms.tdTriTue3 > System.currentTimeMillis()) {
                tiemNang += tn * 2;
            }
            if (this.player.charms.tdTriTue4 > System.currentTimeMillis()) {
                tiemNang += tn * 3;
            }
            if (this.player.charms.tdTriTue4 > System.currentTimeMillis()) {
                tiemNang += tn * 3;
            }
            if (this.player.effectSkill.isChibi && this.player.typeChibi == 2) {
                tiemNang += tn * 2;
            }
            if (this.player.getSession() != null && this.player.getSession().vip > 0
                    || this.player.isPet && ((Pet) this.player).master.getSession() != null
                            && ((Pet) this.player).master.getSession().vip > 0) {
                tiemNang += tn * 3;
            }
            if (this.player.itemTime != null && this.player.itemTime.isUseDK) {
                tiemNang += tn * 2;
            }
            if (this.player.satellite != null && this.player.satellite.isIntelligent) {
                tiemNang += tn / 5;
            }
            if (this.intrinsic != null && this.intrinsic.id == 24) {
                tiemNang += ((long) tiemNang * this.intrinsic.param1 / 100);
            }
            if (this.power >= 60000000000L) {
                tiemNang -= ((long) tiemNang * 80 / 100);
            }
            if (this.player.isPet) {
                if (((Pet) this.player).master.charms.tdDeTu > System.currentTimeMillis()) {
                    tiemNang += tn * 2;
                }
                if (((Pet) this.player).master.nPoint != null && ((Pet) this.player).master.nPoint.tlTNSMPet > 0) {
                    tiemNang += tn / 100 * (((Pet) this.player).master.nPoint.tlTNSMPet + 100);
                }
            }
            if (TimeUtil.checkTime(EventDAO.getRemainingTimeToIncreasePotentialAndPower())) {
                tiemNang *= 2;
            }
            if (MapService.gI().isMapNguHanhSon(this.player.zone.map.mapId)) {
                tiemNang *= 4;
            }
            if (MapService.gI().isMapBanDoKhoBau(this.player.zone.map.mapId)) {
                tiemNang *= 3;
            }
            tiemNang *= Manager.RATE_EXP_SERVER;
            tiemNang = calSubTNSM(tiemNang);
            if (tiemNang <= 0) {
                tiemNang = 1;
            }
        } else {
            tiemNang = 0;
        }
        return tiemNang;
    }

    public double calSubTNSM(double tiemNang) {
        if (power >= 1) {
            tiemNang /= 3;
        } else if (power == Long.MAX_VALUE) {
            tiemNang = 0;

        }
        return tiemNang;
    }

    public long getTileHutHp(boolean isMob) {
        if (isMob) {
            return (short) (this.tlHutHp + this.tlHutHpMob);
        } else {
            return this.tlHutHp;
        }
    }

    public long getTiLeHutMp() {
        return this.tlHutMp;
    }

    public double subDameInjureWithDeff(double dame) {
        double def = this.def;
        dame -= def;
        if (dame < 0) {
            dame = 1;
        }
        return dame;
    }

    /*------------------------------------------------------------------------*/
    public boolean canOpenPower() {
        return this.power >= getPowerLimit();
    }

    public double getPowerLimit() {
        if (powerLimit != null) {
            return powerLimit.getPower();
        }
        return 0;
    }

    public double getPowerNextLimit() {
        PowerLimit powerLimit = PowerLimitManager.getInstance().get(limitPower + 1);
        if (powerLimit != null) {
            return powerLimit.getPower();
        }
        return 0;
    }

    // **************************************************************************
    // POWER - TIEM NANG
    public void powerUp(double power) {
        this.power += power;
        TaskService.gI().checkDoneTaskPower(player, this.power);
    }

    public void tiemNangUp(double tiemNang) {
        this.tiemNang += tiemNang;
    }

    public void increasePoint(byte type, short point) {
        if (powerLimit == null) {
            return;
        }
        if (point <= 0) {
            return;
        }
        boolean updatePoint = false;
        double tiemNangUse = 0;
        if (type == 0) {
            int pointHp = point * 20;
            tiemNangUse = point * (2 * (this.hpg + 1000) + pointHp - 20) / 2;
            if ((this.hpg + pointHp) <= powerLimit.getHp()) {
                if (doUseTiemNang(tiemNangUse)) {
                    hpg += pointHp;
                    updatePoint = true;
                }
            } else {
                Service.gI().sendThongBao(player, "HP của bạn đã đạt mức tối đa");
                Service.gI().sendMoney(player);
                return;
            }
        }
        if (type == 1) {
            int pointMp = point * 20;
            tiemNangUse = point * (2 * (this.mpg + 1000) + pointMp - 20) / 2;
            if ((this.mpg + pointMp) <= powerLimit.getMp()) {
                if (doUseTiemNang(tiemNangUse)) {
                    mpg += pointMp;
                    updatePoint = true;
                }
            } else {
                Service.gI().sendThongBao(player, "KI của bạn đã đạt mức tối đa");
                Service.gI().sendMoney(player);
                return;
            }
        }
        if (type == 2) {
            tiemNangUse = point * (2 * this.dameg + point - 1) / 2 * 100;
            if ((this.dameg + point) <= powerLimit.getDamage()) {
                if (doUseTiemNang(tiemNangUse)) {
                    dameg += point;
                    updatePoint = true;
                }
            } else {
                Service.gI().sendThongBao(player, "Sức đánh của bạn đã đạt mức tối đa");
                Service.gI().sendMoney(player);
                return;
            }
        }
        if (type == 3) {
            tiemNangUse = 2 * (this.defg + 5) / 2 * 100000;
            if ((this.defg + point) <= powerLimit.getDefense()) {
                if (doUseTiemNang(tiemNangUse)) {
                    defg += point;
                    updatePoint = true;
                }
            } else {
                Service.gI().sendThongBao(player, "Giáp của bạn đã đạt mức tối đa");
                Service.gI().sendMoney(player);
                return;
            }
        }
        if (type == 4) {
            tiemNangUse = 50000000L;
            for (int i = 0; i < this.critg; i++) {
                tiemNangUse *= 5L;
            }
            if ((this.critg + point) <= powerLimit.getCritical()) {
                if (doUseTiemNang(tiemNangUse)) {
                    critg += point;
                    updatePoint = true;
                }
            } else {
                Service.gI().sendThongBao(player, "Chí mạng của bạn đã đạt mức tối đa");
                Service.gI().sendMoney(player);
                return;
            }
        }
        if (updatePoint) {
            Service.gI().point(player);
        }
    }

    // public void increasePoint(byte type, short point) {
    // if (point <= 0 || point > 100) {
    // return;
    // }
    // long tiemNangUse;
    // if (type == 0) {
    // int pointHp = point * 20;
    // tiemNangUse = point * (2 * (this.hpg + 1000) + pointHp - 20) / 2;
    // if ((this.hpg + pointHp) <= getHpMpLimit()) {
    // if (doUseTiemNang(tiemNangUse)) {
    // hpg += pointHp;
    // }
    // } else {
    // Service.gI().sendThongBaoOK(player, "Vui lòng mở giới hạn sức mạnh");
    // return;
    // }
    // }
    // if (type == 1) {
    // int pointMp = point * 20;
    // tiemNangUse = point * (2 * (this.mpg + 1000) + pointMp - 20) / 2;
    // if ((this.mpg + pointMp) <= getHpMpLimit()) {
    // if (doUseTiemNang(tiemNangUse)) {
    // mpg += pointMp;
    // }
    // } else {
    // Service.gI().sendThongBaoOK(player, "Vui lòng mở giới hạn sức mạnh");
    // return;
    // }
    // }
    // if (type == 2) {
    // TaskService.gI().checkDoneTaskNangCS(player);
    // tiemNangUse = point * (2 * this.dameg + point - 1) / 2 * 100;
    // if ((this.dameg + point) <= getDameLimit()) {
    // if (doUseTiemNang(tiemNangUse)) {
    // dameg += point;
    // }
    // TaskService.gI().checkDoneTaskNangCS(player);
    // } else {
    // Service.gI().sendThongBaoOK(player, "Vui lòng mở giới hạn sức mạnh");
    // return;
    // }
    // }
    // if (type == 3) {
    // tiemNangUse = 2 * (this.defg + 5) / 2 * 100000;
    // if ((this.defg + point) <= getDefLimit()) {
    // if (doUseTiemNang(tiemNangUse)) {
    // defg += point;
    // }
    // } else {
    // Service.gI().sendThongBaoOK(player, "Vui lòng mở giới hạn sức mạnh");
    // return;
    // }
    // }
    // if (type == 4) {
    // tiemNangUse = 50000000L;
    // for (int i = 0; i < this.critg; i++) {
    // tiemNangUse *= 5L;
    // }
    // if ((this.critg + point) <= getCritLimit()) {
    // if (doUseTiemNang(tiemNangUse)) {
    // critg += point;
    // }
    // } else {
    // Service.gI().sendThongBaoOK(player, "Vui lòng mở giới hạn sức mạnh");
    // return;
    // }
    // }
    // Service.gI().point(player);
    // }
    private boolean doUseTiemNang(double tiemNang) {
        if (this.tiemNang < tiemNang) {
            Service.gI().sendThongBaoOK(player, "Bạn không đủ tiềm năng");
            return false;
        }
        if (this.tiemNang >= tiemNang && this.tiemNang - tiemNang >= 0) {
            this.tiemNang -= tiemNang;
            TaskService.gI().checkDoneTaskUseTiemNang(player);
            return true;
        }
        return false;
    }

    public double getFullTN() {
        double tnhp = 0, tnki = 0, tnsd = 0, tng = 0, tncm = 0;

        if (hpg > 0) {
            tnhp = (((hpg / 20L) * (50L + (50L + (hpg / 20L) - 1L)) / 2L) * 20L);
        }
        if (mpg > 0) {
            tnki = (((mpg / 20L) * (50L + (50L + (mpg / 20L) - 1L)) / 2L) * 20L);
        }
        if (dameg > 0) {
            tnsd = ((dameg * (dameg - 1L) * 100L) / 2L);
        }
        if (defg > 0) {
            tng = ((defg * (500000L + (500000L + (defg - 1L) * 100000L))) / 2L);
        }
        if (critg > 0) {
            tncm = ((50L * (((long) Math.pow(5L, critg) - 1L)) / (5L - 1L) * 1000000L));
        }
        return tnhp + tnki + tnsd + tng + tncm;
    }

    // --------------------------------------------------------------------------
    private long lastTimeHoiPhuc;
    private long lastTimeHoiStamina;

    public void update() {
        if (player != null && player.effectSkill != null) {
            if (player.effectSkill.isCharging && player.effectSkill.countCharging < 10) {
                int tiLeHoiPhuc = SkillUtil.getPercentCharge(player.playerSkill.skillSelect.point);
                if (player.effectSkill.isCharging && !player.isDie() && !player.effectSkill.isHaveEffectSkill()
                        && (hp < hpMax || mp < mpMax)) {
                    double hpRecovered = hpMax / 100 * tiLeHoiPhuc;
                    double mpRecovered = mpMax / 100 * tiLeHoiPhuc;

                    PlayerService.gI().hoiPhuc(player, hpRecovered, mpRecovered);

                    if (player.effectSkill.countCharging % 3 == 0) {
                        Service.gI().chat(player, "Phục hồi năng lượng " + getCurrPercentHP() + "%");
                    }
                } else {
                    EffectSkillService.gI().stopCharge(player);
                }
                if (++player.effectSkill.countCharging >= 10) {
                    EffectSkillService.gI().stopCharge(player);
                }
            }

            if (Util.canDoWithTime(lastTimeHoiPhuc, 30000)) {
                PlayerService.gI().hoiPhuc(this.player, hpHoi, mpHoi);
                this.lastTimeHoiPhuc = System.currentTimeMillis();
            }

            if (Util.canDoWithTime(lastTimeHoiStamina, 60000) && this.stamina < this.maxStamina) {
                this.stamina++;
                this.lastTimeHoiStamina = System.currentTimeMillis();

                if (!this.player.isBoss && !this.player.isPet) {
                    PlayerService.gI().sendCurrentStamina(this.player);
                }
            }
        }
    }

    public void dispose() {
        this.intrinsic = null;
        this.player = null;
        this.tlHp = null;
        this.tlMp = null;
        this.tlDef = null;
        this.tlDame = null;
        this.tlDameAttMob = null;
        this.tlTNSM = null;
    }
}
