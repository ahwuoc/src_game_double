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

public class tokuda extends Boss {
    //fg

    private long st;

    public tokuda() throws Exception {
        super(BossID.tokuda, false, true, BossesData.tokuda);
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
    // 1. Cộng điểm săn boss
    int diemsb = Util.nextInt(1, 2);
    plKill.point_sb += diemsb;
    Service.getInstance().sendThongBaoAllPlayer(
        "⚔️ " + plKill.name + " đã tiêu diệt " + this.name + " và nhận được " + diemsb + " điểm Săn Boss!");

    // 2. Rớt item thường (rất nhiều)
    int[] itemThuong = {1204,  1206,1568,1150,1161,1162,1163,1164};
    int soLuongThuong = Util.nextInt(10, 20); // rơi 10~20 item
    for (int i = 0; i < soLuongThuong; i++) {
        int itemId = itemThuong[Util.nextInt(itemThuong.length)];
        int x = this.location.x + Util.nextInt(-50, 50);
        int y = this.zone.map.yPhysicInTop(x, this.location.y - 24);
        Service.gI().dropItemMap(this.zone, new ItemMap(zone, itemId, 1, x, y, plKill.id));
    }

    // 3. Rớt item hiếm (15% mỗi món)
    int[] itemHiem = {1260, 1261,1865,1866 };
    for (int itemId : itemHiem) {
        if (Util.nextInt(100) < 15) {
            int x = this.location.x + Util.nextInt(-30, 30);
            int y = this.zone.map.yPhysicInTop(x, this.location.y - 24);
            Service.gI().dropItemMap(this.zone, new ItemMap(zone, itemId, 1, x, y, plKill.id));
            Service.getInstance().sendThongBaoAllPlayer(
                "✨ " + plKill.name + " đã nhặt được vật phẩm hiếm [ID " + itemId + "] từ " + this.name + "!");
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