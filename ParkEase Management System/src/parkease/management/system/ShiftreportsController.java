package parkease.management.system;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.MapValueFactory;

public class ShiftreportsController implements Initializable {

    // Corrected declarations with proper generics
    @FXML
    private TableView<Map> shifttable;
    @FXML
    private TableColumn<Map, String> colAdmin;
    @FXML
    private TableColumn<Map, String> colStartTime;
    @FXML
    private TableColumn<Map, String> colEndTime;
    @FXML
    private TableColumn<Map, String> colDuration;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Set up the table columns with proper typing
        colAdmin.setCellValueFactory(new MapValueFactory<>("admin"));
        colStartTime.setCellValueFactory(new MapValueFactory<>("start"));
        colEndTime.setCellValueFactory(new MapValueFactory<>("end"));
        colDuration.setCellValueFactory(new MapValueFactory<>("duration"));

        // Load data from database
        loadShiftData();
    }

    private void loadShiftData() {
        ObservableList<Map> shiftData = FXCollections.observableArrayList();
        
        String query = "SELECT admin_name, start_time, end_time, duration FROM shifts ORDER BY start_time DESC";
        
        try (Connection conn = DBConnect.connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Map<String, String> row = new HashMap<>();
                row.put("admin", rs.getString("admin_name"));
                row.put("start", rs.getTimestamp("start_time").toLocalDateTime()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                row.put("end", rs.getTimestamp("end_time") != null ? 
                        rs.getTimestamp("end_time").toLocalDateTime()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "N/A");
                row.put("duration", rs.getString("duration"));
                
                shiftData.add(row);
            }
            
            shifttable.setItems(shiftData);
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}