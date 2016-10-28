package team.six.mastermind.common;

import java.util.Arrays;

/**
 * This class represents the packets being sent between server and client.
 *
 * @author Jonathan Bizier
 */
public class MMPacket {

    // Bytes to be sent
    private byte[] components = new byte[4];

    /**
     * Default constructor.
     */
    public MMPacket() {
    }

    /**
     * Overloaded constructor. Bytes given are put into the array.
     *
     * @param comp1 First byte
     * @param comp2 Second byte
     * @param comp3 Third byte
     * @param comp4 Fourth byte
     */
    public MMPacket(byte comp1, byte comp2, byte comp3, byte comp4) {
        // Putting components into byte array
        components[0] = comp1;
        components[1] = comp2;
        components[2] = comp3;
        components[3] = comp4;
    }

    /**
     * Returns the byte array.
     *
     * @return The array of bytes
     */
    public byte[] getBytes() {
        return components;
    }

    /**
     * Puts each of the components in the byte buffer into the components array.
     *
     * @param byteBuffer The array of compenents desired
     */
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
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + Arrays.hashCode(this.components);
        return hash;
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
