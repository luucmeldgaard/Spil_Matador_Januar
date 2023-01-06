package dtu.matador.game;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class FieldLoader {

    Map<String, Map<String, String>> boardMap;


    public FieldLoader(String selectedBoard) throws IOException, ParseException {

        this.boardMap = JSONtoMapBoard(selectedBoard + ".json");

    }

    private Map<String, Map<String, String>> JSONtoMapBoard(String filename) throws IOException, ParseException {

        // The map to return
        Map<String, Map<String, String>> board = new HashMap<>();

        // Makes it possible to parse JSON and map JSON text
        JSONParser jsonParser = new JSONParser();
        ObjectMapper mapper = new ObjectMapper();

        // Tries to parse the JSON to a Map<String, Map<String>, <String>
        try (FileReader fieldFileReader = new FileReader(filename)) {

            // Creates an array of strings from an object created by the JsonParser
            // to be able to parse each field to the mapper.
            Object obj = jsonParser.parse(fieldFileReader);
            String objString = obj.toString();
            objString = objString.substring(1, objString.length() - 1);
            String[] objArray = objString.split("},");

            // Adds each field to the board map
            for (String item : objArray) {
                item = item + "}";
                System.out.println(item);
                Map<String, String> map = mapper.readValue(item, Map.class);
                board.put(map.get("position"), map);
            }

            return board;

        }


    }
}