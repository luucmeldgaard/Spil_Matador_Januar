package dtu.matador.game;

import java.util.HashMap;
import java.util.Map;

public class Player {

    //A player's attributes are implemented

    static int nextId = 0;
    private String id;
    private final String name;
    private final String color;
    private int position;
    private int balance;
    private static final DiceCup diceCup = new DiceCup();
    private int boardSize;

    private int jailed = 0;

    private int jailCards;

    private int[] lastPlayedDieRoll;

    public Player(String name, String color, String position, String balance, String id,
                  String boardSize, String jailed, String jailCards, String lastPlayedDieRoll0,
                  String lastPlayedDieRoll1) {
        this.name = name;
        this.color = color;
        this.position = Integer.parseInt(position);
        this.balance = Integer.parseInt(balance);
        this.jailCards = Integer.parseInt(jailCards);
        this.id = id;
        this.boardSize = Integer.parseInt(boardSize);
        this.jailed = Integer.parseInt(jailed);
        this.lastPlayedDieRoll = new int[] {Integer.parseInt(lastPlayedDieRoll0), Integer.parseInt(lastPlayedDieRoll1)};

    }

    public Player(String name, String color, int position, int balance) {
        this.name = name;
        this.color = color;
        this.position = position;
        this.balance = balance;
        this.jailCards = 0;
        setId();
    }

    /*Rolls number of dice and updates a player's
    /position, and returns an int[] of dieFaces
     */
    protected int[] rollDie() {
        this.lastPlayedDieRoll = diceCup.roll();
        return this.lastPlayedDieRoll;
    }

    public String getId() {return this.id;}
    public void setId() {
        this.id = String.valueOf(nextId);
        nextId += 1;
    }

    //Return name, color and position
    public String getName(){
        return this.name;
    }

    public String getColor(){
        return this.color;
    }

    public int getPosition(){
        return this.position;
    }

    public int getBalance() {return this.balance;}

    public void setPosition(int position) {this.position = position % boardSize; }

    //Prints a move from the player and updates their position
    public void movePosition(int move){
        setPosition(this.position += move);
    }

    public void setBoardSize(int size) {boardSize = size; }

    public void addBalance(int amount) {
        this.balance += amount;
    }
    public void setBalance(int amount) { this.balance = amount;}
    public void setjailed (int jailedstatus){
        jailed = jailedstatus;
    }

    public int getjailed(){
        return jailed;
    }

    public boolean balanceCheck(int amount) {
        int nextBalance = this.balance + amount;
        return nextBalance >= 0;
    }

    public void addJailCard() {
        this.jailCards += 1;
    }

    public boolean useJailCard() { //This method currently seems useless, because we aren't going to present the user the option to use
        //the jail card if they dont have one, and so we need to check whether they have one before, and therefore the return statement
        //is unnessecary
        if (this.jailCards >= 1) {
            this.jailCards -= 1;
            jailed = 0;
            return true;
        }
        else {return false;}
    }

    public int getJailCards() { return this.jailCards; }
    public void setJailCards(int amount) { this.jailCards = amount; }

    public int[] getLastPlayedDieRoll() {
        return this.lastPlayedDieRoll;
    }
    public void setLastPlayedDieRoll(int[] dieRoll) { this.lastPlayedDieRoll = dieRoll; }

    public Map<String, String> snapshot() {
        Map<String, String> playerInfoToMap = new HashMap<>();
        playerInfoToMap.put("id", this.id);
        playerInfoToMap.put("name", this.name);
        playerInfoToMap.put("color", this.color);
        playerInfoToMap.put("position", String.valueOf(this.position));
        playerInfoToMap.put("balance", String.valueOf(this.balance));
        playerInfoToMap.put("boardSize", String.valueOf(boardSize));
        playerInfoToMap.put("jailed", String.valueOf(this.jailed));
        playerInfoToMap.put("jailCards", String.valueOf(this.jailCards));
        playerInfoToMap.put("lastPlayedDieRoll0", String.valueOf(this.lastPlayedDieRoll[0]));
        playerInfoToMap.put("lastPlayedDieRoll1", String.valueOf(this.lastPlayedDieRoll[1]));
        return playerInfoToMap;
    }
}
