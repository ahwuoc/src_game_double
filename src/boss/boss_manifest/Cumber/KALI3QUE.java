package boss.boss_manifest.Cumber;


import boss.*;
import consts.ConstPlayer;
import consts.ConstTask;
import item.Item;
import map.ItemMap;
import player.Player;
import services.*;
import utils.Util;

public class KALI3QUE extends Boss {

    private long st;
    private int timeLeaveMap;

    public KALI3QUE() throws Exception {
        super(BossID.KALI3QUE, false, true, BossesData.KALI3QUE, BossesData.KALI3QUE);
    }

    @Override
public void reward(Player plKill) {
    // 1. Đồ tùy loại (TL) – tỉ lệ 15%
    if (Util.isTrue(15, 100)) {
        ItemMap it = ItemService.gI().randDoTL(this.zone, 1, this.location.x,
            this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24), plKill.id);
        Service.gI().dropItemMap(this.zone, it);
    }

    // 2. Item thường – rơi ngẫu nhiên nhiều món
    int[] itemThuong = {1204,  1206,1568,1150,1161,1162,1163,1164,457,861};
    int countThuong = Util.nextInt(5, 10);
    for (int i = 0; i < countThuong; i++) {
        int itemId = itemThuong[Util.nextInt(itemThuong.length)];
        int x = this.location.x + Util.nextInt(-30, 30);
        int y = this.zone.map.yPhysicInTop(x, this.location.y - 24);
        Service.gI().dropItemMap(this.zone, new ItemMap(zone, itemId, 1, x, y, plKill.id));
    }

    // 3. Item hiếm – xác suất 20%
    if (Util.isTrue(20, 100)) {
        int itemId = 1866; // ví dụ item hiếm
        int x = this.location.x + Util.nextInt(-15, 15);
        int y = this.zone.map.yPhysicInTop(x, this.location.y - 24);
        Service.gI().dropItemMap(this.zone, new ItemMap(zone, itemId, 1, x, y, plKill.id));
        Service.getInstance().sendThongBaoAllPlayer("⚡ " + plKill.name + " vừa nhận được vật phẩm hiếm từ " + this.name + "!");
    }

    // 4. Item siêu hiếm – xác suất 3%
    if (Util.isTrue(3, 100)) {
        int itemId = 1865;
        
        int x = this.location.x + Util.nextInt(-10, 10);
        int y = this.zone.map.yPhysicInTop(x, this.location.y - 24);
        Service.gI().dropItemMap(this.zone, new ItemMap(zone, itemId,1, x, y, plKill.id));
        Service.getInstance().sendThongBaoAllPlayer("🌟 " + plKill.name + " đã nhặt được SIÊU VẬT PHẨM từ boss " + this.name + "!");
    }
}
}