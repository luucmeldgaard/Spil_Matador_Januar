package dtu.matador.game;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class DiceCupTest {

    @Test
    public void dieInRangeOneToSix() {
        DiceCup diceCup = new DiceCup();
        int result;
        for (int i = 0; i < 200; i++) {
            result = diceCup.roll();
            System.out.println(result);
            Assert.assertTrue(result >= 2 && result <= 12);
        }


    }
}