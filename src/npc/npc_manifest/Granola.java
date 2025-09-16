package npc.npc_manifest;

import consts.ConstNpc;
import item.Item;
import npc.Npc;
import player.Player;
import services.InventoryService;
import services.Service;
import services.func.ChangeMapService;

public class Granola extends Npc {

    public Granola(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            switch (mapId) {
                case 173 -> {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "|2|ƯỚC MƠ CỦA TA LÀ TIÊU DIỆT TÊN GOMAH ĐỂ TRỞ LẠI LÀM VUA ĐỊA NGỤC.\n"
                            + "|0|Tên Gomah đã lấy đi con mắt địa ngục của ta, hãy giúp ta lấy lại nó.\n"
                            + "|1|Muốn đến địa ngục, ngươi cần 1 Thẻ Ngọc Rồng (ID: 1578).\n"
                            + "|3|Lưu ý: Khi qua địa ngục server sẽ trừ trực tiếp vào số thẻ ngọc rồng có sẵn.\n"
                            + "Người chơi cần thoát game để cập nhật số thẻ chính xác còn lại",
                            "Tới Địa Ngục");
                }
                default -> super.openBaseMenu(player);
            }
        }
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            if (player.iDMark.isBaseMenu()) {
                switch (mapId) {
                    case 173 -> {
                        if (select == 0) { // Người chơi chọn "Tới Địa Ngục"
                            // Dịch chuyển đến địa ngục
                            ChangeMapService.gI().changeMapNonSpaceship(player, 179, 300, 300);
                        } else if (select == 1) { 
                            return; 
                        }
                    }
                }
            }
        }
    }
}
