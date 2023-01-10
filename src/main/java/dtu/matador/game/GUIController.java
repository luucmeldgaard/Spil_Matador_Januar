package dtu.matador.game;

import gui_fields.GUI_Car;
import gui_fields.GUI_Field;
import gui_fields.GUI_Player;
import gui_main.GUI;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class GUIController {

    private static GUIController guiControllerObject;
    public GUI gui;

    public FieldLoader fieldloader;
    GUI_Field[] guiFields;
    FieldController board;
    int boardSize;

    ArrayList<GUI_Player> guiPlayers;
    int currentGUIPlayer;

    Map<String, String> colorMap = fieldloader.getColorMap();

    private GUIController(String selectedBoard) {
        board = new FieldController(selectedBoard);
        GUICreator fields = new GUICreator();
        guiFields = fields.setup(board.getFieldMap());
        gui = new GUI(guiFields);
        guiPlayers = new ArrayList<>();
        currentGUIPlayer = 0;
        boardSize = guiFields.length;

    }


    public static GUIController getInstance(String selectedBoard) {
        if (guiControllerObject == null) {
            guiControllerObject = new GUIController(selectedBoard);
        }
        else {System.out.println("GUI instance already initialized..."); }
        return guiControllerObject;
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

    public void movePlayerTo(int position) {
        guiPlayers.get(currentGUIPlayer).getCar().setPosition(gui.getFields()[position]);
    }

    public int getBoardSize() {return boardSize; }

    public String getNameFromInput(){
        String playername = gui.getUserString("Enter your name here", 1, 50, false);
        return playername;
    }

}