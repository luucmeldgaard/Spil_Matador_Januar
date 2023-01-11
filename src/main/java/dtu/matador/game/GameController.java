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
        int numPlayers = (gui.getNumberOfPlayers());
        ArrayList<Player> players = new ArrayList<>();
        gui.fillColorSelector();
        for (int i = 0; i < numPlayers; i++) {
            String name = gui.getNameFromInput();
            System.out.println("Select player color");
            String chosenColor = gui.colorDropDownList();
            Player player = new Player(name, chosenColor, 0, 500);
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
        gui.buttonRequest("Roll Dice", "Roll");
        int[] dieValues = player.rollDie();
        int total = (dieValues[0] + dieValues[1]);
        gui.setDice(dieValues);
        int oldplayerpos = player.getPosition();




        // player moves
        //TODO: MAKE THIS MOVE ONLY ONCE player.movePosition(total);
        int newplayerpost = player.getPosition();
        gui.movePlayerTo(player.getId(), oldplayerpos, player.getPosition());
    }

    /*

        public void movePlayerTo(String playerID, int startPosition, int endPosition) {
        playersPassedStartOnce += 1;
        int steps = endPosition - startPosition;
        int currentPosition = startPosition;
        if (steps < 0) {
            steps += boardSize;
        }
        for (int i = 0; i < steps; i++) {
            currentPosition++;
            if (currentPosition >= boardSize) {
                currentPosition = 0;
            }
            movePlayerOnce(playerID, currentPosition);
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
     */

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