package dtu.matador.game;

import gui_main.GUI;

import java.awt.*;
import java.util.ArrayList;

public class GameController {

    //Initiating instances and an int boardSize
    GUIController gui;
    int boardSize;
    static GameState currentGameState;

    //Main method. Runs the program
    public static void main(String[] args) {
        //GUI.setNull_fields_allowed(true); //This messes up the GUI but allows it to render with null fields, making troublefixing easier
        currentGameState = new GameState();
        currentGameState.menu();
        currentGameState.play();


    }

    //Sets the board in the GUI
    public void setBoard(String selectedBoard) {
        gui = GUIController.getGUIInstance(selectedBoard);
        boardSize = gui.getBoardSize();
    }

    //Adds a player, a player color and a playerID to the GUI
    public ArrayList<Player> addPlayers() {
        int numPlayers = (gui.getNumberOfPlayers());
        ArrayList<Player> players = new ArrayList<>();
        gui.fillColorSelector();
        for (int i = 0; i < numPlayers; i++) {
            String name = gui.getNameFromInput();
            System.out.println("Select player color");
            String chosenColor = gui.colorDropDownList();
            Player player = new Player(name, chosenColor, 0, 500000, true);
            //player.setId(player.toString());
            player.setBoardSize(boardSize);
            System.out.println(name + "'s ID is: " + player.getId());
            gui.addPlayer(player.getId(), player.getName(), player.getBalance(), player.getPosition(), player.getColor());
            players.add(player);
        }
        return players;
    }


    //This method makes it possible for a player to move forward equal to the value of their dice roll
    public void playRound(Player player) {
        if (player.jailed == 0){
            playRoundUnjailed(player);
        }
        else {
            playRoundJailed(player);
        }
        // roll dice

    }
    public void movePlayer(Player player, int[] dieValues){
        String playerName = player.getName();
        int total = (dieValues[0] + dieValues[1]);
        gui.setDice(dieValues);
        int oldplayerpos = player.getPosition();
        // player moves
        //int newplayerpost = player.getPosition();
        player.setPosition(oldplayerpos + total);
        gui.movePlayerTo(player.getId(), oldplayerpos, player.getPosition());
    }
    public void playRoundUnjailed(Player player){
        String playerName = player.getName();
        gui.buttonRequest(("Det er " + playerName + "'s tur. Kast med terningerne"), "Kast");
        int[] dieValues = player.rollDie();
        movePlayer(player, dieValues);
    }

    public void playRoundJailed(Player player) {
        String playerName = player.getName();
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