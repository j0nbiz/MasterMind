package team.six.mastermind.common;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
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
}
