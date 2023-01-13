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
        gui = new GUIController("FieldData");
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
        if (player.jailed == 0){
            playRoundUnjailed(player);
        }
        else {
            playRoundJailed(player);
        }
        // roll dice

    }
    private void movePlayer(Player player, int[] dieValues){
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
    private void playRoundUnjailed(Player player){
        String playerName = player.getName();
        gui.buttonRequest(("Det er " + playerName + "'s tur. Kast med terningerne"), "Kast");
        int[] dieValues = player.rollDie();
        movePlayer(player, dieValues);
    }

    private void playRoundJailed(Player player) {
        //String playerName = player.getName(); //Ununsed code
        if (player.jailed == 1 || player.jailed == 2) {
            if (gui.payOrRoll()) { //If this is true, the player picked "Slå med terningerne"
                int[] dieValues = player.rollDie();
                if (dieValues[0] == dieValues[1]) {
                    player.jailed = 0;
                    gui.displayGeneralMessage("Tillykke! Du er kommet ud af fængslet");
                    movePlayer(player, dieValues);
                }
                else {
                    gui.displayGeneralMessage("Det var desværre ikke to ens");
                    player.jailed += 1;
                }
            } else {
                //TODO: TRÆK 1000 kroner fra spillerens balance
                playRoundUnjailed(player);
            }
        }
        else {
            //TODO: TRÆK 1000 kroner fra spillerens balance
            player.jailed = 0;

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