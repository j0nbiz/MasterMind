package team.six.fxmlcontrollers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

/**
 * FXML Controller for How To Play window.
 *
 * @author Erika Bourque
 */
public class FXMLHowToController implements Initializable {

    private Stage stage;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // loaded
    }

    /**
     * Sets the context for the class in place of overloaded constructor.
     *
     * @param stage The How To Play window's stage
     */
    public void setContext(Stage stage) {
        this.stage = stage;
    }

    /**
     * Event handler for the Close button. Closes the window.
     *
     * @param event
     */
    @FXML
    void onClose(ActionEvent event) {
        stage.close();
    }
}
