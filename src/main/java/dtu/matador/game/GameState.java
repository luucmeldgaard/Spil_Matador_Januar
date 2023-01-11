package dtu.matador.game;

import java.util.ArrayList;

public class GameState {
    //Instances of classes are added
    GameController controller;
    static ArrayList<Player> players;
    static Player currentPlayer;
    static int currentPlayerNum;

    public GameState() {
        controller = new GameController();
        //players = new ArrayList<Player>();
        currentPlayerNum = 0;
    }

    public void menu() {
        controller.setBoard("FieldData");
        players = controller.addPlayers();
        System.out.println(players);
        currentPlayer = players.get(0);
    }

    public void play() {
        while (true) {
            System.out.println(currentPlayer);
            controller.playRound(currentPlayer);
            nextPlayer();
        }
    }

    public void nextPlayer(){
        currentPlayerNum = ((currentPlayerNum +1)%players.size());
        currentPlayer = players.get(currentPlayerNum);
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

    public boolean handleTransaction(String playerID, String receiverID, int price, boolean critical) {
        Player player = getPlayerFromID(playerID);

        boolean confirmation = player.balanceCheck(price);
        if (!confirmation) {
            if (receiverID != null) {
                Player receiver = getPlayerFromID(receiverID);
                int playerActualBalance = player.getBalance();
                receiver.addBalance(playerActualBalance);
                return false;
            }
            if (critical) {
                // TODO player has lost and will be removed
                return false;
            }
            return false;
        }

        else {
            player.addBalance(price);
            if (receiverID != null) {
                Player receiver = getPlayerFromID(receiverID);
                receiver.addBalance(Math.abs(price));
            }
            return true;
        }
    }

}
