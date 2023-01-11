package dtu.matador.game;

import dtu.matador.game.fields.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static dtu.matador.game.GUIController.currentGUIPlayer;
import static dtu.matador.game.GUIController.getGUIInstance;

public class FieldController {

    ArrayList<FieldSpaces> fields;
    FieldLoader fieldLoader;
    Map<String, Map<String, String>> fieldMap;
    FieldSpaces currentField;
    static GUIController gui;
    GameState currentGameState = new GameState();


    public FieldController(String selectedBoard) {
        fieldLoader = new FieldLoader(selectedBoard);
        fieldMap = fieldLoader.getFieldMap();

        fields = new ArrayList<>();
        for (int i = 0; i < fieldMap.size(); i++) {
            fields.add(null);
        }
        setupFields();
    }

    public FieldController() {
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
                                                         field.get("color1"), field.get("color2"), field.get("price"), field.get("pawnForAmount"), field.get("position"), field.get("owner"), field.get("housing")));
                }
                case "ferry" -> {
                    fields.set(fieldPosition, new Ferry(field.get("title"), field.get("subtext"), field.get("subtext"), field.get("rent"),
                            field.get("color1"),        field.get("color2"), field.get("price"), field.get("pawnForAmount"), field.get("position"), field.get("owner")));
                }
                case "brewery" -> {
                    fields.set(fieldPosition, new Brewery(field.get("title"), field.get("subtext"), field.get("subtext"), field.get("rent"),
                            field.get("color1"),          field.get("color2"), field.get("price"), field.get("pawnForAmount"), field.get("position"), field.get("owner")));
                }
                case "refuge" -> {
                    fields.set(fieldPosition, new Refuge(field.get("title"), field.get("subtext"), field.get("subtext"),
                                                         field.get("color1"), field.get("color2"), field.get("position")));
                }
                case "start" -> {
                    fields.set(fieldPosition, new StartField(field.get("title"), field.get("subtext"), field.get("subtext"),
                                                             field.get("color1"), field.get("color2"), field.get("position"), field.get("income")));
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
    public void updateFieldMap(Property property) {
        Map<String, String> updatedFieldInfo = new HashMap<>();

        String fieldPosition = String.valueOf(property.getPosition());
        String color2 = property.getColor2();
        String housing = null;
        fieldMap.get(fieldPosition).replace("color2", color2);
        if (property instanceof Street) {
            housing = String.valueOf(((Street) property).getHousing());
            fieldMap.get(fieldPosition).replace("housing", housing);
        }
    }

    public void landOnField(String playerID, int position) {
        // Check for type of field
        currentField = fields.get(position);
        // Redirects to landOn "fieldType"
        if (currentField instanceof Property) {
            landOnProperty(playerID, ((Property) currentField));
        }
        else if (currentField instanceof Jail) {
            landOnJail(playerID, ((Jail) currentField));
        }
        else if (currentField instanceof StartField) {
            landOnStart(playerID, ((StartField) currentField));
        }
        // property, chance, jail, etc.
    }

    private void landOnJail(String playerID, Jail currentField) {

    }

    public void landOnStart(String playerID, StartField start) {
        int income = start.getIncome();
        createTransaction(playerID, null, income, false);
    }

    public void landOnProperty(String playerID, Property property) {
        String owner = property.getOwner();
        if (owner == null) {
            System.out.println("This field is not owned by anyone!");
            String choice = gui.buttonRequest("Buy or auction?", "Buy", "Auction");
            if (choice.equals("Buy")) {
                property.buy(playerID);
                if (property.getOwner().equals(playerID)) {
                    updateFieldMap(property);
                    updateGUI(property, playerID);
                }
            } else if (choice.equals("Auction")) {
                property.auction(playerID);
            }

        }
        // Redirect to type of Property
        else if (currentField instanceof Street) {
            landOnStreet(playerID, ((Street) currentField));
        } else if (currentField instanceof Brewery) {
            landOnUtility(playerID, ((Brewery) currentField));
        } else if (currentField instanceof Ferry) {
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
        if (owner == null) return;
        System.out.println(owner);
        Player player = currentGameState.getPlayerFromID(playerID);
        int balance = currentGameState.getPlayerFromID(playerID).getBalance();

        //If the field is owned by the current player, they have the choice to buy a house if they have sufficient funds
        if (owner.equals(playerID)) {
            System.out.println("This field is owned by you. ");

            //checks if the owner has sufficient funds to buy a house
            if (balance >= street.getBuildPrice()) {
                int nextBuildPrice = street.getBuildPrice();
                String response = gui.buttonRequest("Do you want to buy a house for " + nextBuildPrice + "?","Buy","No");
                if (response.equals("Buy")) {
                    boolean transactionSuccess = createTransaction(playerID, null, nextBuildPrice, false);
                    if (transactionSuccess) {
                        street.buildHouse();
                        updateGUI(((Property) street), playerID);
                        updateFieldMap(((Property) street));

                    }
                }
            }
            else {
                // player does not care about housing because he doesn't have the money for it.
            }
        }
        else {
            System.out.println("This field is owned by someone else!");
            street.getRent();
            street.getOwner();

            // if owned(own playerID)
            // Options: Build, Pledge, Sell housing

            // if owned(other playerID)
            // pay rent

            // if null
            // Buy, Auction
        }

    }

    public void buy () {
        //
    }

    public void landOnChance () {
        //
    }

    /**
     *
     * @param playerID
     * @param receiverID
     * @param price
     * @param critical
     * @return
     */
    public boolean createTransaction (String playerID, String receiverID,int price, boolean critical) {
        boolean transactionSuccess = false;
        String userRequest;
        String message;
        if (receiverID != null) {
            message = "The player will have to pay the owner a rent of " + price;
            gui.buttonRequest(message, "Pay rent");
            transactionSuccess = currentGameState.handleTransaction(playerID, receiverID, price, critical);
            int receiverBalanceChange = currentGameState.getPlayerFromID(receiverID).getBalance();
            gui.updateGUIPlayerBalance(receiverID, receiverBalanceChange);
        }
        else {
            if (price <= 0) {
                message = "The player will lose " + price;
                userRequest = gui.buttonRequest(message, "Pay", "Cancel");
                if (userRequest.equals("Cancel")) {
                    return false;
                }
                }
            else {
                message = "You recieved " + price;
                userRequest = gui.buttonRequest(message, "Ok");
            }
        }
        transactionSuccess = currentGameState.handleTransaction(playerID, receiverID, price, critical);
        int playerBalanceChange = currentGameState.getPlayerFromID(playerID).getBalance();
        gui.updateGUIPlayerBalance(playerID, playerBalanceChange);

        return transactionSuccess;

    }


    public void insufficientFunds () {
        gui.buttonRequest("You have insufficient funds. ", "Ok");
    }

    public void updateGUI(Property property, String playerID) {
        String playerColor = currentGameState.getPlayerFromID(playerID).getColor();
        if (property instanceof Street) {
            gui.updateProperty(property.getPosition(), playerColor, ((Street) property).getHousing());
        }
        else {
            gui.updateProperty(property.getPosition(), playerColor);
        }
    }

}
