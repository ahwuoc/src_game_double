//package boss.boss_list_ViThu;
//
//
//
//import boss.Boss;
//import boss.BossID;
//import boss.BossStatus;
//import boss.BossesData;
//import item.Item.ItemOption;
//import map.ItemMap;
//import player.Player;
//import services.Service;
//import utils.Util;
//
//public class conmemay extends Boss {
//    //fg
//
//    private long st;
//
//    public conmemay() throws Exception {
//        super(BossID.conmemay, false, true, BossesData.conmemay);
//    }
//
//    @Override
//    public void moveTo(int x, int y) {
//        if (this.currentLevel == 1) {
//            return;
//        }
//        super.moveTo(x, y);
//    }
//
//    @Override
//    public void reward(Player plKill) {
//
//      int diemsb = Util.nextInt(1, 2); //50 sd 
//        plKill.point_sb += diemsb;
//         Service.getInstance().sendThongBaoAllPlayer( "Bạn \n" + plKill.name  + "\nNhận Được " + diemsb + " \nĐiểm Săn Boss");        
//        
//     
//        
//        for (int i = 1; i < Util.nextInt(1, 3) + 1; i++) {
//            Service.gI().dropItemMap(this.zone, new ItemMap(zone, 1204, 1, this.location.x - i * 10, this.zone.map.yPhysicInTop(this.location.x,
//                    this.location.y - 24), plKill.id));
//        }
//
//        if (this.currentLevel == 1) {
//            return;
//        }
//    }
//
//    @Override
//    protected void notifyJoinMap() {
//        if (this.currentLevel == 1) {
//            return;
//        }
//        super.notifyJoinMap();
//    }
//
//    @Override
//    public void doneChatS() {
//        this.changeStatus(BossStatus.JOIN_MAP);
//    }
//
//    @Override
//    public void autoLeaveMap() {
//        if (Util.canDoWithTime(st, 900000)) {
//            this.leaveMapNew();
//        }
//        if (this.zone != null && this.zone.getNumOfPlayers() > 0) {
//            st = System.currentTimeMillis();
//        }
//    }
//
//    @Override
//    public void joinMap() {
//        super.joinMap();
//        st = System.currentTimeMillis();
//    }
//
//}
//
