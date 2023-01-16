package dtu.matador.game;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Map;

public class GUICreatorTest {

    @Test
    public void EmptyGuiFieldsCreated() {
        GUICreator guiCreator = new GUICreator();
        ArrayList<Map<String, String>> fieldMap;
        fieldMap = new ArrayList<>();
        guiCreator.setup(fieldMap);
    }

    @Test
    public void guiFieldsCreated() {
        GUICreator guiCreator = new GUICreator();
        Loader loader = new Loader("test_FieldData");
        ArrayList<Map<String, String>> fieldMap;
        fieldMap = loader.getBoardList();
        guiCreator.setup(fieldMap);
    }

}