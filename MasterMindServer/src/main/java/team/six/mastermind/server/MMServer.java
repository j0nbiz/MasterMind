package team.six.mastermind.server;

import java.io.*;
import java.net.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.six.mastermind.common.MMGame;
import team.six.mastermind.common.MMPacket;

/**
 *
 * @author j0nbiz
 */
public class MMServer {
    // Logger
    private final Logger log = LoggerFactory.getLogger(getClass().getName());
    
    // Net IO variables
    private static final int BUFFSIZE = 4; // Packet holds 4 components

    private ServerSocket serverSocket;

    private byte[] byteBuffer = new byte[BUFFSIZE];
    private int bytesRcvd;
    private int totalBytesRcvd = 0;

    public MMServer() throws IOException {
        this.serverSocket = new ServerSocket(50000);

        // Automaticaly start the server
        this.start();
    }

    public void start() throws IOException {
        
        
        log.info("Server initialized!");
        log.info("Address: " + InetAddress.getLocalHost().getHostAddress());
        log.info("");
        
        // Block here until connection is made with client
        Socket client = serverSocket.accept();
        log.info("Client connected! Address: " + client.getInetAddress().getHostAddress());
        log.info("");

        // Create packet to interpret response
        MMPacket packet = new MMPacket();

        // Create null game to know first packet and implement testing feature of using specified answer
        MMGame game = null;

        for (;;) {
            while (totalBytesRcvd < byteBuffer.length) {
                // Check for client disconnection and throw exception
                if ((bytesRcvd = client.getInputStream().read(byteBuffer, totalBytesRcvd, byteBuffer.length - totalBytesRcvd)) == -1) {
                    throw new SocketException("Connection was interrupted!");
                }
                totalBytesRcvd += bytesRcvd;
            }
            totalBytesRcvd = 0; // Reset buffer counter
                
            // Fill current packet
            packet.decode(byteBuffer);

            // Interpret packets
            if (game == null) {
                if (packet.equals(new MMPacket((byte) 0, (byte) 0, (byte) 0, (byte) 0))) {
                    // New game with random answer
                    game = new MMGame();
                } else {
                    // New game with specified answer
                    game = new MMGame(packet);
                }

                log.info("Game created! (Answer = " + game.getAnswer().toString() + ")");
                log.info("");
            } else if (game.getRound() > 0) {
                // Interpret incoming packet
                log.info("Round: " + game.getRound());
                log.info("Guess: " + packet.toString());
                log.info("");

                //Send back interpretation
                for (byte comp : game.interpret(packet).getBytes()) {
                    client.getOutputStream().write(comp); // Send all packet components to server
                }
            } if(game.getRound() == 10) {
                log.info("Game over!");
            }
        }
    }
}
