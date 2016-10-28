package team.six.mastermind.client;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
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
    private Socket server;
    OutputStream out;
    
    // Net IO variables
    private static final int BUFFSIZE = 4; // Packet holds 4 components

    private byte[] byteBuffer = new byte[BUFFSIZE];
    private int bytesRcvd;
    private int totalBytesRcvd = 0;
    
    // GUI elements
    ArrayList<MMPacket> hints = new ArrayList<>();

    public MMClient(Socket mmServer) throws IOException {
        this.server = mmServer;
        this.out = mmServer.getOutputStream(); // Get target
    }
    
    public MMPacket sendPacket(MMPacket packet) throws IOException{     
        // Reset buffer counter
        totalBytesRcvd = 0;
        
        for (byte comp : packet.getBytes()) {
            out.write(comp); // Send all packet components to server
        }
        
        log.info("1");
        
        // Create packet to get hint
        MMPacket hint = new MMPacket();
        
        while (totalBytesRcvd < byteBuffer.length) {
            log.info("2");
            // Check for server interupt and throw exception
            if ((bytesRcvd = server.getInputStream().read(byteBuffer, totalBytesRcvd, byteBuffer.length - totalBytesRcvd)) == -1) {
                throw new SocketException("Connection was interrupted!");
            }
            totalBytesRcvd += bytesRcvd;
        }
        log.info("3");
        // Reset buffer counter
        //totalBytesRcvd = 0;
        
        // Fill current packet
        hint.decode(byteBuffer);
        
        // Add hint to hint list if not server message response (ex: new game)
        /*if (!packet.equals(new MMPacket((byte) 0, (byte) 0, (byte) 0, (byte) 0))){
            hints.add(hint);
            
            // Logging
            log.info("Got new hint");
            log.info("Hint: " + hint.toString());
        }else{
            log.info("Server request was read...");
        }*/
        
        return hint;
    }

    public void disconnect() throws IOException {
        server.close(); // Close server connection
    }
}
