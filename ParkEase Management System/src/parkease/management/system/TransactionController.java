package parkease.management.system;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class TransactionController implements Initializable {

    @FXML
    private TableView<Vehicles> transactionTable;

    @FXML
    private TableColumn<Vehicles, String> colType;

    @FXML
    private TableColumn<Vehicles, String> colNum;

    @FXML
    private TableColumn<Vehicles, String> colErT;

    @FXML
    private TableColumn<Vehicles, String> colExT;

    @FXML
    private TableColumn<Vehicles, String> colDurT;

    @FXML
    private TableColumn<Vehicles, String> colPrice;

    @FXML
    private TableColumn<Vehicles, String> colAdmin;

    ObservableList<Vehicles> transactionList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadTransactions();
    }

    private void loadTransactions() {
        try {
            Connection con = DBConnect.connect();
            String query = "SELECT * FROM vehicles WHERE alltime IS NOT NULL AND user_id = ?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, Session.getInstance().getUserID()); // current logged-in admin
            ResultSet rs = stmt.executeQuery();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            while (rs.next()) {
                String type = rs.getString("vtype");
                String number = rs.getString("vnumber");
                String entime = rs.getString("entime");
                String alltime = rs.getString("alltime");
                int userID = rs.getInt("user_id");

                // Calculate duration
                LocalDateTime entry = LocalDateTime.parse(entime, formatter);
                LocalDateTime exit = LocalDateTime.parse(alltime, formatter);
                Duration duration = Duration.between(entry, exit);
                long minutes = duration.toMinutes();

                String durText = minutes + " mins";

                // Price: e.g., 10 currency units per hour
                double ratePerHour = 10.0;
                double price = Math.ceil(minutes / 60.0) * ratePerHour;

                transactionList.add(new Vehicles(type, number, entime, alltime, durText, String.valueOf(price), "Admin " + userID));
            }

            colType.setCellValueFactory(new PropertyValueFactory<>("vtype"));
            colNum.setCellValueFactory(new PropertyValueFactory<>("vnumber"));
            colErT.setCellValueFactory(new PropertyValueFactory<>("entime"));
            colExT.setCellValueFactory(new PropertyValueFactory<>("alltime"));
            colDurT.setCellValueFactory(new PropertyValueFactory<>("duration"));  // must be added in model
            colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));    // must be added in model
            colAdmin.setCellValueFactory(new PropertyValueFactory<>("admin"));    // must be added in model

            transactionTable.setItems(transactionList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
