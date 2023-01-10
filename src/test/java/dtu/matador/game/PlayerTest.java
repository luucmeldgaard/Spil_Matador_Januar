package dtu.matador.game;

import org.junit.Assert;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.*;

public class PlayerTest {
    @Test
    public void playerMovesForwardFromStart() {
        Player player = new Player("Jakob", Color.BLUE,0,5000);
        int currentPosition = player.getPosition();
        int[] dieValues = player.rollDie();
        for (int dieRoll : dieValues) {
            player.movePosition(dieRoll); }

        int newPosition = player.getPosition();
        assertTrue(newPosition > currentPosition);
    }
@Test
    public void RollEqualsPositionalChange(){
        Player player = new Player("Jakob", Color.BLUE,5,5000);
        int startPosition = player.getPosition();
        int[] dieValues = player.rollDie();

        int total = 0;
        for (int dieRoll : dieValues) {
            total += dieRoll; }

        for (int dieRoll : dieValues) {
            player.movePosition(dieRoll); }

        int endPosition = player.getPosition();
        int positionalChange = endPosition - startPosition;
        assertEquals(positionalChange, total);
}
}
