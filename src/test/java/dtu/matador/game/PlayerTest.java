package dtu.matador.game;

import org.junit.Assert;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.*;

public class PlayerTest {
    @Test
    public void playerMovesForwardOnce() {
        Player player = new Player("Jakob", Color.BLUE,0,5000);
        int currentPosition = player.getPosition();
        player.rollDie();
        int newPosition = player.getPosition();
        Assert.assertTrue(newPosition > currentPosition);
    }


}