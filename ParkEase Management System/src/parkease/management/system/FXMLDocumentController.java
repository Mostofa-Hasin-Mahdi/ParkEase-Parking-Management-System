/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package parkease.management.system;
import java.sql.Connection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author Hasin
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private Button lgnbtn;
    @FXML
    private Button supbtn;
    @FXML
    private TextField nmfd;
    @FXML
    private TextField psfd;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
private void loginMethod(ActionEvent event) throws IOException {
    String username = nmfd.getText();
    String password = psfd.getText();

    try (Connection con = DBConnect.connect();
         PreparedStatement stmt = con.prepareStatement("SELECT id FROM users WHERE username=? AND password=?")) {

        stmt.setString(1, username);
        stmt.setString(2, password);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            int userId = rs.getInt("id");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("homepage.fxml"));
            Parent root = loader.load();

            HomepageController controller = loader.getController();
            controller.setLoggedInUserId(userId);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } else {
            System.out.println("Login Failed");
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
}



    @FXML
    private void supmethod(ActionEvent event) throws IOException {
             Stage stage = new Stage();
             Parent root = FXMLLoader.load(getClass().getResource("FXML.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
    }
    
}
