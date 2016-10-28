package team.six.mastermind.client;

import java.io.IOException;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import team.six.fxmlcontrollers.FXMLConnectController;
import team.six.mastermind.common.MMConfig;

/**
 * This class starts the GUI application.
 *
 * @author Jonathan Bizier
 */
public class MMClientApp extends Application {

    public static void main(String[] args) throws IOException {
        launch(args);
    }

    private MMConfig conf;

    /**
     * Prepares and loads the GUI.
     *
     * @param stage The stage for the GUI
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        this.conf = new MMConfig();

        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/FXMLConnect.fxml"));
        loader.setResources(ResourceBundle.getBundle("MessagesBundle"));

        GridPane root = (GridPane) loader.load();

        FXMLConnectController cont = loader.getController();
        cont.setContext(this, stage, conf);
        loader.setController(cont);

        Scene scene = new Scene(root);

        stage.setTitle("MasterMind");
        stage.resizableProperty().set(false);
        stage.setScene(scene);
        stage.show();
    }
}
