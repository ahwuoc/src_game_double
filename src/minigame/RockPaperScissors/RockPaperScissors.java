
/*
 * Copyright by HAIRMOD
 */

package minigame.RockPaperScissors;

import consts.ConstFont;
import npc.Npc;
import npc.npc_manifest.LyTieuNuong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import player.Player;
import services.ItemTimeService;
import services.Service;
import utils.Util;

public class RockPaperScissors {

    public static final byte KEO = 0;
    public static final byte BUA = 1;
    public static final byte BAO = 2;

    private static final Logger log = LoggerFactory.getLogger(RockPaperScissors.class);

    public static final long timePlay = 15; // 15 giây để chơi

    // Các mức cược lớn (dùng kiểu long + hậu tố L)
    public static final long COST_0 = 500_000_0000L;      // 500 triệu vàng
    public static final long COST_1 = 1_000_000_0000L;    // 1 tỷ vàng
    public static final long COST_2 = 5_000_000_0000L;    // 5 tỷ vàng
//    public static final long COST_3 = 10_000_000_0000L;   // 10 tỷ vàng
//    public static final long COST_4 = 100_000_000_0000L;  // 100 tỷ vàng

    /**
     * Xử lý khi người chơi chọn mức cược từ menu chính.
     */
    public static void confirmMenu(Npc npc, Player player, int select) {
        long tienDatCuoc;

        switch (select) {
            case 0 -> tienDatCuoc = COST_0;
            case 1 -> tienDatCuoc = COST_1;
            case 2 -> tienDatCuoc = COST_2;
//            case 3 -> tienDatCuoc = COST_3;
//            case 4 -> tienDatCuoc = COST_4;
            default -> {
                Service.gI().sendThongBao(player, "Mức cược không hợp lệ!");
                return;
            }
        }

        String money = Util.numberFormat(tienDatCuoc);
        player.iDMark.setMoneyKeoBuaBao((int) tienDatCuoc);
        player.iDMark.setTimePlayKeoBuaBao(System.currentTimeMillis() + (timePlay * 1000));
        ItemTimeService.gI().sendTextTimeKeoBuaBao(player, (int) timePlay);

        npc.createOtherMenu(player, LyTieuNuong.ConstMiniGame.MENU_PLAY_KEO_BUA_BAO,
                ConstFont.BOLD_GREEN + "Mức vàng cược: " + money + "\n" +
                        "Hãy chọn Kéo, Búa hoặc Bao\n" +
                        ConstFont.BOLD_RED + "Thời gian " + timePlay + " giây bắt đầu",
                "Kéo", "Búa", "Bao", "Đổi\nmức cược", "Nghỉ chơi");
    }

    /**
     * Xử lý khi người chơi chọn hành động chơi hoặc thay đổi mức cược.
     */
    public static void confirmPlay(Npc npc, Player player, int select) {
        switch (select) {
            case KEO, BUA, BAO -> {
                long tienCuoc = player.iDMark.getMoneyKeoBuaBao();

                if (player.inventory.gold < tienCuoc) {
                    long thieu = tienCuoc - player.inventory.gold;
                    Service.gI().sendThongBao(player, "Bạn không đủ vàng, còn thiếu " + Util.numberToMoney(thieu) + " vàng nữa");
                    return;
                }

                // Lưu lựa chọn của người chơi và máy chọn ngẫu nhiên
                player.iDMark.setKeoBuaBaoPlayer((byte) select);
                player.iDMark.setKeoBuaBaoServer((byte) Util.nextInt(3));

                // Xử lý kết quả
                int result = RockPaperScissorsService.checkWinLose(player);
                if (result == 1) {
                    RockPaperScissorsService.winKeoBuaBao(npc, player);
                } else if (result == 2) {
                    RockPaperScissorsService.loseKeoBuaBao(npc, player);
                } else {
                    RockPaperScissorsService.hoaKeoBuaBao(npc, player);
                }
            }
            case 3 -> {
                // Hiển thị menu chọn mức cược lại
                npc.createOtherMenu(player, LyTieuNuong.ConstMiniGame.MENU_KEO_BUA_BAO,
                        "Hãy chọn mức cược.",
                        "5000 Tr vàng",
                        "10 Tỷ vàng",
                        "50 Tỷ vàng");
//                        "100 Tỷ vàng",
//                        "1000 Tỷ vàng");
            }
            default -> {
                // Thoát hoặc chọn sai
                Service.gI().sendThongBao(player, "Cảm ơn bạn đã tham gia!");
            }
        }
    }
}
