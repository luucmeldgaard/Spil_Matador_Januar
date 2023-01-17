package dtu.matador.game;

import dtu.matador.game.fields.*;

import java.util.*;

public class FieldController {

    private ArrayList<FieldSpaces> fields;
    private ArrayList<Map<String, String>> fieldList;
    private PropertyBank propertyBank;
    private ArrayList<Map<String, String>> chanceList;
    public FieldSpaces currentField;
    private final PlayerController playerController;
    public final GUIController gui;
    int boardSize;


    public FieldController(PlayerController injectPlayerController, GUIController injectGui, ArrayList<Map<String, String>> selectedBoard, ArrayList<Map<String, String>> selectedChance) {
        this.playerController = injectPlayerController;
        this.gui = injectGui;
        fieldList = selectedBoard;
        chanceList = selectedChance;
        this.propertyBank = new PropertyBank();

        fields = new ArrayList<>();
        for (int i = 0; i < fieldList.size(); i++) {
            fields.add(null);
        }
        setupFields();
        this.boardSize = fieldList.size();
        gui.setGUI(fieldList);

        loadInPlayers();

        for (ArrayList<Property> propertyList : propertyBank.getPropertyMap().values()) {
            for (Property property : propertyList) {
                if (property.getOwner() != null) {
                    updateGUI(property, property.getOwner());
                }
            }
        }
    }

    protected void setupFields() {
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
        String owner = property.getOwner();
        String color2 = property.getColor2();
        String housing;
        fieldList.get(fieldPosition).replace("color2", color2);
        if (owner != null) {
            fieldList.get(fieldPosition).replace("owner", owner);
        }
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
        Map<String, String> fieldAsMap;
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

        Map<String, String> fieldAsMap;
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
        Player player = playerController.getPlayerFromID(playerID);
        if (startPosition > currentPosition && passStart) {
            System.out.println("Spilleren har passeret start");
            landOnStart(playerID, ((StartField) fields.get(0)));
        }
        // Check for type of field
        currentField = fields.get(currentPosition);
        // Redirects to landOn "fieldType"
        if (currentField instanceof Property) {
            String propertyname = ((Property) currentField).name;
            String description = ((Property) currentField).description;
            //HUUUUGE string construction for showing in gui. Could be done with loop but theres no real reason to
            String currentrent = "Nuværende: " + Integer.toString(((Property) currentField).getCurrentRentToShow());
            String rent0 = "Husleje med 0 bygninger: " + Integer.toString(((Property) currentField).getRent0());
            String rent1 = "Husleje med 1 hus: " + Integer.toString(((Property) currentField).getRent1());
            String rent2 = "Husleje med 2 huse: " + Integer.toString(((Property) currentField).getRent2());
            String rent3 = "Husleje med 3 huse: " + Integer.toString(((Property) currentField).getRent3());
            String rent4 = "Husleje med 4 huse: " + Integer.toString(((Property) currentField).getRent4());
            String rent5 = "Husleje med 1 hotel: " + Integer.toString(((Property) currentField).getRent5());
            String boatrent1 = "Husleje med 1 båd: " + Integer.toString(((Property) currentField).getRent0());
            String boatrent2 = "Husleje med 2 både: " + Integer.toString(((Property) currentField).getRent1());
            String boatrent3 = "Husleje med 3 både: " + Integer.toString(((Property) currentField).getRent2());
            String boatrent4 = "Husleje med 4 både: " + Integer.toString(((Property) currentField).getRent3());
            String breweryrent1 = "Husleje med 1 bryggeri: " + Integer.toString(((Property) currentField).getRent0());
            String breweryrent2 = "Husleje med 2 bryggerier: " + Integer.toString(((Property) currentField).getRent1());

            if (description == null) {
                description = "";
            }
            if (currentField instanceof Ferry){
                gui.displayInMiddle(propertyname,description,boatrent1,boatrent2,boatrent3,boatrent4);
            }
            if (currentField instanceof Brewery){
                gui.displayInMiddle(propertyname,description,breweryrent1,breweryrent2);
            }
            if (currentField instanceof Street){
                gui.displayInMiddle(propertyname,description,rent0,rent1,rent2,rent3,rent4,rent5,currentrent);
            }
        landOnProperty(playerID, ((Property) currentField));
        }
        else if (currentField instanceof Jail) {
            gui.displayInMiddle("Fængslet!");
            landOnJail(playerID, ((Jail) currentField));
        } else if (currentField instanceof StartField) {
            //landOnStart(playerID, ((StartField) currentField));
        } else if (currentField instanceof Chance) {
            landOnChance(playerID, ((Chance) currentField));
        } else if (currentField instanceof TaxField) {
            gui.displayInMiddle("Skat :(");
            landOnTax(playerID, ((TaxField) currentField));
        }
        gui.displayInMiddle("");

        // property, chance, jail, etc.
    }

