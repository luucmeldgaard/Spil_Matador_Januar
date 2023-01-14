package dtu.matador.game;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

import static org.junit.Assert.*;

public class GUIControllerTest {

    @Test
    public void loadGUI() {
        FieldLoader fieldLoader = new FieldLoader("test_fieldSpaces");
        ArrayList<Map<String, String>> fieldMap;
        fieldMap = fieldLoader.getFieldMap();
        GUIController gui = new GUIController();
    }

    @Test
    public void addNewPlayerToBoard() throws InterruptedException {
        FieldLoader fieldLoader = new FieldLoader("test_fieldSpaces");
        ArrayList<Map<String, String>> fieldMap;
        fieldMap = fieldLoader.getFieldMap();
        GUIController gui = new GUIController();
        //guiController.addPlayer("@001", "Torben", 500, 0, Color.RED);
    }

}