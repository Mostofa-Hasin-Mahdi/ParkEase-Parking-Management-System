package parkease.management.system;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
    private TableView<Transaction> transactionTable;

    @FXML
    private TableColumn<Transaction, String> colId;

    @FXML
    private TableColumn<Transaction, String> colType;

    @FXML
    private TableColumn<Transaction, String> colNum;

    @FXML
    private TableColumn<Transaction, String> colErT;

    @FXML
    private TableColumn<Transaction, String> colExT;

    @FXML
    private TableColumn<Transaction, String> colDurT;

    @FXML
    private TableColumn<Transaction, String> colPrice;

    @FXML
    private TableColumn<Transaction, String> colAdmin;

    private ObservableList<Transaction> transactionList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize columns
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colType.setCellValueFactory(new PropertyValueFactory<>("vtype"));
        colNum.setCellValueFactory(new PropertyValueFactory<>("vnumber"));
        colErT.setCellValueFactory(new PropertyValueFactory<>("entime"));
        colExT.setCellValueFactory(new PropertyValueFactory<>("extime"));
        colDurT.setCellValueFactory(new PropertyValueFactory<>("duration"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colAdmin.setCellValueFactory(new PropertyValueFactory<>("userId"));
        
        loadTransactions();
    }

    private void loadTransactions() {
    transactionList.clear();
    try (Connection con = DBConnect.connect();
         PreparedStatement stmt = con.prepareStatement(
             "SELECT t.*, u.username " +  
             "FROM transactions t " +
             "LEFT JOIN users u ON t.user_id = u.id " +
             "ORDER BY t.entime DESC")) {  
        
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            transactionList.add(new Transaction(
                String.valueOf(rs.getInt("id")),
                rs.getString("vtype"),
                rs.getString("vnumber"),
                rs.getString("entime"),
                rs.getString("extime"),
                rs.getString("duration") + " hrs",
                rs.getString("price"),
                rs.getString("username") 
            ));
        }
        transactionTable.setItems(transactionList);
    } catch (Exception e) {
        e.printStackTrace();
    }
}
}