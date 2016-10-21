package team.six.mastermind.common;

import java.io.IOException;
import java.util.Random;

/**
 *
 * @author j0nbiz
 */
public class MMGame {

    private final MMPacket answer;
    private int round = 0;

    public MMGame() throws IOException {
        this.answer = seedAnswer();
    }

    public MMGame(MMPacket answer) {
        this.answer = answer;
    }

    private MMPacket seedAnswer() throws IOException {
        Random ran = new Random();

        // Return packet with random components
        return new MMPacket((byte) (ran.nextInt(7) + 1), (byte) (ran.nextInt(7) + 1), (byte) (ran.nextInt(7) + 1), (byte) (ran.nextInt(7) + 1));
    }

    public MMPacket getAnswer() {
        return answer;
    }

    public int getRound() {
        return this.round;
    }

    public MMPacket interpret(MMPacket guess) throws IOException {
        // Increment round count on every guess
        round++;

        // Check for good answer
        if(guess.equals(answer)){
            return new MMPacket((byte) 1, (byte) 1, (byte) 1, (byte) 1);
        }
        
        // byte array that will be used to populate returned MMPacket hint
        // 0 being no peg, 1 being a white peg, 2 being a black peg
        byte[] hints = new byte[]{0,  0,  0,  0};
        
        for(int i = 0; i < guess.getBytes().length; i++){
            if(guess.getBytes()[i] == answer.getBytes()[i]){
                hints[i] = 1;
            }else{
                for(int j = 0; i < answer.getBytes().length; i++){
                    if(guess.getBytes()[i] == answer.getBytes()[j]){
                        hints[i] = 2;
                    }
                }
            }
        }
        
        // TODO:
        // Assert color
        
        
        // Assert position
        
        
        return new MMPacket(hints[0], hints[1], hints[2], hints[3]);
    }
}
