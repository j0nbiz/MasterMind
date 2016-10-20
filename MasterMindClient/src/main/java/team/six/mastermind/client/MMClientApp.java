package team.six.mastermind.client;

import java.io.IOException;
import java.net.Socket;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import static javafx.application.Application.launch;
import team.six.mastermind.common.MMPacket;


public class MMClientApp /*extends Application*/ {

    /*
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Scene.fxml"));
        
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/Styles.css");
        
        stage.setTitle("JavaFX and Maven");
        stage.setScene(scene);
        stage.show();
    }*/

    public static void main(String[] args) throws IOException {
        MMClient client = new MMClient(new Socket("localhost", 50000));
        
        client.sendStartReq();
        client.sendGuess(new MMPacket((byte) 5, (byte) 5, (byte) 5, (byte) 5));
        client.sendGuess(new MMPacket((byte) 2, (byte) 4, (byte) 6, (byte) 7));
    }
}
