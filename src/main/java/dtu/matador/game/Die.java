package dtu.matador.game;
import java.util.Random;

public class Die {

    public Die(){}

//Each dice will show a number between 1 and 6.
    public int roll(int sides) {
        Random rand = new Random();
        return rand.nextInt(0,6)+1;
    }

}
