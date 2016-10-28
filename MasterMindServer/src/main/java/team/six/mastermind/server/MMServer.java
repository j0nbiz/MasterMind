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
    // Logger
    private final Logger log = LoggerFactory.getLogger(getClass().getName());
    
    // Net IO variables
    private static final int BUFFSIZE = 4; // Packet holds 4 components

    private ServerSocket serverSocket;
    private Socket client;
    
    private byte[] byteBuffer = new byte[BUFFSIZE];
    private int bytesRcvd;
    private int totalBytesRcvd = 0;

    /**
     * Default constructor.  Opens the server socket and starts the server.
     * 
     * @throws IOException 
     */
    public MMServer() throws IOException {
        this.serverSocket = new ServerSocket(50000);

        // Automaticaly start the server
        this.start();
    }

    /**
     * Starts the server running on a loop.  Interacts with the game when a client connects.
     * 
     * @throws IOException 
     */
    public void start() throws IOException {
        log.info("Server initialized!");
        log.info("Address: " + InetAddress.getLocalHost().getHostAddress());
        log.info("");
        
        // Block here until connection is made with client
        client = serverSocket.accept();
        log.info("Client connected! Address: " + client.getInetAddress().getHostAddress());
        log.info("");

        // Create packet to interpret response
        MMPacket packet = new MMPacket();

        // Create null game to know first packet and implement testing feature of using specified answer
        MMGame game = null;

        for (;;) {
            try{
                receivePackets();
            }catch(SocketException e){
                client.close(); // Close old client

                log.info("Client connection interupted! Waiting for new client...");
                log.info("");

                client = serverSocket.accept(); // Accept new client

                game = null; // Unset game to fall into if statement

                log.info("Client connected! Address: " + client.getInetAddress().getHostAddress());
                log.info("");
                
                totalBytesRcvd = 0; // Reset buffer to accept new packet in case connection was lost before all 4 bytes received
                
                receivePackets(); // Receive new packets
            }
            
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
            }else if (packet.equals(new MMPacket((byte) 10, (byte) 10, (byte) 10, (byte) 10))) {
                // Check for game restart request
                // Send back succes request and allow client loop to continue
                for (byte comp : new MMPacket((byte) 0, (byte) 0, (byte) 0, (byte) 0).getBytes()) {
                    client.getOutputStream().write(comp); // Send all packet components to client
                }

                client.close(); // Close old client
                client = serverSocket.accept(); // Accept new client

                game = null; // Unset game to fall into if statement
                
                log.info("Restarting game!");
                log.info("");

            }else{
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
    }
    
    public void receivePackets() throws SocketException, IOException{
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
