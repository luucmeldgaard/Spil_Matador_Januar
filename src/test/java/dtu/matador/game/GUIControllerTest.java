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

}