package dtu.matador.game;

import dtu.matador.game.fields.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class FieldController {

    private ArrayList<FieldSpaces> fields;
    private Map<String, Map<String, String>> fieldMap;
    private Map<String, Map<String, String>> chanceMap;
    public FieldSpaces currentField;
    private final PlayerController playerController;
    private final GUIController gui;


    public FieldController(PlayerController injectPlayerController, GUIController injectGui, String selectedBoard) {
        this.playerController = injectPlayerController;
        this.gui = injectGui;
        FieldLoader fieldLoader = new FieldLoader(selectedBoard);
        fieldMap = fieldLoader.getFieldMap();

        fields = new ArrayList<>();
        for (int i = 0; i < fieldMap.size(); i++) {
            fields.add(null);
        }
        setupFields();
        gui.setGUI(fieldMap);
        chanceMap = fieldLoader.getChanceMap();

    }

    public FieldController(PlayerController injectPlayerController, GUIController injectGui) {
        this.playerController = injectPlayerController;
        this.gui = injectGui;
    }



    protected void setupFields() {
        FieldController controller = new FieldController(this.playerController, gui);
        for (Map<String, String> field : fieldMap.values()) {
            int fieldPosition = Integer.parseInt(field.get("position"));
            switch (field.get("fieldType")) {
                case "property" -> {
                    fields.set(fieldPosition, new Street(controller, field.get("title"), field.get("subtext"), field.get("subtext"), field.get("rent"), field.get("rent1"), field.get("rent2"),
                            field.get("rent3"), field.get("rent4"), field.get("rent5"), field.get("color1"), field.get("color2"), field.get("price"), field.get("pawnForAmount"),
                            field.get("position"), field.get("owner"), field.get("housing"), field.get("neighborhood"), field.get("groupsize")));
                }
                case "ferry" -> {
                    fields.set(fieldPosition, new Ferry(controller, field.get("title"), field.get("subtext"), field.get("subtext"), field.get("rent"), field.get("rent"), field.get("rent"),
                            field.get("rent"), field.get("rent"), field.get("rent"), field.get("color1"), field.get("color2"), field.get("price"), field.get("pawnForAmount"),
                            field.get("position"), field.get("owner"), field.get("neighborhood"), field.get("groupsize")));
                }
                case "brewery" -> {
                    fields.set(fieldPosition, new Brewery(controller, field.get("title"), field.get("subtext"), field.get("subtext"), field.get("rent"), field.get("rent"), field.get("rent"),
                            field.get("rent"), field.get("rent"), field.get("rent"), field.get("color1"), field.get("color2"), field.get("price"), field.get("pawnForAmount"),
                            field.get("position"), field.get("owner"), field.get("neighborhood"), field.get("groupsize")));
                }
                case "refuge" -> {
                    fields.set(fieldPosition, new Refuge(controller, field.get("title"), field.get("subtext"), field.get("subtext"), field.get("color1"), field.get("color2"),
                            field.get("position")));
                }
                case "start" -> {
                    fields.set(fieldPosition, new StartField(controller, field.get("title"), field.get("subtext"), field.get("subtext"), field.get("color1"), field.get("color2"),
                            field.get("position"), field.get("income")));
                }
                case "chance" -> {
                    fields.set(fieldPosition, new Chance(controller, field.get("title"), field.get("subtext"), field.get("subtext"), field.get("color1"), field.get("color2"),
                            field.get("position")));
                }
                case "jail" -> {
                    fields.set(fieldPosition, new Jail(controller, field.get("title"), field.get("subtext"), field.get("subtext"), field.get("color1"), field.get("color2"),
                            field.get("position")));

                }
            }
        }
    }

    protected FieldSpaces getField(int position) {
        return fields.get(position);
    }

    protected ArrayList<FieldSpaces> getFieldsArray() {
        return fields;
    }

    protected Map<String, Map<String, String>> getFieldMap() {
        return fieldMap;
    }

    protected Map<String, String> getFieldAsMap(int position) {
        String pos = String.valueOf(position);
        return fieldMap.get(pos);
    }

    // overwrites chosen values of a specific field in the fieldMap
    protected void updateFieldMap(Property property) {
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
    protected void landOnField(String playerID, int startPosition, int currentPosition) {
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
        System.out.println("SØREN RYGE (der burde nok ske noget fængselsrelateret her)");
        gui.displayGeneralMessage("Du er desværre kommet i fængsel");
        Player player = playerController.getPlayerFromID(playerID);
        player.setjailed(1);
        int oldpos = player.getPosition();
        player.setPosition(10);
        gui.movePlayerTo(playerID,oldpos,player.getPosition());
        /*
        currentGameState.getPlayerFromID(playerID).jailed = 1;
        int oldpos = currentGameState.getPlayerFromID(playerID).getPosition();
        currentGameState.getPlayerFromID(playerID).setPosition(30);
        int newpos = currentGameState.getPlayerFromID(playerID).getPosition();
        gui.movePlayerTo(playerID,oldpos,newpos);
        */
    }

    private void landOnStart(String playerID, StartField start) {
        int income = start.getIncome();
        String message = "You passed start, gain " + income + "!";
        createTransaction(playerID, null, income, false, message);
    }

    private void landOnProperty(String playerID, Property property) {
        String owner = property.getOwner();
        Player player = playerController.getPlayerFromID(playerID);
        if (owner == null) {
            System.out.println("This field is not owned by anyone!");
            String choice = gui.buttonRequest("Buy or auction?", "Buy", "Auction");
            if (choice.equals("Buy")) {
                property.buy(playerID);
                if (property.getOwner() != null) {
                    if (property.getPosition() == 5 || property.getPosition() == 15 || property.getPosition() == 25 || property.getPosition() == 35){
                        player.addFerries();
                        System.out.println(player.getFerries());
                    }
                    if (property.getOwner().equals(playerID)) {
                        updateFieldMap(property);
                        updateGUI(property, playerID);


                        ArrayList<Property> propertyList = player.getPlayerHousing().getPropertiesFromColor(property.getNeighborhood());

                        if (propertyList == null) {
                            propertyList = new ArrayList<>();
                            propertyList.add(property);
                        } else {
                            propertyList.add(property);
                        }
                        player.getPlayerHousing().addProperty(property.getNeighborhood(), propertyList);
                    }
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

    private void landOnFerry(String playerID, Ferry ferry) {
        String owner = ferry.getOwner();
        if (owner == null) return;
        System.out.println(owner);
        Player player = playerController.getPlayerFromID(playerID);
        int balance = playerController.getPlayerFromID(playerID).getBalance();

        if (owner.equals(playerID)) {
            System.out.println("This ferry is owned by you. ");
        }
        else {
            String message = "This ferry is owned by someone else!";
            //TODO den nedenstående variable skal ændres således at den ser på ejerens ferries og ikke den nuværende spillers...
            switch (player.getFerries()) {
                case 1 -> {
                    int rent = ferry.getRent();
                    System.out.println(rent);
                    String receiverID = ferry.getOwner();

                    createTransaction(playerID, receiverID, rent, true, message);
                }
                case 2 -> {
                    int rent1 = ferry.getRent1();
                    System.out.println(rent1);
                    String receiverID = ferry.getOwner();

                    createTransaction(playerID, receiverID, rent1, true, message);
                }
                case 3 -> {
                    int rent2 = ferry.getRent2();
                    System.out.println(rent2);
                    String receiverID = ferry.getOwner();

                    createTransaction(playerID, receiverID, rent2, true, message);
                }
                case 4 -> {
                    int rent3 = ferry.getRent3();
                    System.out.println(rent3);
                    String receiverID = ferry.getOwner();

                    createTransaction(playerID, receiverID, rent3, true, message);
                }
            }

            /*int rent = ferry.getRent();
            System.out.println(rent);
            String receiverID = ferry.getOwner();

            createTransaction(playerID, receiverID, rent, true, message);*/
        }
    }

    private void landOnUtility(String playerID, Brewery currentField) {
    }

    private void landOnStreet(String playerID, Street street) {
        // Check for ownership
        // if ownership = null, owned(other playerID), owned(own playerID)
        String owner = street.getOwner();
        if (owner == null) return;
        System.out.println(owner);
        Player player = playerController.getPlayerFromID(playerID);
        int balance = playerController.getPlayerFromID(playerID).getBalance();

        //If the field is owned by the current player, they have the choice to buy a house if they have sufficient funds
        if (owner.equals(playerID)) {
            System.out.println("This field is owned by you. ");
            Property property = (Property) street;
            //checks if the owner has sufficient funds to buy a house
            if (balance >= street.getBuildPrice()) {

                if(!player.getPlayerHousing().canBuyHouse(property.getNeighborhood())) return;

                int nextBuildPrice = street.getBuildPrice();
                if (street.housing < 5) {
                    String response = gui.buttonRequest("Do you want to buy a house for " + nextBuildPrice + "?", "Buy", "No");
                    if (response.equals("Buy")) {
                        boolean transactionSuccess = createTransaction(playerID, null, nextBuildPrice, false, "Buying house for " + street.getBuildPrice());
                        if (transactionSuccess) {


                            for(Property propertyList : player.getPlayerHousing().getPropertiesFromColor(property.getNeighborhood())){
                                street.buildHouse();
                                updateGUI(propertyList, playerID);
                                updateFieldMap(propertyList);
                            }
                        }
                    }
                }
            }
            // player does not care about housing because he doesn't have the money for it.

        }
        else {
            String message = "This field is owned by someone else!";
            int rent = street.getRent();
            System.out.println(rent);
            String receiverID = street.getOwner();

            createTransaction(playerID, receiverID, rent, true, message);
            // if owned(own playerID)
            // Options: Build, Pledge, Sell housing

            // if owned(other playerID)
            // pay rent

            // if null
            // Buy, Auction
        }

    }

    private void landOnChance (String playerID, Chance chance) {
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
                    ArrayList<String> allPlayerIDs = playerController.getAllPlayerIDs();
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
                    int moveBy = Integer.parseInt(card.get(key));
                    int currentPosition = playerController.getPlayerFromID(playerID).getPosition();
                    playerController.getPlayerFromID(playerID).movePosition(moveBy);
                    int endPosition = playerController.getPlayerFromID(playerID).getPosition();
                    gui.movePlayerTo(playerID, currentPosition, endPosition);
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
    protected boolean createTransaction (String playerID, String receiverID, int amount, boolean critical, String message) {
        boolean transactionSuccess = false;
        String userRequest;

        // If a player needs to make a payment to another player
        if (receiverID != null) {
            gui.buttonRequest(message + " Pay " + playerController.getPlayerFromID(receiverID).getName() + " " + Math.abs(amount), "Pay");
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
        transactionSuccess = playerController.handleTransaction(playerID, receiverID, amount, critical);

        // updates balance to gui if transaction is successful
        if (transactionSuccess) {
            int newPlayerBalance = playerController.getPlayerFromID(playerID).getBalance();
            gui.updateGUIPlayerBalance(playerID, newPlayerBalance);
        }

        // updates a potential receiving player's balance to the gui
        if (receiverID != null) {
            int newReceiverBalance = playerController.getPlayerFromID(receiverID).getBalance();
            gui.updateGUIPlayerBalance(receiverID, newReceiverBalance);
        }

        if (playerController.getPlayerFromID(playerID) == null) {
            gui.buttonRequest("You are broke and have lost the game... ", "Ok");
            gui.removePlayer(playerID);
        }

        return transactionSuccess;

    }


    protected void insufficientFunds () {
        gui.buttonRequest("You have insufficient funds. ", "Ok");
    }

    protected void updateGUI(Property property, String playerID) {
            String playerColor = playerController.getPlayerFromID(playerID).getColor();
            if (property instanceof Street) {
                gui.updateProperty(property.getPosition(), playerColor, ((Street) property).getHousing());
            } else {
                gui.updateProperty(property.getPosition(), playerColor);
            }
    }


}
