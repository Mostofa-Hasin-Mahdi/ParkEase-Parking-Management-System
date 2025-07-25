package parkease.management.system;

import java.io.IOException;
import java.net.URL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;

import java.sql.Timestamp;  
import java.util.HashSet;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class HomepageController implements Initializable {
    @FXML private TableView<Vehicles> tableview;
    @FXML private TableColumn<Vehicles, Integer> colID;
    @FXML private TableColumn<Vehicles, String> colType;
    @FXML private TableColumn<Vehicles, String> colNum;
    @FXML private TableColumn<Vehicles, String> colSlot;
    @FXML private TableColumn<Vehicles, String> colErT;
    @FXML private TableColumn<Vehicles, String> colAt;

    private TextField tfID;
    @FXML private TextField tfVN;
    private TextField tfET;
    @FXML private TextField tfAT;

    @FXML private Button addbtn;
    @FXML private Button upbtn;
    @FXML private Button delbtn;

    private int loggedInUserId;
    @FXML
    private Button trnscbtn;
    @FXML
    private ComboBox<String> cbVehicleType;
    @FXML
    private ComboBox<String> cbMetroCode;
    @FXML
    private ComboBox<String> cbAllocatedSlot;
    @FXML
    private Label currentUser;
    @FXML
    private Button logout;
    
    private void showCurrentUsername() {
    try (Connection con = DBConnect.connect();
         PreparedStatement stmt = con.prepareStatement("SELECT username FROM users WHERE id = ?")) {
        
        stmt.setInt(1, loggedInUserId);
        ResultSet rs = stmt.executeQuery();
        
        if (rs.next()) {
            currentUser.setText("" + rs.getString("username"));
        }
    } catch (Exception e) {
        e.printStackTrace();
        currentUser.setText("User: Unknown");
    }
}

    // Called externally from login controller
    public void setLoggedInUserId(int id) {
         this.loggedInUserId = id;
    Session.getInstance().setUserID(id); 
    loadDataFromDatabase();
    showCurrentUsername();
    }
    
    private void initializeParkingSlots() {
    // First generate all possible slots
    ObservableList<String> allSlots = FXCollections.observableArrayList();
    for (char letter = 'A'; letter <= 'E'; letter++) {
        for (int number = 1; number <= 4; number++) {
            allSlots.add(letter + String.valueOf(number));
        }
    }
    
    // Now refresh available slots by filtering occupied ones
    refreshAvailableSlots(allSlots);
}
    
    
private void initializeMetroCodes() {
    ObservableList<String> metroCodes = FXCollections.observableArrayList();
    
    // Dhaka Metro (DHA-)
    metroCodes.addAll(
        "DHA-KA", "DHA-KHA", "DHA-GA", "DHA-GHA",
        "DHA-CH", "DHA-CHA", "DHA-JA", "DHA-JHA",
        "DHA-TA", "DHA-THA", "DHA-DA", "DHA-NA"
    );
    
    // Chittagong Metro (CHA-)
    metroCodes.addAll(
        "CHA-KA", "CHA-KHA", "CHA-GA", "CHA-GHA",
        "CHA-CH", "CHA-CHA", "CHA-JA", "CHA-JHA"
    );
    
    // Khulna Metro (KHA-)
    metroCodes.addAll(
        "KHA-KA", "KHA-KHA", "KHA-GA", "KHA-GHA",
        "KHA-CH", "KHA-CHA", "KHA-JA"
    );
    
    // Rajshahi Metro (RA-)
    metroCodes.addAll(
        "RA-KA", "RA-KHA", "RA-GA", "RA-GHA",
        "RA-CH", "RA-CHA", "RA-JA"
    );
    
    // Sylhet Metro (SY-)
    metroCodes.addAll(
        "SY-KA", "SY-KHA", "SY-GA", "SY-GHA",
        "SY-CH", "SY-CHA"
    );
    
    // Barishal Metro (BA-)
    metroCodes.addAll(
        "BA-KA", "BA-KHA", "BA-GA", "BA-CH",
        "BA-CHA", "BA-JA"
    );
    
    // Rangpur Metro (R-)
    metroCodes.addAll(
        "R-KA", "R-KHA", "R-GA", "R-CH",
        "R-CHA", "R-JA"
    );
    
    // Mymensingh Metro (MY-)
    metroCodes.addAll(
        "MY-KA", "MY-KHA", "MY-GA", "MY-CH",
        "MY-CHA"
    );
    
    // Add military and special codes
    metroCodes.addAll(
        "MIL-KA",  // Military vehicles
        "GOV-KA",  // Government vehicles
        "DIP-KA",  // Diplomatic vehicles
        "PRI-KA"   // Privileged vehicles
    );
    
    cbMetroCode.getItems().setAll(metroCodes);
}
    
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbVehicleType.getItems().addAll("Sedan", "SUV", "Motorcycle", "Truck");
       
        initializeMetroCodes();
        initializeParkingSlots();
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
        // No need to set tfID or tfET anymore
        cbVehicleType.setValue(selected.getVtype());
        
        String[] parts = selected.getVnumber().split("-");
        if (parts.length >= 2) {
            cbMetroCode.setValue(parts[0]);
            tfVN.setText(parts[1]);
        }
        
        cbAllocatedSlot.setValue(selected.getSlot());
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
            
            String entimeStr = rs.getTimestamp("entime") != null 
                ? rs.getTimestamp("entime").toLocalDateTime()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                : "N/A";

            vehicleList.add(new Vehicles(
                String.valueOf(rs.getInt("id")),
                rs.getString("vtype"),
                rs.getString("vnumber"),
                rs.getString("slot"),
                entimeStr,  // Use formatted datetime string
                rs.getString("alltime"),
                String.valueOf(rs.getInt("user_id"))
            ));
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    tableview.setItems(vehicleList);
}
    
   private void refreshAvailableSlots(ObservableList<String> allSlots) {
    try (Connection con = DBConnect.connect();
         PreparedStatement stmt = con.prepareStatement(
             "SELECT slot FROM vehicles WHERE slot IS NOT NULL")) {
        
        ResultSet rs = stmt.executeQuery();
        Set<String> occupiedSlots = new HashSet<>();
        
        while (rs.next()) {
            occupiedSlots.add(rs.getString("slot"));
        }
        
        // Filter out occupied slots from all possible slots
        ObservableList<String> availableSlots = FXCollections.observableArrayList(allSlots);
        availableSlots.removeAll(occupiedSlots);
        
        // Update ComboBox
        cbAllocatedSlot.setItems(availableSlots);
        
    } catch (Exception e) {
        e.printStackTrace();
    }
}
    
 @FXML
