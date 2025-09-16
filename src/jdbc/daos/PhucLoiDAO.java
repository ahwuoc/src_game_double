/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jdbc.daos;

import item.Item;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import models.PhucLoiTemPlate;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
import services.ItemService;

/**
 *
 * @author Administrator
 */
public class PhucLoiDAO {
    public static byte[] map = new byte[1000];
    public static List<Integer> list = new ArrayList<Integer>();
    public static PhucLoiTemPlate[] loadPhucLois(Connection con){
        PhucLoiTemPlate[] PHUC_LOIS = new PhucLoiTemPlate[1000];
        try{
            PreparedStatement ps = con.prepareStatement("select * from phuc_loi");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int type = rs.getInt("type");
                int param = rs.getInt("param");
                map[id] = (byte)type;
                PHUC_LOIS[id] = new PhucLoiTemPlate(name,param);
                list.add(id);
            }
            ps = con.prepareStatement("select * from items_phuc_loi");
            rs = ps.executeQuery();
            while(rs.next()){
                int id = rs.getInt("id");
                int id_item = rs.getInt("id_item");
                int id_phuc_loi = rs.getInt("id_phuc_loi");
                int quatity = rs.getInt("quantity");
                
                Item item = ItemService.gI().createNewItem((short)id_item, quatity);
                JSONArray dataOption = (JSONArray)JSONValue.parse(rs.getString("options"));
                
                for(int i = 0;i<dataOption.size();i++){
                    JSONArray dataItem = (JSONArray) JSONValue.parse(dataOption.get(i).toString());
                    int optionID = Integer.parseInt(String.valueOf(dataItem.get(0)));
                    long param = Long.parseLong(String.valueOf(dataItem.get(1)));
                    item.itemOptions.add(new Item.ItemOption(optionID,param));
                }
                if(PHUC_LOIS[id_phuc_loi] == null) continue;
                PHUC_LOIS[id_phuc_loi].items.add(item);
            }
            return PHUC_LOIS;
        }catch(Exception e){
            e.printStackTrace();
            return new PhucLoiTemPlate[5];
        }
    }
}
