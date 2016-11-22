/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package team.six.mastermind.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.six.mastermind.common.MMGame;
import team.six.mastermind.common.MMPacket;

/**
 *
 * @author 1141669
 */
public class GameThread implements Runnable {

    private Socket client;
    private final Logger log = LoggerFactory.getLogger(getClass().getName());
    private ServerSocket serverSocket;
    private static final int BUFFSIZE = 4; // Packet holds 4 components
    private byte[] byteBuffer = new byte[BUFFSIZE];
    private int bytesRcvd;
    private int totalBytesRcvd = 0;
    MMGame game;

    public GameThread(Socket client) {
        this.client = client;
    }

    public void run() {
        // Create packet to interpret response
        MMPacket packet = new MMPacket();
        for (;;) {

            try {
                receivePackets();
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

                    // Send back succes request and allow client loop to continue
                    for (byte comp : new MMPacket((byte) 0, (byte) 0, (byte) 0, (byte) 0).getBytes()) {
                        client.getOutputStream().write(comp); // Send all packet components to client
                    }
                } else if (packet.equals(new MMPacket((byte) 10, (byte) 10, (byte) 10, (byte) 10))) {
                    // Check for game restart request
                    // Send back succes request and allow client loop to continue
                    for (byte comp : new MMPacket((byte) 0, (byte) 0, (byte) 0, (byte) 0).getBytes()) {
                        client.getOutputStream().write(comp); // Send all packet components to client
                    }

                    // Disconnect
                    client.close(); // Close old client

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
            } catch (SocketException e) {
                try {
                    client.close(); // Close old client
                    log.info("Client connection closed.");
                    break;
                } catch (IOException ex) {
                    java.util.logging.Logger.getLogger(GameThread.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(GameThread.class.getName()).log(Level.SEVERE, null, ex);
                break;
            }
        }
    }

    /**
     * This waits and receives packets
     *
     * @throws SocketException, IOException
     */
    public void receivePackets() throws SocketException, IOException {
        while (totalBytesRcvd < byteBuffer.length) {
            // Check for client disconnection and throw exception
            if ((bytesRcvd = client.getInputStream().read(byteBuffer, totalBytesRcvd, byteBuffer.length - totalBytesRcvd)) == -1) {
                throw new SocketException("Connection lost!");
            }
            totalBytesRcvd += bytesRcvd;
        }
        totalBytesRcvd = 0; // Reset buffer counter
    }
}
