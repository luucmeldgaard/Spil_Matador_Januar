package dtu.matador.game;

import dtu.matador.game.fields.*;

import java.util.*;

public class FieldController {

    private ArrayList<FieldSpaces> fields;
    private ArrayList<Map<String,String>> fieldList;
    private ArrayList<Map<String,String>> chanceMap;
    public FieldSpaces currentField;
    private final PlayerController playerController;
    public final GUIController gui;
    int boardSize;


    public FieldController(PlayerController injectPlayerController, GUIController injectGui, String selectedBoard) {
        this.playerController = injectPlayerController;
        this.gui = injectGui;
        FieldLoader fieldLoader = new FieldLoader(selectedBoard);
        fieldList = fieldLoader.getFieldList();

        fields = new ArrayList<>();
        for (int i = 0; i < fieldList.size(); i++) {
            fields.add(null);
        }
        setupFields();
        this.boardSize = fieldList.size();
        gui.setGUI(fieldList);
        chanceMap = fieldLoader.getChanceList();

    }

    public FieldController(PlayerController injectPlayerController, GUIController injectGui) {
        this.playerController = injectPlayerController;
        this.gui = injectGui;
    }



    protected void setupFields() {
        FieldController controller = new FieldController(this.playerController, gui);
        for (Map<String, String> field : fieldList) {
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
                case "tax" -> {
                    fields.set(fieldPosition, new TaxField(controller, field.get("title"), field.get("subtext"), field.get("subtext"), field.get("color1"), field.get("color2"),
                            field.get("price"), field.get("position")));
                }
            }
        }
    }

    protected FieldSpaces getField(int position) {
        return fields.get(position);
    }

    protected ArrayList<FieldSpaces> getAllFieldSpaces() {
        return fields;
    }

    protected ArrayList<Map<String, String>> getFieldList() {
        return fieldList;
    }

    protected Map<String, String> getFieldAsMap(int position) {
        return fieldList.get(position);
    }

    // overwrites chosen values of a specific field in the fieldMap
    protected void updateFieldMap(Property property) {
        Map<String, String> updatedFieldInfo = new HashMap<>();

        int fieldPosition = property.getPosition();
        String color2 = property.getColor2();
        String housing = null;
        fieldList.get(fieldPosition).replace("color2", color2);
        if (property instanceof Street) {
            housing = String.valueOf(((Street) property).getHousing());
            fieldList.get(fieldPosition).replace("housing", housing);
        }
    }

    public ArrayList<FieldSpaces> lookUpFields(ArrayList<FieldSpaces> lookUp, String searchKey, String searchValue) {
        ArrayList<FieldSpaces> fieldsFound = new ArrayList<>();
        Map <String, String> fieldAsMap;
        for (FieldSpaces field : lookUp) {
            fieldAsMap = getFieldAsMap(field.getPosition());
            if (fieldAsMap.get(searchKey).equals(searchValue)) {
                fieldsFound.add(field);
            }
        }
        return fieldsFound;
    }

    protected void landOnField(String playerID, int startPosition, int currentPosition, boolean passStart) {
        if (startPosition > currentPosition && passStart) {
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
        Player player = playerController.getPlayerFromID(playerID);
        if (currentField.getInstanceOfJail() == 0) {
            if (player.getjailed() > 0 && player.getjailed() <= 3) {
                String choice = gui.buttonRequest("Vil du slå med terningerne eller betale dig ud?", "Roll", "Pay");
                if (choice.equals("Roll")) {
                    int[] dieValues = player.rollDie();
                    gui.setDice(dieValues);
                    if (dieValues[0] == dieValues[1]) {
                        player.setjailed(0);
                        gui.displayGeneralMessage("Tillykke! Du er kommet ud af fængslet");
                        int currentPosition = player.getPosition();
                        player.movePosition(dieValues[2]);
                        gui.movePlayerTo(player.getId(), currentPosition,player.getPosition());
                    }
                    else {
                        gui.displayGeneralMessage("Det var desværre ikke to ens");
                        player.setjailed(player.getjailed() + 1);
                    }
                }
                else {
                    payForJail(player);
                }
            }
            else {
                gui.displayGeneralMessage(gui.buttonRequest("Du er på besøg i fængslet", "ok"));
            }
        }
        else{
            System.out.println("SØREN RYGE (der burde nok ske noget fængselsrelateret her)");
            gui.displayGeneralMessage("Du er desværre kommet i fængsel");
            playerController.getPlayerFromID(playerID).setjailed(1);
            int currentPosition = player.getPosition();
            int jailPosition = currentField.getFirstJailInstancePosition();
            player.setPosition(jailPosition);
            gui.movePlayerTo(playerID, currentPosition,player.getPosition());
        }
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
        Player player = playerController.getPlayerFromID(playerID);
        Random rand = new Random();
        int cardNumber = 0;
        if (chanceMap.size() != 1) {
            cardNumber = rand.nextInt(0, chanceMap.size() - 1);
        }
        Map <String, String> rawCard = chanceMap.get(cardNumber);
        Map <String, String> card = new HashMap<>();
        boolean payIfCrossStart = false;
        boolean condition = false;
        int rentMultiplier = 1;

        for (String key : rawCard.keySet()) {
            if (!rawCard.get(key).equals("")) {
                card.put(key, rawCard.get(key));
            }
            if (key.equals("PayIfCrossStart") && rawCard.get(key).equals("1")) {
                System.out.println("PayIfCrossStart = true");
                payIfCrossStart = true;
            }
            if (key.equals("condition") && rawCard.get(key).equals("1")) {
                System.out.println("Condition");
                condition = true;
            }
            if (key.equals("RentMultiplier") && !rawCard.get(key).equals("")) {
                System.out.println("RentMultiplier");
                rentMultiplier = Integer.parseInt(rawCard.get(key));
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
                    int ownedHouses = 0;
                    ArrayList<FieldSpaces> ownedFields = lookUpFields(fields,"owner", playerID);
                    ArrayList<FieldSpaces> ownedStreets = lookUpFields(ownedFields,"street", playerID);
                    for (FieldSpaces field : ownedFields) {
                        int housingOnField = ((Street) field).getHousing();
                        if (housingOnField < 5) {
                            ownedHouses += housingOnField;
                        }
                    }
                    int amount = Integer.parseInt(card.get(key)) * ownedHouses;
                    createTransaction(playerID, null, amount, true, message);

                }
                case "CashAddedPerHotel" -> {
                    System.out.println("CashAddedPerHotel");
                    System.out.println("CashAddedPerHouse");
                    int ownedHotels = 0;
                    ArrayList<FieldSpaces> ownedFields = lookUpFields(fields,"owner", playerID);
                    ArrayList<FieldSpaces> ownedStreets = lookUpFields(ownedFields,"street", playerID);
                    for (FieldSpaces field : ownedFields) {
                        int housingOnField = ((Street) field).getHousing();
                        if (housingOnField == 5) {
                            ownedHotels += 1;
                        }
                    }
                    int amount = Integer.parseInt(card.get(key)) * ownedHotels;
                    createTransaction(playerID, null, amount, true, message);

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
                    int moveBy = Integer.parseInt(card.get(key));
                    int currentPosition = playerController.getPlayerFromID(playerID).getPosition();
                    playerController.getPlayerFromID(playerID).movePosition(moveBy);
                    int endPosition = playerController.getPlayerFromID(playerID).getPosition();
                    gui.movePlayerTo(playerID, currentPosition, endPosition);
                    landOnField(playerID, currentPosition, endPosition, payIfCrossStart);
                }
                case "MoveToType" -> {
                    System.out.println("MoveToType");
                    gui.buttonRequest(message, "Ok");
                    // TODO make player move to the next instance of a specific field type
                    int currentPlayerPosition = player.getPosition();

                    List<Integer> allFieldTypePositions = new ArrayList<>();
                    List<FieldSpaces> allFieldsOfType = lookUpFields(fields, "fieldType", "ferry");
                    for (FieldSpaces field : allFieldsOfType) {
                        int fieldPosition = field.getPosition();
                        allFieldTypePositions.add(fieldPosition);
                    }

                    int closest = boardSize;
                    for (int fieldPosition : allFieldTypePositions) {
                        if (Math.abs(fieldPosition - currentPlayerPosition) < Math.abs(closest - currentPlayerPosition)) {
                            closest = fieldPosition;
                        }
                    }
                    FieldSpaces fieldShortestDistance = fields.get(closest);

                    if (fieldShortestDistance instanceof Property) {
                        String owner = ((Property) fieldShortestDistance).getOwner();
                        if (owner != null && owner != playerID) {
                            int rentToPay = ((Property) fieldShortestDistance).getRent() * rentMultiplier;
                            player.setPosition(closest);
                            gui.movePlayerTo(playerID, currentPlayerPosition, closest);
                            createTransaction(playerID, owner, rentToPay, true, message);
                        }
                        else {
                            player.setPosition(closest);
                            gui.movePlayerTo(playerID, currentPlayerPosition, closest);
                            landOnField(playerID, currentPlayerPosition, player.getPosition(), payIfCrossStart);
                        }
                    }
                    else {
                        player.setPosition(closest);
                        gui.movePlayerTo(playerID, currentPlayerPosition, closest);
                        landOnField(playerID, currentPlayerPosition, player.getPosition(), payIfCrossStart);
                    }

                }
                case "MoveTo" -> {
                    System.out.println("MoveTo");
                    int cardPosition  = Integer.parseInt(card.get(key));
                    int startPlayerPosition = player.getPosition();
                    player.setPosition(cardPosition);
                    gui.movePlayerTo(playerID, startPlayerPosition, player.getPosition());
                    landOnField(playerID, startPlayerPosition, player.getPosition(), payIfCrossStart);
                }
                case "JailFreeCard" -> {
                    System.out.println("JailFreeCard");
                    player.addJailCard();
                }
            }
        }

    }
    public void playRoundJailed(Player player) {

    }

    private void payForJail(Player player){
        boolean transactionSuccess = createTransaction(player.getId(),null,-1000,true,"Du betaler for at komme ud af fængsel");
        if (transactionSuccess) {
            player.setjailed(0);
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
