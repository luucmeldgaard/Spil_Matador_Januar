package dtu.matador.game;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class FieldControllerTest {

    @Test
    public void testArray() {
        FieldController fieldController = new FieldController("test_fieldSpaces");
        fieldController.setupFields();
        int numberOfFields = fieldController.getFieldsArray().size();
        for (int i = 0; i < numberOfFields; i++) {
            FieldSpaces field = fieldController.getField(i);
            System.out.println(field.getName());
        }

    }

}