package team.six.mastermind.client;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.six.mastermind.common.MMPacket;

/**
 * This class connects the client to the server.
 *
 * @author Jonathan Bizier
 */
public class MMClient {

    // Net IO variables
    private static final int BUFFSIZE = 4; // Packet holds 4 components
    // Logger
    private final Logger log = LoggerFactory.getLogger(getClass().getName());

    // Server variables
    private Socket server;
    OutputStream out;

    private byte[] byteBuffer = new byte[BUFFSIZE];
    private int bytesRcvd;
    private int totalBytesRcvd = 0;

    // GUI elements
    ArrayList<MMPacket> hints = new ArrayList<>();

    /**
     * Constructor, requires the socket to the server.
     *
     * @param mmServer The server's socket
     * @throws IOException
     */
    public MMClient(Socket mmServer) throws IOException {
        this.server = mmServer;
        this.out = mmServer.getOutputStream(); // Get target
    }

    /**
     * Sends the guess packet to the server, and returns the hint packet sent by
     * the server.
     *
     * @param packet The guess packet
     * @return The hint packet
     * @throws IOException
     */
    public MMPacket sendPacket(MMPacket packet) throws IOException {
        // Reset buffer counter
        totalBytesRcvd = 0;

        for (byte comp : packet.getBytes()) {
            out.write(comp); // Send all packet components to server
        }

        // Create packet to get hint
        MMPacket hint = new MMPacket();

        while (totalBytesRcvd < byteBuffer.length) {
            // Check for server interupt and throw exception
            if ((bytesRcvd = server.getInputStream().read(byteBuffer, totalBytesRcvd, byteBuffer.length - totalBytesRcvd)) == -1) {
                throw new SocketException("Connection was interrupted!");
            }
            totalBytesRcvd += bytesRcvd;
        }

        // Fill current packet
        hint.decode(byteBuffer);

        return hint;
    }

    /**
     * Closes server connection.
     *
     * @throws IOException
     */
    public void disconnect() throws IOException {
        server.close();
    }
}
