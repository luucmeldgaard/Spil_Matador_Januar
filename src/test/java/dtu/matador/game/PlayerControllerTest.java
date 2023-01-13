package dtu.matador.game;

import static org.junit.Assert.*;
import org.junit.Test;

public class PlayerControllerTest {
    PlayerController playerController = new PlayerController();



    @Test
    public void testHandleTransactionPositive() {
        String targetPlayerName = "player1";
        String beneficiaryName = "player2";
        int amount = 1000;
        boolean critical = false;

        Player targetPlayer = new Player(targetPlayerName, "myBlue", 5, 5000);
        Player beneficiary = new Player(beneficiaryName, "myRed", 10, 5000);

        playerController.players.add(targetPlayer);
        playerController.players.add(beneficiary);

        boolean result = playerController.handleTransaction(targetPlayer.getId(), beneficiary.getId(), amount, critical);

        assertTrue(result);
        assertEquals(4000, targetPlayer.getBalance());
        assertEquals(6000, beneficiary.getBalance());
    }

    @Test
    public void testHandleTransactionNegative() {
        String targetPlayerName = "player1";
        String beneficiaryName = "player2";
        int amount = 150;
        boolean critical = true;

        Player targetPlayer = new Player(targetPlayerName, "myBlue", 5, 5000);
        Player beneficiary = new Player(beneficiaryName, "myRed", 10, 5000);



        boolean result = playerController.handleTransaction(targetPlayer.getId(), beneficiary.getId(), amount, critical);

        assertFalse(result);
        assertEquals(0, targetPlayer.getBalance());
        assertEquals(150, beneficiary.getBalance());
    }
}