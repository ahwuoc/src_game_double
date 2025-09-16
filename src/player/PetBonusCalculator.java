package player;

import consts.ConstPlayer;
import consts.ConstPet;

public class PetBonusCalculator {

    private static final int HP_BONUS_PIC = 20;
    private static final int HP_BONUS_MABU = 40;
    private static final int HP_BONUS_BERUS = 60;
    private static final int HP_BONUS_BLACK = 80;
    private static final int HP_BONUS_XENCON = 100;
    private static final int HP_BONUS_BROLY = 120;
    private static final int HP_BONUS_BLACK_ALT = 140;
    private static final int HP_BONUS_BLACK_ROSE = 160;
    private static final int HP_BONUS_ZAMASU = 180;
    private static final int HP_BONUS_CUMBER = 200;
    private static final int HP_BONUS_MABU0 = 220;

    private static final int MP_BONUS_PIC = 20;
    private static final int MP_BONUS_MABU = 40;
    private static final int MP_BONUS_BERUS = 60;
    private static final int MP_BONUS_BLACK = 80;
    private static final int MP_BONUS_XENCON = 100;
    private static final int MP_BONUS_BROLY = 120;
    private static final int MP_BONUS_BLACK_ALT = 140;
    private static final int MP_BONUS_BLACK_ROSE = 160;
    private static final int MP_BONUS_ZAMASU = 180;
    private static final int MP_BONUS_CUMBER = 200;
    private static final int MP_BONUS_MABU0 = 220;

    private static final int DAME_BONUS_PIC_DIVISOR = 5;
    private static final int DAME_BONUS_MABU_DIVISOR = 4;
    private static final int DAME_BONUS_BERUS_MULTIPLIER = 3;
    private static final int DAME_BONUS_BERUS_DIVISOR = 10;
    private static final int DAME_BONUS_BLACK_MULTIPLIER = 2;
    private static final int DAME_BONUS_BLACK_DIVISOR = 5;
    private static final int DAME_BONUS_XENCON_MULTIPLIER = 3;
    private static final int DAME_BONUS_XENCON_DIVISOR = 4;
    private static final int DAME_BONUS_BROLY_MULTIPLIER = 4;
    private static final int DAME_BONUS_BROLY_DIVISOR = 5;
    private static final int DAME_BONUS_BLACK_ALT_MULTIPLIER = 5;
    private static final int DAME_BONUS_BLACK_ALT_DIVISOR = 6;
    private static final int DAME_BONUS_BLACK_ROSE_MULTIPLIER = 6;
    private static final int DAME_BONUS_BLACK_ROSE_DIVISOR = 7;
    private static final int DAME_BONUS_ZAMASU_MULTIPLIER = 7;
    private static final int DAME_BONUS_ZAMASU_DIVISOR = 8;
    private static final int DAME_BONUS_CUMBER_MULTIPLIER = 8;
    private static final int DAME_BONUS_CUMBER_DIVISOR = 9;
    private static final int DAME_BONUS_MABU0_MULTIPLIER = 9;
    private static final int DAME_BONUS_MABU0_DIVISOR = 10;

    private Player player;

    public PetBonusCalculator(Player player) {
        this.player = player;
    }

    public boolean isPet() {
        return player.isPet;
    }

    public int getPetType() {
        if (!isPet()) {
            return -1;
        }
        return ((Pet) player).typePet;
    }

    public boolean isPetInFusion() {
        if (!isPet()) {
            return false;
        }
        return ((Pet) player).status == Pet.FUSION;
    }

    public boolean isMasterInFusion() {
        if (!isPet()) {
            return false;
        }
        Pet pet = (Pet) player;
        return pet.master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA
                || pet.master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2;
    }

    public double calculatePetHpBonus(double baseHp) {
        if (!isPet() || !isMasterInFusion()) {
            return 0;
        }

        int petType = getPetType();
        double bonus = 0;

        switch (petType) {
            case ConstPet.TYPE_PIC:
                bonus = baseHp * HP_BONUS_PIC / 100L;
                break;
            case ConstPet.TYPE_MABU:
                bonus = baseHp * HP_BONUS_MABU / 100L;
                break;
            case ConstPet.TYPE_BERUS:
                bonus = baseHp * HP_BONUS_BERUS / 100L;
                break;
            case ConstPet.TYPE_BLACK:
                bonus = baseHp * HP_BONUS_BLACK / 100L;
                break;
            case ConstPet.TYPE_XENCON:
                bonus = baseHp * HP_BONUS_XENCON / 100L;
                break;
            case ConstPet.TYPE_BROLY:
                bonus = baseHp * HP_BONUS_BROLY / 100L;
                break;
            case ConstPet.TYPE_BLACK_ALT:
                bonus = baseHp * HP_BONUS_BLACK_ALT / 100L;
                break;
            case ConstPet.TYPE_BLACK_ROSE:
                bonus = baseHp * HP_BONUS_BLACK_ROSE / 100L;
                break;
            case ConstPet.TYPE_ZAMASU:
                bonus = baseHp * HP_BONUS_ZAMASU / 100L;
                break;
            case ConstPet.TYPE_CUMBER:
                bonus = baseHp * HP_BONUS_CUMBER / 100L;
                break;
            case ConstPet.TYPE_MABU0:
                bonus = baseHp * HP_BONUS_MABU0 / 100L;
                break;
        }

        return bonus;
    }

