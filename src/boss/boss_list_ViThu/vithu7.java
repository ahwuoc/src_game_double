package boss.boss_list_ViThu;

import boss.Boss;
import boss.BossID;
import boss.BossStatus;
import boss.BossesData;
import item.Item.ItemOption;
import map.ItemMap;
import player.Player;
import services.Service;
import utils.Util;

public class vithu7 extends Boss {
    //fg

    private long st;

    public vithu7() throws Exception {
        super(BossID.vithu7, false, true, BossesData.vithu7);
    }

    @Override
    public void moveTo(int x, int y) {
        if (this.currentLevel == 1) {
            return;
        }
        super.moveTo(x, y);
    }

    @Override
    public void reward(Player plKill) {
        
          int diemsb = Util.nextInt(1, 2); //50 sd 
        plKill.point_sb += diemsb;
         Service.getInstance().sendThongBaoAllPlayer( "Bạn \n" + plKill.name  + "\nNhận Được " + diemsb + " \nĐiểm Săn Boss");        
        
       int diemvithu = 1;
        plKill.point_vithu += diemvithu;
         Service.getInstance().sendThongBaoAllPlayer( "Bạn \n" + plKill.name  + "\nNhận Được " + diemvithu + " \nĐiểm Vĩ Thú");          
        
        
   // 3. Rơi item thường (10–15 món)
    int[] itemThuong = {1204,  1206,1568,1150,1161,1162,1163,1164}; // thêm nhiều loại item thường nếu muốn
    int soLuongThuong = Util.nextInt(10, 15);
    for (int i = 0; i < soLuongThuong; i++) {
        int itemId = itemThuong[Util.nextInt(itemThuong.length)];
        int x = this.location.x + Util.nextInt(-40, 40);
        int y = this.zone.map.yPhysicInTop(x, this.location.y - 24);
        Service.gI().dropItemMap(this.zone, new ItemMap(zone, itemId, 1, x, y, plKill.id));
    }

    // 4. Rơi item hiếm (tỷ lệ 20%)
    int[] itemHiem = {1260, 1262};
    for (int itemId : itemHiem) {
        if (Util.isTrue(20, 100)) {
            int x = this.location.x + Util.nextInt(-30, 30);
            int y = this.zone.map.yPhysicInTop(x, this.location.y - 24);
            Service.gI().dropItemMap(this.zone, new ItemMap(zone, itemId, 1, x, y, plKill.id));
            Service.getInstance().sendThongBaoAllPlayer(
                "⚡ " + plKill.name + " đã nhặt được vật phẩm hiếm [ID " + itemId + "] từ boss " + this.name + "!");
        }
    }

    // 4. Rớt item siêu hiếm (3% mỗi món)
    int[] itemSieuHiem = {1679,1680,1681,1682,1683}; // ví dụ: trang bị VIP, ngọc rồng
    for (int itemId : itemSieuHiem) {
        if (Util.nextInt(100) < 3) {
            int x = this.location.x + Util.nextInt(-20, 20);
            int y = this.zone.map.yPhysicInTop(x, this.location.y - 24);
            Service.gI().dropItemMap(this.zone, new ItemMap(zone, itemId, 1, x, y, plKill.id));
            Service.getInstance().sendThongBaoAllPlayer(
                "🌟 " + plKill.name + " cực kỳ may mắn! Nhận được SIÊU VẬT PHẨM [ID " + itemId + "] từ boss " + this.name + "!");
        }
    }

    if (this.currentLevel == 1) return;
}
}

