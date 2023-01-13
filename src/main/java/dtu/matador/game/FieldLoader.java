package dtu.matador.game;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class FieldLoader {

    Map<String, Map<String, String>> boardMap;
    Map<String, String> colorMap;
    Map<String, Map<String, String>> chanceMap;

    public FieldLoader(String selectedBoard) {
        boardMap = JSONtoMap(selectedBoard + ".json", "position");
        this.colorMap = new HashMap<>();
        this.colorMap = JSONtoMapColors("colors.json");
        this.chanceMap = JSONtoMap("chance" + ".json", "Number");
    }

    private Map<String, Map<String, String>> JSONtoMap(String filename, String outerMapKey) {

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
                Map<String, String> map = mapper.readValue(item, Map.class);
                board.put(map.get(outerMapKey), map);
            }

        }
        //throws an exception
        catch (IOException | ParseException ex) {
            throw new RuntimeException(ex);
        }

        return board;
    }
    //Converts JSON to colors on map
    private Map<String, String> JSONtoMapColors(String filename) {
        JSONParser jsonParser = new JSONParser();
        try (FileReader fieldFileReader = new FileReader(filename)) {
            Object obj = jsonParser.parse(fieldFileReader);

            ObjectMapper mapper = new ObjectMapper();
            String objString = obj.toString();
            objString = objString.replace("[", "");
            objString = objString.replace("]", "");
            Map<String, String> map = mapper.readValue(objString, Map.class);
            for (String color : map.keySet()) {
                System.setProperty(color, map.get(color));
            }
            return map;
        }
        catch (IOException | ParseException ex) {
            throw new RuntimeException(ex);
        }
    }

    // returns the Mapped fields
    public Map<String, Map<String,String>> getFieldMap() {
        return boardMap;
    }

    public Map<String, String> getColorMap(){return colorMap;}

    public Map<String, Map<String,String>> getChanceMap() {return chanceMap; }
}