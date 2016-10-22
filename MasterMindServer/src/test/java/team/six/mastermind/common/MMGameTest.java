package team.six.mastermind.common;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for MMGame.
 * 
 * @author Erika Bourque
 */
public class MMGameTest {
    
    public MMGameTest() {
    }

    @Test
    public void getAnswerTest() {
        // Set Up
        MMPacket expected = new MMPacket((byte) 1, (byte) 2, (byte) 3, (byte) 4);
        MMPacket result;
        MMGame game = new MMGame(expected);
        
        // Action
        result = game.getAnswer();
        
        // Assert
        assertEquals(expected, result);
    }
    
    @Test
    public void getRoundTest()
    {
        // Set Up
        MMPacket answer = new MMPacket((byte) 1, (byte) 2, (byte) 3, (byte) 4);
        MMPacket wrongGuess = new MMPacket((byte) 1, (byte) 1, (byte) 1, (byte) 1);
        MMGame game = new MMGame(answer);
        int result;
        
        // Action
        game.interpret(wrongGuess);
        result = game.getRound();
        
        // Assert
        assertEquals(1, result);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void interpretErrorTest()
    {
        // Set Up
        MMPacket answer = new MMPacket((byte) 1, (byte) 2, (byte) 3, (byte) 4);
        MMPacket wrongGuess = new MMPacket((byte) 9, (byte) 9, (byte) 9, (byte) 9);
        MMGame game = new MMGame(answer);
        
        // Action
        game.interpret(wrongGuess);
    }
}
