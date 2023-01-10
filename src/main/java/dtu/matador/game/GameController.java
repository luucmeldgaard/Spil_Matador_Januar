package dtu.matador.game;

import gui_main.GUI;

import java.awt.*;
import java.util.Properties;
import java.util.Scanner;

public class GameController {

    GUIController gui;
    int boardSize;
    static GameState currentGameState;

    public static void main(String[] args) {
        GUI.setNull_fields_allowed(true); //This messes up the GUI but allows it to render with null fields, making troublefixing easier
        GameState currentGameState;
        currentGameState = GameState.getInstance();
        currentGameState.menu();
        currentGameState.play();

    }

    public void setBoard(String selectedBoard) {
        gui = new GUIController(selectedBoard);
        boardSize = gui.getBoardSize();
    }

    public Player addPlayer() {

        String name = gui.getNameFromInput();
        System.out.println("Select player color");
        Color chosencolor = gui.colorDropDownList();
        Player player = new Player(name, chosencolor, 0, 5000);
        //player.setId(player.toString());
        player.setBoardSize(boardSize);
        System.out.println(name + "'s ID is: " + player.getId());
        gui.addPlayer(player.getId(), player.getName(), player.getBalance(), player.getPosition(), player.getColor());
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
            gui.movePlayerTo(player.getId(), player.getPosition());
        }

    }

    public void landOn() {
        // retrieves fieldtype from Field Controller

    }

    public boolean billPlayer(String playerID, int price) {
        Player player = currentGameState.getPlayerFromID(playerID);
        return player.setBalance(price);
    }

    public void Property() {

    }

    public void updateGUI(Player player) {
    }

    public void updateGUI(FieldController board) {

    }


}