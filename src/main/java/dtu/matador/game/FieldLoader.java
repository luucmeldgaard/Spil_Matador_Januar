package dtu.matador.game;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FieldLoader {

    ArrayList<Map<String, String>> boardList;
    Map<String, String> colorMap;
    ArrayList<Map<String, String>> chanceList;

    public FieldLoader(String selectedBoard) {
        String filename = ("./src/main/resources/" + selectedBoard + ".json");
        System.out.println("opening " + filename);
        boardList = JSONtoList(filename,"position");
        this.colorMap = new HashMap<>();
        this.colorMap = JSONtoMapColors("./src/main/resources/colors.json");
        this.chanceList = JSONtoList("./src/main/resources/chance" + ".json", "Number");
    }

    private ArrayList<Map<String, String>> JSONtoList(String filename, String outerMapKey) {

        // The map to return
        ArrayList<Map<String, String>> board = new ArrayList<>();

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
            for (int i = 0; i < objArray.length; i++) {
                board.add(null);
            }

            // Adds each field to the board map
            for (String item : objArray) {
                item = item + "}";
                Map<String, String> map = mapper.readValue(item, Map.class);
                int position = Integer.parseInt(map.get(outerMapKey));
                board.set(position, map);
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
    public ArrayList<Map<String,String>> getFieldList() {
        return boardList;
    }

    public Map<String, String> getColorMap(){return colorMap;}

    public ArrayList<Map<String,String>> getChanceList() {return chanceList; }
}