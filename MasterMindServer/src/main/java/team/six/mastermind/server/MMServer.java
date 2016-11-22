package team.six.mastermind.server;

import java.io.*;
import java.net.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class interacts with the game, and transfers packets to the client.
 *
 * @author Jonathan Bizier
 * @author Erika Bourque
 */
public class MMServer {

    // Logger
    private final Logger log = LoggerFactory.getLogger(getClass().getName());

    // Server variables
    private ServerSocket serverSocket;

    /**
     * Default constructor. Opens the server socket and starts the server.
     *
     * @throws IOException
     */
    public MMServer() throws IOException {
        this.serverSocket = new ServerSocket(50000);

        // Automaticaly start the server
        this.start();
    }

    /**
     * Starts the server running on a loop. Interacts with the game when a
     * client connects.
     *
     * @throws IOException
     */
    public void start() throws IOException {
        log.info("Server initialized!");
        log.info("Address: " + InetAddress.getLocalHost().getHostAddress());
        log.info("");

        for (;;) {
            try {
                // Wait for client connection
                Socket client = serverSocket.accept();
                log.info("Client connected! Address: " + client.getInetAddress().getHostAddress());

                // Create game thread and start it
                GameThread game = new GameThread(client);
                Thread thread = new Thread(game);
                thread.start();
                log.info("Thread Created!");
                log.info("");
            } catch (SocketException e) {
                log.info("Client connection interupted!");
            }
        }
    }

}
