package team.six.mastermind.server;

import java.io.IOException;
import java.net.ServerSocket;

/**
 *
 * @author j0nbiz
 */
public class MMServerApp {

    // Create a server instance
    public static void main(String[] args) throws IOException, InterruptedException {
        MMServer server = new MMServer(new ServerSocket(50000));
    }
}
