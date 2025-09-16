package quayTamBao;

import java.util.ArrayList;
import java.util.List;

import item.Item.ItemOption;
import network.Message;
import player.Player;
import item.Item;
import services.InventoryService;
import services.ItemService;
import services.Service;
import utils.Util;


public class TamBaoService {

    public static TamBaoService instance;

    public static TamBaoService gI() {
        if (instance == null) {
            instance = new TamBaoService();
        }
        return instance;
    }

    public static List<Item> listItem = new ArrayList<Item>();
    public static List<Item> listItemVip = new ArrayList<Item>();
    public static long VAN_MAY = 150;

    public static void loadItem() {
        //tạo vp id 30
        //    if (Util.isTrue(10, 100)) {
        Item a1 = ItemService.gI().createNewItem((short) (1913));
        a1.itemOptions.add(new ItemOption(50, Util.nextInt(1, 80)));
        a1.itemOptions.add(new ItemOption(77, Util.nextInt(1, 80)));
        a1.itemOptions.add(new ItemOption(103, Util.nextInt(1, 80)));
        a1.itemOptions.add(new ItemOption(210, 3));
        a1.itemOptions.add(new ItemOption(72, 3));
        if (Util.isTrue(99, 100)) {
            a1.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        a1.color = 4;
        listItem.add(a1);

        Item a2 = ItemService.gI().createNewItem((short) (1846));
        a2.itemOptions.add(new ItemOption(50, Util.nextInt(1, 60)));
        a2.itemOptions.add(new ItemOption(77, Util.nextInt(1, 60)));
        a2.itemOptions.add(new ItemOption(103, Util.nextInt(1, 60)));
        a2.itemOptions.add(new ItemOption(210, 3));
        a2.itemOptions.add(new ItemOption(72, 3));
        if (Util.isTrue(99, 100)) {
            a2.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        a2.color = 4;
        listItem.add(a2);

       Item a3 = ItemService.gI().createNewItem((short) (1847));
        a3.itemOptions.add(new ItemOption(50, Util.nextInt(1, 80)));
        a3.itemOptions.add(new ItemOption(77, Util.nextInt(1, 80)));
        a3.itemOptions.add(new ItemOption(103, Util.nextInt(1, 80)));
        a3.itemOptions.add(new ItemOption(210, 3));
        a3.itemOptions.add(new ItemOption(72, 3));
        if (Util.isTrue(95, 100)) {
            a3.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        a3.color = 4;
        listItem.add(a3);

         Item a4 = ItemService.gI().createNewItem((short) (1848));
        a4.itemOptions.add(new ItemOption(50, Util.nextInt(1, 80)));
        a4.itemOptions.add(new ItemOption(77, Util.nextInt(1, 80)));
        a4.itemOptions.add(new ItemOption(103, Util.nextInt(1, 80)));
        a4.itemOptions.add(new ItemOption(210, 3));
        a4.itemOptions.add(new ItemOption(72, 3));
        if (Util.isTrue(99, 100)) {
            a4.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        a4.color = 4;
        listItem.add(a4);

         Item a5 = ItemService.gI().createNewItem((short) (1849));
        a5.itemOptions.add(new ItemOption(50, Util.nextInt(1, 80)));
        a5.itemOptions.add(new ItemOption(77, Util.nextInt(1, 80)));
        a5.itemOptions.add(new ItemOption(103, Util.nextInt(1, 80)));
        a5.itemOptions.add(new ItemOption(210, 3));
        a5.itemOptions.add(new ItemOption(72, 3));
        if (Util.isTrue(99, 100)) {
            a5.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        a5.color = 4;
        listItem.add(a5);

         Item a6 = ItemService.gI().createNewItem((short) (1850));
        a6.itemOptions.add(new ItemOption(50, Util.nextInt(1, 80)));
        a6.itemOptions.add(new ItemOption(77, Util.nextInt(1, 80)));
        a6.itemOptions.add(new ItemOption(103, Util.nextInt(1, 80)));
        a6.itemOptions.add(new ItemOption(210, 3));
        a6.itemOptions.add(new ItemOption(72, 3));
        if (Util.isTrue(99, 100)) {
            a6.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        a6.color = 4;
        listItem.add(a6);

        Item a7 = ItemService.gI().createNewItem((short) (1851));
        a7.itemOptions.add(new ItemOption(50, Util.nextInt(1, 80)));
        a7.itemOptions.add(new ItemOption(77, Util.nextInt(1, 80)));
        a7.itemOptions.add(new ItemOption(103, Util.nextInt(1, 80)));
        a7.itemOptions.add(new ItemOption(210, 3));
        a7.itemOptions.add(new ItemOption(72, 3));
        if (Util.isTrue(99, 100)) {
            a7.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        a7.color = 4;
        listItem.add(a7);

        Item a8 = ItemService.gI().createNewItem((short) (1852));
        a8.itemOptions.add(new ItemOption(50, Util.nextInt(1, 80)));
        a8.itemOptions.add(new ItemOption(77, Util.nextInt(1, 80)));
        a8.itemOptions.add(new ItemOption(103, Util.nextInt(1, 80)));
        a8.itemOptions.add(new ItemOption(210, 3));
        a8.itemOptions.add(new ItemOption(72, 3));
        if (Util.isTrue(99, 100)) {
            a8.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        a8.color = 4;
        listItem.add(a8);

         Item a9 = ItemService.gI().createNewItem((short) (1853));
        a9.itemOptions.add(new ItemOption(50, Util.nextInt(1, 80)));
        a9.itemOptions.add(new ItemOption(77, Util.nextInt(1, 80)));
        a9.itemOptions.add(new ItemOption(103, Util.nextInt(1, 80)));
        a9.itemOptions.add(new ItemOption(210, 3));
        a9.itemOptions.add(new ItemOption(72, 3));
        if (Util.isTrue(99, 100)) {
            a9.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        a9.color = 4;
        listItem.add(a9);

        
        
        
       Item a10 = ItemService.gI().createNewItem((short) (291));
        a10.itemOptions.add(new ItemOption(50, Util.nextInt(1, 80)));
        a10.itemOptions.add(new ItemOption(77, Util.nextInt(1, 80)));
        a10.itemOptions.add(new ItemOption(103, Util.nextInt(1, 80)));
        a10.itemOptions.add(new ItemOption(210, 3));
        a10.itemOptions.add(new ItemOption(72, 3));
        if (Util.isTrue(99, 100)) {
            a10.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        a10.color = 4;
        listItem.add(a10);
        
          Item a11 = ItemService.gI().createNewItem((short) (1463));
        a11.itemOptions.add(new ItemOption(237, 1));
        a11.itemOptions.add(new ItemOption(72, 1));
        if (Util.isTrue(99, 100)) {
            a11.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        a11.color = 4;
        listItem.add(a11);
        
        Item a12 = ItemService.gI().createNewItem((short) (1875));
        a12.itemOptions.add(new ItemOption(237, 1));
        a12.itemOptions.add(new ItemOption(72, 1));
        if (Util.isTrue(99, 100)) {
            a12.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
    //    a12.color = 4;
        listItem.add(a12);   
        
          Item a13 = ItemService.gI().createNewItem((short) (1875));
        a13.itemOptions.add(new ItemOption(237, 1));
        a13.itemOptions.add(new ItemOption(72, 1));
        if (Util.isTrue(99, 100)) {
            a13.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
     //   a13.color = 4;
        listItem.add(a13);   
        
          Item a14 = ItemService.gI().createNewItem((short) (1875));
        a14.itemOptions.add(new ItemOption(237, 1));
        a14.itemOptions.add(new ItemOption(72, 1));
        if (Util.isTrue(99, 100)) {
            a14.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
     //   a14.color = 4;
        listItem.add(a14);  
        
          Item a15 = ItemService.gI().createNewItem((short) (1875));
        a15.itemOptions.add(new ItemOption(237, 1));
        a15.itemOptions.add(new ItemOption(72, 1));
        if (Util.isTrue(99, 100)) {
            a15.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
    //    a15.color = 4;
        listItem.add(a15);   
        
         Item a16 = ItemService.gI().createNewItem((short) (1875));
        a16.itemOptions.add(new ItemOption(237, 1));
        a16.itemOptions.add(new ItemOption(72, 1));
        if (Util.isTrue(99, 100)) {
            a16.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
     //   a16.color = 4;
        listItem.add(a16);    
        
          Item a17 = ItemService.gI().createNewItem((short) (1875));
        a17.itemOptions.add(new ItemOption(237, 1));
        a17.itemOptions.add(new ItemOption(72, 1));
        if (Util.isTrue(99, 100)) {
            a17.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
    //    a17.color = 4;
        listItem.add(a17);   
        
         Item a18 = ItemService.gI().createNewItem((short) (1875));
        a18.itemOptions.add(new ItemOption(237, 1));
        a18.itemOptions.add(new ItemOption(72, 1));
        if (Util.isTrue(99, 100)) {
            a18.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
    //    a18.color = 4;
        listItem.add(a18);    
        
         Item a19 = ItemService.gI().createNewItem((short) (1875));
        a19.itemOptions.add(new ItemOption(237, 1));
        a19.itemOptions.add(new ItemOption(72, 1));
        if (Util.isTrue(99, 100)) {
            a19.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
     //   a19.color = 4;
        listItem.add(a19);    
        
         Item a20 = ItemService.gI().createNewItem((short) (921));
        a20.itemOptions.add(new ItemOption(237, 1));
        a20.itemOptions.add(new ItemOption(72, 1));
        if (Util.isTrue(99, 100)) {
            a20.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        a20.color = 4;
        listItem.add(a20);   
        
        Item a21 = ItemService.gI().createNewItem((short) (1481));
        a21.itemOptions.add(new ItemOption(50, Util.nextInt(1, 80)));
        a21.itemOptions.add(new ItemOption(77, Util.nextInt(1, 80)));
        a21.itemOptions.add(new ItemOption(103, Util.nextInt(1, 80)));
        a21.itemOptions.add(new ItemOption(237, 3));
        a21.itemOptions.add(new ItemOption(72, 3));
        if (Util.isTrue(99, 100)) {
            a21.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        a21.color = 4;
        listItem.add(a21);   
        
         Item a22 = ItemService.gI().createNewItem((short) (1875));
        a22.itemOptions.add(new ItemOption(237, 1));
        a22.itemOptions.add(new ItemOption(72, 1));
        if (Util.isTrue(99, 100)) {
            a22.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
    //    a22.color = 4;
        listItem.add(a22);     
        
  Item a23 = ItemService.gI().createNewItem((short) (1875));
        a23.itemOptions.add(new ItemOption(237, 1));
        a23.itemOptions.add(new ItemOption(72, 1));
        if (Util.isTrue(99, 100)) {
            a23.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
   //     a23.color = 4;
        listItem.add(a23);    
      

     Item a24 = ItemService.gI().createNewItem((short) (1875));
        a24.itemOptions.add(new ItemOption(237, 1));
        a24.itemOptions.add(new ItemOption(72, 1));
        if (Util.isTrue(99, 100)) {
            a24.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
    //    a24.color = 4;
        listItem.add(a24);    
        
          Item a25 = ItemService.gI().createNewItem((short) (1875));
        a25.itemOptions.add(new ItemOption(237, 1));
        a25.itemOptions.add(new ItemOption(72, 1));
        if (Util.isTrue(99, 100)) {
            a25.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
    //    a25.color = 4;
        listItem.add(a25);    
        
          Item a26 = ItemService.gI().createNewItem((short) (1875));
        a26.itemOptions.add(new ItemOption(237, 1));
        a26.itemOptions.add(new ItemOption(72, 1));
        if (Util.isTrue(99, 100)) {
            a26.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
   //     a26.color = 4;
        listItem.add(a26);    
        
          Item a27 = ItemService.gI().createNewItem((short) (1875));
        a27.itemOptions.add(new ItemOption(237, 1));
        a27.itemOptions.add(new ItemOption(72, 1));
        if (Util.isTrue(99, 100)) {
            a27.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
     //   a27.color = 4;
        listItem.add(a27);    
        
          Item a28 = ItemService.gI().createNewItem((short) (1875));
        a28.itemOptions.add(new ItemOption(237, 1));
        a28.itemOptions.add(new ItemOption(72, 1));
        if (Util.isTrue(99, 100)) {
            a28.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
   //     a28.color = 4;
        listItem.add(a28);    
        
         Item a29 = ItemService.gI().createNewItem((short) (1875));
        a29.itemOptions.add(new ItemOption(237, 1));
        a29.itemOptions.add(new ItemOption(72, 1));
        if (Util.isTrue(99, 100)) {
            a29.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
   //     a29.color = 4;
        listItem.add(a29);     
        
        Item a30 = ItemService.gI().createNewItem((short) (1572));
        a30.itemOptions.add(new ItemOption(50, Util.nextInt(1, 80)));
        a30.itemOptions.add(new ItemOption(77, Util.nextInt(1, 80)));
        a30.itemOptions.add(new ItemOption(103, Util.nextInt(1, 80)));
        a30.itemOptions.add(new ItemOption(237, 3));
        a30.itemOptions.add(new ItemOption(72, 3));
        if (Util.isTrue(99, 100)) {
            a30.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        a30.color = 4;
        listItem.add(a30);    
        
        Item a31 = ItemService.gI().createNewItem((short) (1620));
        a31.itemOptions.add(new ItemOption(50, Util.nextInt(1, 80)));
        a31.itemOptions.add(new ItemOption(77, Util.nextInt(1, 80)));
        a31.itemOptions.add(new ItemOption(103, Util.nextInt(1, 80)));
        a31.itemOptions.add(new ItemOption(237, 3));
        a31.itemOptions.add(new ItemOption(72, 3));
        if (Util.isTrue(99, 100)) {
            a31.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        a31.color = 4;
        listItem.add(a31);     
        
         Item a32 = ItemService.gI().createNewItem((short) (1875));
        a32.itemOptions.add(new ItemOption(237, 1));
        a32.itemOptions.add(new ItemOption(72, 1));
        if (Util.isTrue(99, 100)) {
            a32.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
     //   a32.color = 4;
        listItem.add(a32);      
        
          Item a33 = ItemService.gI().createNewItem((short) (1875));
        a33.itemOptions.add(new ItemOption(237, 1));
        a33.itemOptions.add(new ItemOption(72, 1));
        if (Util.isTrue(99, 100)) {
            a33.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
  //      a33.color = 4;
        listItem.add(a33);  
        
       Item a34 = ItemService.gI().createNewItem((short) (1875));
        a34.itemOptions.add(new ItemOption(237, 1));
        a34.itemOptions.add(new ItemOption(72, 1));
        if (Util.isTrue(99, 100)) {
            a34.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
    //    a34.color = 4;
        listItem.add(a34);      
        
         Item a35 = ItemService.gI().createNewItem((short) (1875));
        a35.itemOptions.add(new ItemOption(237, 1));
        a35.itemOptions.add(new ItemOption(72, 1));
        if (Util.isTrue(99, 100)) {
            a35.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
   //     a35.color = 4;
        listItem.add(a35);    
        
        
          Item a36 = ItemService.gI().createNewItem((short) (1875));
        a36.itemOptions.add(new ItemOption(237, 1));
        a36.itemOptions.add(new ItemOption(72, 1));
        if (Util.isTrue(99, 100)) {
            a36.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
   //     a36.color = 4;
        listItem.add(a36);   
        
          Item a37 = ItemService.gI().createNewItem((short) (1875));
        a37.itemOptions.add(new ItemOption(237, 1));
        a37.itemOptions.add(new ItemOption(72, 1));
        if (Util.isTrue(99, 100)) {
            a37.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
  //      a37.color = 4;
        listItem.add(a37);   
        
          Item a38 = ItemService.gI().createNewItem((short) (1875));
        a38.itemOptions.add(new ItemOption(237, 1));
        a38.itemOptions.add(new ItemOption(72, 1));
        if (Util.isTrue(99, 100)) {
            a38.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
    //    a38.color = 4;
        listItem.add(a38);   
        
         Item a39 = ItemService.gI().createNewItem((short) (1875));
        a39.itemOptions.add(new ItemOption(237, 1));
        a39.itemOptions.add(new ItemOption(72, 1));
        if (Util.isTrue(99, 100)) {
            a39.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        a39.color = 4;
        listItem.add(a39);    
        
         Item a40 = ItemService.gI().createNewItem((short) (1623));
        a40.itemOptions.add(new ItemOption(50, Util.nextInt(1, 80)));
        a40.itemOptions.add(new ItemOption(77, Util.nextInt(1, 80)));
        a40.itemOptions.add(new ItemOption(103, Util.nextInt(1, 80)));
        a40.itemOptions.add(new ItemOption(237, 3));
        a40.itemOptions.add(new ItemOption(72, 3));
        if (Util.isTrue(99, 100)) {
            a40.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        a40.color = 4;
        listItem.add(a40);       
        
         Item a41 = ItemService.gI().createNewItem((short) (1678));
        a41.itemOptions.add(new ItemOption(50, Util.nextInt(1, 80)));
        a41.itemOptions.add(new ItemOption(77, Util.nextInt(1, 80)));
        a41.itemOptions.add(new ItemOption(103, Util.nextInt(1, 80)));
        a41.itemOptions.add(new ItemOption(237, 3));
         a41.itemOptions.add(new ItemOption(238, 1));
        a41.itemOptions.add(new ItemOption(72, 3));
        if (Util.isTrue(99, 100)) {
            a41.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        a41.color = 4;
        listItem.add(a41);  
        
          Item a42 = ItemService.gI().createNewItem((short) (1875));
        a42.itemOptions.add(new ItemOption(237, 1));
        a42.itemOptions.add(new ItemOption(72, 1));
        if (Util.isTrue(99, 100)) {
            a42.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
    //    a42.color = 4;
        listItem.add(a42);    
        
        
         Item a43 = ItemService.gI().createNewItem((short) (1875));
        a43.itemOptions.add(new ItemOption(237, 1));
        a43.itemOptions.add(new ItemOption(72, 1));
        if (Util.isTrue(99, 100)) {
            a43.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
   //     a43.color = 4;
        listItem.add(a43);     
        
          Item a44 = ItemService.gI().createNewItem((short) (1875));
        a44.itemOptions.add(new ItemOption(237, 1));
        a44.itemOptions.add(new ItemOption(72, 1));
        if (Util.isTrue(99, 100)) {
            a44.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
  //      a44.color = 4;
        listItem.add(a44);    
        
          Item a45 = ItemService.gI().createNewItem((short) (1875));
        a45.itemOptions.add(new ItemOption(237, 1));
        a45.itemOptions.add(new ItemOption(72, 1));
        if (Util.isTrue(99, 100)) {
            a45.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
   //     a45.color = 4;
        listItem.add(a45);    
        
         Item a46 = ItemService.gI().createNewItem((short) (1875));
        a46.itemOptions.add(new ItemOption(237, 1));
        a46.itemOptions.add(new ItemOption(72, 1));
        if (Util.isTrue(99, 100)) {
            a46.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
    //    a46.color = 4;
        listItem.add(a46);     
        
          Item a47 = ItemService.gI().createNewItem((short) (1875));
        a47.itemOptions.add(new ItemOption(237, 1));
        a47.itemOptions.add(new ItemOption(72, 1));
        if (Util.isTrue(99, 100)) {
            a47.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
//        a47.color = 4;
        listItem.add(a47);    
        
         Item a48 = ItemService.gI().createNewItem((short) (1875));
        a48.itemOptions.add(new ItemOption(237, 1));
        a48.itemOptions.add(new ItemOption(72, 1));
        if (Util.isTrue(99, 100)) {
            a48.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
    //    a48.color = 4;
        listItem.add(a48);     
        
          Item a49 = ItemService.gI().createNewItem((short) (1875));
        a49.itemOptions.add(new ItemOption(237, 1));
        a49.itemOptions.add(new ItemOption(72, 1));
        if (Util.isTrue(99, 100)) {
            a49.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
 //       a49.color = 4;
        listItem.add(a49);    
        
          Item a50 = ItemService.gI().createNewItem((short) (1833));
        a50.itemOptions.add(new ItemOption(50, Util.nextInt(1, 80)));
        a50.itemOptions.add(new ItemOption(77, Util.nextInt(1, 80)));
        a50.itemOptions.add(new ItemOption(103, Util.nextInt(1, 80)));
        a50.itemOptions.add(new ItemOption(237, 3));
        a50.itemOptions.add(new ItemOption(72, 3));
        if (Util.isTrue(99, 100)) {
            a50.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        a50.color = 4;
        listItem.add(a50);  
        
        Item a51 = ItemService.gI().createNewItem((short) (1834));
        a51.itemOptions.add(new ItemOption(50, Util.nextInt(1, 80)));
        a51.itemOptions.add(new ItemOption(77, Util.nextInt(1, 80)));
        a51.itemOptions.add(new ItemOption(103, Util.nextInt(1, 80)));
        a51.itemOptions.add(new ItemOption(237, 3));
        a51.itemOptions.add(new ItemOption(72, 3));
        if (Util.isTrue(99, 100)) {
            a51.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        a51.color = 4;
        listItem.add(a51);   
        
           Item a52 = ItemService.gI().createNewItem((short) (1835));
        a52.itemOptions.add(new ItemOption(50, Util.nextInt(1, 80)));
        a52.itemOptions.add(new ItemOption(77, Util.nextInt(1, 80)));
        a52.itemOptions.add(new ItemOption(103, Util.nextInt(1, 80)));
        a52.itemOptions.add(new ItemOption(237, 3));
        a52.itemOptions.add(new ItemOption(72, 3));
        if (Util.isTrue(99, 100)) {
            a52.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        a52.color = 4;
        listItem.add(a52);   
        
          Item a53 = ItemService.gI().createNewItem((short) (1836));
        a53.itemOptions.add(new ItemOption(50, Util.nextInt(1, 80)));
        a53.itemOptions.add(new ItemOption(77, Util.nextInt(1, 80)));
        a53.itemOptions.add(new ItemOption(103, Util.nextInt(1, 80)));
        a53.itemOptions.add(new ItemOption(237, 3));
        a53.itemOptions.add(new ItemOption(72, 3));
        if (Util.isTrue(99, 100)) {
            a53.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        a53.color = 4;
        listItem.add(a53);   
        
         Item a54 = ItemService.gI().createNewItem((short) (1837));
        a54.itemOptions.add(new ItemOption(50, Util.nextInt(1, 80)));
        a54.itemOptions.add(new ItemOption(77, Util.nextInt(1, 80)));
        a54.itemOptions.add(new ItemOption(103, Util.nextInt(1, 80)));
        a54.itemOptions.add(new ItemOption(237, 3));
        a54.itemOptions.add(new ItemOption(72, 3));
        if (Util.isTrue(99, 100)) {
            a54.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        a54.color = 4;
        listItem.add(a54);   
        
        Item a55 = ItemService.gI().createNewItem((short) (1838));
        a55.itemOptions.add(new ItemOption(50, Util.nextInt(1, 80)));
        a55.itemOptions.add(new ItemOption(77, Util.nextInt(1, 80)));
        a55.itemOptions.add(new ItemOption(103, Util.nextInt(1, 80)));
        a55.itemOptions.add(new ItemOption(237, 3));
        a55.itemOptions.add(new ItemOption(72, 3));
        if (Util.isTrue(99, 100)) {
            a55.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        a55.color = 4;
        listItem.add(a55);   
        
        Item a56 = ItemService.gI().createNewItem((short) (1099));
        a56.itemOptions.add(new ItemOption(30, Util.nextInt(1, 10)));
        if (Util.isTrue(99, 100)) {
            a56.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        a56.color = 4;
        listItem.add(a56);    
        
         Item a57 = ItemService.gI().createNewItem((short) (1100));
        a57.itemOptions.add(new ItemOption(30, Util.nextInt(1, 10)));
        if (Util.isTrue(99, 100)) {
            a57.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        a57.color = 4;
        listItem.add(a57);   
        
        Item a58 = ItemService.gI().createNewItem((short) (1101));
        a58.itemOptions.add(new ItemOption(30, Util.nextInt(1, 10)));
        if (Util.isTrue(99, 100)) {
            a58.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        a58.color = 4;
        listItem.add(a58);    
        
        Item a59 = ItemService.gI().createNewItem((short) (1102));
        a59.itemOptions.add(new ItemOption(30, Util.nextInt(1, 10)));
        if (Util.isTrue(99, 100)) {
            a59.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        a59.color = 4;
        listItem.add(a59);    
        
         Item a60 = ItemService.gI().createNewItem((short) (1103));
        a60.itemOptions.add(new ItemOption(30, Util.nextInt(1, 10)));
        if (Util.isTrue(99, 100)) {
            a60.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        a60.color = 4;
        listItem.add(a60);   
        
        Item b1 = ItemService.gI().createNewItem((short) (1351));
        b1.itemOptions.add(new ItemOption(50,Util.nextInt(1, 80)));
        b1.itemOptions.add(new ItemOption(77, Util.nextInt(1, 80)));
        b1.itemOptions.add(new ItemOption(103, Util.nextInt(1, 80)));
        b1.itemOptions.add(new ItemOption(237, Util.nextInt(1, 4)));
        b1.itemOptions.add(new ItemOption(72, Util.nextInt(1, 4)));
        if (Util.isTrue(99, 100)) {
            b1.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        b1.color = 4;
        listItemVip.add(b1);    
        
          Item b2 = ItemService.gI().createNewItem((short) (1352));
        b2.itemOptions.add(new ItemOption(50,Util.nextInt(1, 80)));
        b2.itemOptions.add(new ItemOption(77, Util.nextInt(1, 80)));
        b2.itemOptions.add(new ItemOption(103, Util.nextInt(1, 80)));
        b2.itemOptions.add(new ItemOption(237, Util.nextInt(1, 4)));
        b2.itemOptions.add(new ItemOption(72, Util.nextInt(1, 4)));
        if (Util.isTrue(99, 100)) {
            b2.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        b2.color = 4;
        listItemVip.add(b2);    
        
           Item b3 = ItemService.gI().createNewItem((short) (1353));
        b3.itemOptions.add(new ItemOption(50,Util.nextInt(1, 80)));
        b3.itemOptions.add(new ItemOption(77, Util.nextInt(1, 80)));
        b3.itemOptions.add(new ItemOption(103, Util.nextInt(1, 80)));
        b3.itemOptions.add(new ItemOption(237, Util.nextInt(1, 4)));
        b3.itemOptions.add(new ItemOption(72, Util.nextInt(1, 4)));
        if (Util.isTrue(99, 100)) {
            b3.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        b3.color = 4;
        listItemVip.add(b3);    
        
          Item b4 = ItemService.gI().createNewItem((short) (1354));
        b4.itemOptions.add(new ItemOption(50,Util.nextInt(1, 80)));
        b4.itemOptions.add(new ItemOption(77, Util.nextInt(1, 80)));
        b4.itemOptions.add(new ItemOption(103, Util.nextInt(1, 80)));
        b4.itemOptions.add(new ItemOption(237, Util.nextInt(1, 4)));
        b4.itemOptions.add(new ItemOption(72, Util.nextInt(1, 4)));
        if (Util.isTrue(99, 100)) {
            b4.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        b4.color = 4;
        listItemVip.add(b4);   
        
          Item b5 = ItemService.gI().createNewItem((short) (1355));
        b5.itemOptions.add(new ItemOption(50,Util.nextInt(1, 80)));
        b5.itemOptions.add(new ItemOption(77, Util.nextInt(1, 80)));
        b5.itemOptions.add(new ItemOption(103, Util.nextInt(1, 80)));
        b5.itemOptions.add(new ItemOption(237, Util.nextInt(1, 4)));
        b5.itemOptions.add(new ItemOption(72, Util.nextInt(1, 4)));
        if (Util.isTrue(99, 100)) {
            b5.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        b5.color = 4;
        listItemVip.add(b5);  
        
         Item b6 = ItemService.gI().createNewItem((short) (1356));
        b6.itemOptions.add(new ItemOption(50,Util.nextInt(1, 80)));
        b6.itemOptions.add(new ItemOption(77, Util.nextInt(1, 80)));
        b6.itemOptions.add(new ItemOption(103, Util.nextInt(1, 80)));
        b6.itemOptions.add(new ItemOption(237, Util.nextInt(1, 4)));
        b6.itemOptions.add(new ItemOption(72, Util.nextInt(1, 4)));
        if (Util.isTrue(99, 100)) {
            b6.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        b6.color = 4;
        listItemVip.add(b6);  
        
         Item b7 = ItemService.gI().createNewItem((short) (1357));
        b7.itemOptions.add(new ItemOption(50,Util.nextInt(1, 80)));
        b7.itemOptions.add(new ItemOption(77, Util.nextInt(1, 80)));
        b7.itemOptions.add(new ItemOption(103, Util.nextInt(1, 80)));
        b7.itemOptions.add(new ItemOption(237, Util.nextInt(1, 4)));
        b7.itemOptions.add(new ItemOption(72, Util.nextInt(1, 4)));
        if (Util.isTrue(99, 100)) {
            b7.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        b7.color = 4;
        listItemVip.add(b7);  
        
         Item b8 = ItemService.gI().createNewItem((short) (1358));
        b8.itemOptions.add(new ItemOption(50,Util.nextInt(1, 80)));
        b8.itemOptions.add(new ItemOption(77, Util.nextInt(1, 88)));
        b8.itemOptions.add(new ItemOption(103, Util.nextInt(1, 80)));
        b8.itemOptions.add(new ItemOption(237, Util.nextInt(1, 4)));
        b8.itemOptions.add(new ItemOption(72, Util.nextInt(1, 4)));
        if (Util.isTrue(99, 100)) {
            b8.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        b8.color = 4;
        listItemVip.add(b8); 
        
          Item b9 = ItemService.gI().createNewItem((short) (1359));
        b9.itemOptions.add(new ItemOption(50,Util.nextInt(1, 80)));
        b9.itemOptions.add(new ItemOption(77, Util.nextInt(1, 80)));
        b9.itemOptions.add(new ItemOption(103, Util.nextInt(1, 80)));
        b9.itemOptions.add(new ItemOption(237, Util.nextInt(1, 4)));
        b9.itemOptions.add(new ItemOption(72, Util.nextInt(1, 4)));
        if (Util.isTrue(99, 100)) {
            b9.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        b9.color = 4;
        listItemVip.add(b9); 
        
        Item b10 = ItemService.gI().createNewItem((short) (1360));
        b10.itemOptions.add(new ItemOption(50,Util.nextInt(1, 80)));
        b10.itemOptions.add(new ItemOption(77, Util.nextInt(1, 80)));
        b10.itemOptions.add(new ItemOption(103, Util.nextInt(1, 80)));
        b10.itemOptions.add(new ItemOption(237, Util.nextInt(1, 4)));
        b10.itemOptions.add(new ItemOption(72, Util.nextInt(1, 4)));
        if (Util.isTrue(99, 100)) {
            b10.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        b10.color = 4;
        listItemVip.add(b10);  
        
           Item b11 = ItemService.gI().createNewItem((short) (1439));
        b11.itemOptions.add(new ItemOption(50,Util.nextInt(1, 100)));
        b11.itemOptions.add(new ItemOption(77, Util.nextInt(1, 100)));
        b11.itemOptions.add(new ItemOption(103, Util.nextInt(1, 100)));
        b11.itemOptions.add(new ItemOption(237, Util.nextInt(1, 5)));
        b11.itemOptions.add(new ItemOption(72, Util.nextInt(1, 5)));
        if (Util.isTrue(99, 100)) {
            b11.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        b11.color = 4;
        listItemVip.add(b11);  
        
        
           Item b12 = ItemService.gI().createNewItem((short) (1875));
        b12.itemOptions.add(new ItemOption(72, Util.nextInt(1, 5)));
        if (Util.isTrue(99, 100)) {
            b12.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
   //     b12.color = 4;
        listItemVip.add(b12);  
        
       
           Item b13 = ItemService.gI().createNewItem((short) (1875));
        b13.itemOptions.add(new ItemOption(72, Util.nextInt(1, 5)));
        if (Util.isTrue(99, 100)) {
            b13.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
    //    b13.color = 4;
        listItemVip.add(b13);   
        
             Item b14 = ItemService.gI().createNewItem((short) (1875));
        b14.itemOptions.add(new ItemOption(72, Util.nextInt(1, 5)));
        if (Util.isTrue(99, 100)) {
            b14.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
     //   b14.color = 4;
        listItemVip.add(b14); 
        
            Item b15 = ItemService.gI().createNewItem((short) (1875));
        b15.itemOptions.add(new ItemOption(72, Util.nextInt(1, 5)));
        if (Util.isTrue(99, 100)) {
            b15.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
     //   b15.color = 4;
        listItemVip.add(b15);  
        
            Item b16 = ItemService.gI().createNewItem((short) (1875));
        b16.itemOptions.add(new ItemOption(72, Util.nextInt(1, 5)));
        if (Util.isTrue(99, 100)) {
            b16.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
    //    b16.color = 4;
        listItemVip.add(b16);  
        
         Item b17 = ItemService.gI().createNewItem((short) (1875));
        b17.itemOptions.add(new ItemOption(72, Util.nextInt(1, 5)));
        if (Util.isTrue(99, 100)) {
            b17.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
     //   b17.color = 4;
        listItemVip.add(b17);  
          
         Item b18 = ItemService.gI().createNewItem((short) (1875));
        b18.itemOptions.add(new ItemOption(72, Util.nextInt(1, 5)));
        if (Util.isTrue(99, 100)) {
            b18.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
     //   b18.color = 4;
        listItemVip.add(b18);  
        
         Item b19 = ItemService.gI().createNewItem((short) (1875));
        b19.itemOptions.add(new ItemOption(72, Util.nextInt(1, 5)));
        if (Util.isTrue(99, 100)) {
            b19.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
     //   b19.color = 4;
        listItemVip.add(b19);   
        
             Item b20 = ItemService.gI().createNewItem((short) (1885));
       
        b20.itemOptions.add(new ItemOption(72, Util.nextInt(1, 5)));
        if (Util.isTrue(99, 100)) {
            b20.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        b20.color = 4;
        listItemVip.add(b20);  
        
          Item b21 = ItemService.gI().createNewItem((short) (1687));
        b21.itemOptions.add(new ItemOption(50,Util.nextInt(1, 100)));
        b21.itemOptions.add(new ItemOption(77, Util.nextInt(1, 100)));
        b21.itemOptions.add(new ItemOption(103, Util.nextInt(1, 100)));
        b21.itemOptions.add(new ItemOption(237, Util.nextInt(1, 5)));
        b21.itemOptions.add(new ItemOption(72, Util.nextInt(1, 5)));
        if (Util.isTrue(99, 100)) {
            b21.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        b21.color = 4;
        listItemVip.add(b21);   
        
         Item b22 = ItemService.gI().createNewItem((short) (1875));
        b22.itemOptions.add(new ItemOption(72, Util.nextInt(1, 5)));
        if (Util.isTrue(99, 100)) {
            b22.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
     //   b19.color = 4;
        listItemVip.add(b22);   
        
          Item b23 = ItemService.gI().createNewItem((short) (1875));
        b23.itemOptions.add(new ItemOption(72, Util.nextInt(1, 5)));
        if (Util.isTrue(99, 100)) {
            b23.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
     //   b19.color = 4;
        listItemVip.add(b23);  
        
         Item b24 = ItemService.gI().createNewItem((short) (1875));
        b24.itemOptions.add(new ItemOption(72, Util.nextInt(1, 5)));
        if (Util.isTrue(99, 100)) {
            b24.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
     //   b19.color = 4;
        listItemVip.add(b24);    
        
        
         Item b25 = ItemService.gI().createNewItem((short) (1875));
        b24.itemOptions.add(new ItemOption(72, Util.nextInt(1, 5)));
        if (Util.isTrue(99, 100)) {
            b25.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
     //   b19.color = 4;
        listItemVip.add(b25);      
        
           Item b26 = ItemService.gI().createNewItem((short) (1875));
        b26.itemOptions.add(new ItemOption(72, Util.nextInt(1, 5)));
        if (Util.isTrue(99, 100)) {
            b26.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
     //   b19.color = 4;
        listItemVip.add(b26);      
        
             Item b27 = ItemService.gI().createNewItem((short) (1875));
        b27.itemOptions.add(new ItemOption(72, Util.nextInt(1, 5)));
        if (Util.isTrue(99, 100)) {
            b27.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
     //   b19.color = 4;
        listItemVip.add(b27);   
        
        
          Item b28 = ItemService.gI().createNewItem((short) (1875));
        b28.itemOptions.add(new ItemOption(72, Util.nextInt(1, 5)));
        if (Util.isTrue(99, 100)) {
            b28.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
     //   b19.color = 4;
        listItemVip.add(b28);      
        
        
            Item b29 = ItemService.gI().createNewItem((short) (1875));
        b29.itemOptions.add(new ItemOption(72, Util.nextInt(1, 5)));
        if (Util.isTrue(99, 100)) {
            b29.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
     //   b19.color = 4;
        listItemVip.add(b29);  
        
         Item b30 = ItemService.gI().createNewItem((short) (1678));
        b30.itemOptions.add(new ItemOption(50,Util.nextInt(1, 100)));
        b30.itemOptions.add(new ItemOption(77, Util.nextInt(1, 100)));
        b30.itemOptions.add(new ItemOption(103, Util.nextInt(1, 100)));
        b30.itemOptions.add(new ItemOption(237, Util.nextInt(1, 5)));
        b30.itemOptions.add(new ItemOption(72, Util.nextInt(1, 5)));
        if (Util.isTrue(99, 100)) {
            b30.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        b30.color = 4;
        listItemVip.add(b30);     
        
           Item b31 = ItemService.gI().createNewItem((short) (925));
      
        b31.itemOptions.add(new ItemOption(30, Util.nextInt(1, 5)));
        if (Util.isTrue(99, 100)) {
            b31.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        b31.color = 4;
        listItemVip.add(b31);     
        
        
           Item b32 = ItemService.gI().createNewItem((short) (1875));
        b32.itemOptions.add(new ItemOption(72, Util.nextInt(1, 5)));
        if (Util.isTrue(99, 100)) {
            b32.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
     //   b19.color = 4;
        listItemVip.add(b32);  
        
           Item b33 = ItemService.gI().createNewItem((short) (1875));
        b33.itemOptions.add(new ItemOption(72, Util.nextInt(1, 5)));
        if (Util.isTrue(99, 100)) {
            b33.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
     //   b19.color = 4;
        listItemVip.add(b33);  
        
        
           Item b34 = ItemService.gI().createNewItem((short) (1875));
        b34.itemOptions.add(new ItemOption(72, Util.nextInt(1, 5)));
        if (Util.isTrue(99, 100)) {
            b34.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
     //   b19.color = 4;
        listItemVip.add(b34);  
        
        
           Item b35 = ItemService.gI().createNewItem((short) (1875));
        b35.itemOptions.add(new ItemOption(72, Util.nextInt(1, 5)));
        if (Util.isTrue(99, 100)) {
            b35.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
     //   b19.color = 4;
        listItemVip.add(b35);  
        
        
        
           Item b36 = ItemService.gI().createNewItem((short) (1875));
        b36.itemOptions.add(new ItemOption(72, Util.nextInt(1, 5)));
        if (Util.isTrue(99, 100)) {
            b36.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
     //   b19.color = 4;
        listItemVip.add(b36);  
        
        
      
           Item b37 = ItemService.gI().createNewItem((short) (1875));
        b37.itemOptions.add(new ItemOption(72, Util.nextInt(1, 5)));
        if (Util.isTrue(99, 100)) {
            b37.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
     //   b19.color = 4;
        listItemVip.add(b37);    
        
            Item b38 = ItemService.gI().createNewItem((short) (1875));
        b38.itemOptions.add(new ItemOption(72, Util.nextInt(1, 5)));
        if (Util.isTrue(99, 100)) {
            b38.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
     //   b19.color = 4;
        listItemVip.add(b38); 
        
          Item b39 = ItemService.gI().createNewItem((short) (1875));
        b39.itemOptions.add(new ItemOption(72, Util.nextInt(1, 5)));
        if (Util.isTrue(99, 100)) {
            b39.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
     //   b19.color = 4;
        listItemVip.add(b39);  
        
         Item b40 = ItemService.gI().createNewItem((short) (926));
      
        b40.itemOptions.add(new ItemOption(30, Util.nextInt(1, 5)));
        if (Util.isTrue(99, 100)) {
            b40.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        b40.color = 4;
        listItemVip.add(b40);   
        
          Item b41 = ItemService.gI().createNewItem((short) (927));
      
        b41.itemOptions.add(new ItemOption(30, Util.nextInt(1, 5)));
        if (Util.isTrue(99, 100)) {
            b41.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        b41.color = 4;
        listItemVip.add(b41);   
        
           Item b42 = ItemService.gI().createNewItem((short) (1875));
        b42.itemOptions.add(new ItemOption(72, Util.nextInt(1, 5)));
        if (Util.isTrue(99, 100)) {
            b42.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
     //   b19.color = 4;
        listItemVip.add(b42);  
        
        
          Item b43 = ItemService.gI().createNewItem((short) (1875));
        b43.itemOptions.add(new ItemOption(72, Util.nextInt(1, 5)));
        if (Util.isTrue(99, 100)) {
            b43.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
     //   b19.color = 4;
        listItemVip.add(b43);  
         
           Item b44 = ItemService.gI().createNewItem((short) (1875));
        b44.itemOptions.add(new ItemOption(72, Util.nextInt(1, 5)));
        if (Util.isTrue(99, 100)) {
            b44.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
     //   b19.color = 4;
        listItemVip.add(b44);  
        
            Item b45 = ItemService.gI().createNewItem((short) (1875));
        b45.itemOptions.add(new ItemOption(72, Util.nextInt(1, 5)));
        if (Util.isTrue(99, 100)) {
            b45.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
     //   b19.color = 4;
        listItemVip.add(b45);  
        
          Item b46 = ItemService.gI().createNewItem((short) (1875));
        b46.itemOptions.add(new ItemOption(72, Util.nextInt(1, 5)));
        if (Util.isTrue(99, 100)) {
            b46.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
     //   b19.color = 4;
        listItemVip.add(b46);  
        
        
         Item b47 = ItemService.gI().createNewItem((short) (1875));
        b47.itemOptions.add(new ItemOption(72, Util.nextInt(1, 5)));
        if (Util.isTrue(99, 100)) {
            b47.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
     //   b19.color = 4;
        listItemVip.add(b47);   
        
           Item b48 = ItemService.gI().createNewItem((short) (1875));
        b48.itemOptions.add(new ItemOption(72, Util.nextInt(1, 5)));
        if (Util.isTrue(99, 100)) {
            b48.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
     //   b19.color = 4;
        listItemVip.add(b48);   
        
        
           Item b49 = ItemService.gI().createNewItem((short) (1875));
        b49.itemOptions.add(new ItemOption(72, Util.nextInt(1, 5)));
        if (Util.isTrue(99, 100)) {
            b49.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
     //   b19.color = 4;
        listItemVip.add(b49);    
        
         Item b50 = ItemService.gI().createNewItem((short) (928));
      
        b50.itemOptions.add(new ItemOption(30, Util.nextInt(1, 5)));
        if (Util.isTrue(99, 100)) {
            b50.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        b50.color = 4;
        listItemVip.add(b50);      
        
        Item b51 = ItemService.gI().createNewItem((short) (1241));
      b51.itemOptions.add(new ItemOption(50, Util.nextInt(1, 100)));
      b51.itemOptions.add(new ItemOption(77, Util.nextInt(1, 100)));
      b51.itemOptions.add(new ItemOption(103, Util.nextInt(1, 100)));
      b51.itemOptions.add(new ItemOption(237, Util.nextInt(1, 4)));
        b51.itemOptions.add(new ItemOption(72, Util.nextInt(1, 4)));
        if (Util.isTrue(99, 100)) {
            b51.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        b51.color = 4;
        listItemVip.add(b51);      
        
        
        Item b52 = ItemService.gI().createNewItem((short) (1242));
      b52.itemOptions.add(new ItemOption(50, Util.nextInt(1, 100)));
      b52.itemOptions.add(new ItemOption(77, Util.nextInt(1, 100)));
      b52.itemOptions.add(new ItemOption(103, Util.nextInt(1, 100)));
      b52.itemOptions.add(new ItemOption(237, Util.nextInt(1, 4)));
        b52.itemOptions.add(new ItemOption(72, Util.nextInt(1, 4)));
        if (Util.isTrue(99, 100)) {
            b52.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        b52.color = 4;
        listItemVip.add(b52);       
        
        
        Item b53 = ItemService.gI().createNewItem((short) (1243));
      b53.itemOptions.add(new ItemOption(50, Util.nextInt(1, 100)));
      b53.itemOptions.add(new ItemOption(77, Util.nextInt(1, 100)));
      b53.itemOptions.add(new ItemOption(103, Util.nextInt(1, 100)));
      b53.itemOptions.add(new ItemOption(237, Util.nextInt(1, 4)));
        b53.itemOptions.add(new ItemOption(72, Util.nextInt(1, 4)));
        if (Util.isTrue(99, 100)) {
            b53.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        b53.color = 4;
        listItemVip.add(b53);          
        
        
         Item b54 = ItemService.gI().createNewItem((short) (1244));
      b54.itemOptions.add(new ItemOption(50, Util.nextInt(1, 100)));
      b54.itemOptions.add(new ItemOption(77, Util.nextInt(1, 100)));
      b54.itemOptions.add(new ItemOption(103, Util.nextInt(1, 100)));
      b54.itemOptions.add(new ItemOption(237, Util.nextInt(1, 4)));
        b54.itemOptions.add(new ItemOption(72, Util.nextInt(1, 4)));
        if (Util.isTrue(99, 100)) {
            b54.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        b54.color = 4;
        listItemVip.add(b54);    
        
         Item b55 = ItemService.gI().createNewItem((short) (1245));
      b55.itemOptions.add(new ItemOption(50, Util.nextInt(1, 100)));
      b55.itemOptions.add(new ItemOption(77, Util.nextInt(1, 100)));
      b55.itemOptions.add(new ItemOption(103, Util.nextInt(1, 100)));
      b55.itemOptions.add(new ItemOption(237, Util.nextInt(1, 4)));
        b55.itemOptions.add(new ItemOption(72, Util.nextInt(1, 4)));
        if (Util.isTrue(99, 100)) {
            b55.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        b55.color = 4;
        listItemVip.add(b55);           
        
          Item b56 = ItemService.gI().createNewItem((short) (1315));
      b56.itemOptions.add(new ItemOption(50, Util.nextInt(1, 100)));
      b56.itemOptions.add(new ItemOption(77, Util.nextInt(1, 110)));
      b56.itemOptions.add(new ItemOption(103, Util.nextInt(1, 100)));
      b56.itemOptions.add(new ItemOption(237, Util.nextInt(1, 4)));
        b56.itemOptions.add(new ItemOption(72, Util.nextInt(1, 4)));
        if (Util.isTrue(99, 100)) {
            b56.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        b56.color = 4;
        listItemVip.add(b56);            
        
        
           Item b57 = ItemService.gI().createNewItem((short) (1341));
      b57.itemOptions.add(new ItemOption(50, Util.nextInt(1, 100)));
      b57.itemOptions.add(new ItemOption(77, Util.nextInt(1, 100)));
      b57.itemOptions.add(new ItemOption(103, Util.nextInt(1, 100)));
      b57.itemOptions.add(new ItemOption(237, Util.nextInt(1, 4)));
        b57.itemOptions.add(new ItemOption(72, Util.nextInt(1, 4)));
        if (Util.isTrue(99, 100)) {
            b57.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        b57.color = 4;
        listItemVip.add(b57);           
        
        
        
           Item b58 = ItemService.gI().createNewItem((short) (1317));
      b58.itemOptions.add(new ItemOption(50, Util.nextInt(1, 100)));
      b58.itemOptions.add(new ItemOption(77, Util.nextInt(1, 100)));
      b58.itemOptions.add(new ItemOption(103, Util.nextInt(1, 100)));
      b58.itemOptions.add(new ItemOption(237, Util.nextInt(1, 4)));
        b58.itemOptions.add(new ItemOption(72, Util.nextInt(1, 4)));
        if (Util.isTrue(99, 100)) {
            b58.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        b58.color = 4;
        listItemVip.add(b58);           
        
        
        
           Item b59 = ItemService.gI().createNewItem((short) (1342));
      b59.itemOptions.add(new ItemOption(50, Util.nextInt(1, 100)));
      b59.itemOptions.add(new ItemOption(77, Util.nextInt(1, 100)));
      b59.itemOptions.add(new ItemOption(103, Util.nextInt(1, 100)));
      b59.itemOptions.add(new ItemOption(237, Util.nextInt(1, 4)));
        b59.itemOptions.add(new ItemOption(72, Util.nextInt(1, 4)));
        if (Util.isTrue(99, 100)) {
            b59.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        b59.color = 4;
        listItemVip.add(b59);    
        
           Item b60 = ItemService.gI().createNewItem((short) (1003));
      b60.itemOptions.add(new ItemOption(50, Util.nextInt(1, 100)));
      b60.itemOptions.add(new ItemOption(77, Util.nextInt(1, 100)));
      b60.itemOptions.add(new ItemOption(103, Util.nextInt(1, 100)));
      b60.itemOptions.add(new ItemOption(237, Util.nextInt(1, 4)));
        b60.itemOptions.add(new ItemOption(72, Util.nextInt(1, 4)));
        b60.color = 4;
        listItemVip.add(b60);    
        
        
        
        
        
        
        
        
        

        for (int i = 457; i < 457; i++) {
            Item item = ItemService.gI().createNewItem((short) i);
            listItemVip.add(item);

            Item itemVip = ItemService.gI().createNewItem((short) (i));

            listItemVip.add(itemVip);

        }

    }

    public static void sendDataQuay(Player player, byte type) {
        Message msg = null;
        try {
            msg = new Message(69);
            msg.writer().writeByte(type);
            int size = type == 0 ? listItem.size() : listItemVip.size();
            size = type == 0 ? listItem.size() : listItemVip.size();
            msg.writer().writeInt(size);
            for (int i = 0; i < size; i++) {
                msg.writer().writeInt(type == 0 ? listItem.get(i).template.id : listItemVip.get(i).template.id);
                // Màu sắc, để từ 0->7
                msg.writer().writeInt(type == 0 ? listItem.get(i).color : listItemVip.get(i).color);
            }
            //Van may
            msg.writer().writeInt(Util.nextInt(1, 150));
            //icon chia khoa
            msg.writer().writeInt(30207);
            msg.writer().writeInt(30208);
            player.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendListReceive(List<Item> list, Player player) {
        try {
            Message msg = new Message(69);
            msg.writer().writeByte(1);
            int size = list.size();
            msg.writer().writeInt(size);
            for (int i = 0; i < size; i++) {
                msg.writer().writeInt(list.get(i).template.id);
                // Màu sắc, để từ 0->7
                msg.writer().writeInt(list.get(i).color);
            }
            player.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void readData(Message msg, Player player) {
        try {
            int type = msg.reader().readByte();
            if (type == 1 || type == 4) {
                int luotQuay = msg.reader().readInt();
                List<Item> receive = new ArrayList<>();
                for (int i = 0; i < luotQuay; i++) {
                    receive.add(type == 1 ? listItem.get(Util.nextInt(listItem.size() - 1)) : listItemVip.get(Util.nextInt(listItemVip.size() - 1)));
                }
                int dem = 0;
                int slKey = 0;
                for (Item it : player.inventory.itemsBag) {
                    if (it == null || it.template == null) {
                        dem++;
                        continue;
                    }
                    if ((type == 1 && it.template.id == 1873) || (type == 4 && it.template.id == 1874)) {
                        slKey += it.quantity;
                    }

                }
                if (slKey < luotQuay) {
                    Service.gI().sendThongBao(player, "Bạn không đủ key để quay");
                    return;
                }
                if (dem < luotQuay) {
                    Service.gI().sendThongBao(player, "Hành trang không đủ chỗ trống");
                    return;
                } else {
                    slKey = luotQuay;
                    for (Item i : receive) {
                        i.quantity = 1;
                        InventoryService.gI().addItemBag(player, i,999999);
                        
                    }
                    for (Item it : player.inventory.itemsBag) {
                        if (slKey <= 0) {
                            break;
                        }
                        if (it == null || it.template == null) {
                            continue;
                        }
                        if ((type == 1 && it.template.id == 1873) || (type == 4 && it.template.id == 1874)) {
                            int min = slKey < it.quantity ? slKey : it.quantity;
                            slKey -= min;
                            InventoryService.gI().subQuantityItemsBag(player, it, min);
                            listItem.clear();
                            listItemVip.clear();
                            loadItem();
                        }
                    }
                    InventoryService.gI().sendItemBag(player);
                    sendListReceive(receive, player);
                }
            } else if (type == 0 || type == 3) {
                sendDataQuay(player, (byte) type);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
