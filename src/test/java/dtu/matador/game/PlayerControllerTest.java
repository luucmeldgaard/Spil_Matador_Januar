package dtu.matador.game;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class PlayerControllerTest {

    private ArrayList<Player> players = new ArrayList<>();
    private ArrayList<String> removedPlayers = new ArrayList<>();

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
    
    @Test
    public void removesPlayerFromTheGame(String playerID) {
        PlayerController playerController = new PlayerController();
        Player player = getPlayerFromID(playerID);
        removedPlayers.add(player.getId());
        players.remove(player);
    }

    @Test
    private Player getPlayerFromID(String playerID) {
        for (Player player : players) {
            String id = player.getId();
            if (id.equals(playerID)) {
                return player;
            }
        }
        return null;
    }

    @Test
    public void addsAPlayerToTheGame() {
        String name = "haidar";
        String chosenColor = "rød";
        int position = 1;
        int startBalance = 50000;


        Player player = new Player(name, chosenColor, position, startBalance);
        this.players.add(player);

        //Checks if the player is null
        assertNull(player);
        assertNull(this.players);
        //Checks if the player is now added
        assertTrue(this.players.size() == 1);
    }
}