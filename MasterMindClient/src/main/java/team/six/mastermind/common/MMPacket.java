package team.six.mastermind.common;

import java.io.*;

/**
 *
 * @author j0nbiz
 */
public class MMPacket {

    // Bytes to be sent
    private byte[] components = new byte[4];

    public MMPacket() {
    }

    public MMPacket(byte comp1, byte comp2, byte comp3, byte comp4) throws IOException {
        // Putting components into byte array
        components[0] = comp1;
        components[1] = comp2;
        components[2] = comp3;
        components[3] = comp4;
    }

    public byte[] getBytes() {
        return components;
    }

    public void decode(byte[] byteBuffer) {
        components[0] = byteBuffer[0];
        components[1] = byteBuffer[1];
        components[2] = byteBuffer[2];
        components[3] = byteBuffer[3];
    }

    @Override
    public String toString() {
        return components[0] + " " + components[1] + " " + components[2] + " " + components[3];
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        final MMPacket other = (MMPacket) obj;

        for (int i = 0; i < components.length; i++) {
            if (this.components[i] != other.components[i]) {
                return false;
            }
        }

        return true;
    }
}
