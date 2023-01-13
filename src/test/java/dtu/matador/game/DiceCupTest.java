package dtu.matador.game;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class DiceCupTest {

    @Test
    public void dieInRangeOneToSix() {
        DiceCup diceCup = new DiceCup();
        int[] result;
        for (int i = 0; i < 200; i++) {
            result = diceCup.roll();
            for (int j = 0; j < 2; j++) {
                int dieFace = result[j];
                //System.out.println(dieFace);
                Assert.assertTrue(dieFace <= 6 && dieFace >= 1);
            }


        }
    }
    @Test
    public void totalIsRight(){
        DiceCup diceCup = new DiceCup();
        for (int i = 0;i<10000;i++) {
            int[] nums = diceCup.roll();
            assert (nums[0] + nums[1] == nums[2]);

        }
    }
}