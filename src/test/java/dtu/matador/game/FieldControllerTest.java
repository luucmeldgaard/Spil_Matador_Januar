package dtu.matador.game;

import org.junit.Assert;
import org.junit.Test;

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

    @Test
    public void updateFieldZeroWithNewColor() {
        FieldController fieldController = new FieldController("test_fieldSpaces");
        fieldController.setupFields();
        fieldController.getFieldAsMap(0);
        FieldSpaces field = fieldController.getField(0);
        ((PropertyFields) fieldController.getField(0)).setColor2("test_red");
        //fieldController.updateFieldMap(0);
        Assert.assertEquals(field.getColor2(), fieldController.getFieldAsMap(0).get("color2"));
    }

}