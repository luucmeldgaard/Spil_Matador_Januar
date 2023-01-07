package dtu.matador.game;

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
        return new Player(name, color, 0, 5000);
    }

    public void updateGUI(Player player) {

    }


}