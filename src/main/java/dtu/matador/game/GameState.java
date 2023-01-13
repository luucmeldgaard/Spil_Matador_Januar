package dtu.matador.game;

import java.util.ArrayList;

public class GameState {

    ArrayList<Player> players;
    Player currentPlayer;
    int currentPlayerNum;

    public GameState() {
        this.players = new ArrayList<>();
        this.currentPlayerNum = 0;
    }

    public void addPlayer(String name, String chosenColor, int position, int balance) {
        Player player = new Player(name, chosenColor, position, balance);
        this.players.add(player);
    }

    public void nextPlayer(){
        this.currentPlayerNum = ((currentPlayerNum +1)%players.size());
        this.currentPlayer = players.get(currentPlayerNum);
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerNum);
    }

    public Player getPlayerFromID(String playerID) {
        for (Player player : players) {
            String id = player.getId();
            if (id.equals(playerID)) {
                return player;
            }
        }
        return null;
    }

    public ArrayList<String> getAllPlayerIDs() {
        ArrayList<String> playerIDs = new ArrayList<>();
        for (Player player : players) {
            String id = player.getId();
            playerIDs.add(id);
        }
        return playerIDs;
    }

    public void removePlayerFromState(String playerID) {
        Player player = getPlayerFromID(playerID);
        players.remove(player);
    }

    public boolean handleTransaction(String targetPlayerID, String beneficiaryID, int amount, boolean critical) {
        Player targetPlayer = getPlayerFromID(targetPlayerID);

        boolean confirmation = targetPlayer.balanceCheck(amount);
        if (!confirmation) {
            if (beneficiaryID != null) {
                Player beneficiary = getPlayerFromID(beneficiaryID);
                int targetActualBalance = targetPlayer.getBalance();
                beneficiary.addBalance(targetActualBalance);
            }
            if (critical) {
                // TODO player has lost and will be removed
                System.out.println("The player has lost");
                players.remove(targetPlayer);
            }
            return false;
        }


        else {
            targetPlayer.addBalance(amount);
            if (beneficiaryID != null) {
                Player beneficiary = getPlayerFromID(beneficiaryID);
                beneficiary.addBalance(Math.abs(amount));
            }
            return true;
        }
    }

}
