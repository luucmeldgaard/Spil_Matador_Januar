package dtu.matador.game;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.*;

public class Loader {


    ArrayList<String> boardFiles;
    ArrayList<String> colorFiles;
    ArrayList<String> chanceFiles;
    ArrayList<Map<String, String>> boardList;
    Map<String, String> settings;
    Map<String, String> colorMap;
    ArrayList<Map<String, String>> chanceList;

    public Loader(int loadGame) {
        if (loadGame == 0) {
            this.settings = JSONtoMap("settings.json", "settings");
            this.boardFiles = getAllFilesOfType("board", "./json/");
            this.colorFiles = getAllFilesOfType("colors", "./json/");
            this.chanceFiles = getAllFilesOfType("chance", "./json/");
            this.boardList = new ArrayList<>();
            this.colorMap = JSONtoMap(this.settings.get("colors"), "colors");
            this.chanceList = JSONtoList(this.settings.get("chance"), "Number", "chance");
        }
        else { loadFromExistingGame(loadGame); }
    }

    public ArrayList<String> getAllFilesOfType(String SnakeCaseSuffix, String CanonicalPath) {

        ArrayList<String> allFiles = new ArrayList<>();

        File folder = new File("./json");
        File[] listOfFiles = folder.listFiles();

        assert listOfFiles != null;
        for (File file : listOfFiles) {
            if (file.isFile() && file.getName().endsWith(SnakeCaseSuffix + ".json")) {
                allFiles.add(file.getName());
            }
        }

        return allFiles;
    }

    /*public void loadAll(String selectedBoard) {
        this.boardList = JSONtoList(selectedBoard + ".json", "position", "board");
        this.colorMap = JSONtoMap(selectedColors + ".json", "colors");
        this.chanceList = JSONtoList(selectedChance + ".json", "Number", "chance");
    }*/


    // A JSON reader which reads a JSON and parses it to a string,
    // which it then returns.
    public String read(String filename, String jsonType) {

        // String to return
        String objString = "";

        String path = "";
        if (filename.contains("/")) {
            System.out.println("File has already been found. Full path has been provided. ");
            path = filename;
        }
        else {
            // Defines the file and retrieves its canonical path
            File file = new File(path + filename);
            try {
                path = file.getCanonicalPath();
                System.out.println(path);
            } catch (IOException e) {
                System.out.println("No directory found");
            }
        }

        // Tries to parse the JSON to a Map<String, Map<String>, <String>
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {

            // Makes it possible to parse JSON
            JSONParser jsonParser = new JSONParser();

            // Creates an array of strings from an object created by the JsonParser
            // to be able to parse each field to the mapper.
            Object obj = jsonParser.parse(reader);

            // Parses the Object to a String
            objString = obj.toString();
            System.out.println("Successfully read: " + path);
        }
        // throws an exception
        // If the file doesn't exist in the canonical path ./json folder,
        // then it will try to retrieve the default JSON game files which can be found in the ressources.
        catch (IOException | ParseException ex) {
            try {
                switch (jsonType) {
                    case "settings" -> filename = "default_settings.json";
                    case "board" -> filename = "default_board.json";
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

        // TODO close reader
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
    private Map<String, String> JSONtoMap(String filename, String jsonType) {
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

    public ArrayList<String> getFoundBoardFileNames() {
        return this.boardFiles;
    }

    public ArrayList<String> getFoundColorFileNames() {
        return this.colorFiles;
    }

    public ArrayList<String> getFoundChanceFileNames() {
        return this.chanceFiles;
    }

    public void setBoard(String filename) {
        String outerMapKey = "position";
        this.boardList = JSONtoList(filename, outerMapKey, "board");
    }

    // returns the Mapped fields
    public ArrayList<Map<String,String>> getBoardList() {
        return boardList;
    }

    public Map<String, String> getColorMap(){return colorMap;}

    public ArrayList<Map<String,String>> getChanceList() {return chanceList; }

    public boolean saveGame(ArrayList<Map<String, String>> boardList, String saveLocation) {

        String path = "";
        File file = new File("./json/Saved Games/" + saveLocation);
        try {
            path = file.getCanonicalPath();
            System.out.println(path);
        } catch (IOException e) {
            System.out.println("No directory found. ");
            return false;
        }

        boolean saveSettingsSuccess = saveMap(path, this.settings, "settings");
        System.out.println("Success");
        boolean saveBoardSuccess = saveList(path, this.boardList, "position", "board");
        System.out.println("Success");
        boolean saveColorsSuccess = saveMap(path, this.colorMap, "colors");
        System.out.println("Success");
        boolean saveChanceListSuccess = saveList(path, this.chanceList, "Number", "chance");
        System.out.println("Success");
        if (saveSettingsSuccess && saveBoardSuccess && saveColorsSuccess && saveChanceListSuccess) {
            return true;
        }
        else { return false; }
    }

    private boolean saveMap(String path, Map<String, String> mapToJSON, String jsonType) {

        JSONArray jsonMap = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        for (String key : mapToJSON.keySet()) {
            jsonObject.put(key, mapToJSON.get(key));
        }
        jsonMap.add(jsonObject);

        try (FileWriter fieldFile = new FileWriter(path + "/_" + jsonType + ".json")) {
            fieldFile.write(jsonMap.toJSONString());
            fieldFile.flush();
        } catch (IOException ex) {
            System.out.println("Couldn't write to save file. ");
            return false;
        }
        return true;
    }

    private boolean saveList(String path, ArrayList<Map<String, String>> listToJSON, String outerMapKey, String jsonType) {
        JSONObject jsonBoard = new JSONObject();
        for (Map<String, String> field : listToJSON) {
            jsonBoard.put(field.get(outerMapKey), field);
        }

        try (FileWriter fieldFile = new FileWriter(path + "/_" + jsonType + ".json")) {
            fieldFile.write(jsonBoard.toJSONString());
            fieldFile.flush();
        } catch (IOException ex) {
            System.out.println("Couldn't write to save file. ");
            return false;
        }
        return true;
    }

    private void loadFromExistingGame(int loadGame) {

        String path = "";
        File file = new File("./json/Saved Games/" + loadGame + "/");
        try {
            path = file.getCanonicalPath();
            System.out.println(path);
        } catch (IOException e) {
            System.out.println("No directory found. ");
        }

        String settingsPath = getAllFilesOfType("_settings", path).get(0);
        String boardPath = getAllFilesOfType("_board", path).get(0);
        String colorsPath = (getAllFilesOfType("_colors", path).get(0));
        String chancePath = (getAllFilesOfType("_chance", path).get(0));

        this.settings = JSONtoMap(settingsPath, "settings");
        this.boardList = JSONtoList(boardPath, "position", "board");
        this.colorMap = JSONtoMap(colorsPath, "colors");
        this.chanceList = JSONtoList(chancePath, "Number", "chance");

    }
}