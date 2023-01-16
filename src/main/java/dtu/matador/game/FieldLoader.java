package dtu.matador.game;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.*;

public class FieldLoader {

    ArrayList<Map<String, String>> boardList;
    Map<String, String> colorMap;
    ArrayList<Map<String, String>> chanceList;

    public FieldLoader(String selectedBoard) {
        boardList = JSONtoList(selectedBoard + ".json", "position", "board");
        this.colorMap = new HashMap<>();
        this.colorMap = JSONtoMapColors("colors.json", "colors");
        this.chanceList = JSONtoList("chance" + ".json", "Number", "chance");
    }


    // A JSON reader which reads a JSON and parses it to a string,
    // which it then returns.
    public String read(String filename, String jsonType) {

        // String to return
        String objString = "";

        // Defines the file and retrieves its canonical path
        String path = "";
        File file = new File("./json/" + filename);
        try {
            path = file.getCanonicalPath();
            System.out.println(path);
        } catch (IOException e) {
            System.out.println("No files found");
        }

        // Tries to parse the JSON to a Map<String, Map<String>, <String>
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {

            // Makes it possible to parse JSON
            JSONParser jsonParser = new JSONParser();

            // Creates an array of strings from an object created by the JsonParser
            // to be able to parse each field to the mapper.
            Object obj = jsonParser.parse(reader);
            objString = obj.toString();
        }

        // throws an exception
        // If the file doesn't exist in the canonical path ./json folder,
        // then it will try to retrieve the default JSON game files which can be found in the ressources.
        catch (IOException | ParseException ex) {
            try {
                switch (jsonType) {
                    case "board" -> filename = "default_fieldData.json";
                    case "chance" -> filename = "default_chance.json";
                    case "colors" -> filename = "default_colors.json";
                }
                InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filename);
                assert inputStream != null;
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                // Makes it possible to parse JSON
                JSONParser jsonParser = new JSONParser();

                // Creates an array of strings from an object created by the JsonParser
                // to be able to parse each field to the mapper.
                Object obj = jsonParser.parse(reader);
                objString = obj.toString();
            }
            catch (IOException | ParseException e) {
                throw new RuntimeException(e);
            }
        }
        return objString;
    }

    // Creates an ArrayList of maps from a JSON file
    private ArrayList<Map<String, String>> JSONtoList(String filename, String outerMapKey, String jsonType) {

        // The map to return
        ArrayList<Map<String, String>> board = new ArrayList<>();

        // Makes it possible to map JSON text
        ObjectMapper mapper = new ObjectMapper();

        String objString = read(filename, jsonType);
        objString = objString.substring(1, objString.length() - 1);
        String[] objArray = objString.split("},");
        for (int i = 0; i < objArray.length; i++) {
            board.add(null);
        }

        try {
            // Adds each field to the board map
            for (String item : objArray) {
                item = item + "}";
                Map<String, String> map = mapper.readValue(item, Map.class);
                int position = Integer.parseInt(map.get(outerMapKey));
                board.set(position, map);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return board;
    }

    //Converts JSON to colors on map
    private Map<String, String> JSONtoMapColors(String filename, String jsonType) {
        ObjectMapper mapper = new ObjectMapper();
        String objString = read(filename, jsonType);
        objString = objString.replace("[", "");
        objString = objString.replace("]", "");
        try{
            Map<String, String> map = mapper.readValue(objString, Map.class);
            for (String color : map.keySet()) {
                System.setProperty(color, map.get(color));
            }
            return map;
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // returns the Mapped fields
    public ArrayList<Map<String,String>> getFieldList() {
        return boardList;
    }

    public Map<String, String> getColorMap(){return colorMap;}

    public ArrayList<Map<String,String>> getChanceList() {return chanceList; }
}