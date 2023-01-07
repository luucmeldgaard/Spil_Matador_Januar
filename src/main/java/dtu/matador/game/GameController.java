package dtu.matador.game;

import java.awt.*;
import java.util.Scanner;

public class GameController {

    FieldController board;
    GUIController gui;

    public static void main(String[] args) {
        GameState currentGameState;
        currentGameState = GameState.getInstance();
        currentGameState.menu();

    }

    public void setBoard(String selectedBoard) {
        board = new FieldController(selectedBoard);
        board.setupFields();
        this.gui = GUIController.getInstance(board.getFieldMap());
    }

    public Player addPlayer() {
        System.out.println("Enter player name");
        Scanner scan = new Scanner(System.in);
        String name = scan.nextLine();
        System.out.println("Select player color");
        String color = scan.next();
        Player player = new Player(name, Color.BLUE, 0, 5000);
        player.setId(player.toString());
        gui.addPlayer(player.getId(), player.getName(), player.getBalance(), player.getPosition(), player.getColor());
        gui.movePlayerTo(player.getId(), 4);
        return player;
    }

    public void LandOn() {}

    public void updateGUI(Player player) {
    }

    public void updateGUI(FieldController board) {

    }


}