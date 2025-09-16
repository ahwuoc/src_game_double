/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import item.Item;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class PhucLoiTemPlate {
    public String name;
    public int param;
    public PhucLoiTemPlate(String name,int param){
        this.name = name;
        this.param = param;
    }
    public List<Item> items = new ArrayList<Item>();
    
}
