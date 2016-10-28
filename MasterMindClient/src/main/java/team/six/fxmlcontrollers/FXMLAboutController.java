package team.six.fxmlcontrollers;

import java.net.URL;
import java.util.ResourceBundle;
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
     * 
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // loaded
    }

    /**
     * Sets the context for the class in place of overloaded constructor.
     * 
     * @param stage     The About window's stage
     */
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
