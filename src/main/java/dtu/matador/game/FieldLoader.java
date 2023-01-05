package dtu.matador.game;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FieldLoader {

    Map<String, Map<String, String>> boardMap;
    Map<Integer, String> positionalMap;
    Map<String, String> colorMap;


    public FieldLoader(String selectedBoard) {

        this.boardMap = new HashMap<>();
        this.positionalMap = new HashMap<>();
        this.colorMap = new HashMap<>();

        this.boardMap = JSONtoMapBoard(selectedBoard + ".json");
        for (Map<String, String> fieldObject : this.boardMap.values()) {
            this.positionalMap.put(Integer.parseInt(fieldObject.get("position")), fieldObject.get("title"));
        }

    }
    private Map<String, Map<String, String>> JSONtoMapBoard(String filename) {

        return new HashMap<>();
    }

}
