package team.six.mastermind.client;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.six.mastermind.common.MMPacket;

/**
 *
 * @author j0nbiz
 */
public class MMClient {
    // Logger
    private final Logger log = LoggerFactory.getLogger(getClass().getName());

    // Server variables
    private Socket mmServer;
    OutputStream out;
    
    // Net IO variables
    private static final int BUFFSIZE = 4; // Packet holds 4 components

    private byte[] byteBuffer = new byte[BUFFSIZE];
    private int bytesRcvd;
    private int totalBytesRcvd = 0;

    public MMClient(Socket mmServer) throws IOException {
        this.mmServer = mmServer;
        this.out = mmServer.getOutputStream(); // Get target
    }
    
    public void send(MMPacket packet) throws IOException{
        
        for (byte comp : new MMPacket((byte) 0, (byte) 0, (byte) 0, (byte) 0).getBytes()) {
            out.write(comp); // Send all packet components to server
        }
        
        while (totalBytesRcvd < byteBuffer.length) {
            totalBytesRcvd += bytesRcvd;

                
        }
        totalBytesRcvd = 0;
    }

    public void sendStartReq() throws IOException {
        // Using 0 in all fields as start game request
        for (byte comp : new MMPacket((byte) 0, (byte) 0, (byte) 0, (byte) 0).getBytes()) {
            out.write(comp); // Send all packet components to server
        }
    }

    public void sendGuess(MMPacket packet) throws IOException {
        for (byte comp : packet.getBytes()) {
            out.write(comp); // Send all packet components to server
        }
    }

    public void disconnect() throws IOException {
        mmServer.close(); // Close server connection
    }
}
