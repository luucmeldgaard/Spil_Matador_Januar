package dtu.matador.game;

import gui_main.GUI;

import java.awt.*;
import java.util.ArrayList;

public class GameController {

    private static PlayerController playerController;
    private static GUIController gui;
    private static FieldController board;
    private static final int[] IGNORE_DIE_VALUES = null;
    private static int rolledDoublesInARow = 0;

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
        while (playerController.getAllPlayerIDs().size() != 1) {
            Player player = playerController.getCurrentPlayer();
            playRound(player); // set later
            int[] lastDieRoll = player.getLastPlayedDieRoll();
            if (lastDieRoll[0] == lastDieRoll[1] && lastDieRoll[0] != 0) {
                gui.buttonRequest("Since you rolled a double, it's your turn again!", "Ok");
                rolledDoublesInARow += 1;
            }
            else {
                for (String removedID : playerController.getRemovedPlayerIDs()) {
                    gui.removePlayer(removedID);
                }
                rolledDoublesInARow = 0;
                playerController.nextPlayer(); // set later
            }
        }
        System.out.println("game ended");
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
            player.setLastPlayedDieRoll(new int[]{0,0});
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
                ArrayList<String> allPlayerIDs = playerController.getAllPlayerIDs();
                if (allPlayerIDs.size() == 1) {
                    gui.buttonRequest("Error. You are the only player left", "Continue");
                    playRound(player);
                }
                else {
                    String id = player.getId();
                    playerController.removePlayer(id);
                    gui.removePlayer(id);
                }
            }
        }
    }

    private static void movePlayer(Player player, int currentPosition, int[] dieValues, boolean passStartBonus){
        int total;
        // player position already been set
        if (dieValues == IGNORE_DIE_VALUES) {
            gui.movePlayerTo(player.getId(), currentPosition, player.getPosition());
            board.landOnField(player.getId(), currentPosition, player.getPosition(), passStartBonus);
        }
        // normal gameplay
        else {
            if (rolledDoublesInARow == 2 && dieValues[0] == dieValues[1]) {
                gui.buttonRequest("You rolled doubles 3 times in a row! We suspect cheating...", "Go to Jail");
                int jailPosition = board.getJailPosition();
                player.setPosition(jailPosition);
                gui.movePlayerTo(player.getId(), currentPosition, player.getPosition());
                player.setjailed(1);
                player.setLastPlayedDieRoll(new int[]{0,0});
                rolledDoublesInARow = 0;
            }
            else {
                total = dieValues[2];
                gui.setDice(dieValues);
                player.setPosition(currentPosition + total);

                // player moves
                gui.movePlayerTo(player.getId(), currentPosition, player.getPosition());
                board.landOnField(player.getId(), currentPosition, player.getPosition(), passStartBonus);
            }
        }
    }

    private static void cheatMenu(Player player) {
        int currentPosition = player.getPosition();
        String response = gui.dropDownList("Vælg en snydekode", "Tilbage", "Flyt til felt", "Sæt næste terningslag", "Modtag løsladelseskort",
                                                  "Sæt pengebeholdning", "Sæt en anden spillers pengebeholdning", "Køb alle grunde", "Vind spillet");
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
                player.setLastPlayedDieRoll(setDieValues);
                movePlayer(player, currentPosition, setDieValues, true);
            }
            case "Modtag løsladelseskort" -> {
                int amount = gui.intRequest("Sæt antal løsladelseskort", 0, Integer.MAX_VALUE - player.getJailCards());
                player.setJailCards(amount);
                playRound(player);
            }
            case "Sæt pengebeholdning" -> {
                int amount = gui.intRequest("Sæt pengebeholdning", 0, Integer.MAX_VALUE - player.getBalance());
                player.setBalance(amount);
                gui.updateGUIPlayerBalance(player.getId(), amount);
                playRound(player);
            }
            case "Sæt en anden spillers pengebeholdning" -> {
                ArrayList<String> allPlayerIDs = playerController.getAllPlayerIDs();
                ArrayList<String> playerNamesArrayList = new ArrayList<>();
                for (String targetID : allPlayerIDs) {
                    if (targetID != player.getId()) {
                        playerNamesArrayList.add(playerController.getPlayerFromID(targetID).getName());
                    }
                }
                String[] playerNames = playerNamesArrayList.toArray(new String[0]);
                String name = gui.buttonRequest("Vælg på en spiller", playerNames);
                Player targetPlayer = playerController.getPlayerFromName(name);
                int amount = gui.intRequest("Sæt pengebeholdning", 0, Integer.MAX_VALUE - targetPlayer.getBalance());
                targetPlayer.setBalance(amount);
                gui.updateGUIPlayerBalance(targetPlayer.getId(), amount);
                playRound(player);
            }
            case "Køb alle grunde" -> {}
            case "Gå i fængsel" -> {
                int jailPosition = board.getJailPosition();
                player.setPosition(jailPosition);
                gui.movePlayerTo(player.getId(), currentPosition, player.getPosition());
                player.setjailed(1);
            }
            case "Vind spillet" -> {
                ArrayList<String> allPlayerIDs = playerController.getAllPlayerIDs();
                for (String targetID : allPlayerIDs) {
                    if (!targetID.equals(player.getId())) {
                        playerController.removePlayer(targetID);
                        gui.removePlayer(targetID);
                    }
                }
            }
        }
    }


}