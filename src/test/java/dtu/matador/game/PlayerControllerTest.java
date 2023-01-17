package dtu.matador.game;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

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

    //Tests if a player gets removed from the game
    @Test
    public void testRemovesPlayerFromTheGame() {
        ArrayList<Player> players = new ArrayList<>();
        ArrayList<String> removedPlayers = new ArrayList<>();
        PlayerController playerController = new PlayerController();
        Player player = new Player("","",1,1);

        players.add(player);

        assertTrue("is empty", removedPlayers.isEmpty());
        removedPlayers.add(player.getId());
        assertTrue("removed a player", removedPlayers.size() > 0);


        assertTrue("checking if the player is still in the array", players.size() > 0);
        players.remove(player);
        assertTrue("Checking if the player got removed from the array", players.isEmpty());



    }

    // Checks if the playerID equals the Player
    @Test
    public void testGetsThePlayerBasedOnPlayerID() {
        ArrayList<Player> players = new ArrayList<>();
        for (Player player : players) {
            String id = player.getId();
            assertTrue(id.equals("playerID"));
        }

    }

    @Test
    public void testAddsAPlayerToTheGame() {
        ArrayList<Player> players = new ArrayList<>();
        ArrayList<String> removedPlayers = new ArrayList<>();

        String name = "haidar";
        String chosenColor = "rød";
        int position = 1;
        int startBalance = 50000;


        Player player = new Player(name, chosenColor, position, startBalance);
        players.add(player);

        //Creates a mock object for the Arraylist 'players'. One player is added to each arraylist, and get compared.
        ArrayList<Player> mockPlayersArray = new ArrayList<>();
        mockPlayersArray.add(player);

        //Checks if the player is now added
        assertArrayEquals(players.toArray(),mockPlayersArray.toArray());

    }
}