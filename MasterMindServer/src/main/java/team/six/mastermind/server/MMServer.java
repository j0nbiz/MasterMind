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
    private static final int BUFFSIZE = 4; // Packet holds 4 components

    private final Logger log = LoggerFactory.getLogger(getClass().getName());

    private ServerSocket serverSocket;

    private byte[] byteBuffer = new byte[BUFFSIZE];
    private int bytesRcvd;
    private int totalBytesRcvd = 0;

    public MMServer(ServerSocket serversock) throws IOException {
        this.serverSocket = serversock;

        // Automaticaly start the server
        this.start();
    }

    public void start() throws IOException {
        log.info("Server initialized!");
        log.info("");

        // Block here until connection is made with client
        Socket client = serverSocket.accept();

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

                // Fill current packet
                packet.decode(byteBuffer);

                // Logging
                log.info("Got new packet:");

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
                } else if (game.getRound() != 10) {
                    // Interpret incoming packet
                    log.info("Round: " + game.getRound());
                    log.info("Guess: " + packet.toString() + " (Matches answer? " + game.interpret(packet) + ")");
                    log.info("");
                } else {
                    log.info("Game over!");
                }
            }
            totalBytesRcvd = 0;
        }
    }
}
