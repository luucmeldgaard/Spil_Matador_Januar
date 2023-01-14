package dtu.matador.game;

import gui_main.GUI;

import java.awt.*;
import java.util.ArrayList;

public class GameController {

    private static PlayerController playerController;
    private static GUIController gui;
    private static FieldController board;

    //Main method. Runs the program
    public static void main(String[] args) {
        playerController = new PlayerController();
        GUI.setNull_fields_allowed(true);
        gui = new GUIController();
        menu();
        play();
    }

    private static void menu() {
        String chosenBoard = gui.buttonRequest("Choose board", "fieldData");
        gui.close();
        setBoard(chosenBoard);
        setupPlayers();
    }
    private static void play() {
        while (true) {
            playRound(playerController.getCurrentPlayer()); // set later
            playerController.nextPlayer(); // set later
        }
    }

    //Sets the board in the GUI
    private static void setBoard(String selectedBoard) {
        board = new FieldController(playerController, gui, selectedBoard);
    }

    //Adds a player, a player color and a playerID to the GUI
    private static void setupPlayers() {
        int numPlayers = gui.getNumberOfPlayers();
        ArrayList<Player> players = new ArrayList<>();
        gui.fillColorSelector();
        for (int i = 0; i < numPlayers; i++) {
            String name = gui.getNameFromInput();
            System.out.println("Select player color");
            String chosenColor = gui.colorDropDownList();
            playerController.addPlayer(name, chosenColor, 0, 50000);
        }
        for (String id : playerController.getAllPlayerIDs()) {
            Player player = playerController.getPlayerFromID(id);
            player.setBoardSize(gui.getBoardSize()); // set later
            gui.addPlayer(player.getId(), player.getName(), player.getBalance(), player.getPosition(), player.getColor());
        }
    }


    //This method makes it possible for a player to move forward equal to the value of their dice roll
    private static void playRound(Player player) {
        player = playerController.getCurrentPlayer();
        if (player.getjailed() != 0){
            board.landOnField(player.getId(), player.getPosition(), player.getPosition());
        }
        else {
            String playerName = player.getName();
            gui.buttonRequest(("Det er " + playerName + "'s tur. Kast med terningerne"), "Kast");
            int[] dieValues = player.rollDie();
            movePlayer(player, dieValues);
        }
    }
    private static void movePlayer(Player player, int[] dieValues){
        int total = dieValues[2];
        gui.setDice(dieValues);
        int currentPlayerPosition = player.getPosition();

        // player moves
        player.setPosition(currentPlayerPosition + total);
        gui.movePlayerTo(player.getId(), currentPlayerPosition, player.getPosition());
        board.landOnField(player.getId(), currentPlayerPosition, player.getPosition());
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