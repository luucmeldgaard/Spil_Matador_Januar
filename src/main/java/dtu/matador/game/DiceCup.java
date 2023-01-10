package dtu.matador.game;

public class DiceCup {
    Die die1 = new Die(6);
    Die die2 = new Die(6);
    public int[] roll() {
        return new int[] {die1.roll(), die2.roll()};
    }

}
