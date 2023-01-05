package dtu.matador.game;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FieldLoader {

    Map<String, String> boardMap;
    Map<Integer, String> positionalMap;


    public FieldLoader(String selectedBoard) {

        this.boardMap = new HashMap<>();
        this.positionalMap = new HashMap<>();

        this.boardMap = JSONtoMapBoard(selectedBoard + ".json");

    }
    private Map<String, String> JSONtoMapBoard(String filename) {

        Map<String, String> JSONParsedMap = new HashMap<>();

        // Get information from JSON and convert to a readable Map

        JSONParser jsonParser = new JSONParser();
        try (FileReader fieldFileReader = new FileReader(filename)) {
            Object obj = jsonParser.parse(fieldFileReader);

            String objData = obj.toString();
            String[] rawObjDataArray = objData.split("}");

            String fieldString;
            int iteration = 0;
            String[] objDataArray = new String[rawObjDataArray.length];

            for (String fieldObject : rawObjDataArray) {
                fieldString = fieldObject;
                fieldString = fieldString.replace("[", "");
                fieldString = fieldString.replace("{\"board\":[", "");
                fieldString = fieldString.replace("]", "");
                objDataArray[iteration] = fieldString;
                iteration += 1;
            }

            for (int i = 0; i < objDataArray.length; i++) {
                System.out.println(objDataArray[i]);
            }

        }
        catch (IOException | ParseException ex) {
            throw new RuntimeException(ex);
        }

        return JSONParsedMap;
    }


}
