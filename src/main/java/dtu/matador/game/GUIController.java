package dtu.matador.game;

import gui_main.GUI;

import java.util.Map;

class GUIController {

    private static GUIController guiControllerObject;
    private static GUI gui;

    private GUIController(Map<String, Map<String, String>> fieldMap) {
        GUICreator fields = new GUICreator();
        gui = new GUI(fields.setup(fieldMap));
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

    public void addPlayer() {

    }

    public void rollDie() {

    }

    public void movePlayerOneField() {

    }

    public void movePlayer(int amount) {
        for (int i = 0; i < amount; i++) {
            movePlayerOneField();
        }
    }

    public void movePlayerTo() {

    }



}
