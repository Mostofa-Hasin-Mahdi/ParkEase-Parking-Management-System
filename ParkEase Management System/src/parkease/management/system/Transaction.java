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
public class Transaction {
    private SimpleStringProperty id;
    private SimpleStringProperty vtype;
    private SimpleStringProperty vnumber;
    private SimpleStringProperty entime;
    private SimpleStringProperty extime;
    private SimpleStringProperty duration;
    private SimpleStringProperty price;
    private SimpleStringProperty userId;

    public Transaction(String id, String vtype, String vnumber, String entime, 
                      String extime, String duration, String price, String userId) {
        this.id = new SimpleStringProperty(id);
        this.vtype = new SimpleStringProperty(vtype);
        this.vnumber = new SimpleStringProperty(vnumber);
        this.entime = new SimpleStringProperty(entime);
        this.extime = new SimpleStringProperty(extime);
        this.duration = new SimpleStringProperty(duration);
        this.price = new SimpleStringProperty(price);
        this.userId = new SimpleStringProperty(userId);
    }

    // Getters
    public String getId() { return id.get(); }
    public String getVtype() { return vtype.get(); }
    public String getVnumber() { return vnumber.get(); }
    public String getEntime() { return entime.get(); }
    public String getExtime() { return extime.get(); }
    public String getDuration() { return duration.get(); }
    public String getPrice() { return price.get(); }
    public String getUserId() { return userId.get(); }
}
    
    

