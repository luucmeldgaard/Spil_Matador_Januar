package dtu.matador.game;

import static org.junit.Assert.*;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Ignore;
import org.junit.Test;

import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.Map;

public class FieldLoaderTest {

    @Test
    public void jsonCanBeReadFrom() {
        JSONParser jsonParser = new JSONParser();

        // tries to read from a test JSON file and converts it to a string
        try (FileReader fieldFileReader = new FileReader("test_fieldSpaces" + ".json")) {
            Object obj = jsonParser.parse(fieldFileReader);
            String objString = obj.toString();
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void getFieldMapReturnsHashmap() {
        FieldLoader fieldLoader = new FieldLoader("test_fieldSpaces");

        Map<String, Map<String, String>> testMap;
        testMap = fieldLoader.getFieldMap();

        assertEquals(java.util.HashMap.class, testMap.getClass());
    }

    @Ignore
    @Test
    public void prettyPrintGetFieldMap() {

        FieldLoader fieldLoader = new FieldLoader("test_fieldSpaces");
        Map<String, Map<String, String>> testMap;
        testMap = fieldLoader.getFieldMap();

        for (Map<String, String> field : testMap.values()) {
            System.out.println("_____field_____");
            for (String key : field.keySet()) {
                System.out.println(key + ": " + field.get(key));
            }
        }

    }

    @Ignore
    @Test
    public void PrintGetFieldMap() {

        FieldLoader fieldLoader = new FieldLoader("test_fieldSpaces");
        Map<String, Map<String, String>> testMap;
        testMap = fieldLoader.getFieldMap();

        String testMapString = fieldLoader.getFieldMap().toString();

        char lastLetter = ' ';
        for (int i = 0; i < testMapString.length(); i++) {
            char currentLetter = testMapString.charAt(i);
            System.out.print(currentLetter);
            if (currentLetter == ',' && lastLetter == '}') {
                System.out.println();
            }
            lastLetter = currentLetter;
        }

    }

}