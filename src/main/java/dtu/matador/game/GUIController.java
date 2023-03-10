package dtu.matador.game;

import gui_codebehind.GUI_Center;
import gui_fields.*;
import gui_main.GUI;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

class GUIController {

    private static GUI gui;
    private GUI_Field[] guiFields;
    int boardSize;
    ArrayList<GUI_Player> guiPlayers;
    int numberOfPlayers;

    public GUIController() {
        gui = new GUI(new GUI_Field[0], Color.DARK_GRAY);
        GUI_Center.getInstance().clearLabels();
        GUI_Center.getInstance().setBGColor(Color.DARK_GRAY);
        this.guiPlayers = new ArrayList<>();
    }

    public void setGUI(ArrayList<Map<String,String>> fieldMap) {
        GUICreator fields = new GUICreator();
        guiFields = fields.setup(fieldMap);
        boardSize = guiFields.length;
        GUI.setNull_fields_allowed(false);
        GUI_Center.getInstance().displayDefault();
        gui = new GUI(guiFields);
    }

    public String buttonRequest(String message, String... buttons){
        return gui.getUserButtonPressed(message, buttons);
    }

    public int intRequest(String message, int minValue, int maxValue) {
        return gui.getUserInteger(message, minValue, maxValue);
    }

    public String dropDownList(String message, String... buttons) {
        return gui.getUserSelection(message, buttons);
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


    private GUI_Car addCar(Color primaryColor, Color patternColor) {
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
    }

    public void displayGeneralMessage(String message){
        gui.showMessage(message);
    }


    public void setDice(int[] dice) {
        int die1 = dice[0];
        int die2 = dice[1];

        gui.setDice(die1,die2);

    }

    private void movePlayerOnce(String playerID, int nextPosition, int endPosition) {
        if (nextPosition >= boardSize) {
            nextPosition = nextPosition % boardSize;
        }

        GUI_Player player = guiPlayers.get(Integer.parseInt(playerID));
        player.getCar().setPosition(guiFields[nextPosition]);

        if (nextPosition == endPosition) {
            for (int i = 0; i < 4; i++) {
                guiFields[nextPosition].setForeGroundColor(Color.WHITE);
                try {
                    Thread.sleep(90);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                guiFields[nextPosition].setForeGroundColor(Color.BLACK);
                try {
                    Thread.sleep(90);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
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
            movePlayerOnce(playerID, currentPosition, endPosition);
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void removePlayer(String playerID) {
        if (Integer.parseInt(playerID) < guiPlayers.size()) {
            GUI_Player player = guiPlayers.get(Integer.parseInt(playerID));
            player.getCar().setPosition(null);
        }
    }

    public int getBoardSize() {return boardSize; }

    public void displayInMiddle(String textToShow){
        gui.displayChanceCard(textToShow);
    }

    public void displayInMiddle(String... strings){
        String res = "";
        for (int i = 0; i < strings.length;i++) {
            res = res + strings[i];
            res = res + "\r\n";
        }
        System.out.println(res);
        displayInMiddle(res);
    }


    public int getNumberOfPlayers(){
         numberOfPlayers = Integer.parseInt((gui.getUserSelection(
                "Vælg antal spillere",
                "2", "3", "4", "5", "6"
        )));
         return numberOfPlayers;
    }

    public static ArrayList<String> takenNames = new ArrayList<String>();


    public String getNameFromInput(){
        while (true) {
            String playername = retrieveNameFromInput();
            if (!(takenNames.contains(playername))){
                takenNames.add(playername);
                System.out.println("!(takenNames.contains(playername))");
                return playername;
            }
            else{
                gui.showMessage("En anden spiller har allerede taget dette navn. Vælg venligst et andet");
            }
        }
    }
    public String retrieveNameFromInput(){
        String name = gui.getUserString("Indtast dit navn her", 1, 30, true);
        return name;
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

    public void close(){
        gui.close();
    }

}