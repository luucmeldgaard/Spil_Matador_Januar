package dtu.matador.game;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class GUICreatorTest {

    @Test
    public void EmptyGuiFieldsCreated() {
        GUICreator guiCreator = new GUICreator();
        Map<String, Map<String, String>> fieldMap;
        fieldMap = new HashMap<>();
        guiCreator.setup(fieldMap);
    }

    @Test
    public void guiFieldsCreated() {
        GUICreator guiCreator = new GUICreator();
        FieldLoader fieldLoader = new FieldLoader("test_fieldSpaces");
        Map<String, Map<String, String>> fieldMap;
        fieldMap = fieldLoader.getFieldMap();
        guiCreator.setup(fieldMap);
    }

}