package dtu.matador.game;

import dtu.matador.game.fields.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static dtu.matador.game.GUIController.currentGUIPlayer;
import static dtu.matador.game.GameState.getStateInstance;
import static dtu.matador.game.GUIController.getGUIInstance;

public class FieldController {

    ArrayList<FieldSpaces> fields;
    FieldLoader fieldLoader;
    Map<String, Map<String, String>> fieldMap;
    FieldSpaces currentField;
    static GUIController gui;
    static GameState currentGameState = getStateInstance();



    public FieldController(String selectedBoard) {
        System.out.println(currentGameState.tester());
        fieldLoader = new FieldLoader(selectedBoard);
        fieldMap = fieldLoader.getFieldMap();

        fields = new ArrayList<>();
        for (int i = 0; i < fieldMap.size(); i++) {
            fields.add(null);
        }
        setupFields();
    }

    public FieldController(){
    }

    public void setGUI() {
        gui = getGUIInstance();
    }

    public void setupFields() {
        for (Map<String, String> field : fieldMap.values()) {
            int fieldPosition = Integer.parseInt(field.get("position"));
            switch (field.get("fieldType")) {
                case "property" -> {
                    fields.set(fieldPosition, new Street(field.get("title"), field.get("subtext"), field.get("subtext"), field.get("rent"),
                            field.get("color1"), field.get("color2"), field.get("price"), field.get("pawnForAmount"), field.get("position"), field.get("owner")));
                }
                case "ferry" -> {
                    fields.set(fieldPosition, new Ferry(field.get("title"), field.get("subtext"), field.get("subtext"), field.get("rent"),
                            field.get("color1"), field.get("color2"), field.get("price"), field.get("pawnForAmount"), field.get("position"), field.get("owner")));
                }
                case "brewery" -> {
                    fields.set(fieldPosition, new Brewery(field.get("title"), field.get("subtext"), field.get("subtext"), field.get("rent"),
                            field.get("color1"), field.get("color2"), field.get("price"), field.get("pawnForAmount"), field.get("position"), field.get("owner")));
                }
                case "refuge" -> {
                    fields.set(fieldPosition, new Refuge(field.get("title"), field.get("subtext"), field.get("subtext"), field.get("rent"),
                            field.get("color1"), field.get("color2"), field.get("price"), field.get("pawnForAmount"), field.get("position"), field.get("owner")));
                }
            }
        }
    }
    public FieldSpaces getField(int position) {
        return fields.get(position);
    }

    public ArrayList<FieldSpaces> getFieldsArray() {
        return fields;
    }

    public Map<String, Map<String, String>> getFieldMap() {
        return fieldMap;
    }

    public Map<String, String> getFieldAsMap(int position) {
        String pos = String.valueOf(position);
        return fieldMap.get(pos);
    }

    // overwrites chosen values of a specific field in the fieldMap
    public void updateFieldMap(int position) {
        System.out.println(fieldMap.get(String.valueOf(position)));
        Map<String, String> updatedFieldInfo = new HashMap<>();
        updatedFieldInfo = ((PropertyFields) fields.get(position)).updateGuiField();
        for (String key : updatedFieldInfo.keySet()) {
            //fieldMap.get(String.valueOf(position)).remove(key);
            fieldMap.get(String.valueOf(position)).replace(key, updatedFieldInfo.get(key));
        }
    }

    public void landOnField(String playerID, int position) {
        // Check for type of field
        currentField = fields.get(position);
        // Redirects to landOn "fieldType"
        if (currentField instanceof Property) {
            landOnProperty(playerID, ((Property) currentField));
        }
        if (currentField instanceof Jail) {
            landOnJail(playerID, ((Jail) currentField));
        }
        // property, chance, jail, etc.
    }

    private void landOnJail(String playerID, Jail currentField) {

    }

    public void landOnProperty(String playerID, Property property) {
        System.out.println(currentGameState.tester());
        String owner = property.getOwner();
        if (owner == null){
            System.out.println("This field is not owned by anyone!");
            String choice = gui.buttonRequest("Buy or auction?", "Buy", "Auction");
            if (choice.equals("Buy")) {
                property.buy(playerID);
            }
            else if (choice.equals("Auction")){
                property.auction(playerID);
            }

        }
        // Redirect to type of Property
        else if (currentField instanceof Street) {
            landOnStreet(playerID, ((Street) currentField));
        }
        else if (currentField instanceof Brewery) {
            landOnUtility(playerID, ((Brewery) currentField));
        }
        else if (currentField instanceof Ferry) {
            landOnFerry(playerID, ((Ferry) currentField));
        }
        // Street, Utility, Ferry, etc.
    }

    private void landOnFerry(String playerID, Ferry currentField) {
    }

    private void landOnUtility(String playerID, Brewery currentField) {
    }

    public void landOnStreet(String playerID, Street street) {
        // Check for ownership
        // if ownership = null, owned(other playerID), owned(own playerID)
        String owner = street.getOwner();
        System.out.println(owner);

        if (owner.equals(playerID)) {
            System.out.println("This field is owned by you. ");
        }
        else {
            System.out.println("This field is owned by someone else!");
        }

        // if owned(own playerID)
        // Options: Build, Pledge, Sell housing

        // if owned(other playerID)
        // pay rent

        // if null
        // Buy, Auction
    }


    public void buy() {
    }

    public void landOnChance() {
        //
    }

    public boolean createTransaction(String playerID, String receiverID, int price, boolean critical) {
        if (receiverID != null) {
            String message = "The player will have to pay the owner a rent of " + price;
            gui.buttonRequest(message, "Pay rent");
            int receiverBalanceChange = currentGameState.getPlayerFromID(receiverID).getBalance();
            gui.updateGUIPlayerBalance(receiverID, receiverBalanceChange);
        } else {
            String message = "The player will lose " + price;
            String userRequest = gui.buttonRequest(message, "Pay", "Cancel");
            if (userRequest.equals("Cancel")) {
                return false;
            }
        }
        int playerBalanceChange = currentGameState.getPlayerFromID(playerID).getBalance();
        gui.updateGUIPlayerBalance(playerID, playerBalanceChange);

        return currentGameState.handleTransaction(playerID, receiverID, price, critical);

    }




    public void insufficientFunds() {
        gui.buttonRequest("You have insufficient funds. ", "Ok");
    }

}