    private void landOnJail(String playerID, Jail currentField) {
        Player player = playerController.getPlayerFromID(playerID);
        if (currentField.getInstanceOfJail() == 0) { //First field of jail
            if (player.getjailed() > 0) { //Is player even jailed?
                if (player.getJailCards() > 0) {
                    if (player.getjailed() < 4 || player.getBalance() > 999) {
                        String ans = gui.buttonRequest(player.getName() + " vil du bruge et jail-free card?", "ja", "nej");
                        if (ans.equals("ja")) {
                            player.useJailCard();
                            gui.buttonRequest("Godt valg! slå med terningerne", "Slå");
                            int rolls[] = player.rollDie();
                            gui.setDice(rolls);
                            int oldpos = player.getPosition();
                            player.movePosition(rolls[2]);
                            gui.movePlayerTo(playerID, oldpos, player.getPosition());
                            landOnField(playerID, oldpos, player.getPosition(), false); //Passstart shouldn't be permanently false but eh
                        } else {
                            jailWithoutCard(player, playerID);
                        }
                    } else {
                        gui.buttonRequest("Du er desværre nødsaget til at bruge dit jail-free card", "okay");
                        player.useJailCard();
                        //playerController.nextPlayer();
                    }
                } else jailWithoutCard(player, playerID);
            }
        } else { //This is what happens if you land on the second jail field (sending you to jail)
            gui.displayGeneralMessage("Du er desværre kommet i fængsel");
            playerController.getPlayerFromID(playerID).setjailed(1);
            int currentPosition = player.getPosition();
            int jailPosition = currentField.getFirstJailInstancePosition();
            player.setPosition(jailPosition);
            gui.movePlayerTo(playerID, currentPosition, player.getPosition());
        }
    }

    private void jailWithoutCard(Player player, String playerID) {

        if (player.getjailed() < 4) {
            String choice = gui.buttonRequest(player.getName() + " vil du slå med terningerne eller betale 1000 kr for at komme ud?", "Slå", "Betal");
            if (choice.equals("Slå")) {
                int[] rolls = player.rollDie();
                gui.setDice(rolls);
                player.setjailed(player.getjailed() + 1);
                if (rolls[0] == rolls[1]) {
                    player.setjailed(0);
                    gui.displayGeneralMessage("Tillykke! Du er kommet ud af fængslet");
                    int oldpos = player.getPosition();
                    player.movePosition(rolls[2]);
                    gui.movePlayerTo(playerID, oldpos, player.getPosition());
                    landOnField(playerID, oldpos, player.getPosition(), false); //Passstart shouldnt be permanently false but eh
                } else if (player.getjailed() < 4) {
                    System.out.println("Player" + player.getName() + "jailed status is: " + player.getjailed());
                    gui.buttonRequest("Det var desværre ikke to ens", "Okay");
                    //playerController.nextPlayer();

                } else {
                    payForJail(player);
                    int oldpos = player.getPosition();
                    player.movePosition(rolls[2]);
                    gui.movePlayerTo(playerID, oldpos, player.getPosition());
                    landOnField(playerID, oldpos, player.getPosition(), false);
                    //playerController.nextPlayer();
                }
            } else {
                payForJail(player);
                gui.buttonRequest("Godt valg! slå med terningerne", "Slå");
                int[] rolls = player.rollDie();
                int oldpos = player.getPosition();
                player.movePosition(rolls[2]);
                gui.movePlayerTo(playerID, oldpos, player.getPosition());
                landOnField(playerID, oldpos, player.getPosition(), false);
                //playerController.nextPlayer();
            }
        } else if (player.getjailed() == 4) {
            payForJail(player);
            gui.buttonRequest("Efter at have betalt 1000 kr. kommer du endelig ud. Slå med terningerne", "Slå");
            int[] rolls = player.rollDie();
            int oldpos = player.getjailed();
            player.movePosition(rolls[2]);
            gui.movePlayerTo(playerID, oldpos, player.getPosition());
            landOnField(playerID, oldpos, player.getPosition(), false);
            //playerController.nextPlayer();
            player = playerController.getCurrentPlayer();
        }
    }

