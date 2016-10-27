/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package team.six.fxmlcontrollers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

/**
 * FXML Controller for About window.
 *
 * @author Erika Bourque
 */
public class FXMLAboutController implements Initializable {
    private Stage stage;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // loaded
    }

    public void setContext(Stage stage){
        this.stage = stage;
    }
    
    /**
     * Event handler for the Close button.  Closes the window.
     * 
     * @param event 
     */
    @FXML
    void onClose(ActionEvent event)
    {
        stage.close();
    }    
}
