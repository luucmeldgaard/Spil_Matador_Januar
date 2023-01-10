package dtu.matador.game;

import gui_main.GUI;

import java.awt.*;
import java.util.Properties;
import java.util.Scanner;

public class GameController {

    GUIController gui;
    int boardSize;

    public static void main(String[] args) {
        GUI.setNull_fields_allowed(true); //This messes up the GUI but allows it to render with null fields, making troublefixing easier
        GameState currentGameState;
        currentGameState = GameState.getInstance();
        currentGameState.menu();
        currentGameState.play();

    }

    public void setBoard(String selectedBoard) {
        gui = GUIController.getInstance(selectedBoard);
        boardSize = gui.getBoardSize();
    }

    public Player addPlayer() {
        System.out.println("Enter player name");
        Scanner scan = new Scanner(System.in);
        String name = scan.nextLine();
        System.out.println("Select player color");
        String color = scan.next();
        Player player = new Player(name, Color.BLUE, 0, 5000);
        player.setId(player.toString());
        player.setBoardSize(boardSize);
        gui.addPlayer(player.getId(), player.getName(), player.getBalance(), player.getPosition(), player.getColor());
        gui.movePlayerTo(0);
        return player;
    }

    public void playRound(Player player) {
        // roll dice
        while (true){
            gui.buttonRequest("Roll Dice", "Roll");
            int[] dieValues = player.rollDie();
            int total = dieValues[0] + dieValues[1];
            gui.setDice(dieValues);
            // player moves
            player.movePosition(total);
            gui.movePlayerTo(player.getPosition());
        }

    }

    public void landOn() {
        // retrieves fieldtype from Field Controller

    }

    public void Property() {

    }

    public void updateGUI(Player player) {
    }

    public void updateGUI(FieldController board) {

    }


}