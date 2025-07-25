/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package parkease.management.system;

import javafx.beans.property.SimpleStringProperty;

public class Vehicles {
    private SimpleStringProperty id;
    private SimpleStringProperty vtype;
    private SimpleStringProperty vnumber;
    private SimpleStringProperty slot;
    private SimpleStringProperty entime;
    private SimpleStringProperty alltime;
    private SimpleStringProperty userId; 
    private SimpleStringProperty duration; 
    private SimpleStringProperty price;    

    
     public Vehicles(String id, String vtype, String vnumber, String slot, String entime, String alltime) {
        this(id, vtype, vnumber, slot, entime, alltime, null); 
    }

    public Vehicles(String id, String vtype, String vnumber, String slot, String entime, String alltime, String userId) {
        this.id = new SimpleStringProperty(id);
        this.vtype = new SimpleStringProperty(vtype);
        this.vnumber = new SimpleStringProperty(vnumber);
        this.slot = new SimpleStringProperty(slot);
        this.entime = new SimpleStringProperty(entime);
        this.alltime = new SimpleStringProperty(alltime);
    }

    // Getters
   public String getId() { return id.get(); }
    public String getVtype() { return vtype.get(); }
    public String getVnumber() { return vnumber.get(); }
    public String getSlot() { return slot.get(); }
    public String getEntime() { return entime.get(); } 
    public String getAlltime() { return alltime.get(); }
    public String getUserId() { return userId == null ? null : userId.get(); }
    

    // Setters
    public void setId(String id) { this.id.set(id); }
    public void setVtype(String vtype) { this.vtype.set(vtype); }
    public void setVnumber(String vnumber) { this.vnumber.set(vnumber); }
    public void setSlot(String slot) { this.slot.set(slot); }
    public void setEntime(String entime) { this.entime.set(entime); }
    public void setAlltime(String alltime) { this.alltime.set(alltime); }
    public void setUserId(String userId) { 
        if (this.userId == null) {
            this.userId = new SimpleStringProperty(userId);
        } else {
            this.userId.set(userId); 
        }
    }
}

