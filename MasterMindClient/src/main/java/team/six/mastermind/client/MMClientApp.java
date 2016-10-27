package team.six.mastermind.client;

import java.io.IOException;
import java.net.Socket;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import team.six.fxmlcontrollers.FXMLConnectController;
import team.six.mastermind.common.MMConfig;
import team.six.mastermind.common.MMPacket;

/**
 *
 * @author j0nbiz
 */
public class MMClientApp extends Application {
    private MMConfig conf;
    
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
    
    public static void main(String[] args) throws IOException {
        launch(args);
    }
    
    /*
    MMClient client = new MMClient(new Socket("localhost", 50000));

        // Game start request (Random answer)
        client.sendPacket(new MMPacket((byte) 0, (byte) 0, (byte) 0, (byte) 0));
        
        // Test packet
        client.sendPacket(new MMPacket((byte) 5, (byte) 5, (byte) 5, (byte) 5));
        client.sendPacket(new MMPacket((byte) 2, (byte) 4, (byte) 6, (byte) 7));
    */
}
