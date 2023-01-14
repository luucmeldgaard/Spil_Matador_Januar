package dtu.matador.game;

public class DiceCup {
    private final Die die1 = new Die(6);
    private final Die die2 = new Die(6);

    /**
     * @return - returns the value of each die and the total of the roll
     */
    protected int[] roll() {
        int roll1 = die1.roll();
        int roll2 = die2.roll();
        int total = roll1 + roll2;
        return new int[]{roll1, roll2, total};
    }
}

