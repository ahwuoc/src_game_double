package boss.boss_list_doremon;
import boss.Boss;
import boss.BossID;
import boss.BossStatus;
import boss.BossesData;
import item.Item.ItemOption;
import map.ItemMap;
import player.Player;
import services.Service;
import utils.Util;

public class xeko extends Boss {

    private long st;

    public xeko() throws Exception {
        super(BossID.xeko, false, true, BossesData.xeko);
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
        
        
          for (int i = 1; i < Util.nextInt(5, 15) + 1; i++) {
            Service.gI().dropItemMap(this.zone, new ItemMap(zone, 1884, 1, this.location.x - i * 10, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id));
        }

         int slcarot = Util.nextInt(1, 3);
        for (int i = 0; i < slcarot; i++) {
            ItemMap carot = new ItemMap(zone, 863, 1, 10 * i + this.location.x, zone.map.yPhysicInTop(this.location.x, 0), -1);
            carot.options.add(new ItemOption(50, Util.nextInt(5, 80)));
            carot.options.add(new ItemOption(77, Util.nextInt(1, 80)));
            carot.options.add(new ItemOption(103, Util.nextInt(1, 80)));
            carot.options.add(new ItemOption(5, Util.nextInt(1, 10)));
            carot.options.add(new ItemOption(237, 5));
             carot.options.add(new ItemOption(231, 5));
             if (Util.isTrue(997, 1000)) {
             carot.options.add(new ItemOption(93, Util.nextInt(1, 7)));
            }
            Service.getInstance().dropItemMap(this.zone, carot);
        
        }
        if (this.currentLevel == 1) {
            return;
        }
    }

    @Override
    protected void notifyJoinMap() {
        if (this.currentLevel == 1) {
            return;
        }
        super.notifyJoinMap();
    }

    @Override
    public void doneChatS() {
        this.changeStatus(BossStatus.JOIN_MAP);
    }

    @Override
    public void autoLeaveMap() {
        if (Util.canDoWithTime(st, 900000)) {
            this.leaveMapNew();
        }
        if (this.zone != null && this.zone.getNumOfPlayers() > 0) {
            st = System.currentTimeMillis();
        }
    }

    @Override
    public void joinMap() {
        super.joinMap();
        st = System.currentTimeMillis();
    }

  
  
}
