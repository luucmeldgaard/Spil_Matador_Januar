package dtu.matador.game;

import org.junit.Test;

import static org.junit.Assert.*;

public class FieldControllerTest {

    @Test
    public void testArray() {
        FieldController fieldController = new FieldController("fieldSpaces");
        fieldController.setupFields();
        int numberOfFields = fieldController.getFieldMap().size();
        for (int i = 0; i < numberOfFields; i++) {
            System.out.println(fieldController.getField(i).getName());
        }
        for (int i = 0; i < numberOfFields; i++) {
            System.out.println(fieldController.getField(i).getColor1());
        }

    }

}