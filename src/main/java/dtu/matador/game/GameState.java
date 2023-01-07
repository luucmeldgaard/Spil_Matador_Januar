package dtu.matador.game;

import java.util.ArrayList;
import java.util.Scanner;

public class GameState {
    GameController controller;
    ArrayList<Player> playerArray;
    Scanner scan;
    Player currentActivePlayer;

    private static GameState gameStateObject;
    private GameState() {
        playerArray = new ArrayList<Player>();
        controller = new GameController();
        scan = new Scanner(System.in);

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
        System.out.println("Number of players: ");
        Scanner scan = new Scanner(System.in);
        int numberOfPlayers = scan.nextInt();
        for (int i = 0; i<numberOfPlayers; i++) {
            this.playerArray.add(controller.addPlayer());
        }
        currentActivePlayer = playerArray.get(0);

    }

}
