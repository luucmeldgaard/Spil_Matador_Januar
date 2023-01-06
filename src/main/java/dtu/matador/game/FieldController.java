package dtu.matador.game;

import dtu.matador.game.fields.Street;

import java.util.ArrayList;
import java.util.Map;

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
                case "property" -> {fields.set(fieldPosition, new Street(field.get("name"), field.get("subtext"), field.get("subtext"), field.get("rent"), field.get("color1"), field.get("color2"), field.get("price"), field.get("owner")));
                }
            }
        }

    }

    public FieldSpaces getField(int position) {
        return fields.get(position);
    }

}
