package team.six.mastermind.server;

import java.io.IOException;

/**
 * This app starts the MMServer.
 *
 * @author Jonathan Bizier
 */
public class MMServerApp {
    // Create a server instance
    public static void main(String[] args) throws IOException, InterruptedException {
        MMServer server = new MMServer();
    }
}
