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

    public static GameState getInstance() {
        if (gameStateObject == null) {
            gameStateObject = new GameState();
        }
        else {System.out.println("Gamestate instance already initialized..."); }
        return gameStateObject;
    }

    public void menu() {
        controller.setBoard("fieldSpaces");
        players.add(controller.addPlayer());
        currentPlayer = players.get(0);
    }

    public void play() {
        controller.playRound(currentPlayer);
    }
}
