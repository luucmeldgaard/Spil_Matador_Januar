package dtu.matador.game;

import org.junit.Test;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class FieldControllerTest {

    @Test
    public void testMapAndFieldsAreOfEqualSize() {
        // creates a mock object of GUIController
        GUIController gui = mock(GUIController.class);

        // Creates a new instance of PlayerController
        PlayerController playerController = new PlayerController();

        //Creates a new instance of a mock loader
        Loader loader = new Loader(0);


        // Creates a new instance of the FieldController
        FieldController fieldController = new FieldController(playerController, gui, loader.getBoardList(), loader.getChanceList());

        // asserts that the size of the fieldMap and
        // the fields ArrayList, are the same
        int fieldMapSize = fieldController.getFieldList().size();
        int fieldsSize = fieldController.getAllFieldSpaces().size();
        assertEquals(fieldMapSize, fieldsSize);
    }

    @Test
    public void testPropertyFieldUpdatesFieldMap() {

        GUIController gui = mock(GUIController.class);


        // Creates a new instance of PlayerController
        PlayerController playerController = new PlayerController();

        //Creates a new instance of a mock loader
        //Issue is that the size getfield returns
        //fields.get(position)
        //the size fields is defined by iteratively adding null spaces until there are fieldlist.size null values
        //fieldlist is only given a size > 0 in fieldController (the constructor for FieldController)
        //I have all the knowledge of the issue and none of the knowledge on how to fix the issue
        Loader loader = new Loader(0);

        // Creates a new instance of the FieldController
        FieldController fieldController = new FieldController(playerController, gui, loader.getBoardList(), loader.getChanceList());
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

        //creates a mock for selectedBoardArray and selectedChanceArray
        ArrayList<Map<String, String>> mockSelectedBoardArray = new ArrayList<>();
        ArrayList<Map<String, String>> mockSelectedChanceArray = new ArrayList<>();
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
        FieldController fieldController = new FieldController(playerController, gui, mockSelectedBoardArray, mockSelectedChanceArray);

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

        //Creates a new instance of a mock loader
        Loader loader = new Loader(0);


        // Creates a new instance of the FieldController
        FieldController fieldController = new FieldController(playerController, gui, loader.getBoardList(), loader.getChanceList());

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