private void addActn(ActionEvent event) {
    try (Connection con = DBConnect.connect();
         PreparedStatement stmt = con.prepareStatement(
             "INSERT INTO vehicles (user_id, vtype, vnumber, slot, entime, alltime) VALUES (?, ?, ?, ?, ?, ?)",
             PreparedStatement.RETURN_GENERATED_KEYS)) {
        
        // Get input values
        String vehicleType = cbVehicleType.getValue();
        String metroCode = cbMetroCode.getValue();
        String vehicleNumber = metroCode + "-" + tfVN.getText();
        String allocatedSlot = cbAllocatedSlot.getValue();
        
        // Set current time
        LocalDateTime now = LocalDateTime.now();
        String formattedTime = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        
        // Execute insert
        stmt.setInt(1, loggedInUserId);
        stmt.setString(2, vehicleType);
        stmt.setString(3, vehicleNumber);
        stmt.setString(4, allocatedSlot);
        stmt.setString(5, formattedTime);
        stmt.setString(6, tfAT.getText());
        
        int affectedRows = stmt.executeUpdate();
        
        if (affectedRows > 0) {
            String adminName = getAdminName(loggedInUserId);
            showParkingSlip(vehicleType, vehicleNumber, formattedTime, 
                          allocatedSlot, tfAT.getText(), adminName);
            
            // Regenerate all slots and refresh available ones
            initializeParkingSlots();
            loadDataFromDatabase();
            clearFields();
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}
private String getAdminName(int userId) throws Exception {
    try (Connection con = DBConnect.connect();
         PreparedStatement stmt = con.prepareStatement(
             "SELECT username FROM users WHERE id = ?")) {
        
        stmt.setInt(1, userId);
        ResultSet rs = stmt.executeQuery();
        
        if (rs.next()) {
            return rs.getString("username");
        }
        return "Admin " + userId;
    }
}

private void showParkingSlip(String vehicleType, String vehicleNumber, 
                           String entryTime, String slot, 
                           String allocatedTime, String adminName) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("parkingslip.fxml"));
        Parent root = loader.load();
        
        // Get controller and set data
        ParkingslipController controller = loader.getController();
        controller.setParkingData(vehicleType, vehicleNumber, entryTime, 
                                slot, allocatedTime, adminName);
        
        Stage stage = new Stage();
        stage.setTitle("Parking Slip");
        stage.setScene(new Scene(root));
        stage.show();
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
                 "UPDATE vehicles SET vtype=?, vnumber=?, slot=?, alltime=? WHERE id=? AND user_id=?")) {

            // Get values from ComboBoxes
            String vehicleType = cbVehicleType.getValue();
            String metroCode = cbMetroCode.getValue();
            String vehicleNumber = metroCode + "-" + tfVN.getText();
            String allocatedSlot = cbAllocatedSlot.getValue();

            stmt.setString(1, vehicleType);
            stmt.setString(2, vehicleNumber);
            stmt.setString(3, allocatedSlot);
            stmt.setString(4, tfAT.getText());
            stmt.setInt(5, Integer.parseInt(selected.getId()));
            stmt.setInt(6, loggedInUserId);

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
    int basePrice = 30; // First 3 hours
    
    if (hours <= 3) {
        return basePrice;
    } else {
        
        long extraHours = hours - 3;
        return basePrice + (int)extraHours * 10;
    }
}


    @FXML
