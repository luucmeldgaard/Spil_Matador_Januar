package dtu.matador.game;

import gui_main.GUI;

import java.awt.*;
import java.util.ArrayList;

public class GameController {

    private static PlayerController playerController;
    private static GUIController gui;
    private static FieldController board;
    private static final int[] IGNORE_DIE_VALUES = null;

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
        int currentPosition = player.getPosition();
        if (player.getjailed() != 0){
            board.landOnField(player.getId(), player.getPosition(), player.getPosition(), false);
        }
        else {
            String playerName = player.getName();
            String response = gui.buttonRequest("Det er " + playerName + "'s tur. Kast med terningerne", "Kast", "Andet");
            if (response.equals("Andet")) {
                bonusMenuHandler(player);
            }
            else {
                if (playerController.getPlayerFromID(player.getId()) != null) {
                    int[] dieValues = player.rollDie();
                    movePlayer(player, currentPosition, dieValues, true);
                }
            }
        }
    }

    private static void bonusMenuHandler(Player player) {
        String response = gui.dropDownList("Vælg en action fra menuen", "Tilbage", "Snydekoder", "Lav byttehandel", "Gem Spil", "Sælg boliger", "Giv Op");
        switch (response) {
            case "Tilbage" -> { playRound(player); }
            case "Snydekoder" -> { cheatMenu(player); }
            case "Lav byttehandel" -> {}
            case "Gem Spil" -> {}
            case "Sælg bolig(er)" -> {}
            case "Giv Op" -> {
                String id = player.getId();
                playerController.removePlayer(id);
                gui.removePlayer(id);
            }
        }
    }

    private static void movePlayer(Player player, int currentPosition, int[] dieValues, boolean passStartBonus){
        int total;
        // player position already been set
        if (dieValues == IGNORE_DIE_VALUES) {
            System.out.println("int array was 0");
        }
        // normal gameplay
        else {
            total = dieValues[2];
            gui.setDice(dieValues);
            player.setPosition(currentPosition + total);
        }



        // player moves
        gui.movePlayerTo(player.getId(), currentPosition, player.getPosition());
        board.landOnField(player.getId(), currentPosition, player.getPosition(), passStartBonus);
    }

    private static void cheatMenu(Player player) {
        int currentPosition = player.getPosition();
        String response = gui.dropDownList("Vælg en snydekode", "Tilbage", "Flyt til felt", "Sæt næste terningslag", "Modtag løsladelseskort",
                                                  "Sæt balance", "Sæt en anden spillers balance", "Køb alle grunde", "Vind spillet");
        switch (response) {
            case "Tilbage" -> { bonusMenuHandler(player); }
            case "Flyt til felt" -> {
                ArrayList<String> allFieldNames = board.lookUpFieldStringValues("title");
                String[] allFieldNamesStringArray = allFieldNames.toArray(new String[0]);
                response = gui.dropDownList("Væg et felt", allFieldNamesStringArray);
                System.out.println("felt valgt: " + response);
                ArrayList<String> fieldPositionAsArray = board.lookUpFieldStringValues(null, "title", response, "position");
                int fieldPosition = Integer.parseInt(fieldPositionAsArray.get(0));
                System.out.println("Felt position: " + fieldPosition);
                player.setPosition(fieldPosition);
                movePlayer(player, currentPosition, IGNORE_DIE_VALUES, true);
            }
            case "Sæt næste terningslag" -> {
                String[] dieFaces = new String[]{"1", "2", "3", "4", "5", "6"};
                int die1 = Integer.parseInt(gui.dropDownList("Choose first die", dieFaces));
                int die2 = Integer.parseInt(gui.dropDownList("Choose first die", dieFaces));
                int[] setDieValues = new int[]{die1, die2, die1+die2};
                movePlayer(player, currentPosition, setDieValues, true);
            }
            case "Modtag løsladelseskort" -> {}
            case "Sæt balance" -> {}
            case "Sæt en anden spillers balance" -> {}
            case "Køb alle grunde" -> {}
            case "Gå i fængsel" -> {}
            case "Vind spillet" -> {}
        }
    }


}