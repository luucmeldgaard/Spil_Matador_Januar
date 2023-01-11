package dtu.matador.game;

import gui_fields.GUI_Car;
import gui_fields.GUI_Field;
import gui_fields.GUI_Player;
import gui_main.GUI;

import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

class GUIController {

    private static GUIController guiControllerObject;
    private static GUI gui;
    static GUI_Field[] guiFields;
    static FieldController board;
    static int boardSize;

    static ArrayList<GUI_Player> guiPlayers;
    static int currentGUIPlayer;
    static GameController gameController;

    private GUIController(String selectedBoard) {
        board = new FieldController(selectedBoard);
        GUICreator fields = new GUICreator();
        guiFields = fields.setup(board.getFieldMap());
        gui = new GUI(guiFields);
        guiPlayers = new ArrayList<>();
        currentGUIPlayer = 0;
        boardSize = guiFields.length;
        gameController = new GameController();
    }

    public static GUIController getGUIInstance(String selectedBoard) {
        if (guiControllerObject == null) {
            guiControllerObject = new GUIController(selectedBoard);
        }
        else {System.out.println("GUI instance already initialized..."); }
        board.setGUI();
        return guiControllerObject;
    }

    public static GUIController getGUIInstance() {
        if (guiControllerObject != null) {
            System.out.println("GUI instance already initialized...");
            return guiControllerObject;
        }
        else {return null; }
    }

    public String buttonRequest(String message, String... buttons){
        return gui.getUserButtonPressed(message, buttons);
    }


    public Color colorDropDownList() {
        String chosenColorString = gui.getUserSelection(
                "Select a colour",
                "Rød", "Blå", "Lyserød", "Hvid", "Gul"
        );
        Color chosenColor = Color.black;
        //This should be done with a switch case or maybe a loop to look cleaner, but this works for now
        if (chosenColorString.equals("Rød")){ //This should be remade to pick colors from the colors.json we made
            chosenColor = Color.red;
            }
        if (chosenColorString.equals("Blå")){
            chosenColor = Color.blue;
        }
        if (chosenColorString.equals("Lyserød")){
            chosenColor = Color.pink;
        }
        if (chosenColorString.equals("Hvid")){
            chosenColor = Color.white;

        }
        return chosenColor;
    }


    public GUI_Car addCar(Color primaryColor, Color patternColor) {
        GUI_Car car = new GUI_Car(primaryColor, patternColor, GUI_Car.Type.CAR, GUI_Car.Pattern.FILL);
        return car;
    }

    /**
     *
     * @param playerID
     * @param name
     * @param balance
     * @param position
     * @param primary
     */
    public void addPlayer(String playerID, String name, int balance, int position, Color primary) {
        GUI_Car car = addCar(primary, Color.BLACK);
        GUI_Player player = new GUI_Player(name, balance, car);
        gui.addPlayer(player);
        player.getCar().setPosition(gui.getFields()[position]);
        guiPlayers.add(player);
    }

    public void setDice(int[] dice) {
        int die1 = dice[0];
        int die2 = dice[1];

        gui.setDice(die1,die2);

    }

    public void movePlayerOnce(String playerID, int nextPosition) {
        if (nextPosition >= boardSize) {
            nextPosition = nextPosition % boardSize;
        }
        GUI_Player player = guiPlayers.get(Integer.parseInt(playerID));
        player.getCar().setPosition(guiFields[nextPosition]);

        System.out.println(guiFields[nextPosition].getTitle());

        if (nextPosition != 0){
            if (guiFields[nextPosition - 1].getTitle().equals("Start")) {
                System.out.println("__________START_______________");
                board.landOnField(playerID, nextPosition);
            }
        }

        /*if (nextPosition >= boardSize) {
            nextPosition = nextPosition % boardSize; // if next position exceed board size, get the mod
        }
        GUI_Player player = guiPlayers.get(Integer.parseInt(playerID));
        player.getCar().setPosition((guiFields[nextPosition]));
        System.out.println(guiFields[nextPosition].getTitle());*/
    }


    public void movePlayerTo(String playerID, int startPosition, int endPosition) {
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
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        board.landOnField(playerID, endPosition);
    }

    /*
        GUI_Player guiPlayer = guiPlayers.get(Integer.parseInt(playerID));
        int nextPosition;
        for (int i = 0; i < positionAfter; i++) {
            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            currentPosition += 1;
            movePlayerOnce(playerID, currentPosition);
        }*/


    public int getBoardSize() {return boardSize; }

    public int getNumberOfPlayers(){
        return Integer.parseInt((gui.getUserSelection(
                "Select a number of players",
                "2", "3", "4", "5", "6"
        )));
    }

    public String getNameFromInput(){
        String playername = gui.getUserString("Enter your name here", 1, 30, true);
        return playername;
    }

    public void updateGUIPlayerBalance(String playerID, int balance) {
        GUI_Player guiPlayer = guiPlayers.get(Integer.parseInt(playerID));
        guiPlayer.setBalance(balance);

    }

}