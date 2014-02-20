/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sam.client.fxml;

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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import net.sam.client.clientmain.Client;

/**
 *
 * @author janhorak
 */
public class ClientUIController implements Initializable {

    @FXML
    private CheckBox chk_defaultIP;
    @FXML
    private CheckBox chk_defaultPORT;

    @FXML
    private TextField tf_defaultIP;
    @FXML
    private TextField tf_defaultPORT;
    @FXML
    private TextField tf_username;

    @FXML
    private ToggleButton connect;
    
    @FXML
    private TextArea messageArea;
    @FXML
    private TextField messageField;
    @FXML
    private Button sendButton;

    private Client c;
    @FXML
    private void setOwnIP() {
        if (chk_defaultIP.isSelected()) {
            tf_defaultIP.setText("127.0.0.1");
            tf_defaultIP.setDisable(true);
        } else {
            tf_defaultIP.setDisable(false);
            tf_defaultIP.setText("");
        }
    }

    @FXML
    private void setOwnPORT() {
        if (chk_defaultPORT.isSelected()) {
            tf_defaultPORT.setText("2222");
            tf_defaultPORT.setDisable(true);
        } else {
            tf_defaultPORT.setDisable(false);
            tf_defaultPORT.setText("");
        }
    }

    @FXML
    private void connect() {
        if (connect.isSelected()) {
            chk_defaultIP.setDisable(true);
            chk_defaultPORT.setDisable(true);
            tf_defaultIP.setDisable(true);
            tf_defaultPORT.setDisable(true);
            tf_username.setDisable(true);
            messageArea.setDisable(false);
            sendButton.setDisable(false);
            messageField.setDisable(false);
            connect.setText("Disconnect");
            try {
                c.connect();
            } catch (IOException ex) {
                Logger.getLogger(ClientUIController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            chk_defaultIP.setDisable(false);
            chk_defaultPORT.setDisable(false);
            tf_defaultIP.setDisable(false);
            tf_defaultPORT.setDisable(false);
            tf_username.setDisable(false);
            messageArea.setDisable(true);
            sendButton.setDisable(true);
            messageField.setDisable(true);
            connect.setText("Connect");
            try {
                c.disconnect();
            } catch (IOException ex) {
                Logger.getLogger(ClientUIController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        c = new Client();
    }

}
