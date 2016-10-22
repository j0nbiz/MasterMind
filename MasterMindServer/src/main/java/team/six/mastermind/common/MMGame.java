package team.six.mastermind.common;

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
     */
    public MMGame() {
        this.answer = seedAnswer();
    }

    /**
     * Overloaded constructor. Creates a game with the specified answer.
     *
     * @param answer The game's answer
     */
    public MMGame(MMPacket answer) {
        this.answer = answer;
    }

    /**
     * This method creates a randomly generated answer for the game.
     *
     * @return The answer packet
     */
    private MMPacket seedAnswer() {
        Random ran = new Random();

        // Return packet with random components
        return new MMPacket((byte) (ran.nextInt(7) + 1), (byte) (ran.nextInt(7) + 1), (byte) (ran.nextInt(7) + 1), (byte) (ran.nextInt(7) + 1));
    }

    /**
     * This method returns the game's answer.
     *
     * @return The answer packet
     */
    public MMPacket getAnswer() {
        return answer;
    }

    /**
     * This method returns the game's round number.
     *
     * @return The game's round
     */
    public int getRound() {
        return this.round;
    }

    /**
     * This method verifies if the guess is the correct answer. It returns a
     * response, which contains the hints. For a win, hints will contain all 1.
     * Hints Scheme: 
     * 0 is completely wrong
     * 1 is correct color in correct position
     * 2 is correct color in wrong position
     * 9 is end of game.
     *
     * @param guess The guess packet
     * @return The response packet
     * @throws IllegalArgumentException
     */
    public MMPacket interpret(MMPacket guess) throws IllegalArgumentException {
        byte[] hints = new byte[4];
        int correctColors;
        int matches;

        // Verify that the guesses are valid entries
        for (int i = 0; i < guess.getBytes().length; i++) {
            if ((guess.getBytes()[i] < 1) || (guess.getBytes()[i] > 8)) {
                throw new IllegalArgumentException("Invalid guess: " + guess.getBytes()[i]);
            }
        }

        // Increment round count on every guess, even if it is a good answer
        round++;

        // Check for good answer
        if (guess.equals(answer)) {
            return new MMPacket((byte) 1, (byte) 1, (byte) 1, (byte) 1);
        }
        
        // Check for end of game
        if (round > 9)
        {
            return new MMPacket((byte) 9, (byte) 9, (byte) 9, (byte) 9);
        }

        // Get the total number of correct colors, ignoring if they are a
        // match or not
        correctColors = getColors(guess);

        // Get the number of correct matches
        matches = getMatches(guess);

        // Add hints to hint array
        for (int i = 0; i < hints.length; i++) {
            if (i < matches) {
                // Adding match hints
                hints[i] = 1;
            } else if (i < correctColors) {
                // Will only fall here if some correctColors guesses were not matches
                hints[i] = 2;
            } else {
                // Will only fall here if some guesses were completely wrong
                hints[i] = 0;
            }
        }

        return new MMPacket(hints[0], hints[1], hints[2], hints[3]);
    }

    /**
     * This method verifies the number of correct matches in the guess.
     *
     * @param guess The guess to compare against the answer
     * @return The number of matches
     */
    private int getMatches(MMPacket guess) {
        int num = 0;

        for (int i = 0; i < 4; i++) {
            if (guess.getBytes()[i] == answer.getBytes()[i]) {
                num++;
            }
        }

        return num;
    }

    /**
     * This method verifies the number of correct colors in the guess.
     *
     * @param guess The guess to compare against the answer
     * @return The number of matches
     */
    private int getColors(MMPacket guess) {
        int num = 0;
        byte[] tempAnswer = new byte[4];
        byte[] guessValues = guess.getBytes();
        Boolean isAlreadyFound;

        // Deep copy of answer, do not want to change the initial values
        for (int i = 0; i < tempAnswer.length; i++) {
            tempAnswer[i] = answer.getBytes()[i];
        }

        for (int i = 0; i < guessValues.length; i++) {
            // New guess starting
            isAlreadyFound = false;

            // For each guess, verify if the color is present
            for (int j = 0; j < tempAnswer.length; j++) {
                if ((guessValues[i] == tempAnswer[j]) && (!isAlreadyFound)) {
                    // Don't check tempAnswer for same color (if it appears twice)
                    isAlreadyFound = true;

                    // Guesses don't overlap on same color (if guess has color that appears twice)
                    tempAnswer[j] = -1;

                    // Add to the count
                    num++;
                }
            }
        }

        return num;
    }
}
