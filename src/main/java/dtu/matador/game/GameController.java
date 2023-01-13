package dtu.matador.game;

import gui_main.GUI;

import java.awt.*;
import java.util.ArrayList;

public class GameController {

    static GameState currentGameState;
    static GUIController gui;
    static FieldController board;
    static String chosenBoard;

    int boardSize;

    //Main method. Runs the program
    public static void main(String[] args) {
        //GUI.setNull_fields_allowed(true); //This messes up the GUI but allows it to render with null fields, making troublefixing easier
        currentGameState = new GameState();
        gui = new GUIController("FieldData");
        menu();
        play();
    }

    public static void menu() {
        chosenBoard = gui.buttonRequest("Choose board", "fieldData");
        gui.close();
        setBoard(chosenBoard);
        setupPlayers();
        // currentPlayer = players.get(0); set later
    }
    public static void play() {
        while (true) {
            playRound(currentGameState.getCurrentPlayer()); // set later
            currentGameState.nextPlayer(); // set later
        }
    }

    //Sets the board in the GUI
    public static void setBoard(String selectedBoard) {
        //gui = GUIController.getGUIInstance(selectedBoard);
        board = new FieldController(currentGameState, gui, selectedBoard);
        //boardSize = gui.getBoardSize(); set later
    }

    //Adds a player, a player color and a playerID to the GUI
    public static void setupPlayers() {
        int numPlayers = (gui.getNumberOfPlayers());
        ArrayList<Player> players = new ArrayList<>();
        gui.fillColorSelector();
        for (int i = 0; i < numPlayers; i++) {
            String name = gui.getNameFromInput();
            System.out.println("Select player color");
            String chosenColor = gui.colorDropDownList();
            currentGameState.addPlayer(name, chosenColor, 0, 50000);
        }
        for (String id : currentGameState.getAllPlayerIDs()) {
            Player player = currentGameState.getPlayerFromID(id);
            player.setBoardSize(gui.getBoardSize()); // set later
            gui.addPlayer(player.getId(), player.getName(), player.getBalance(), player.getPosition(), player.getColor());
        }
    }


    //This method makes it possible for a player to move forward equal to the value of their dice roll
    public static void playRound(Player player) {
        String playerID = currentGameState.getCurrentPlayer().getId();
        // roll dice
        String playerName = player.getName();
        gui.buttonRequest(("It is " + playerName + "'s turn. Please roll the dice"), "Roll");
        int[] dieValues = player.rollDie();
        int total = (dieValues[0] + dieValues[1]);
        gui.setDice(dieValues);
        int oldplayerpos = player.getPosition();
        // player moves
        //int newplayerpost = player.getPosition();
        player.setPosition(oldplayerpos + total);
        gui.movePlayerTo(player.getId(), oldplayerpos, player.getPosition());
        board.landOnField(player.getId(), oldplayerpos, player.getPosition());
    }

}