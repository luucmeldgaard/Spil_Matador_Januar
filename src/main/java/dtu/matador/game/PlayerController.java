package dtu.matador.game;

import java.util.ArrayList;
import java.util.Map;

public class PlayerController {

    protected ArrayList<Player> players;
    protected ArrayList<String> removedPlayers;
    private int currentPlayerNum;

    // Constructor for PlayerController
    public PlayerController() {
        this.players = new ArrayList<>();
        this.removedPlayers = new ArrayList<>();
        this.currentPlayerNum = 0;
    }

    //Adds a player to the game
    public void addPlayer(String name, String chosenColor, int position, int balance) {
        Player player = new Player(name, chosenColor, position, balance);
        this.players.add(player);
    }

    //Chooses the next player to roll the die
    public void nextPlayer(){
        this.currentPlayerNum = ((currentPlayerNum +1)%players.size());
        Player currentPlayer = players.get(currentPlayerNum);
    }

    //Gets the current player that is playing the game
    public Player getCurrentPlayer() {
        return players.get(currentPlayerNum);
    }

    public Player getPlayerFromName(String name) {
        for (Player player : players) {
            if (player.getName().equals(name)) {
                return player;
            }
        }
        return null;
    }

    //Gets the player based on their playerID
    public Player getPlayerFromID(String playerID) {
        for (Player player : players) {
            String id = player.getId();
            if (id.equals(playerID)) {
                return player;
            }
        }
        return null;
    }

    //Gets all the playerID's in the game
    public ArrayList<String> getAllPlayerIDs() {
        ArrayList<String> playerIDs = new ArrayList<>();
        for (Player player : players) {
            String id = player.getId();
            playerIDs.add(id);
        }
        return playerIDs;
    }

    //Removes a player from the game
    public void removePlayer(String playerID) {
        Player player = getPlayerFromID(playerID);
        removedPlayers.add(player.getId());
        players.remove(player);
    }

    //Gets all the removed players based on their playerID's.
    public ArrayList<String> getRemovedPlayerIDs() { return this.removedPlayers; }

    public boolean handleTransaction(String targetPlayerID, String beneficiaryID, int amount, boolean critical) {
        Player targetPlayer = getPlayerFromID(targetPlayerID);

        //Checks the balance of a player
        boolean confirmation = targetPlayer.balanceCheck(amount);
        if (!confirmation) {
            if (beneficiaryID != null) {
                Player beneficiary = getPlayerFromID(beneficiaryID);
                int targetActualBalance = targetPlayer.getBalance();
                beneficiary.addBalance(targetActualBalance);
            }
            if (critical) {
                System.out.println("The player has lost");
                players.remove(targetPlayer);
                removedPlayers.add(targetPlayerID);
            }
            return false;
        }

        //Adds balance to the targetPlayer, if the beneficiary ID is not null
        else {
            targetPlayer.addBalance(amount);
            if (beneficiaryID != null) {
                Player beneficiary = getPlayerFromID(beneficiaryID);
                beneficiary.addBalance(Math.abs(amount));
            }
            return true;
        }
    }

    //Gets all players' snapshots
    public ArrayList<Map<String, String>> snapshotOfPlayers() {
        ArrayList<Map<String, String>> snapshotOfPlayers = new ArrayList<>();
        for (Player player : players) {
            Map<String, String> playerSnapshot = player.snapshot();
            snapshotOfPlayers.add(playerSnapshot);
        }
        return snapshotOfPlayers;
    }

    //Loads all the players in the game
    public void loadPlayers(ArrayList<Map<String,String>> playersList) {
        for (Map<String, String> info : playersList) {
            Player player = new Player(info.get("name"), info.get("color"), info.get("position"), info.get("balance"), info.get("id"), info.get("boardSize"),
                    info.get("jailed"), info.get("jailCards"), info.get("lastPlayedDieRoll0"), info.get("lastPlayedDieRoll1"));
            this.players.add(player);
            System.out.println(this.players.get(0).getId() + "_____________________________");
        }
    }

    //Resets all players
    public void reset() {
        players.clear();
    }

}
