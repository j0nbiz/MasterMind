package team.six.mastermind.server;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.six.mastermind.common.MMGame;
import team.six.mastermind.common.MMPacket;

/**
 * This class handles packet receiving and interaction with client
 *
 * @author Erika Bourque
 * @author Jonathan Bizier
 */
public class GameThread implements Runnable {

    // Logger
    private final Logger log = LoggerFactory.getLogger(getClass().getName());

    // Client variables
    private Socket client;
    private MMGame game;
    private static final int BUFFSIZE = 4; // Packet holds 4 components
    private byte[] byteBuffer = new byte[BUFFSIZE];
    private int bytesRcvd;
    private int totalBytesRcvd = 0;

    public GameThread(Socket client) {
        this.client = client;
    }

    public void run() {
        try {
            // Create packet to interpret response
            MMPacket packet = new MMPacket();

            for (;;) {

                // Get new packets
                receivePackets();
                // Fill current packet
                packet.decode(byteBuffer);

                // If no game started
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

                    // Send back succes request and allow client loop to continue
                    for (byte comp : new MMPacket((byte) 0, (byte) 0, (byte) 0, (byte) 0).getBytes()) {
                        client.getOutputStream().write(comp); // Send all packet components to client
                    }
                } else // Check for end game request
                if (packet.equals(new MMPacket((byte) 10, (byte) 10, (byte) 10, (byte) 10))) {
                    for (byte comp : new MMPacket((byte) 0, (byte) 0, (byte) 0, (byte) 0).getBytes()) {
                        client.getOutputStream().write(comp); // Send confirmation
                    }

                    // close client
                    client.close();
                } else {
                    // Interpret incoming packet
                    log.info("Round: " + game.getRound());
                    log.info("Guess: " + packet.toString());

                    MMPacket hint = game.interpret(packet);

                    //Send back interpretation
                    for (byte comp : hint.getBytes()) {
                        client.getOutputStream().write(comp); // Send all packet components to client
                    }

                    log.info("Returning hint: " + hint.toString());
                    log.info("");
                }
            }
        } catch (SocketException e) {
            log.warn("Connection lost!");
        } catch (IOException e) {
            log.warn("Could not write packet components!");
        }
    }

    /**
     * Method that waits and receives packets
     *
     * @throws SocketException, IOException
     */
    public void receivePackets() throws SocketException, IOException {
        while (totalBytesRcvd < byteBuffer.length) {
            // Check for client disconnection and throw exception
            if ((bytesRcvd = client.getInputStream().read(byteBuffer, totalBytesRcvd, byteBuffer.length - totalBytesRcvd)) == -1) {
                client.close(); // Close client since connection is lost
                throw new SocketException("Connection lost!"); // Throw new exception
            }
            totalBytesRcvd += bytesRcvd;
        }
        totalBytesRcvd = 0; // Reset buffer counter
    }
}
