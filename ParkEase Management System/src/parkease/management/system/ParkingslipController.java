/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package parkease.management.system;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author Hasin
 */
public class ParkingslipController implements Initializable {

    @FXML
    private Label vtype;
    @FXML
    private Label vnum;
    @FXML
    private Label entime;
    @FXML
    private Label pslot;
    @FXML
    private Label alltime;
    @FXML
    private Label admin;

    /**
     * Initializes the controller class.
     */
    
    public void setParkingData(String vehicleType, String vehicleNumber, 
                             String entryTime, String slot, 
                             String allocatedTime, String adminName) {
        vtype.setText(vehicleType);
        vnum.setText(vehicleNumber);
        entime.setText(entryTime);
        pslot.setText(slot);
        alltime.setText(allocatedTime);
        admin.setText(adminName);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
