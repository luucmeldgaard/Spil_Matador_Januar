package dtu.matador.game;
import java.util.Random;

public class Die {

    int sides;

    /**
     *
     * @param sides
     */
    public Die(int sides){
        this.sides = sides;
    }

//Each dice will show a number between 1 and 6.
    public int roll() {
        Random rand = new Random();
        return rand.nextInt(0,sides)+1;
    }

}
