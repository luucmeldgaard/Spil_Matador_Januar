package dtu.matador.game;

import gui_fields.GUI_Car;
import gui_fields.GUI_Field;
import gui_fields.GUI_Player;
import gui_main.GUI;

import java.awt.*;
import java.util.ArrayList;

class GUIController {

    public static GUI gui;
    static GUI_Field[] guiFields;
    static FieldController board;
    static int boardSize;

    static ArrayList<GUI_Player> guiPlayers;
    static int currentGUIPlayer;
    GameController gameController;

    public GUIController(String selectedBoard) {
        board = new FieldController(selectedBoard);
        GUICreator fields = new GUICreator();
        guiFields = fields.setup(board.getFieldMap());
        gui = new GUI(guiFields);
        guiPlayers = new ArrayList<>();
        currentGUIPlayer = 0;
        boardSize = guiFields.length;
        board.setGUI();
        gameController = new GameController();
    }

    public GUIController() {}

    public String buttonRequest(String message, String... buttons){
        return gui.getUserButtonPressed(message, buttons);
    }

    public void dropDownList() {

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

    public void movePlayerTo(String playerID, int position) {
        guiPlayers.get(currentGUIPlayer).getCar().setPosition(gui.getFields()[position]);
        board.landOnField(playerID, position);

    }

    public int getBoardSize() {return boardSize; }

    //Player gets the option to pay the bill
    public boolean playerAccept(String playerID, int price) {
        String accept = buttonRequest("Do you accept?", "Yes", "No");
        if (accept.equals("Yes")) {
            gameController.billPlayer(playerID, price);
            return true;
        }
        else { return false; }
    }
}