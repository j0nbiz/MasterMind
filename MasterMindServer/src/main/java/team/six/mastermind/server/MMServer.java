/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package team.six.mastermind.server;

import java.net.*;
import java.io.*;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.six.mastermind.common.MMGame;
import team.six.mastermind.common.MMPacket;

/**
 *
 * @author 1437203
 */
public class MMServer {
    private final Logger log = LoggerFactory.getLogger(getClass().getName());
    
    private ServerSocket serverSocket;
    
    private static final int BUFFSIZE = 4; // Packet holds 4 components
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
            while(totalBytesRcvd < byteBuffer.length){
                // Check for client disconnection and throw exception
                if((bytesRcvd = client.getInputStream().read(byteBuffer, totalBytesRcvd, byteBuffer.length - totalBytesRcvd)) == -1) {
                    throw new SocketException("Connection was interrupted!");
                }
                totalBytesRcvd += bytesRcvd;
                
                // Fill current packet
                packet.decode(byteBuffer);
                
                // Logging
                log.info("Got new packet:");
                log.info("Components: " + packet.toString());
                log.info("");
                
                // Interpret packets
                if(game == null){
                    if(packet.equals(new MMPacket((byte) 0, (byte) 0, (byte) 0, (byte) 0))){
                        // New game with random answer
                        game = new MMGame(); 
                    }else{
                        // New game with specified answer
                        game = new MMGame(packet); 
                    }
                    
                    log.info("Game created! (Answer = " + game.getAnswer().toString() + ")");
                    log.info(""); 
                }else{
                    // Interpret incoming packet
                    log.info("Round: " + game.getRound());
                    log.info("Guess: " + packet.toString() + " (Matches answer? " + game.interpret(packet) + ")");
                }
            }
            totalBytesRcvd = 0;
        }
    }
}
