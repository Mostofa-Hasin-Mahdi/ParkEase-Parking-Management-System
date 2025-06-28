/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package parkease.management.system;

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
         String username = "";
        String password = "";
        username = nmfd.getText();
        password = psfd.getText();
        if(username.equalsIgnoreCase("admin") &&
                password.equalsIgnoreCase("admin")){
            System.out.println("Login Succesful");
            
            //opening new dashboard/window/fxml file
            Stage stage = new Stage();
             Parent root = FXMLLoader.load(getClass().getResource("homepage.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        else{
            System.out.println("Login Failed");
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
