package dtu.matador.game;

import dtu.matador.game.fields.*;

import java.util.*;

public class FieldController {

    private ArrayList<FieldSpaces> fields;
    private ArrayList<Map<String,String>> fieldList;
    private PropertyBank propertyBank;
    private ArrayList<Map<String,String>> chanceList;
    public FieldSpaces currentField;
    private final PlayerController playerController;
    public final GUIController gui;
    int boardSize;


    public FieldController(PlayerController injectPlayerController, GUIController injectGui, ArrayList<Map<String, String>> selectedBoard, ArrayList<Map<String, String>> selectedChance) {
        this.playerController = injectPlayerController;
        this.gui = injectGui;
        fieldList = selectedBoard;
        this.propertyBank = new PropertyBank();

        fields = new ArrayList<>();
        for (int i = 0; i < fieldList.size(); i++) {
            fields.add(null);
        }
        setupFields();
        this.boardSize = fieldList.size();
        gui.setGUI(fieldList);
        chanceList = selectedChance;


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
                    fields.set(fieldPosition, propertyBank.addProperty(field.get("neighborhood"), new Street(field.get("title"), field.get("subtext"), field.get("subtext"), field.get("rent"), field.get("rent1"), field.get("rent2"),
                            field.get("rent3"), field.get("rent4"), field.get("rent5"), field.get("color1"), field.get("color2"), field.get("price"), field.get("pawnForAmount"),
                            field.get("position"), field.get("owner"), field.get("housing"), field.get("neighborhood"), field.get("groupsize"), field.get("buildPrice"))));

                }
                case "ferry" -> {
                    fields.set(fieldPosition, propertyBank.addProperty(field.get("neighborhood"), new Ferry(field.get("title"), field.get("subtext"), field.get("subtext"), field.get("rent"), field.get("rent1"), field.get("rent2"),
                            field.get("rent3"), field.get("rent"), field.get("rent"), field.get("color1"), field.get("color2"), field.get("price"), field.get("pawnForAmount"),
                            field.get("position"), field.get("owner"), field.get("neighborhood"), field.get("groupsize"))));
                }
                case "brewery" -> {
                    fields.set(fieldPosition, propertyBank.addProperty(field.get("neighborhood"), new Brewery(field.get("title"), field.get("subtext"), field.get("subtext"), field.get("rent"), field.get("rent1"), field.get("rent"),
                            field.get("rent"), field.get("rent"), field.get("rent"), field.get("color1"), field.get("color2"), field.get("price"), field.get("pawnForAmount"),
                            field.get("position"), field.get("owner"), field.get("neighborhood"), field.get("groupsize"))));
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
                case "jail" -> {
                    fields.set(fieldPosition, new Jail(field.get("title"), field.get("subtext"), field.get("subtext"), field.get("color1"), field.get("color2"),
                            field.get("position")));

                }
                case "tax" -> {
                    fields.set(fieldPosition, new TaxField(field.get("title"), field.get("subtext"), field.get("subtext"), field.get("color1"), field.get("color2"),
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
        if (lookUp == null) {
            lookUp = fields;
        }
        Map <String, String> fieldAsMap;
        for (FieldSpaces field : lookUp) {
            fieldAsMap = getFieldAsMap(field.getPosition());
            if (fieldAsMap.get(searchKey).equals(searchValue)) {
                fieldsFound.add(field);
            }
        }
        return fieldsFound;
    }

    public ArrayList<String> lookUpFieldStringValues(ArrayList<FieldSpaces> lookUp, String searchKeyCriteria, String searchValueCriteria, String searchKey) {
        ArrayList<String> valuesFound = new ArrayList<>();
        if (lookUp == null) {
            lookUp = fields;
        }
        ArrayList<FieldSpaces> fieldsFound = lookUpFields(lookUp, searchKeyCriteria, searchValueCriteria);

        Map <String, String> fieldAsMap;
        for (FieldSpaces field : fieldsFound) {
            fieldAsMap = getFieldAsMap(field.getPosition());
            String result = fieldAsMap.get(searchKey);
            if (result != null) {
                valuesFound.add(result);
            }
        }
        return valuesFound;
    }

    public ArrayList<String> lookUpFieldStringValues(String searchKey) {
        ArrayList<String> valuesFound = new ArrayList<>();
        for (Map<String, String> fieldAsMap : fieldList) {
            if (fieldAsMap.containsKey(searchKey)) {
                valuesFound.add(fieldAsMap.get(searchKey));
            }
        }
        return valuesFound;
    }

    public int getJailPosition() {
        ArrayList<FieldSpaces> allJailPositions = lookUpFields(fields, "fieldType", "jail");
        int firstInstance;
        for (FieldSpaces jailField : allJailPositions) {
            if (((Jail) jailField).getInstanceOfJail() == 0) {
                return jailField.getPosition();
            }
        }
        return 0;
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
        else if (currentField instanceof TaxField) {
            landOnTax(playerID, ((TaxField) currentField));
        }

        // property, chance, jail, etc.
    }

    private void landOnJail(String playerID, Jail currentField) {
        Player player = playerController.getPlayerFromID(playerID);
        if (currentField.getInstanceOfJail() == 0) {
            if (player.getjailed() > 0){
                if (player.getJailCards() > 0){
                    String choice = gui.buttonRequest("Vil du bruge dit fængselskort for at slippe ud?", "Ja", "Nej");
                    if (choice.equals("Ja")) {
                        System.out.println("Player chose to use get out of jail card");
                        player.useJailCard();
                        gui.buttonRequest("Du kom ud af fængsel, din tur!", "Slå");
                        int[] dieValues = player.rollDie();
                        gui.setDice(dieValues);
                        int startPosition = player.getPosition();
                        player.movePosition(dieValues[2]);
                        gui.movePlayerTo(player.getId(), startPosition,player.getPosition());
                        landOnField(playerID, startPosition, player.getPosition(), true);
                    }
                    else {jailWithoutCard(player,playerID);}
                }
                else {jailWithoutCard(player, playerID);System.out.println("Player had no cards to escape");}
            }
            else {System.out.println("Player is visiting");}
        }
        else{
            gui.displayGeneralMessage("Du er desværre kommet i fængsel");
            playerController.getPlayerFromID(playerID).setjailed(1);
            int currentPosition = player.getPosition();
            int jailPosition = currentField.getFirstJailInstancePosition();
            player.setPosition(jailPosition);
            gui.movePlayerTo(playerID, currentPosition, player.getPosition());
        }
    }

    private void jailWithoutCard(Player player, String playerID){

        if (player.getjailed() > 0 && player.getjailed() <= 3) {
            String choice2 = gui.buttonRequest("Vil du slå med terningerne eller betale dig ud?", "Slå", "Betal");
            if (choice2.equals("Slå")) {
                int[] dieValues = player.rollDie();
                gui.setDice(dieValues);
                if (dieValues[0] == dieValues[1]) {
                    player.setjailed(0);
                    gui.displayGeneralMessage("Tillykke! Du er kommet ud af fængslet");
                    int startPosition = player.getPosition();
                    player.movePosition(dieValues[2]);
                    gui.movePlayerTo(player.getId(), startPosition,player.getPosition());
                    landOnField(playerID, startPosition, player.getPosition(), true);
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
        else if (player.getjailed() == 0) {
            gui.displayGeneralMessage(gui.buttonRequest("Du er på besøg i fængslet", "ok"));
        }
        else {
            gui.buttonRequest("Du har ikke flere forsøg. Du er nødsaget til at betale");
            payForJail(player);
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

                String message = property.buyMessage();
                boolean purchase = createTransaction(playerID,null, property.getPrice(), false, message);
                if (purchase) {
                    property.setOwner(playerID);
                }
                else {
                    gui.buttonRequest("You have insufficient funds. ", "Ok");
                    auction(playerID, property);
                }

                if (property.getOwner() != null) {
                    if (property.getPosition() == 5 || property.getPosition() == 15 || property.getPosition() == 25 || property.getPosition() == 35){
                        player.addFerries();
                        System.out.println(player.getFerries());
                    }
                    if (property.getOwner().equals(playerID)) {
                        updateFieldMap(property);
                        updateGUI(property, playerID);
                    }
                }
            } else if (choice.equals("Auction")) {
                gui.buttonRequest("You have insufficient funds. ", "Ok");
                auction(playerID, property);
            }

        }
        // Redirect to type of Property
        else if (currentField instanceof Street) {
            landOnStreet(playerID, ((Street) currentField));
        } else if (currentField instanceof Brewery) {
            landOnBrewery(playerID, ((Brewery) currentField));
        } else if (currentField instanceof Ferry) {
            landOnFerry(playerID, ((Ferry) currentField));
        }
        // Street, Brewery, Ferry, etc.
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
            //TODO den nedenstående variable skal ændres således at den ser på ejerens ferries og ikke den nuværende spillers...
            int ferriesOwned = 1;
            for (Property property : propertyBank.getPropertiesFromGroup(ferry.getNeighborhood())) {
                if (property.getOwner() != null) {
                    if (property.getOwner().equals(owner)) {
                        ferriesOwned += 1;
                    }
                }
            }

            int rent = ferry.getRent(ferriesOwned);
            System.out.println(rent);
            String receiverID = ferry.getOwner();
            String message = "Denne færge er ejet af en anden! Betal " + Math.abs(rent) + ", da der ejes " + ferriesOwned + " færge(r). ";

            createTransaction(playerID, receiverID, rent, true, message);
        }
    }

    private void landOnBrewery(String playerID, Brewery currentField) {
        Player player = playerController.getPlayerFromID(playerID);
        String owner = currentField.getOwner();
        if (owner == null) {
            return;
        }
        if (!owner.equals(playerID)) {
            int breweriesOwnedByOwner = 0;
            ArrayList<Property> allBreweries = propertyBank.getPropertiesFromGroup("brewery");
            for (Property brewery : allBreweries) {
                if (brewery.getOwner() != null) {
                    if (brewery.getOwner().equals(owner)) {
                        breweriesOwnedByOwner += 1;
                    }
                }
            }
            int totalDieRoll = player.getLastPlayedDieRoll()[2];
            int rent = currentField.getRent(breweriesOwnedByOwner);
            //int multiplier = currentField.getMultiplier(breweriesOwnedByOwner);
            int breweryRent = totalDieRoll*rent;
            createTransaction(playerID, owner, breweryRent, true,"");
        }
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
            if (balance >= street.getBuildPrice() && propertyBank.canBuyHouse(playerID, property.getNeighborhood()) && street.getHousing() < 5) {

            int nextBuildPrice = street.getBuildPrice();
                String response = gui.buttonRequest("Do you want to purchase a house on each of your properties of the same color for " + Math.abs(nextBuildPrice * street.getGroupSize()) + "?", "Buy", "Not Now");
                if (response.equals("Buy")) {
                    boolean transactionSuccess = createTransaction(playerID, null, nextBuildPrice * property.getGroupSize(), false, "Buying house for " + Math.abs(nextBuildPrice * street.getGroupSize()));
                    if (transactionSuccess) {

                        for(Property propertyInGroup : propertyBank.getPropertiesFromGroup(property.getNeighborhood())){
                            propertyInGroup.buildHouse();
                            updateGUI(propertyInGroup, playerID);
                            updateFieldMap(propertyInGroup);
                        }
                    }
                }
            }

        }
        else {
            String message = "This field is owned by someone else!";
            int rent = street.getRent();
            System.out.println(rent);
            String receiverID = street.getOwner();

            createTransaction(playerID, receiverID, rent, true, message);
        }

    }

    private void landOnChance (String playerID, Chance chance) {
        Player player = playerController.getPlayerFromID(playerID);
        Random rand = new Random();
        int cardNumber = 0;
        if (chanceList.size() != 1) {
            cardNumber = rand.nextInt(0, chanceList.size() - 1);
        }
        Map <String, String> rawCard = chanceList.get(cardNumber);
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
                    ArrayList<FieldSpaces> ownedStreets = lookUpFields(ownedFields,"fieldType", "street");
                    for (FieldSpaces field : ownedStreets) {
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
                    int ownedHotels = 0;
                    ArrayList<FieldSpaces> ownedFields = lookUpFields(fields,"owner", playerID);
                    ArrayList<FieldSpaces> ownedStreets = lookUpFields(ownedFields,"fieldType", "street" );
                    for (FieldSpaces field : ownedStreets) {
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
                        if (owner != null && !owner.equals(playerID)) {
                            int totalOwnedByOwner = 0;
                            ArrayList<Property> ownedByOwner = propertyBank.getPropertiesFromGroup(((Property) fieldShortestDistance).getNeighborhood());
                            for (Property property : ownedByOwner) {
                                if (property.getOwner() != null) {
                                    if (property.getOwner().equals(owner)) {
                                        totalOwnedByOwner += 1;
                                    }
                                }
                            }
                            int rentToPay = 0;
                            if (fieldShortestDistance instanceof Ferry) {
                                rentToPay = ((Ferry) fieldShortestDistance).getRent(totalOwnedByOwner) * rentMultiplier;
                            }
                            else if (fieldShortestDistance instanceof Street) {
                                rentToPay = ((Street) fieldShortestDistance).getRent() * rentMultiplier;
                            }
                            else if (fieldShortestDistance instanceof Brewery) {
                                rentToPay = ((Brewery) fieldShortestDistance).getRent(totalOwnedByOwner) * rentMultiplier;
                            }
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
    public void landOnTax(String playerID, TaxField currentField) {
        int bill = currentField.getBill();
        String message = "Skattefar giver dig et skattesmæk på " + Math.abs(bill);
        createTransaction(playerID, null, bill, true, message);
    }

    private void payForJail(Player player){
        boolean transactionSuccess = createTransaction(player.getId(),null,-1000,true,"Du betaler for at komme ud af fængsel");
        if (transactionSuccess) {
            player.setjailed(0);
        }
    }

    private void auction(String playerID, Property property) {
        // TODO Auction property off to highest bidder!
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

    public void purchaseAllProperties(String playerID, boolean overwriteOwners, boolean resetHousing) {
        ArrayList<FieldSpaces> allProperties = lookUpFields(fields, "fieldType", "property");
        allProperties.addAll(lookUpFields(fields, "fieldType", "ferry"));
        allProperties.addAll(lookUpFields(fields, "fieldType", "brewery"));

        for (FieldSpaces property : allProperties) {
            if (!overwriteOwners) {
                if (((Property) property).getOwner() != null && !((Property) property).getOwner().equals(playerID)) {
                    continue;
                }
            }

            // TODO remove housing from the original player

            ((Property) property).setOwner(playerID);
            if (property instanceof Street && resetHousing) {
                ((Street) property).setHousing(0);
            }
            updateFieldMap(((Property)property));
            updateGUI(((Property)property), playerID);
        }
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
