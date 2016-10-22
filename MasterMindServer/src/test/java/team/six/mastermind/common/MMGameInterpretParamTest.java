/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package team.six.mastermind.common;

import java.util.Arrays;
import java.util.Collection;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 *
 * @author Panda
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
                new MMPacket((byte) 3, (byte) 3, (byte) 3, (byte) 3)},
            // All color hint
            {new MMPacket((byte) 1, (byte) 2, (byte) 3, (byte) 4), 
                new MMPacket((byte) 4, (byte) 1, (byte) 2, (byte) 3), 
                new MMPacket((byte) 2, (byte) 2, (byte) 2, (byte) 2)},
            // Three match hint
            {new MMPacket((byte) 1, (byte) 2, (byte) 3, (byte) 4), 
                new MMPacket((byte) 1, (byte) 2, (byte) 3, (byte) 5), 
                new MMPacket((byte) 1, (byte) 1, (byte) 1, (byte) 3)},
            // Half match, half color hint
            {new MMPacket((byte) 1, (byte) 2, (byte) 3, (byte) 4), 
                new MMPacket((byte) 1, (byte) 2, (byte) 4, (byte) 3), 
                new MMPacket((byte) 1, (byte) 1, (byte) 2, (byte) 2)},
            // Two match, one color and one wrong hint
            {new MMPacket((byte) 1, (byte) 2, (byte) 3, (byte) 4), 
                new MMPacket((byte) 1, (byte) 2, (byte) 5, (byte) 3), 
                new MMPacket((byte) 1, (byte) 1, (byte) 2, (byte) 3)},
            // Same colors answers match
            {new MMPacket((byte) 1, (byte) 1, (byte) 1, (byte) 1), 
                new MMPacket((byte) 1, (byte) 2, (byte) 2, (byte) 2), 
                new MMPacket((byte) 1, (byte) 3, (byte) 3, (byte) 3)},
            // Same colors answers color
            {new MMPacket((byte) 1, (byte) 2, (byte) 2, (byte) 2), 
                new MMPacket((byte) 2, (byte) 3, (byte) 3, (byte) 3), 
                new MMPacket((byte) 2, (byte) 3, (byte) 3, (byte) 3)},
            // Same colors guess color
            {new MMPacket((byte) 1, (byte) 2, (byte) 2, (byte) 2), 
                new MMPacket((byte) 3, (byte) 3, (byte) 1, (byte) 1), 
                new MMPacket((byte) 2, (byte) 3, (byte) 3, (byte) 3)},
            // Win
            {new MMPacket((byte) 1, (byte) 1, (byte) 1, (byte) 1), 
                new MMPacket((byte) 1, (byte) 1, (byte) 1, (byte) 1), 
                new MMPacket((byte) 0, (byte) 0, (byte) 0, (byte) 0)},
        });
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
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