    public double calculatePetMpBonus(double baseMp) {
        if (!isPet() || !isMasterInFusion()) {
            return 0;
        }

        int petType = getPetType();
        double bonus = 0;

        switch (petType) {
            case ConstPet.TYPE_PIC:
                bonus = baseMp * MP_BONUS_PIC / 100L;
                break;
            case ConstPet.TYPE_MABU:
                bonus = baseMp * MP_BONUS_MABU / 100L;
                break;
            case ConstPet.TYPE_BERUS:
                bonus = baseMp * MP_BONUS_BERUS / 100L;
                break;
            case ConstPet.TYPE_BLACK:
                bonus = baseMp * MP_BONUS_BLACK / 100L;
                break;
            case ConstPet.TYPE_XENCON:
                bonus = baseMp * MP_BONUS_XENCON / 100L;
                break;
            case ConstPet.TYPE_BROLY:
                bonus = baseMp * MP_BONUS_BROLY / 100L;
                break;
            case ConstPet.TYPE_BLACK_ALT:
                bonus = baseMp * MP_BONUS_BLACK_ALT / 100L;
                break;
            case ConstPet.TYPE_BLACK_ROSE:
                bonus = baseMp * MP_BONUS_BLACK_ROSE / 100L;
                break;
            case ConstPet.TYPE_ZAMASU:
                bonus = baseMp * MP_BONUS_ZAMASU / 100L;
                break;
            case ConstPet.TYPE_CUMBER:
                bonus = baseMp * MP_BONUS_CUMBER / 100L;
                break;
            case ConstPet.TYPE_MABU0:
                bonus = baseMp * MP_BONUS_MABU0 / 100L;
                break;
        }

        return bonus;
    }

    public double calculatePetDameBonus(double baseDame) {
        if (!isPet() || !isMasterInFusion()) {
            return 0;
        }

        int petType = getPetType();
        double bonus = 0;

        switch (petType) {
            case ConstPet.TYPE_PIC:
                bonus = baseDame / DAME_BONUS_PIC_DIVISOR;
                break;
            case ConstPet.TYPE_MABU:
                bonus = baseDame / DAME_BONUS_MABU_DIVISOR;
                break;
            case ConstPet.TYPE_BERUS:
                bonus = baseDame * DAME_BONUS_BERUS_MULTIPLIER / DAME_BONUS_BERUS_DIVISOR;
                break;
            case ConstPet.TYPE_BLACK:
                bonus = baseDame * DAME_BONUS_BLACK_MULTIPLIER / DAME_BONUS_BLACK_DIVISOR;
                break;
            case ConstPet.TYPE_XENCON:
                bonus = baseDame * DAME_BONUS_XENCON_MULTIPLIER / DAME_BONUS_XENCON_DIVISOR;
                break;
            case ConstPet.TYPE_BROLY:
                bonus = baseDame * DAME_BONUS_BROLY_MULTIPLIER / DAME_BONUS_BROLY_DIVISOR;
                break;
            case ConstPet.TYPE_BLACK_ALT:
                bonus = baseDame * DAME_BONUS_BLACK_ALT_MULTIPLIER / DAME_BONUS_BLACK_ALT_DIVISOR;
                break;
            case ConstPet.TYPE_BLACK_ROSE:
                bonus = baseDame * DAME_BONUS_BLACK_ROSE_MULTIPLIER / DAME_BONUS_BLACK_ROSE_DIVISOR;
                break;
            case ConstPet.TYPE_ZAMASU:
                bonus = baseDame * DAME_BONUS_ZAMASU_MULTIPLIER / DAME_BONUS_ZAMASU_DIVISOR;
                break;
            case ConstPet.TYPE_CUMBER:
                bonus = baseDame * DAME_BONUS_CUMBER_MULTIPLIER / DAME_BONUS_CUMBER_DIVISOR;
                break;
            case ConstPet.TYPE_MABU0:
                bonus = baseDame * DAME_BONUS_MABU0_MULTIPLIER / DAME_BONUS_MABU0_DIVISOR;
                break;
        }

        return bonus;
    }

    public boolean canApplySkillEffect() {
        return !isPet() || (isPet() && !isPetInFusion());
    }

    public boolean isPetEatingMeal() {
        if (!isPet()) {
            return false;
        }
        Pet pet = (Pet) player;
        return pet.itemTime != null && pet.master.itemTime.isEatMeal;
    }

    public boolean isMasterVip() {
        if (!isPet()) {
            return false;
        }
        Pet pet = (Pet) player;
        return pet.master.getSession() != null && pet.master.getSession().vip > 0;
    }

    public boolean isMasterUsingDeTu() {
        if (!isPet()) {
            return false;
        }
        Pet pet = (Pet) player;
        return pet.master.charms.tdDeTu > System.currentTimeMillis();
    }

    public Player getMaster() {
        if (!isPet()) {
            return null;
        }
        return ((Pet) player).master;
    }
}
