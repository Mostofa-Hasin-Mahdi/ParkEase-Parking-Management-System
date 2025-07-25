/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package parkease.management.system;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Hasin
 */
public class SignupController implements Initializable {

    @FXML
    private TextField txtusername;
    @FXML
    private TextField txtpass;
    @FXML
    private Button btnregister;
    @FXML
    private Label msgprompt;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

   @FXML
    private void register(ActionEvent event) {
        String username = txtusername.getText().trim();
        String password = txtpass.getText().trim();
        
        // Validate input
        if (username.isEmpty() || password.isEmpty()) {
            msgprompt.setText("Username and password cannot be empty!");
            return;
        }
        
        if (username.length() > 50) {
            msgprompt.setText("Username must be 50 characters or less");
            return;
        }
        
        if (password.length() > 100) {
            msgprompt.setText("Password must be 100 characters or less");
            return;
        }
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            // Get database connection using your existing method
            conn = DBConnect.connect();
            
            if (conn == null) {
                msgprompt.setText("Could not connect to database");
                return;
            }
            
            // Prepare SQL statement
            String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            
            // Execute the statement
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                msgprompt.setText("Registration successful!");
                
                // Close the window after successful registration
                Stage stage = (Stage) btnregister.getScene().getWindow();
                stage.close();
            } else {
                msgprompt.setText("Registration failed!");
            }
            
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) { // Duplicate entry error code for MySQL
                msgprompt.setText("Username already exists!");
            } else {
                msgprompt.setText("Database error: " + e.getMessage());
                e.printStackTrace();
            }
        } finally {
            // Close resources in finally block
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