private void delActn(ActionEvent event) {
    Vehicles selected = tableview.getSelectionModel().getSelectedItem();
    if (selected == null) return;

    try (Connection conn = DBConnect.connect()) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime entryTime = LocalDateTime.parse(selected.getEntime(), formatter);
        LocalDateTime exitTime = LocalDateTime.now();
        int duration = (int) ChronoUnit.HOURS.between(entryTime, exitTime);
        int price = calculatePrice(entryTime, exitTime);

        String sql = "INSERT INTO transactions (user_id, vtype, vnumber, entime, extime, duration, price) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, loggedInUserId);
            pst.setString(2, selected.getVtype());
            pst.setString(3, selected.getVnumber());
            pst.setString(4, selected.getEntime()); 
            pst.setString(5, exitTime.format(formatter)); 
            pst.setString(6, String.valueOf(duration));
            pst.setString(7, String.valueOf(price));
            pst.executeUpdate();
        }

        // Delete from vehicles
        try (PreparedStatement deleteStmt = conn.prepareStatement(
            "DELETE FROM vehicles WHERE id = ? AND user_id = ?")) {
            deleteStmt.setInt(1, Integer.parseInt(selected.getId()));
            deleteStmt.setInt(2, loggedInUserId);
            deleteStmt.executeUpdate();
        }

        initializeParkingSlots();
        loadDataFromDatabase();
        clearFields();
    } catch (Exception e) {
        e.printStackTrace();
    }
}


    private void clearFields() {
    tfVN.clear();
    tfAT.clear();
    cbVehicleType.getSelectionModel().clearSelection();
    cbMetroCode.getSelectionModel().clearSelection();
    cbAllocatedSlot.getSelectionModel().clearSelection();
}





    @FXML
    private void transactn(ActionEvent event) throws IOException {
         
        FXMLLoader loader = new FXMLLoader(getClass().getResource("transaction.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    
    }

    @FXML
    private void UserLogout(ActionEvent event) {
        Stage stage = (Stage) logout.getScene().getWindow();
    stage.close();
    }
}
