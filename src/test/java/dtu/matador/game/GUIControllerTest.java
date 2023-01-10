package dtu.matador.game;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.awt.*;
import java.util.Map;

import static org.junit.Assert.*;

public class GUIControllerTest {

    @Ignore
    @Test
    public void loadGUI() {
        FieldLoader fieldLoader = new FieldLoader("test_fieldSpaces");
        Map<String, Map<String, String>> fieldMap;
        fieldMap = fieldLoader.getFieldMap();
        GUIController guiController = GUIController.getGUIInstance("test_fieldSpaces");
    }

    @Test
    public void addNewPlayerToBoard() throws InterruptedException {
        FieldLoader fieldLoader = new FieldLoader("test_fieldSpaces");
        Map<String, Map<String, String>> fieldMap;
        fieldMap = fieldLoader.getFieldMap();
        GUIController guiController = GUIController.getGUIInstance("test_fieldSpaces");
        guiController.addPlayer("@001", "Torben", 500, 0, Color.RED);
    }

}