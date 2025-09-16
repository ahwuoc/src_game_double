package npc.npc_manifest;

/**
 *
 */
import consts.ConstNpc;
import consts.ConstTask;
import consts.ConstTaskBadges;
import item.Item;

import java.util.ArrayList;
import java.util.List;

import jdbc.daos.PlayerDAO;
import models.SuperRank.SuperRankService;
import npc.Npc;
import player.Player;
import server.Manager;
import services.InventoryService;
import services.ItemService;
import services.NpcService;
import services.PetService;
import services.Service;
import services.TaskService;
import services.func.Input;
import services.func.TopService;
import shop.ShopService;
import task.Badges.BadgesTaskService;
import utils.Util;

public class cadich extends Npc {

    public cadich(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

   
    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                this.createOtherMenu(player, ConstNpc.BASE_MENU,
                        "|7|Xin Chào Anh Chị Đến Với Saga\n"
                                + "|4|Đây Là Bảng Xếp Hạng Đua Top\n"
                                + "Wesite:https://Dragonballsaga.vn",
                        "TOP\nSỨC MẠNH",
                        "TOP\nNHIỆM VỤ",
                        "TOP\nSIÊU HẠNG",
                        "TOP\nSỰ KIỆN",
                        "TOP\nNẠP TIỀN");
            }
        }
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            if (player.iDMark.isBaseMenu()) {
                switch (select) {
                    case 0: // mã quà tặng
                       TopService.gI().showListTopPower(player);
                        break;
                      case 1: // mã quà tặng
                       TopService.gI().showListTopTask(player);
                        break;   
                      case 2: // mã quà tặng
                        SuperRankService.gI().topList(player, 3);
                        break;   
                       case 3: // mã quà tặng
                       TopService.gI().showListTopEvent(player);
                        break;    
                        case 4: // mã quà tặng
                            Service.gI().showListTop(player, Manager.TopNap);
                 //      TopService.gI().showListTopVnd(player);
                        break;    
                        
                        
                        
                 
                }
            }
        }
    }
}
