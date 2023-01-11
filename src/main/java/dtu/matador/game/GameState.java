package dtu.matador.game;

import java.util.ArrayList;

public class GameState {
    GameController controller;
    static ArrayList<Player> players;
    static Player currentPlayer;
    static int currentPlayerNum;

    public GameState() {
        controller = new GameController();
        players = new ArrayList<Player>();
        currentPlayerNum = 0;
    }

    public void menu() {
        controller.setBoard("FieldData");
        //TODO: Make loop for adding more players
        players = controller.addPlayers();
        currentPlayer = players.get(0);
    }

    public void play() {
        System.out.println(currentPlayer);
        controller.playRound(currentPlayer);
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
        boolean confirmation = false;
        if (receiverID == null) {
            confirmation = player.addBalance(price);
        }
        else {
            Player receiver = getPlayerFromID(receiverID);
            receiver.addBalance(Math.abs(price));
            confirmation =  player.addBalance(price);
        }
        if (critical) {
            // THE PLAYER HAS LOST THE GAME
            System.out.println("You have lost the game. ");

        }
        return confirmation;
    }

}
