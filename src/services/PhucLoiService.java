/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import item.Item;
import java.util.ArrayList;
import java.util.List;
import jdbc.daos.PhucLoiDAO;
import models.PhucLoiTemPlate;
import network.Message;
import player.PhucLoi;
import player.Player;
import server.Manager;
import shop.ShopService;

/**
 *
 * @author Administrator
 */
public class PhucLoiService {
    public static void receive(){
        
    }
    public static void readMSG(Player player, Message msg){
        try{
            int type = msg.reader().readByte();
            switch(type){
                case 0 -> sendData(player);
                case 1 -> {
                    int id = msg.reader().readInt();
                    ReceiveLine(player,id);
                }
                case 5 -> {
                    ShopService.gI().opendShop(player, "CUAHANG", false);
                }
                default -> {
                }
            }
        }catch(Exception e){
            
        }
    }
    public static void sendData(Player player){
        Message msg = new Message(70);
        try{
            msg.writer().writeByte(0);
            //send tieu de
            msg.writer().writeUTF(("Hôm nay bạn đã online Hôm Nay "+player.mocPhucLoi[0]+" phút"));
            msg.writer().writeUTF("Điểm danh ngày! [ Đã Đăng Nhập ] " + player.mocPhucLoi[1] +" Ngày");
            msg.writer().writeUTF("Hãy nạp để nhận quà nào bạn ơi! Hôm nay bạn đã nạp " + player.mocPhucLoi[2] +" VND");
            msg.writer().writeUTF("Hãy nạp để nhận quà nào bạn ơi! Hôm nay bạn đã nạp " + player.mocPhucLoi[3] +" VND");
            msg.writer().writeUTF("Tiêu càng nhiều, vận may càng lớn! Bạn đã tiêu " + player.mocPhucLoi[4] + " VND");
            
            int size = PhucLoiDAO.list.size();
            msg.writer().writeInt(size);
            for(int x : PhucLoiDAO.list){
                PhucLoiTemPlate p = Manager.PHUC_LOIS[x];
                msg.writer().writeInt(x);
                msg.writer().writeByte(PhucLoiDAO.map[x]);
                msg.writer().writeUTF(p.name);
//                msg.writer().writeInt(p.param);
                msg.writer().writeInt(p.items.size());
                for(int i=0;i<p.items.size();i++){
                    msg.writer().writeInt(p.items.get(i).template.id);
                    msg.writer().writeInt(p.items.get(i).quantity);
                    msg.writer().writeInt(p.items.get(i).itemOptions.size());
                    for(int j = 0;j < p.items.get(i).itemOptions.size();j++){
                        msg.writer().writeInt( p.items.get(i).itemOptions.get(j).optionTemplate.id);
                        msg.writer().writeLong( p.items.get(i).itemOptions.get(j).param);
                    }
                }
                //send nut
                if(player.phucLoi[x] == null){
                    player.phucLoi[x] = new PhucLoi(x,0,System.currentTimeMillis());
                }
                if(player.phucLoi[x].trace != 0){
                    msg.writer().writeByte(0);
                }else{
                    if(player.mocPhucLoi[PhucLoiDAO.map[x]] >= p.param){
                        msg.writer().writeByte(1);
                    }else{
                        msg.writer().writeByte(2);
                    }
                }
                
            }
            player.sendMessage(msg);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public static void ReceiveLine(Player player, int id){
        try{
            if(id < 0 || id > 5000) return;
            byte type = PhucLoiDAO.map[id];
            if(player.phucLoi[id] == null){
                player.phucLoi[id] = new PhucLoi(id,0,System.currentTimeMillis());
            }
            if(player.phucLoi[id].trace != 0){
                Service.gI().sendThongBao(player, "Bạn đã nhận Rồi!");
                return;
            }
            if(player.mocPhucLoi[type] >= Manager.PHUC_LOIS[id].param){
                for(Item it : Manager.PHUC_LOIS[id].items){
                    Item itn = ItemService.gI().copyItem(it);
                    InventoryService.gI().addItemBag(player, itn,999999);
                }
                InventoryService.gI().sendItemBag(player);
                player.phucLoi[id].trace = 1;
                player.phucLoi[id].lastTimeReceive = System.currentTimeMillis();
                Service.gI().sendThongBao(player, "Nhận thành công!");
                PhucLoiService.sendData(player);
            }else{
                Service.gI().sendThongBao(player, "Chưa Đủ Điều Kiện!");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
