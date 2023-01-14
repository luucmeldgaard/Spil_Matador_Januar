package dtu.matador.game;

import static org.junit.Assert.*;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Ignore;
import org.junit.Test;
import java.awt.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class FieldLoaderTest {

    @Test
    public void jsonCanBeReadFrom() {
        JSONParser jsonParser = new JSONParser();

        // tries to read from a test JSON file and converts it to a string
        try (FileReader fieldFileReader = new FileReader("test_FieldData" + ".json")) {
            Object obj = jsonParser.parse(fieldFileReader);
            String objString = obj.toString();
            assertNotNull(objString);
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void getFieldMapReturnsArrayList() {
        FieldLoader fieldLoader = new FieldLoader("test_FieldData");

        ArrayList<Map<String, String>> fieldList;
        fieldList = fieldLoader.getFieldList();

        assertEquals(java.util.ArrayList.class, fieldList.getClass());
    }

    @Ignore
    @Test
    public void prettyPrintGetFieldMap() {

        FieldLoader fieldLoader = new FieldLoader("test_FieldData");
        ArrayList<Map<String, String>> testList;
        testList = fieldLoader.getFieldList();

        for (Map<String, String> field : testList) {
            System.out.println("_____field_____");
            for (String key : field.keySet()) {
                System.out.println(key + ": " + field.get(key));
            }
        }

    }

    @Ignore
    @Test
    public void PrintGetFieldMap() {

        FieldLoader fieldLoader = new FieldLoader("test_FieldData");
        ArrayList<Map<String, String>> testList;
        testList = fieldLoader.getFieldList();

        String testMapString = fieldLoader.getFieldList().toString();

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

    @Test
    public void testColorsSetSystemProperty() {
        FieldLoader fieldLoader = new FieldLoader("test_FieldData");
        Map<String, String> colors = fieldLoader.getColorMap();

        for (String color : colors.keySet()) {
            assertNotNull(Color.getColor(color));
        }
    }

    @Test
    public void testChanceMapIsCreated() {
        FieldLoader fieldLoader = new FieldLoader("test_FieldData");
        assertNotNull(fieldLoader.getChanceList());
    }

    @Test
    public void testColorMapIsCreated() {
        FieldLoader fieldLoader = new FieldLoader("test_FieldData");
        assertNotNull(fieldLoader.getColorMap());
    }

}