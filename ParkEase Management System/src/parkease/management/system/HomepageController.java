/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package parkease.management.system;
import java.sql.Connection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
         colID.setCellValueFactory(new PropertyValueFactory<>("id"));
    colType.setCellValueFactory(new PropertyValueFactory<>("vtype"));
    colNum.setCellValueFactory(new PropertyValueFactory<>("vnumber"));
    colSlot.setCellValueFactory(new PropertyValueFactory<>("slot"));
    colErT.setCellValueFactory(new PropertyValueFactory<>("entime"));
    colAt.setCellValueFactory(new PropertyValueFactory<>("alltime"));

    loadDataFromDatabase();
    tableview.setOnMouseClicked(event -> {
    Vehicles selected = tableview.getSelectionModel().getSelectedItem();
    if (selected != null) {
        tfID.setText(selected.getId());
        tfVT.setText(selected.getVtype());
        tfVN.setText(selected.getVnumber());
        tfAS.setText(selected.getSlot());
        tfET.setText(selected.getEntime());
        tfAT.setText(selected.getAlltime());
    }
});
    }
    
    private void loadDataFromDatabase() {
    ObservableList<Vehicles> vehicleList = FXCollections.observableArrayList();
    try (Connection con = DBConnect.connect();
         var stmt = con.createStatement();
         var rs = stmt.executeQuery("SELECT * FROM vehicles")) {

        while (rs.next()) {
            vehicleList.add(new Vehicles(
                String.valueOf(rs.getInt("id")),
                rs.getString("vtype"),
                rs.getString("vnumber"),
                rs.getString("slot"),
                rs.getString("entime"),
                rs.getString("alltime")
            ));
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
    tableview.setItems(vehicleList);
}


    @FXML
private void addActn(ActionEvent event) {
    try (Connection con = DBConnect.connect();
         var stmt = con.prepareStatement(
             "INSERT INTO vehicles (vtype, vnumber, slot, entime, alltime) VALUES (?, ?, ?, ?, ?)")) {

        stmt.setString(1, tfVT.getText());
        stmt.setString(2, tfVN.getText());
        stmt.setString(3, tfAS.getText());
        stmt.setString(4, tfET.getText());
        stmt.setString(5, tfAT.getText());

        stmt.executeUpdate();
        loadDataFromDatabase(); // Refresh table

        // Optional: Clear input fields
        tfVT.clear(); tfVN.clear(); tfAS.clear(); tfET.clear(); tfAT.clear();

    } catch (Exception e) {
        e.printStackTrace();
    }
}


    @FXML
private void upActn(ActionEvent event) {
    Vehicles selected = tableview.getSelectionModel().getSelectedItem();

    if (selected != null) {
        try (Connection con = DBConnect.connect();
             var stmt = con.prepareStatement(
                 "UPDATE vehicles SET vtype=?, vnumber=?, slot=?, entime=?, alltime=? WHERE id=?")) {

            stmt.setString(1, tfVT.getText());
            stmt.setString(2, tfVN.getText());
            stmt.setString(3, tfAS.getText());
            stmt.setString(4, tfET.getText());
            stmt.setString(5, tfAT.getText());
            stmt.setInt(6, Integer.parseInt(selected.getId())); // ID as int

            stmt.executeUpdate();
            loadDataFromDatabase(); // refresh
            clearFields();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
    private void clearFields() {
    tfID.clear();
    tfVT.clear();
    tfVN.clear();
    tfAS.clear();
    tfET.clear();
    tfAT.clear();
}



    @FXML
private void delActn(ActionEvent event) {
    Vehicles selected = tableview.getSelectionModel().getSelectedItem();

    if (selected != null) {
        try (Connection con = DBConnect.connect();
             var stmt = con.prepareStatement("DELETE FROM vehicles WHERE id=?")) {

            stmt.setInt(1, Integer.parseInt(selected.getId()));
            stmt.executeUpdate();
            loadDataFromDatabase(); // refresh
            clearFields();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

    
}
