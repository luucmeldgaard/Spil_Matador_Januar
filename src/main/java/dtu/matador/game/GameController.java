package dtu.matador.game;

import gui_main.GUI;

import java.awt.*;
import java.util.ArrayList;

public class GameController {

    GUIController gui;
    int boardSize;
    static GameState currentGameState;

    public static void main(String[] args) {
        //GUI.setNull_fields_allowed(true); //This messes up the GUI but allows it to render with null fields, making troublefixing easier
        currentGameState = new GameState();
        currentGameState.menu();
        currentGameState.play();

    }


    public void setBoard(String selectedBoard) {
        gui = GUIController.getGUIInstance(selectedBoard);
        boardSize = gui.getBoardSize();
    }

    public ArrayList<Player> addPlayers() {
        int numPlayers = Integer.parseInt(gui.buttonRequest("Number of players", "2", "3"));
        ArrayList<Player> players = new ArrayList<>();
        for (int i = 0; i < numPlayers; i++) {
            String name = gui.getNameFromInput();
            System.out.println("Select player color");
            Color chosenColor = gui.colorDropDownList();
            Player player = new Player(name, chosenColor, 0, 5000);
            //player.setId(player.toString());
            player.setBoardSize(boardSize);
            System.out.println(name + "'s ID is: " + player.getId());
            gui.addPlayer(player.getId(), player.getName(), player.getBalance(), player.getPosition(), player.getColor());
            players.add(player);
        }
        return players;
    }



    public void playRound(Player player) {
        // roll dice
        System.out.println(player.getName());
        gui.buttonRequest("Roll Dice", "Roll");
        int[] dieValues = player.rollDie();
        int total = dieValues[0] + dieValues[1];
        gui.setDice(dieValues);
        // player moves
        player.movePosition(total);
        gui.movePlayerTo(player.getId(), player.getPosition());
        System.out.println(player.getId());

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