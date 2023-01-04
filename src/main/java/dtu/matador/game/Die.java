package dtu.matador.game;
import java.util.Random;

public class Die {

    public Die(){}

//Each dice will show a number between 1 and 6.
    public int roll(int sides) {
        Random random = new Random();
        return random.nextInt(0,6)+1;
    }

}
