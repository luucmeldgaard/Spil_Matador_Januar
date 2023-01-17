package dtu.matador.game;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Map;

public class GUIControllerTest {

    @Test
    public void loadGUI() {
        Loader loader = new Loader(0);
        ArrayList<Map<String, String>> fieldList;
        fieldList = loader.getBoardList();
        GUIController gui = new GUIController();
        gui.setGUI(fieldList);
    }

    @Test
    public void addNewPlayerToBoard() throws InterruptedException {
        Loader loader = new Loader(0);
        ArrayList<Map<String, String>> fieldList;
        fieldList = loader.getBoardList();
        GUIController gui = new GUIController();
        gui.setGUI(fieldList);
        gui.addPlayer("0", "Povl", 5000, 0, "test_myGreen");
    }

}