/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package parkease.management.system;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Hasin
 */
public class Vehicles {
     public SimpleStringProperty id;
    public SimpleStringProperty vtype;
    public SimpleStringProperty vnumber;
    public SimpleStringProperty slot;
    public SimpleStringProperty entime;
    public SimpleStringProperty alltime;
    
    public Vehicles(String id, String vtype, String vnumber, String slot, String entime, String alltime){
        this.id = new SimpleStringProperty(id);
        this.vtype = new SimpleStringProperty(vtype);
        this.vnumber = new SimpleStringProperty(vnumber);
        this.slot = new SimpleStringProperty(slot);
        this.entime = new SimpleStringProperty(entime);
        this.alltime = new SimpleStringProperty(alltime);
        
    
    }
    
    public String getId() {
        return id.get();
    }

    public String getVtype() {
        return vtype.get();
    }

    public String getVnumber() {
        return vnumber.get();
    }

    public String getSlot() {
        return slot.get();
    }

    public String getEntime() {
        return entime.get();
    }
    
    public String getAlltime() {
        return alltime.get();
    }
    
     public void setId(String id) {
    this.id.set(id);
}
      public void setVtype(String vtype) {
    this.vtype.set(vtype);
}
      
       public void setVnumber(String vnumber) {
    this.vnumber.set(vnumber);
}

         public void setSlot(String slot) {
    this.slot.set(slot);
}

              public void setEntime(String entime) {
    this.entime.set(entime);
}
              
              public void setAlltime(String alltime) {
    this.alltime.set(alltime);
}
    
    
    
}
