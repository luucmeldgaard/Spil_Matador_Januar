package dtu.matador.game;

import java.util.ArrayList;

public class GameState {
    GameController controller;
    private static GameState gameStateObject;

    ArrayList<Player> players;
    Player currentPlayer;

    private GameState() {
        controller = new GameController();
        players = new ArrayList<Player>();
    }

    public static GameState getStateInstance() {
        if (gameStateObject == null) {
            gameStateObject = new GameState();
        }
        else {System.out.println("Gamestate instance already initialized..."); }
        return gameStateObject;
    }

    public void menu() {
        controller.setBoard("FieldData");
        //TODO: Make loop for adding more players
        players.add(controller.addPlayer());
        currentPlayer = players.get(0);
    }

    public String tester() {
        return "Test";
    }

    public void play() {
        controller.playRound(currentPlayer);
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
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
