package dtu.matador.game;

import gui_fields.GUI_Car;
import gui_fields.GUI_Field;
import gui_fields.GUI_Player;
import gui_fields.GUI_Street;
import gui_main.GUI;
import java.awt.*;
import java.util.ArrayList;


class GUIController {

    private static GUIController guiControllerObject;
    private static GUI gui;
    static GUI_Field[] guiFields;
    static FieldController board;
    static int boardSize;

    static int playersInGame;
    static int playersPassedStartOnce = 0;
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

    public ArrayList<String> colorNamesArrayList = new ArrayList<String>();
    public void fillColorSelector(){
        colorNamesArrayList.add("Rød");
        colorNamesArrayList.add("Blå");
        colorNamesArrayList.add("Lyserød");
        colorNamesArrayList.add("Hvid");
        colorNamesArrayList.add("Lilla");
        colorNamesArrayList.add("Tyrkis");

    }

    public String colorDropDownList() {
        String[] colorNamesArray = new String[colorNamesArrayList.size()];
        colorNamesArray = colorNamesArrayList.toArray(colorNamesArray);
        String chosenColorString = gui.getUserSelection(
                "Vælg en farve", colorNamesArray);

        String chosenColor = "";
        //This should be done with a switch case or maybe a loop to look cleaner, but this works for now
        if (chosenColorString.equals("Rød")){
            chosenColor = "myRed";
            colorNamesArrayList.remove("Rød");
            }
        if (chosenColorString.equals("Blå")){
            chosenColor = "myBlue";
            colorNamesArrayList.remove("Blå");

        }
        if (chosenColorString.equals("Lyserød")){
            chosenColor = "myPink";
            colorNamesArrayList.remove("Lyserød");
        }

        if (chosenColorString.equals("Hvid")){
            chosenColor = "myWhite";
            colorNamesArrayList.remove("Hvid");
        }

        if (chosenColorString.equals("Lilla")) {
            chosenColor = "myPurple";
            colorNamesArrayList.remove("Lilla");
        }

        if (chosenColorString.equals("Tyrkis")) {
            chosenColor = "myTurqouise";
            colorNamesArrayList.remove("Tyrkis");
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
    public void addPlayer(String playerID, String name, int balance, int position, String primary) {
        GUI_Car car = addCar(Color.getColor(primary), Color.BLACK);
        GUI_Player player = new GUI_Player(name, balance, car);
        gui.addPlayer(player);
        player.getCar().setPosition(gui.getFields()[position]);
        guiPlayers.add(player);
        playersInGame += 1;
        System.out.println(playersInGame + "________________");
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


        if (nextPosition != 0 && playersPassedStartOnce > playersInGame){
            if (guiFields[nextPosition - 1].getTitle().equals("Start")) {
                System.out.println("__________START_______________");
                board.landOnField(playerID, nextPosition-1);
            }
        }

    }


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
        }
        board.landOnField(playerID, endPosition);

    }

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

    public void updateProperty(int fieldPosition, String color, int housing) {
        Color newColor = Color.getColor(color);
        GUI_Street field = ((GUI_Street) gui.getFields()[fieldPosition]);
        field.setBorder(newColor);
        if (housing == 5) {
            field.setHotel(true);
        }
        else if (housing >= 0 && housing < 5) {
            field.setHotel(false);
            field.setHouses(housing);
        }
    }

    public void updateProperty(int fieldPosition, String color) {
        Color newColor = Color.getColor(color);
        gui.getFields()[fieldPosition].setBackGroundColor(newColor);
    }

}