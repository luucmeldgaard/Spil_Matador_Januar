package dtu.matador.game;

import gui_fields.GUI_Car;
import gui_fields.GUI_Field;
import gui_fields.GUI_Player;
import gui_main.GUI;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

class GUIController {

    private static GUIController guiControllerObject;
    public GUI gui;
    Map<String, GUI_Player> guiPlayerMap;
    GUI_Field[] guiFields;

    private GUIController(Map<String, Map<String, String>> fieldMap) {
        GUICreator fields = new GUICreator();
        guiPlayerMap = new HashMap<>();
        guiFields = fields.setup(fieldMap);
        gui = new GUI(guiFields);
    }


    public static GUIController getInstance(Map<String, Map<String, String>> fieldMap) {
        if (guiControllerObject == null) {
            guiControllerObject = new GUIController(fieldMap);
        }
        else {System.out.println("GUI instance already initialized..."); }
        return guiControllerObject;
    }

    public String buttonRequest(String message, String[] buttons){
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
        guiPlayerMap.put(playerID, player);
    }

    public void rollDie() {

    }

    private void movePlayerOneField(GUI_Player player, int position) {
        guiPlayerMap.get(player).getCar().setPosition(gui.getFields()[position + 1]);
    }

    public void movePlayer(GUI_Player player, int amount) {
        for (int i = 0; i < amount; i++) {
            movePlayerOneField(player, amount);
        }
    }

    public void movePlayerTo(String playerID, int position) {
        guiPlayerMap.get(playerID).getCar().setPosition(gui.getFields()[position]);
    }



}
