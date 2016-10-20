package team.six.mastermind.client;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import team.six.mastermind.common.MMPacket;

/**
 *
 * @author j0nbiz
 */
public class MMClient {

    private Socket mmServer;
    OutputStream out;

    public MMClient(Socket mmServer) throws IOException {
        this.mmServer = mmServer;
        this.out = mmServer.getOutputStream(); // Get target
    }

    public void sendStartReq() throws IOException {
        // Using 0 in all fields as start game request
        for(byte comp: new MMPacket((byte) 0, (byte) 0, (byte) 0, (byte) 0).getBytes()){
            out.write(comp); // Send all packet components to server
        }
    }

    public void sendGuess(MMPacket packet) throws IOException {
        for(byte comp: packet.getBytes()){
            out.write(comp); // Send all packet components to server
        }
    }

    public void disconnect() throws IOException {
        mmServer.close(); // Close server connection
    }
}
