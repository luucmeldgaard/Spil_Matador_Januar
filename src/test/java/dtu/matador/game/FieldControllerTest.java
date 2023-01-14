package dtu.matador.game;

import org.junit.Assert;
import org.junit.Assert.*;
import org.junit.Test;
import static org.mockito.Mockito.*;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class FieldControllerTest {

    @Test
    public void testMapAndFieldsAreOfEqualSize() {
        // Creates a new instance of the FieldController
        FieldController fieldController = new FieldController(null, new GUIController("test_FieldData"), "test_FieldData");

        // asserts that the size of the fieldMap and
        // the fields ArrayList, are the same
        int fieldMapSize = fieldController.getFieldMap().size();
        int fieldsSize = fieldController.getFieldsArray().size();
        assertEquals(fieldMapSize, fieldsSize);
    }

    @Test
    public void testPropertyFieldUpdatesFieldMap() {

        // Creates a new instance of the FieldController
        FieldController fieldController = new FieldController(null, new GUIController("test_FieldData"), "test_FieldData");

        // Casts a known property field from the ArrayList
        // of Fieldspaces objects to Property
        Property field = ((Property) fieldController.getField(1));

        // Retrieves the property field's position
        int fieldPosition = field.getPosition();

        // sets the property field's Color2 field to a new String color
        field.setColor2("test_myBlue");

        // Updates the fieldMap with the changed property field
        fieldController.updateFieldMap(field);

        // Retrieves the field in the fieldMap and asserts that the color
        // is the same in the fieldMap and the property field.
        Map<String, String> fieldAsMap = fieldController.getFieldAsMap(fieldPosition);
        assertEquals(field.getColor2(), fieldAsMap.get("color2"));
    }

    @Test
    public void testCreateTransactionPositive() {

        // creates a mock object of GUIController
        GUIController gui = mock(GUIController.class);

        // Configures the mock object to return something else
        // when buttonRequest is called
        when(gui.buttonRequest("", "")).thenReturn("Pay");

        // Creates a player using the PlayerController class
        PlayerController playerController = new PlayerController();
        String name = "Povl";
        String chosenColor = "test_myBlue";
        int position = 25;
        int balance = 12345;

        playerController.addPlayer(name, chosenColor, position, balance);

        // Retrieves player ID
        Player player = playerController.getPlayerFromName(name);
        String playerID = player.getId();

        // Creates a new instance of the FieldController where the mock object
        // is one of the dependency injections (GUIController)
        FieldController fieldController = new FieldController(playerController, gui, "test_FieldData");

        // Creates a transaction and asserts
        // that the transaction was successful
        int amountToReceive = 5000;
        String beneficiary = null;
        boolean critical = false;

        boolean result = fieldController.createTransaction(playerID, beneficiary, amountToReceive, critical, "");
        assertTrue(result);

        // asserts that the amountToReceive was added to the player's balance
        int expectedNewPlayerBalance = balance + amountToReceive;
        assertEquals(player.getBalance(), expectedNewPlayerBalance);
    }

    @Test
    public void testCreateTransactionNegativeAndCritical() {

        // creates a mock object of GUIController
        GUIController gui = mock(GUIController.class);

        // Configures the mock object to return something else
        // when buttonRequest is called
        when(gui.buttonRequest("", "")).thenReturn("Pay");

        // Creates a player using the PlayerController class
        PlayerController playerController = new PlayerController();
        String name = "Povl";
        String chosenColor = "test_myBlue";
        int position = 25;
        int balance = 12345;

        playerController.addPlayer(name, chosenColor, position, balance);

        // Retrieves player ID
        Player player = playerController.getPlayerFromName(name);
        String playerID = player.getId();

        // Creates a new instance of the FieldController where the mock object
        // is one of the dependency injections (GUIController)
        FieldController fieldController = new FieldController(playerController, gui, "test_FieldData");

        // Creates a transaction and asserts that the transaction failed
        int amountToReceive = -50000;
        String beneficiary = null;
        boolean critical = true;
        String message = "";

        boolean result = fieldController.createTransaction(playerID, beneficiary, amountToReceive, critical, message);
        assertFalse(result);

        // Checks if the player has been removed from the PlayerController
        // which would indicate that the boolean statement 'critical' worked
        assertNull(playerController.getPlayerFromName(name));
    }

}