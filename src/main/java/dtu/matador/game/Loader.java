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
    ArrayList<String> defaultBoardsFilenames;
    ArrayList<Map<String, String>> boardList;
    Map<String, String> settings;
    Map<String, String> colorMap;
    ArrayList<Map<String, String>> chanceList;
    ArrayList<Map<String, String>> playersList;

    public Loader(int loadGame) {

        generateFolderStructure();
        if (loadGame == 0) {
            defaultBoardsFilenames = new ArrayList<>();
            defaultBoardsFilenames.addAll(Arrays.asList("1. Normal_board.json", "2. Den Lille Bane_board.json", "3. Kriminel Løbebane_board.json"));
            this.settings = JSONtoMap("settings.json", "settings");
            this.boardFiles = getAllFilesOfType("board");
            this.colorFiles = getAllFilesOfType("colors.json");
            this.chanceFiles = getAllFilesOfType("chance.json");
            this.boardList = new ArrayList<>();
            this.colorMap = JSONtoMap(this.settings.get("colors"), "colors");
            this.chanceList = JSONtoList(this.settings.get("chance"), "Number", "chance");
            this.playersList = new ArrayList<>();
        }
        else { loadFromExistingGame(loadGame); }
    }

    public void generateFolderStructure() {
        String[] paths = new String[] {"./json/Saved Games/1", "./json/Saved Games/2", "./json/Saved Games/3", "./json/Saved Games/4", "./json/Saved Games/5"};
        for (String path : paths) {
            File file = new File(path);
            if (!file.exists()) {
                System.out.println(path + " doesn't exist");
                if (file.mkdirs()) {
                    System.out.println(path + " Folder was created");
                } else {
                    System.out.println("A problem occurred while creating directory. ");
                }
            } else {
                System.out.println(path + " already exists");
            }
        }
    }

    public ArrayList<String> getAllFilesOfType(String SnakeCaseSuffix) {

        ArrayList<String> allFiles = new ArrayList<>();


        File folder = new File("./json");
        File[] listOfFiles = folder.listFiles();

        assert listOfFiles != null;
        for (File file : listOfFiles) {
            if (file.isFile() && file.getName().endsWith(SnakeCaseSuffix + ".json")) {
                allFiles.add(file.getName());
            }
        }

        if (SnakeCaseSuffix.equals("board")) {
            allFiles.addAll(defaultBoardsFilenames);
        }

        return allFiles;
    }


    // A JSON reader which reads a JSON and parses it to a string,
    // which it then returns.
    public String read(String filename, String jsonType) {

        // String to return
        String objString;
        String path = "";
        if (filename.contains("/")) {
            System.out.println("File should already have been found. Full path has been provided. ");
            path = filename;
            // Defines the file and retrieves its canonical path
            File file = new File(path);
            try {
                path = file.getCanonicalPath();
                System.out.println(path);
            } catch (IOException e) {
                System.out.println("No directory found");
            }
        }
        else {
            File file = new File("./json/" + filename);
            try {
                path = file.getCanonicalPath();
                System.out.println(path);
            } catch (IOException e) {
                System.out.println("No files found");
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
                if (jsonType.equals("board") && defaultBoardsFilenames.contains(filename)) {
                    System.out.println("Getting board from ressources...");
                }
                else {
                    System.out.println("Didn't successfully read. ");
                    switch (jsonType) {
                        case "settings" -> filename = "default_settings.json";
                        case "board" -> filename = "default_board.json";
                        case "chance" -> filename = "default_chance.json";
                        case "colors" -> filename = "default_colors.json";
                        // TODO add default players
                    }
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

    public ArrayList<Map<String,String>> getPlayersList() {return playersList; }

    public boolean saveGame(ArrayList<Map<String, String>> boardList, ArrayList<Map<String, String>> playersList, String saveLocation) {

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
        boolean saveBoardSuccess = saveList(path, boardList, "board");
        System.out.println("Success");
        boolean saveColorsSuccess = saveMap(path, this.colorMap, "colors");
        System.out.println("Success");
        boolean saveChanceListSuccess = saveList(path, this.chanceList, "chance");
        System.out.println("Success");
        boolean savePlayersMapSuccess = saveList(path, playersList, "players");
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

    private boolean saveList(String path, ArrayList<Map<String, String>> listToJSON, String jsonType) {

        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < listToJSON.size(); i++) {
            Map<String, String> innerMap = listToJSON.get(i);
            JSONObject jsonObject = new JSONObject();
            for (String key : innerMap.keySet()) {
                jsonObject.put(key, innerMap.get(key));
            }
            jsonArray.add(jsonObject);
        }

        try (FileWriter fieldFile = new FileWriter(path + "/_" + jsonType + ".json")) {
            fieldFile.write(jsonArray.toJSONString());
            fieldFile.flush();
        } catch (IOException ex) {
            System.out.println("Couldn't write to save file. ");
            return false;
        }
        return true;
    }

    private void loadFromExistingGame(int loadGame) {
        this.playersList = new ArrayList<>();

        String path = "";
        File file = new File("./json/Saved Games/" + loadGame);
        try {
            path = file.getCanonicalPath();
            System.out.println(path);
        } catch (IOException e) {
            System.out.println("No directory found. ");
        }

        System.out.println("./json/Saved Games/" + loadGame + "/_settings.json");
        System.out.println("./json/Saved Games/" + loadGame + "/_settings.json");
        this.settings = JSONtoMap("./json/Saved Games/" + loadGame + "/_settings.json", "settings");
        this.boardList = JSONtoList("./json/Saved Games/" + loadGame + "/_board.json", "position", "board");
        this.colorMap = JSONtoMap("./json/Saved Games/" + loadGame + "/_colors.json", "colors");
        this.chanceList = JSONtoList("./json/Saved Games/" + loadGame + "/_chance.json", "Number", "chance");
        this.playersList = JSONtoList("./json/Saved Games/" + loadGame + "/_players.json", "id", "players");

    }
}