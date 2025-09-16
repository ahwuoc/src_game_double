package npc.npc_manifest;

/**
 *

 */

import consts.ConstNpc;
import npc.Npc;
import player.Player;
import services.func.ChangeMapService;
import utils.Util;

public class kaioshin extends Npc {

    public kaioshin(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            switch (mapId) {
                case 5 -> {
                    createOtherMenu(player, ConstNpc.BASE_MENU, "Ta Sẽ giúp Ngươi Dịch Chuyển Đến Vị Trí\nBất Cứ Nào Trên Vũ Trụ",
                            "Đến Map\nPseudo", "Đến Map\nNghĩa Địa", "Đến Map\nĐá quý", "Đến Map\nNgục Tù", "Đến Map\nRừng Cây", "Đến Map\nĐịa Ngục");
                    
                    
                }
              case 49 -> {
                    createOtherMenu(player, ConstNpc.BASE_MENU, "Ta Rất Thất Vọng Về Ngươi",
                            "Về Làng Aru");
             
               
                }
                default ->
                    super.openBaseMenu(player);
            }
        }
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            if (player.iDMark.isBaseMenu()) {
                switch (mapId) {
                    case 5 -> {
                        if (select == 0) {
                            ChangeMapService.gI().changeMapNonSpaceship(player, 182, 545, 168);
                        }
                           if (select == 1) {
                            ChangeMapService.gI().changeMapNonSpaceship(player, 181, 128, 264); 
                         }
                           if (select == 2) {
                            ChangeMapService.gI().changeMapNonSpaceship(player, 180, 205, 384);     
                            
                         }
                           if (select == 3) {
                            ChangeMapService.gI().changeMapNonSpaceship(player, 179, 461, 288);        
                             }
                           if (select == 4) {
                            ChangeMapService.gI().changeMapNonSpaceship(player, 177, 500, 360);    
                           }
                           if (select == 5) {
                            ChangeMapService.gI().changeMapNonSpaceship(player, 174, 500, 408);      
                            
                            
                            
                        }
                    }
                    
                    case 49 -> {
                        if (select == 0) {
                            ChangeMapService.gI().changeMapNonSpaceship(player, 0, Util.nextInt(700, 800), 432);
                        }
                    }
                }
            }
        }
    }

}
