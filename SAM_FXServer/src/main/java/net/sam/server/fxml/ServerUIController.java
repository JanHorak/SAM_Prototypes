/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package main.java.net.sam.server.fxml;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import main.java.net.sam.server.servermain.Server;


/**
 *
 * @author janhorak
 */
public class ServerUIController implements Initializable {
    
    
    @FXML
    private CheckBox chk_defaultIP;
    @FXML
    private CheckBox chk_defaultPORT;
    @FXML
    private CheckBox chk_defaultNUMBERUSERS;
    
    @FXML
    private TextField tf_defaultIP;
    @FXML
    private TextField tf_defaultPORT;
    @FXML
    private TextField tf_defaultNUMBERUSERS;
    
    @FXML
    private Button btn_startServer;
    
    @FXML
    private Button btn_stopServer;
    
    @FXML
    private Label statusLabel;
    
    private Server server;
    
    @FXML
    private void setOwnIP(){
        if (chk_defaultIP.isSelected()){
            tf_defaultIP.setText("127.0.0.1");
            tf_defaultIP.setDisable(true);
        }else {
            tf_defaultIP.setDisable(false);
            tf_defaultIP.setText("");
        }
    }
    
    @FXML
    private void setOwnPORT(){
        if (chk_defaultPORT.isSelected()){
            tf_defaultPORT.setText("2222");
            tf_defaultPORT.setDisable(true);
        } else {
            tf_defaultPORT.setDisable(false);
            tf_defaultPORT.setText("");
        }
    }
    
    @FXML
    private void setOwnNUMBER(){
        if (chk_defaultNUMBERUSERS.isSelected()){
            tf_defaultNUMBERUSERS.setText("15");
            tf_defaultNUMBERUSERS.setDisable(true);
        } else {
            tf_defaultNUMBERUSERS.setDisable(false);
            tf_defaultNUMBERUSERS.setText("");
        }
    }
    
    @FXML
    private void stopServer(ActionEvent event){
        try {
            server.shutDown();
        } catch (IOException ex) {
            Logger.getLogger(ServerUIController.class.getName()).log(Level.SEVERE, null, ex);
        }
        btn_startServer.setDisable(false);
        btn_stopServer.setDisable(true);
        statusLabel.setText("Stopped");
    }
    
    @FXML
    private void startServer(ActionEvent event) {
        server = new Server();
        btn_startServer.setDisable(true);
        btn_stopServer.setDisable(false);
        statusLabel.setText("Listening...");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
