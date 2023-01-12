package dtu.matador.game;

import dtu.matador.game.fields.*;
import gui_fields.GUI_Street;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static dtu.matador.game.GUIController.*;

public class FieldController {

    ArrayList<FieldSpaces> fields;
    FieldLoader fieldLoader;
    Map<String, Map<String, String>> fieldMap;
    Map<String, Map<String, String>> chanceMap;
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

        chanceMap = fieldLoader.getChanceMap();

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
                    fields.set(fieldPosition, new Street(field.get("title"), field.get("subtext"), field.get("subtext"), field.get("rent"), field.get("rent1"),field.get("rent2"),
                            field.get("rent3"), field.get("rent4"), field.get("rent5"),field.get("color1"), field.get("color2"), field.get("price"), field.get("pawnForAmount"),
                            field.get("position"), field.get("owner"), field.get("housing")));
                }
                case "ferry" -> {
                    fields.set(fieldPosition, new Ferry(field.get("title"), field.get("subtext"), field.get("subtext"), field.get("rent"), field.get("rent"),field.get("rent"),
                            field.get("rent"), field.get("rent"), field.get("rent"),field.get("color1"), field.get("color2"), field.get("price"), field.get("pawnForAmount"),
                            field.get("position"), field.get("owner")));
                }
                case "brewery" -> {
                    fields.set(fieldPosition, new Brewery(field.get("title"), field.get("subtext"), field.get("subtext"), field.get("rent"), field.get("rent"),field.get("rent"),
                            field.get("rent"), field.get("rent"), field.get("rent"), field.get("color1"), field.get("color2"), field.get("price"), field.get("pawnForAmount"),
                            field.get("position"), field.get("owner")));
                }
                case "refuge" -> {
                    fields.set(fieldPosition, new Refuge(field.get("title"), field.get("subtext"), field.get("subtext"), field.get("color1"), field.get("color2"),
                            field.get("position")));
                }
                case "start" -> {
                    fields.set(fieldPosition, new StartField(field.get("title"), field.get("subtext"), field.get("subtext"), field.get("color1"), field.get("color2"),
                            field.get("position"), field.get("income")));
                }
                case "chance" -> {
                    fields.set(fieldPosition, new Chance(field.get("title"), field.get("subtext"), field.get("subtext"), field.get("color1"), field.get("color2"),
                            field.get("position")));
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
    public void landOnField(String playerID, int startPosition, int currentPosition) {
        if (startPosition > currentPosition) {
            System.out.println("Player passed start");
                landOnStart(playerID, ((StartField) fields.get(0)));
            }
        // Check for type of field
        currentField = fields.get(currentPosition);
        // Redirects to landOn "fieldType"
        if (currentField instanceof Property) {
            landOnProperty(playerID, ((Property) currentField));
        }
        else if (currentField instanceof Jail) {
            landOnJail(playerID, ((Jail) currentField));
        }
        else if (currentField instanceof StartField) {
            //landOnStart(playerID, ((StartField) currentField));
        }
        else if (currentField instanceof Chance) {
            landOnChance(playerID, ((Chance) currentField));
        }
        // property, chance, jail, etc.
    }

    private void landOnJail(String playerID, Jail currentField) {

    }

    public void landOnStart(String playerID, StartField start) {
        int income = start.getIncome();
        String message = "You passed start, gain " + income + "!";
        createTransaction(playerID, null, income, false, message);
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
                if (street.housing < 5) {
                    String response = gui.buttonRequest("Do you want to buy a house for " + nextBuildPrice + "?", "Buy", "No");
                    if (response.equals("Buy")) {
                        boolean transactionSuccess = createTransaction(playerID, null, nextBuildPrice, false, "Buying house for " + street.getBuildPrice());
                        if (transactionSuccess) {
                            street.buildHouse();
                            updateGUI(((Property) street), playerID);
                            updateFieldMap(((Property) street));

                        }
                    }
                }
            }
            // player does not care about housing because he doesn't have the money for it.

        }
        else {
            String message = "This field is owned by someone else!";
            switch (street.housing) {
                case 0 -> {
                    int rent = street.getRent();
                    System.out.println(rent);
                    String receiverID = street.getOwner();

                    createTransaction(playerID, receiverID, rent, true, message);
                }
                case 1 -> {
                    int rent = street.getRent1();
                    System.out.println(rent);
                    String receiverID = street.getOwner();

                    createTransaction(playerID, receiverID, rent, true, message);
                }
                case 2 -> {
                    int rent = street.getRent2();
                    System.out.println(rent);
                    String receiverID = street.getOwner();

                    createTransaction(playerID, receiverID, rent, true, message);
                }
                case 3 -> {
                    int rent = street.getRent3();
                    System.out.println(rent);
                    String receiverID = street.getOwner();

                    createTransaction(playerID, receiverID, rent, true, message);
                }
                case 4 -> {
                    int rent = street.getRent4();
                    System.out.println(rent);
                    String receiverID = street.getOwner();

                    createTransaction(playerID, receiverID, rent, true, message);
                }
                case 5 -> {
                    int rent = street.getRent5();
                    System.out.println(rent);
                    String receiverID = street.getOwner();

                    createTransaction(playerID, receiverID, rent, true, message);
                }
            }
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

    public void landOnChance (String playerID, Chance chance) {
        Random rand = new Random();
        String cardNumber = String.valueOf(rand.nextInt(0, chanceMap.size() - 1));
        Map <String, String> rawCard = chanceMap.get(cardNumber);
        Map <String, String> card = new HashMap<>();
        for (String key : rawCard.keySet()) {
            if (!rawCard.get(key).equals("")) {
                card.put(key, rawCard.get(key));
            }
        }
        for (String key : card.keySet()) {
            System.out.println(key + ": " + card.get(key));
            String message = card.get("Text");
            switch (key) {
                case "Cash added" -> {
                    System.out.println("Cash added");
                    int amount = Integer.parseInt(card.get(key));
                    createTransaction(playerID, null, amount, true, message);
                }
                case "CashAddedPerHouse" -> {
                    System.out.println("CashAddedPerHouse");
                    // TODO the player has an amount of houses. For the number of houses,
                    // TODO the player needs to pay/receive an amount of money
                }
                case "CashAddedPerHotel" -> {
                    System.out.println("CashAddedPerHotel");
                    // TODO the player has an amount of hotels. For the number of hotels,
                    // TODO the player needs to pay/receive an amount of money
                }
                case "CashTakenFromPlayers" -> {
                    System.out.println("CashTakenFromPlayers");
                    ArrayList<String> allPlayerIDs = currentGameState.getAllPlayerIDs();
                    int amount = Integer.parseInt(card.get(key));
                    for (String id : allPlayerIDs) {
                        if (!id.equals(playerID)) {
                            createTransaction(id, playerID, amount, true, message);
                        }
                    }
                }
                case "MoveBy" -> {
                    System.out.println("MoveBy");
                    // TODO make player move
                }
                case "MoveToType" -> {
                    System.out.println("MoveToType");
                    // TODO make player move to the next instance of a specific field type
                }
                case "MoveTo" -> {
                    System.out.println("MoveTo");
                    // TODO make player move to specific field
                }
                case "PayIfCrossStart" -> {
                    System.out.println("PayIfCrossStart");
                    // TODO boolean statement that runs landOnStart
                }
                case "Condition" -> {
                    System.out.println("Condition");
                    // TODO card will only be run of condition is true
                }
                case "RentMultiplier" -> {
                    System.out.println("RentMultiplier");
                    // TODO double the rent that the player needs to play if a field is owned by someone else
                }
                case "JailFreeCard" -> {
                    System.out.println("JailFreeCard");
                    // TODO the player receives a "get-out-of-jail"-card
                }
            }
        }

    }

    /**
     *
     * @param playerID - the current player
     * @param receiverID - a potential receiving player (null if none)
     * @param amount - the size of the transaction (payment should be negative int)
     * @param critical - does the current player NEED to pay
     * @return
     */
    public boolean createTransaction (String playerID, String receiverID, int amount, boolean critical, String message) {
        boolean transactionSuccess = false;
        String userRequest;

        // If a player needs to make a payment to another player
        if (receiverID != null) {
            gui.buttonRequest(message + " Pay " + currentGameState.getPlayerFromID(receiverID).getName() + " " + Math.abs(amount), "Pay");
        }
        else {
            // Payments to the bank
            if (amount <= 0) {
                if (critical) {
                    userRequest = gui.buttonRequest(message, "Pay");
                }
                else {
                    userRequest = gui.buttonRequest(message, "Pay", "Cancel");
                    if (userRequest.equals("Cancel")) {
                        return false;
                    }
                }
                }
            // A type of player income
            else {
                userRequest = gui.buttonRequest(message, "Ok");
            }
        }

        // Handles the transaction and returns a boolean for transaction success
        transactionSuccess = currentGameState.handleTransaction(playerID, receiverID, amount, critical);

        // updates balance to gui if transaction is successful
        if (transactionSuccess) {
            int newPlayerBalance = currentGameState.getPlayerFromID(playerID).getBalance();
            gui.updateGUIPlayerBalance(playerID, newPlayerBalance);
        }

        // updates a potential receiving player's balance to the gui
        if (receiverID != null) {
            int newReceiverBalance = currentGameState.getPlayerFromID(receiverID).getBalance();
            gui.updateGUIPlayerBalance(receiverID, newReceiverBalance);
        }

        if (currentGameState.getPlayerFromID(playerID) == null) {
            gui.buttonRequest("You are broke and have lost the game... ", "Ok");
            gui.removePlayer(playerID);
        }

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