    private void payForJail(Player player) {
        boolean transactionSuccess = createTransaction(player.getId(), null, -1000, true, "Du bliver nødt til at betale for at komme ud af fængsel, betal 1000 kr.");
        if (transactionSuccess) {
            player.setjailed(0);
        }
    }

    private void landOnStart(String playerID, StartField start) {
        int income = start.getIncome();
        String message = "Du passerede start, modtag " + income + " kroner!";
        createTransaction(playerID, null, income, false, message);
    }

    private void landOnProperty(String playerID, Property property) {
        String owner = property.getOwner();
        Player player = playerController.getPlayerFromID(playerID);
        if (owner == null) {
            System.out.println("This field is not owned by anyone!");
            String choice = gui.buttonRequest("Køb eller sæt på auktion?", "Køb", "Auktion");
            if (choice.equals("Køb")) {

                String message = property.buyMessage() + " for " + Math.abs(property.getPrice()) + " kroner?";
                boolean purchase = createTransaction(playerID, null, property.getPrice(), false, message);
                if (purchase) {
                    property.setOwner(playerID);
                } else {
                    gui.buttonRequest("Du har ikke råd. ", "Ok");
                    auction(playerID, property);
                }

                if (property.getOwner() != null) {
                    if (property.getPosition() == 5 || property.getPosition() == 15 || property.getPosition() == 25 || property.getPosition() == 35) {
                        player.addFerries();
                        System.out.println(player.getFerries());
                    }
                    if (property.getOwner().equals(playerID)) {
                        updateFieldMap(property);
                        updateGUI(property, playerID);
                    }
                }
            } else if (choice.equals("Auktion")) {
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
            System.out.println("Du ejer dette rederi. ");
        }
        else {
            int ferriesOwned = 0;
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
            String message = "Denne færge er ejet af en anden! Betal " + Math.abs(rent) + " kroner, da der ejes " + ferriesOwned + " færge(r). ";

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
            int breweryRent = totalDieRoll * rent;
            createTransaction(playerID, owner, breweryRent, true, "");
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

        /*If the field is owned by the current player, they have the choice to either buy or sell house.
        If they do not have any houses on the property, they can only buy.
         */
        if (owner.equals(playerID)) {
            System.out.println("Du ejer denne grund. ");
            Property property = (Property) street;
            //Checks if the owner has sufficient funds to buy a house
            if (balance >= street.getBuildPrice() && propertyBank.canBuyHouse(playerID, property.getNeighborhood()) && street.getHousing() < 5) {
                int nextBuildPrice = street.getBuildPrice();
                String response = "";

                //Checks if the owner has houses or not. If they do, they can either buy or sell a house
                if (street.getHousing() > 0) {
                    String message = "Ønsker du at købe eller sælge et hus i dette nabolag?";
                    response = gui.buttonRequest(message, "Køb", "Sælg");
                }
                //If the owner does not own any houses on the property, they can only buy a house.
                else {
                    String message = "Ønsker du at købe et hus på hver grund i dette nabolag for " + Math.abs(nextBuildPrice) * street.getGroupSize() + " kroner?";
                    response = gui.buttonRequest(message, "Køb", "Nej");
                }
                //The player chooses to buy a house for each property in the neighbourhood
                if (response.equals("Køb")) {
                    boolean transactionSuccess = createTransaction(playerID, null, nextBuildPrice * property.getGroupSize(), false, "Køb huse for " + Math.abs(nextBuildPrice * street.getGroupSize()) + " kroner.");
                    if (transactionSuccess) {

                        //Loops through all the properties in the neighbourhood and updates them with a house each
                        for (Property propertyInGroup : propertyBank.getPropertiesFromGroup(property.getNeighborhood())) {
                            propertyInGroup.buildHouse();
                            updateFieldMap(propertyInGroup);
                            updateGUI(propertyInGroup, playerID);
                        }
                    }
                }
                if (response.equals("Sælg")) {
                    sellHousing(playerID, street);
                }
            }

        } else {
            String message = "Dette felt er ejet af en anden!";
            int rent = street.getRent();
            System.out.println(rent);
            String receiverID = street.getOwner();
            Player receiver = playerController.getPlayerFromID(receiverID);


            int groupSize = street.getGroupSize();
            int ownedByOwner = 0;
            for (Property property : propertyBank.getPropertiesFromGroup(street.getNeighborhood())) {
                if (((Street) property).getOwner() != null) {
                    if (((Street) property).getOwner().equals(receiverID)) {
                        ownedByOwner += 1;
                    }
                }
            }
            if (groupSize == ownedByOwner && street.getHousing() == 0) {
                createTransaction(playerID, receiverID, rent * 2, true, message);
            } else {
                createTransaction(playerID, receiverID, rent, true, message);
            }
        }

    }

    private void landOnChance(String playerID, Chance chance) {
        Player player = playerController.getPlayerFromID(playerID);
        Random rand = new Random();
        int cardNumber = 0;
        if (chanceList.size() != 1) {
            cardNumber = rand.nextInt(0, chanceList.size() - 1);
        }
        Map<String, String> rawCard = chanceList.get(cardNumber);
        Map<String, String> card = new HashMap<>();
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
            gui.displayInMiddle(message);
            switch (key) {
                case "Cash added" -> {
                    System.out.println("Cash added");
                    int amount = Integer.parseInt(card.get(key));
                    createTransaction(playerID, null, amount, true, message);
                }
                case "CashAddedPerHouse" -> {
                    System.out.println("CashAddedPerHouse");
                    int ownedHouses = 0;
                    ArrayList<FieldSpaces> ownedFields = lookUpFields(fields, "owner", playerID);
                    ArrayList<FieldSpaces> ownedStreets = lookUpFields(ownedFields, "fieldType", "street");
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
                    ArrayList<FieldSpaces> ownedFields = lookUpFields(fields, "owner", playerID);
                    ArrayList<FieldSpaces> ownedStreets = lookUpFields(ownedFields, "fieldType", "street");
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
                    gui.buttonRequest(message, "Ok");
                    int currentPosition = playerController.getPlayerFromID(playerID).getPosition();
                    if (moveBy < 0) {
                        moveBy += 40;
                    }
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
                            } else if (fieldShortestDistance instanceof Street) {
                                rentToPay = ((Street) fieldShortestDistance).getRent() * rentMultiplier;
                            } else if (fieldShortestDistance instanceof Brewery) {
                                rentToPay = ((Brewery) fieldShortestDistance).getRent(totalOwnedByOwner) * rentMultiplier;
                            }
                            player.setPosition(closest);
                            gui.movePlayerTo(playerID, currentPlayerPosition, closest);
                            createTransaction(playerID, owner, rentToPay, true, message);
                        } else {
                            player.setPosition(closest);
                            gui.movePlayerTo(playerID, currentPlayerPosition, closest);
                            landOnField(playerID, currentPlayerPosition, player.getPosition(), payIfCrossStart);
                        }
                    } else {
                        player.setPosition(closest);
                        gui.movePlayerTo(playerID, currentPlayerPosition, closest);
                        landOnField(playerID, currentPlayerPosition, player.getPosition(), payIfCrossStart);
                    }

                }
                case "MoveTo" -> {
                    System.out.println("MoveTo");
                    int cardPosition = Integer.parseInt(card.get(key));
                    gui.buttonRequest(message, "Ok");
                    int startPlayerPosition = player.getPosition();
                    player.setPosition(cardPosition);
                    gui.movePlayerTo(playerID, startPlayerPosition, player.getPosition());
                    landOnField(playerID, startPlayerPosition, player.getPosition(), payIfCrossStart);
                }
                case "JailFreeCard" -> {
                    System.out.println("JailFreeCard");
                    gui.buttonRequest(message, "Ok");
                    player.addJailCard();
                }
            }
        }
        gui.displayInMiddle("");
    }

    public void landOnTax(String playerID, TaxField currentField) {
        int bill = currentField.getBill();
        String message = "Skattefar giver dig et skattesmæk på " + Math.abs(bill) + " kroner.";
        createTransaction(playerID, null, bill, true, message);
    }



    private void auction(String playerID, Property property) {

        ArrayList<Player> otherPlayers = new ArrayList<>();
        Map<Integer, Player> bidMap = new HashMap<>();
        ArrayList<String> allPlayerIDs = playerController.getAllPlayerIDs();
        allPlayerIDs.remove(playerID);

        for (String id : allPlayerIDs) {
            if (playerController.getPlayerFromID(id).getBalance() >= property.getPrice() && !id.equals(playerID) ) {
                otherPlayers.add(playerController.getPlayerFromID(id));
            }
        }

        int amount = property.getPrice();
        Player winner;
        int highestBid = 0;

        if (playerController.getAllPlayerIDs().size() > 2) {
            gui.buttonRequest("Blind auktion! Alle spillere, som har råd til at købe feltet, " +
                    "får muligheden for at byde. Den spiller som byder højest vinder auktionen. " +
                    "Når en spiller placerer sit bud, bedes alle andre spillere kigge den anden vej. ", "Start Auktion");


            for (Player otherPlayer : otherPlayers) {

                String bidRequest = gui.buttonRequest("Vil du gerne byde, " + otherPlayer.getName() + "?", "Ja", "Nej");
                if (bidRequest.equals("Ja")) {
                    int bid = gui.intRequest(otherPlayer.getName() + ": Du skal byde højere end " + Math.abs(amount), Math.abs(amount) + 1, otherPlayer.getBalance());
                    bidMap.put(bid, otherPlayer);
                }
            }

            for (int bid : bidMap.keySet()) {
                if (bid > highestBid) {
                    highestBid = bid;
                }
            }
            winner = bidMap.get(highestBid);

            gui.buttonRequest(winner.getName() + " bød " + highestBid + " og vandt auktionen!", "Fortsæt");
        }
        else {
            winner = otherPlayers.get(0);
            highestBid = property.getPrice();
            String bidRequest = gui.buttonRequest(winner.getName() + " Vil du gerne købe grunden for, " + Math.abs(amount) + "?", "Ja", "Nej");
            if (bidRequest.equals("Nej")) {return; }
        }

        boolean transactionSuccess = createTransaction(winner.getId(), null, -Math.abs(highestBid), true, "Gennemfør betaling");
        if (transactionSuccess) {
            property.setOwner(winner.getId());
            updateFieldMap(property);
            updateGUI(property, winner.getId());
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
            gui.buttonRequest(message + " Betal " + playerController.getPlayerFromID(receiverID).getName() + " " + Math.abs(amount) + " kroner.", "Betal");
        }
        else {
            // Payments to the bank
            if (amount <= 0) {
                if (critical) {
                    userRequest = gui.buttonRequest(message, "Betal");
                }
                else {
                    userRequest = gui.buttonRequest(message, "Betal", "Annuller");
                    if (userRequest.equals("Annuller")) {
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
            gui.buttonRequest("Du er gået fallit og har tabt spillet... ", "Ok");
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

    public boolean sellHousing(String playerID, Street street){
        if (street.getHousing() > 0) {
            String sale = gui.buttonRequest("Vil du sælge et hus på hver ejendom i denne farvegruppe?", "Ja" , "Nej");
            Player player = playerController.getPlayerFromID(playerID);
            if (sale.equals("Ja")){
                int sellPrice = 0;
                for (Property propertyInGroup : propertyBank.getPropertiesFromGroup(street.getNeighborhood())) {
                    sellPrice += street.getPrice() * 0.5;
                    Street streetInGroup = (Street) propertyInGroup;
                    streetInGroup.setHousing(streetInGroup.getHousing() - 1);
                    updateFieldMap(propertyInGroup);
                    updateGUI(propertyInGroup, playerID);
                }
                createTransaction(playerID, null, -sellPrice, false, "Du har solgt ét hus på hver ejendom i området. Modtag " + Math.abs(sellPrice) + " kroner.");
            }
            else { return false; }
        }
        return true;
    }

    public boolean sellHousing(String playerID, String streetName){

        ArrayList<FieldSpaces> streetMatchList = lookUpFields(null, "title", streetName);
        if (streetMatchList.size() < 1) {
            gui.buttonRequest("Noget gik galt", "Fortsæt");
            return false;
        }

        Street street = (Street) streetMatchList.get(0);

        if (street.getHousing() > 0) {
            String sale = gui.buttonRequest("Vil du sælge et hus på hver ejendom i denne farvegruppe?", "Ja" , "Nej");
            Player player = playerController.getPlayerFromID(playerID);
            if (sale.equals("Ja")){
                int sellPrice = 0;
                for (Property propertyInGroup : propertyBank.getPropertiesFromGroup(street.getNeighborhood())) {
                    sellPrice += street.getPrice() * 0.5;
                    Street streetInGroup = (Street) propertyInGroup;
                    streetInGroup.setHousing(streetInGroup.getHousing() - 1);
                    updateFieldMap(propertyInGroup);
                    updateGUI(propertyInGroup, playerID);
                }
                createTransaction(playerID, null, -sellPrice, false, "Du har solgt ét hus på hver ejendom i området. Modtag " + Math.abs(sellPrice) + " kroner.");
            }
            else { return false; }

        }
        return true;
    }

    protected ArrayList<String> getPlayerOwnedHousing(String playerID) {
        ArrayList<String> playerOwnedHousing = new ArrayList<>();
        for (ArrayList<Property> propertyList : propertyBank.getPropertyMap().values()) {
            for (Property property : propertyList) {
                if (property instanceof Street) {
                    if (property.getOwner() != null) {
                        if (property.getOwner().equals(playerID) && ((Street) property).getHousing() > 0) {
                            playerOwnedHousing.add(property.getName());
                        }
                    }
                }
            }
        }
        return playerOwnedHousing;
    }

    protected void updateGUI(Property property, String playerID) {
        String playerColor = playerController.getPlayerFromID(playerID).getColor();
        if (property instanceof Street) {
            gui.updateProperty(property.getPosition(), playerColor, ((Street) property).getHousing());
        } else {
            gui.updateProperty(property.getPosition(), playerColor);
        }
    }

    private void loadInPlayers() {
        ArrayList<String> allPlayerIDs = playerController.getAllPlayerIDs();
        for (String id : allPlayerIDs) {
            Player player = playerController.getPlayerFromID(id);
            gui.addPlayer(player.getId(), player.getName(), player.getBalance(), player.getPosition(), player.getColor());
        }

    }

}
