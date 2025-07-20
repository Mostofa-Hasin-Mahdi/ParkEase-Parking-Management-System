package parkease.management.system;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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

public class HomepageController implements Initializable {
    @FXML private TableView<Vehicles> tableview;
    @FXML private TableColumn<Vehicles, Integer> colID;
    @FXML private TableColumn<Vehicles, String> colType;
    @FXML private TableColumn<Vehicles, String> colNum;
    @FXML private TableColumn<Vehicles, String> colSlot;
    @FXML private TableColumn<Vehicles, String> colErT;
    @FXML private TableColumn<Vehicles, String> colAt;

    @FXML private TextField tfID;
    @FXML private TextField tfVT;
    @FXML private TextField tfAS;
    @FXML private TextField tfVN;
    @FXML private TextField tfET;
    @FXML private TextField tfAT;

    @FXML private Button addbtn;
    @FXML private Button upbtn;
    @FXML private Button delbtn;

    private int loggedInUserId;
    @FXML
    private Button trnscbtn;

    // Called externally from login controller
    public void setLoggedInUserId(int id) {
        this.loggedInUserId = id;
        loadDataFromDatabase(); // Load once user is set
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Set up table columns
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colType.setCellValueFactory(new PropertyValueFactory<>("vtype"));
        colNum.setCellValueFactory(new PropertyValueFactory<>("vnumber"));
        colSlot.setCellValueFactory(new PropertyValueFactory<>("slot"));
        colErT.setCellValueFactory(new PropertyValueFactory<>("entime"));
        colAt.setCellValueFactory(new PropertyValueFactory<>("alltime"));

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
             PreparedStatement stmt = con.prepareStatement("SELECT * FROM vehicles WHERE user_id = ?")) {

            stmt.setInt(1, loggedInUserId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                vehicleList.add(new Vehicles(
                    String.valueOf(rs.getInt("id")),
                    rs.getString("vtype"),
                    rs.getString("vnumber"),
                    rs.getString("slot"),
                    rs.getString("entime"),
                    rs.getString("alltime"),
                    rs.getString("user_Id")
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
         PreparedStatement stmt = con.prepareStatement(
             "INSERT INTO vehicles (user_id, vtype, vnumber, slot, entime, alltime) VALUES (?, ?, ?, ?, ?, ?)")) {

        // Auto-set current timestamp for entime
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedNow = now.format(formatter);

        stmt.setInt(1, loggedInUserId);
        stmt.setString(2, tfVT.getText());
        stmt.setString(3, tfVN.getText());
        stmt.setString(4, tfAS.getText());
        stmt.setString(5, formattedNow); // entime set automatically
        stmt.setString(6, tfAT.getText());

        stmt.executeUpdate();
        loadDataFromDatabase();
        clearFields();
    } catch (Exception e) {
        e.printStackTrace();
    }
}


    @FXML
    private void upActn(ActionEvent event) {
        Vehicles selected = tableview.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try (Connection con = DBConnect.connect();
                 PreparedStatement stmt = con.prepareStatement(
                     "UPDATE vehicles SET vtype=?, vnumber=?, slot=?, entime=?, alltime=? WHERE id=? AND user_id=?")) {

                stmt.setString(1, tfVT.getText());
                stmt.setString(2, tfVN.getText());
                stmt.setString(3, tfAS.getText());
                stmt.setString(4, tfET.getText());
                stmt.setString(5, tfAT.getText());
                stmt.setInt(6, Integer.parseInt(selected.getId()));
                stmt.setInt(7, loggedInUserId);

                stmt.executeUpdate();
                loadDataFromDatabase();
                clearFields();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    private int calculatePrice(LocalDateTime entry, LocalDateTime exit) {
    long hours = ChronoUnit.HOURS.between(entry, exit);
    if (hours < 1) return 20;
    else if (hours == 1) return 20;
    else if (hours == 2) return 30;
    else if (hours == 3) return 50;
    else return 70;
}


    @FXML
private void delActn(ActionEvent event) {
    Vehicles selected = tableview.getSelectionModel().getSelectedItem();
    if (selected == null) return;

    try {
        Connection conn = DBConnect.connect();

        // Parse times
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime entryTime = LocalDateTime.parse(selected.getEntime(), formatter);
        LocalDateTime exitTime = LocalDateTime.now();

        int duration = (int) ChronoUnit.HOURS.between(entryTime, exitTime);
        int price = calculatePrice(entryTime, exitTime);

        // Insert into transactions
        String sql = "INSERT INTO transactions (user_id, vtype, vnumber, slot, entime, extime, duration_hours, price) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setInt(1, loggedInUserId);
        pst.setString(2, selected.getVtype());
        pst.setString(3, selected.getVnumber());
        pst.setString(4, selected.getSlot());
        pst.setString(5, selected.getEntime());
        pst.setString(6, exitTime.format(formatter));
        pst.setInt(7, duration);
        pst.setInt(8, price);

        pst.executeUpdate();

        // Delete from vehicles
        PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM vehicles WHERE id = ? AND user_id = ?");
        deleteStmt.setString(1, selected.getId());
        deleteStmt.setInt(2, loggedInUserId);
        deleteStmt.executeUpdate();

        loadDataFromDatabase(); // Refresh table
        clearFields();

    } catch (Exception e) {
        e.printStackTrace();
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
    private void transactn(ActionEvent event) {
    }
}
