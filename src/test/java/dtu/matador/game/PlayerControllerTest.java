package dtu.matador.game;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PlayerControllerTest {

    @Test
    public void testHandleTransactionPositiveForTarget() {
        PlayerController playerController = new PlayerController();
        String targetPlayerName = "player1";
        String beneficiaryName = "player2";
        int amount = 3000;
        boolean critical = false;
        Player beneficiary = null;

        playerController.addPlayer(targetPlayerName, "myBlue", 5, 5000);

        Player targetPlayer = playerController.getPlayerFromName(targetPlayerName);

        boolean result = playerController.handleTransaction(targetPlayer.getId(), null, amount, critical);

        assertTrue(result);
        assertEquals(8000, targetPlayer.getBalance());
    }

    @Test
    public void testHandleTransactionPayBeneficiary() {
        PlayerController playerController = new PlayerController();
        String targetPlayerName = "player1";
        String beneficiaryName = "player2";
        int amount = -2000;
        boolean critical = false;

        playerController.addPlayer(targetPlayerName, "myBlue", 5, 5000);
        playerController.addPlayer(beneficiaryName, "myRed", 5, 5000);

        Player targetPlayer = playerController.getPlayerFromName(targetPlayerName);
        Player beneficiary = playerController.getPlayerFromName(beneficiaryName);

        boolean result = playerController.handleTransaction(targetPlayer.getId(), beneficiary.getId(), amount, critical);

        assertTrue(result);
        assertEquals(3000, targetPlayer.getBalance());
        assertEquals(7000, beneficiary.getBalance());
    }

    @Test
    public void testHandleTransactionTargetCantPay() {
        PlayerController playerController = new PlayerController();
        String targetPlayerName = "player1";
        String beneficiaryName = "player2";
        int amount = -12000;
        boolean critical = true;

        playerController.addPlayer(targetPlayerName, "myBlue", 5, 5000);
        playerController.addPlayer(beneficiaryName, "myRed", 5, 5000);

        Player targetPlayer = playerController.getPlayerFromName(targetPlayerName);
        Player beneficiary = playerController.getPlayerFromName(beneficiaryName);
        int targetPlayerActualBalance = targetPlayer.getBalance();

        boolean result = playerController.handleTransaction(targetPlayer.getId(), beneficiary.getId(), amount, critical);

        assertFalse(result);
        assertNull(playerController.getPlayerFromName(targetPlayerName));
        assertEquals(5000 + targetPlayerActualBalance, beneficiary.getBalance());
    }

}