package dtu.matador.game;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

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
        FieldLoader fieldLoader = new FieldLoader("test_FieldData");
        ArrayList<Map<String, String>> fieldMap;
        fieldMap = fieldLoader.getFieldMap();
        guiCreator.setup(fieldMap);
    }

}