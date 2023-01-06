package dtu.matador.game;

import java.util.ArrayList;
import java.util.Map;

public class FieldController {

    ArrayList<FieldSpaces> fields;
    FieldLoader fieldLoader;
    Map<String, Map<String, String>> fieldMap;



    public FieldController(String selectedBoard) {
        fieldLoader = new FieldLoader(selectedBoard);
        fieldMap = fieldLoader.getFieldMap();
        fields = setupFields();
    }

    public ArrayList<FieldSpaces> setupFields() {
        return fields;
    }

}
