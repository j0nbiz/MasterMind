package team.six.mastermind.common;

import java.io.IOException;
import java.util.Random;

/**
 * This class provides a MasterMind game using packets.
 * 
 * @author Erika Bourque
 */
public class MMGame {

    private final MMPacket answer;
    private int round = 0;

    /**
     * Default constructor. Creates a game with a random answer.
     * 
     * @throws IOException 
     */
    public MMGame() throws IOException {
        this.answer = seedAnswer();
    }

    /**
     * Overloaded constructor.  Creates a game with the specified answer.
     * 
     * @param answer    The game's answer
     */
    public MMGame(MMPacket answer) {
        this.answer = answer;
    }

    /**
     * This method creates a randomly generated answer for the game.
     * 
     * @return              The answer packet
     * @throws IOException 
     */
    private MMPacket seedAnswer() throws IOException {
        Random ran = new Random();

        // Return packet with random components
        return new MMPacket((byte) (ran.nextInt(7) + 1), (byte) (ran.nextInt(7) + 1), (byte) (ran.nextInt(7) + 1), (byte) (ran.nextInt(7) + 1));
    }

    /**
     * This method returns the game's answer.
     * 
     * @return      The answer packet 
     */
    public MMPacket getAnswer() {
        return answer;
    }

    /**
     * This method returns the game's round number.
     * 
     * @return      The game's round 
     */
    public int getRound() {
        return this.round;
    }

    /**
     * This method verifies if the guess is the correct answer.  It returns
     * a response, which contains the hints, or 0 0 0 0 for a win.
     * Hints: 
     * 0 is completely wrong
     * 1 is correct color in correct position
     * 2 is correct color in wrong position
     * 
     * @param guess         The guess packet
     * @return              The response packet
     * @throws IOException 
     */
    public MMPacket interpret(MMPacket guess) throws IOException {
        byte[] hints = new byte[4];
        int colors;
        int matches;        

        // Check for good answer
        if(guess.equals(answer)){
            return new MMPacket((byte) 0, (byte) 0, (byte) 0, (byte) 0);
        }
        
        // Increment round count on every guess
        round++;
        
        // Get the number of correct colors
        colors = getColors(guess);
        
        // Get the number of correct matches
        matches = getMatches(guess);
        
        // Assert position
        
        
        return new MMPacket(hints[0], hints[1], hints[2], hints[3]);
    }
    
    /**
     * This method verifies the number of correct matches in the guess.
     * 
     * @param guess     The guess to compare against the answer
     * @return          The number of matches
     */
    private int getMatches(MMPacket guess)
    {
        int num = 0;
        
        for (int i = 0; i < 4; i++)
        {
            if (guess.getBytes()[i] == answer.getBytes()[i])
            {
                num++;
            }
        }
        
        return num;
    }

    /**
     * This method verifies the number of correct colors in the guess.
     * 
     * @param guess     The guess to compare against the answer
     * @return          The number of matches
     */
    private int getColors(MMPacket guess)
    {
        int num = 0;
        byte[] tempAnswer = answer.getBytes();
        
        // Loop through to verify if color is present
        // change temp so not verify same color twice
        
        return num;
    }
}
