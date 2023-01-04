package dtu.matador.game;

import gui_main.GUI;

class GUIController {

    private static GUIController guiControllerObject;
    private static GUI gui;

    private GUIController() {
        GUICreator fields = new GUICreator();
        gui = new GUI();
    }

    public static GUIController getInstance() {
        if (guiControllerObject == null) {
            guiControllerObject = new GUIController();
        }
        else {System.out.println("GUI instance already initialized..."); }
        return guiControllerObject;
    }

}
