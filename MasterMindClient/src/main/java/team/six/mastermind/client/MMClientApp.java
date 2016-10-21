package team.six.mastermind.client;

import java.io.IOException;
import java.net.Socket;
import team.six.mastermind.common.MMPacket;

/**
 *
 * @author j0nbiz
 */
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

        // Game start request (Random answer)
        client.sendPacket(new MMPacket((byte) 0, (byte) 0, (byte) 0, (byte) 0));
        
        // Test packet
        client.sendPacket(new MMPacket((byte) 5, (byte) 5, (byte) 5, (byte) 5));
        client.sendPacket(new MMPacket((byte) 2, (byte) 4, (byte) 6, (byte) 7));
    }
}
