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

public class LoaderTest {

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
        Loader loader = new Loader(0);

        ArrayList<Map<String, String>> fieldList;
        fieldList = loader.getBoardList();

        assertEquals(java.util.ArrayList.class, fieldList.getClass());
    }

    @Ignore
    @Test
    public void prettyPrintGetFieldMap() {

        Loader loader = new Loader(0);
        ArrayList<Map<String, String>> testList;
        testList = loader.getBoardList();

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

        Loader loader = new Loader(0);
        ArrayList<Map<String, String>> testList;
        testList = loader.getBoardList();

        String testMapString = loader.getBoardList().toString();

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
        Loader loader = new Loader(0);
        Map<String, String> colors = loader.getColorMap();

        for (String color : colors.keySet()) {
            assertNotNull(Color.getColor(color));
        }
    }

    @Test
    public void testChanceMapIsCreated() {
        Loader loader = new Loader(0);
        assertNotNull(loader.getChanceList());
    }

    @Test
    public void testColorMapIsCreated() {
        Loader loader = new Loader(0);
        assertNotNull(loader.getColorMap());
    }

}