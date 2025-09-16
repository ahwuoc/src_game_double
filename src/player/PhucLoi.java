/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package player;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import jdbc.daos.PhucLoiDAO;

/**
 *
 * @author Administrator
 */
public class PhucLoi {
    public static int SO_NGAY_DIEM_DANH = 7;
    public int id;
    public int trace;
    public long lastTimeReceive;
    public PhucLoi(){}
    
    public PhucLoi(int id,int trace,long lastTime){
        this.id = id;
        this.trace = trace;
        this.lastTimeReceive = lastTime;
        if(lastTime == 0){
            this.lastTimeReceive = System.currentTimeMillis();
        }
    }
    
    public byte reset(){
//        LocalDate startDateDiemDanh = LocalDate.of(2025, 3, 26);
        LocalDate oldDate = Instant.ofEpochMilli(lastTimeReceive)
                                   .atZone(ZoneId.systemDefault())
                                   .toLocalDate();
        LocalDate newDate = Instant.ofEpochMilli(System.currentTimeMillis())
                                   .atZone(ZoneId.systemDefault())
                                   .toLocalDate();
        long daysBetween = ChronoUnit.DAYS.between(oldDate, newDate);
        switch(PhucLoiDAO.map[id]){
            case 0, 2 ->{
                if(!oldDate.equals(newDate)){
                    trace = 0;
                    this.lastTimeReceive = System.currentTimeMillis();
                    return PhucLoiDAO.map[id];
                }
            }
            case 1 -> {
                //số ngày điểm danh
                if(daysBetween >= 30){
                    trace = 0;
                    this.lastTimeReceive = System.currentTimeMillis();
                    return PhucLoiDAO.map[id];
                }
            }
            case 4, 3 -> {
                if(daysBetween >= 7){
                    trace = 0;
                    this.lastTimeReceive = System.currentTimeMillis();
                    return PhucLoiDAO.map[id];
                }
            }
            
        }
        return -1;
    }
}

