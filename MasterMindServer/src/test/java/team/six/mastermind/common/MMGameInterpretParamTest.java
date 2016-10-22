package team.six.mastermind.common;

import java.util.Arrays;
import java.util.Collection;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * Parameterized tests for the interpret method in MMGame.
 * 
 * @author Erika Bourque
 */
@RunWith(Parameterized.class)
public class MMGameInterpretParamTest {
    MMPacket guess;
    MMPacket expectedHint;
    MMGame game;
    
    public MMGameInterpretParamTest(MMPacket answer, MMPacket guess, MMPacket expectedHint) {
        this.guess = guess;
        this.expectedHint = expectedHint;
        game = new MMGame(answer);
    }
    
    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
            // All wrong hint
            {new MMPacket((byte) 1, (byte) 1, (byte) 1, (byte) 1), 
                new MMPacket((byte) 2, (byte) 2, (byte) 2, (byte) 2), 
                new MMPacket((byte) 0, (byte) 0, (byte) 0, (byte) 0)},
            // All color hint
            {new MMPacket((byte) 1, (byte) 2, (byte) 0, (byte) 4), 
                new MMPacket((byte) 4, (byte) 1, (byte) 2, (byte) 0), 
                new MMPacket((byte) 2, (byte) 2, (byte) 2, (byte) 2)},
            // Three match hint
            {new MMPacket((byte) 1, (byte) 2, (byte) 0, (byte) 4), 
                new MMPacket((byte) 1, (byte) 2, (byte) 0, (byte) 5), 
                new MMPacket((byte) 1, (byte) 1, (byte) 1, (byte) 0)},
            // Half match, half color hint
            {new MMPacket((byte) 1, (byte) 2, (byte) 0, (byte) 4), 
                new MMPacket((byte) 1, (byte) 2, (byte) 4, (byte) 0), 
                new MMPacket((byte) 1, (byte) 1, (byte) 2, (byte) 2)},
            // Two match, one color and one wrong hint
            {new MMPacket((byte) 1, (byte) 2, (byte) 0, (byte) 4), 
                new MMPacket((byte) 1, (byte) 2, (byte) 5, (byte) 0), 
                new MMPacket((byte) 1, (byte) 1, (byte) 2, (byte) 0)},
            // Same colors answers match
            {new MMPacket((byte) 1, (byte) 1, (byte) 1, (byte) 1), 
                new MMPacket((byte) 1, (byte) 2, (byte) 2, (byte) 2), 
                new MMPacket((byte) 1, (byte) 0, (byte) 0, (byte) 0)},
            // Same colors answers color
            {new MMPacket((byte) 1, (byte) 2, (byte) 2, (byte) 2), 
                new MMPacket((byte) 2, (byte) 0, (byte) 0, (byte) 0), 
                new MMPacket((byte) 2, (byte) 0, (byte) 0, (byte) 0)},
            // Same colors guess color
            {new MMPacket((byte) 1, (byte) 2, (byte) 2, (byte) 2), 
                new MMPacket((byte) 0, (byte) 0, (byte) 1, (byte) 1), 
                new MMPacket((byte) 2, (byte) 0, (byte) 0, (byte) 0)},
            // Win
            {new MMPacket((byte) 1, (byte) 1, (byte) 1, (byte) 1), 
                new MMPacket((byte) 1, (byte) 1, (byte) 1, (byte) 1), 
                new MMPacket((byte) 0, (byte) 0, (byte) 0, (byte) 0)},
        });
    }

    @Test
    public void interpretTest()
    {
        MMPacket result;
        
        // Action
        result = game.interpret(guess);
        
        // Assert
        assertEquals(expectedHint, result);
    }
}
