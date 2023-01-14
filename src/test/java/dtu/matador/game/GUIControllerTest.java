package dtu.matador.game;

import org.junit.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

public class GUIControllerTest {

    @Test
    public void loadGUI() {
        FieldLoader fieldLoader = new FieldLoader("test_FieldData");
        ArrayList<Map<String, String>> fieldList;
        fieldList = fieldLoader.getFieldList();
        GUIController gui = new GUIController();
        gui.setGUI(fieldList);
    }

    @Test
    public void addNewPlayerToBoard() throws InterruptedException {
        FieldLoader fieldLoader = new FieldLoader("test_FieldData");
        ArrayList<Map<String, String>> fieldList;
        fieldList = fieldLoader.getFieldList();
        GUIController gui = new GUIController();
        gui.setGUI(fieldList);
        gui.addPlayer("0", "Povl", 5000, 0, "test_myGreen");
    }

}