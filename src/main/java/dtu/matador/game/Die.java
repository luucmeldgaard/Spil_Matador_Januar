package dtu.matador.game;
import java.util.Random;

public class Die {

    private final int sides;

    /**
     *
     * @param sides - number of sides of the Die
     */
    public Die(int sides){
        this.sides = sides;
    }

//Each die will show a number between 1 and 6.
    protected int roll() {
        Random rand = new Random();
        return rand.nextInt(0,sides)+1;
    }


}
