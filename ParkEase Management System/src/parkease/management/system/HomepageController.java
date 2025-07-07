/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package parkease.management.system;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author Hasin
 */
public class HomepageController implements Initializable {
    @FXML
    private TableView<Vehicles> tableview;
    @FXML
    private TableColumn<Vehicles, Integer> colID;
    @FXML
    private TableColumn<Vehicles, String> colType;
    @FXML
    private TableColumn<Vehicles, String> colNum;
    @FXML
    private TableColumn<Vehicles, String> colSlot;
    @FXML
    private TableColumn<Vehicles, String> colErT;
    @FXML
    private TableColumn<Vehicles, String> colAt;
    @FXML
    private TextField tfID;
    @FXML
    private TextField tfVT;
    @FXML
    private TextField tfAS;
    @FXML
    private TextField tfVN;
    @FXML
    private TextField tfET;
    @FXML
    private TextField tfAT;
    @FXML
    private Button addbtn;
    @FXML
    private Button upbtn;
    @FXML
    private Button delbtn;
    
    ObservableList<Vehicles> list = FXCollections.observableArrayList( 
    new Vehicles("1", "Sedan", "DHA-GA-32152", "A1", "15:31", "2 Hours"),
    new Vehicles("2", "Motorcycle", "DHA-LA-23141", "A2", "15:00", "1 Hours")
    );
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        colID.setCellValueFactory(new PropertyValueFactory<Vehicles, Integer>("id"));
        colType.setCellValueFactory(new PropertyValueFactory<Vehicles, String>("vtype"));
        colNum.setCellValueFactory(new PropertyValueFactory<Vehicles, String>("vnumber"));
        colSlot.setCellValueFactory(new PropertyValueFactory<Vehicles, String>("slot"));
        colErT.setCellValueFactory(new PropertyValueFactory<Vehicles, String>("entime"));
        colAt.setCellValueFactory(new PropertyValueFactory<Vehicles, String>("alltime"));
        tableview.setItems(list);
    }    

    @FXML
    private void addActn(ActionEvent event) {
    }

    @FXML
    private void upActn(ActionEvent event) {
    }

    @FXML
    private void delActn(ActionEvent event) {
    }
    
}
