package team.six.mastermind.server;

import java.io.*;
import java.net.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.six.mastermind.common.MMGame;
import team.six.mastermind.common.MMPacket;

/**
 * This class interacts with the game, and transfers packets to the client.
 *
 * @author Jonathan Bizier
 */
public class MMServer {

    // Net IO variables
    private static final int BUFFSIZE = 4; // Packet holds 4 components
    // Logger
    private final Logger log = LoggerFactory.getLogger(getClass().getName());

    private ServerSocket serverSocket;

    private byte[] byteBuffer = new byte[BUFFSIZE];
    private int bytesRcvd;
    private int totalBytesRcvd = 0;

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

        // Block here until connection is made with client
        for (;;) {
            try {
                Socket client = serverSocket.accept();
                GameThread game = new GameThread(client);
                Thread thread = new Thread(game);
                thread.start();
                log.info("Client connected! Address: " + client.getInetAddress().getHostAddress());
                log.info("");
            } catch (SocketException e) {
                log.info("Client connection interupted!");
            }
        }
    }

}
