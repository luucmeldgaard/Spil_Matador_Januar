package dtu.matador.game;

public class DiceCup {
    Die die = new Die();
    int sides = 6;
    public int roll() {
        return die.roll(sides) + die.roll(sides);
    }

}
