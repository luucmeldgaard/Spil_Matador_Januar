package dtu.matador.game;

import dtu.matador.game.fields.Street;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class FieldController {

    ArrayList<FieldSpaces> fields;
    FieldLoader fieldLoader;
    Map<String, Map<String, String>> fieldMap;




    public FieldController(String selectedBoard) {
        fieldLoader = new FieldLoader(selectedBoard);
        fieldMap = fieldLoader.getFieldMap();

        fields = new ArrayList<>();
        for (int i = 0; i < fieldMap.size(); i++) {
            fields.add(null);
        }

        setupFields();

    }


    public void setupFields() {
        for (Map<String, String> field : fieldMap.values()) {
            int fieldPosition = Integer.parseInt(field.get("position"));
            switch (field.get("fieldType")) {
                case "property" -> {fields.set(fieldPosition, new Street(field.get("title"), field.get("subtext"), field.get("subtext"), field.get("rent"),
                        field.get("color1"), field.get("color2"), field.get("price"), field.get("pawnForAmount"), field.get("position")));
                }
            }
        }

    }

    public FieldSpaces getField(int position) {
        return fields.get(position);
    }

    public ArrayList<FieldSpaces> getFieldsArray() {
        return fields;
    }

    public Map<String, Map<String, String>> getFieldMap() {
        return fieldMap;
    }

    public Map<String, String> getFieldAsMap(int position) {
        String pos = String.valueOf(position);
        return fieldMap.get(pos);
    }

    // overwrites chosen values of a specific field in the fieldMap
    public void updateFieldMap(int position) {
        System.out.println(fieldMap.get(String.valueOf(position)));
        Map<String, String> updatedFieldInfo = new HashMap<>();
        updatedFieldInfo = ((PropertyFields) fields.get(position)).updateGuiField();
        for (String key : updatedFieldInfo.keySet()) {
            //fieldMap.get(String.valueOf(position)).remove(key);
            fieldMap.get(String.valueOf(position)).replace(key, updatedFieldInfo.get(key));
        }
    }

}
