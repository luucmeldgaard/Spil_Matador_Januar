package dtu.matador.game;

import org.junit.Test;

import static org.junit.Assert.*;

public class FieldControllerTest {

    @Test
    public void testArray() {
        FieldController fieldController = new FieldController("fieldSpaces");

        fieldController.setupFields();

    }

}