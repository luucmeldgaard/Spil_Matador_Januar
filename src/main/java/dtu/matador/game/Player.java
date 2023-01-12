package dtu.matador.game;

import java.awt.*;

public class Player {

    //A player's attributes are implemented

    static int nextId = 0;
    String id;
    String name;
    String color;
    int position;
    int balance;
    static DiceCup diceCup = new DiceCup();
    static int boardSize;

    private PlayerHousing playerHousing;

    public Player(String name, String color, int position, int balance) {
        this.name = name;
        this.color = color;
        this.position = position;
        this.balance = balance;
        this.playerHousing = new PlayerHousing();
        setId();
    }

    /*Rolls number of dice and updates a player's
    /position, and returns an int[] of dieFaces
     */
    public int[] rollDie() {
        return diceCup.roll();
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

    public boolean getFirstturn(){return this.firstturn;}

    public boolean changeFirstturn() {
        return firstturn = false;
    }

    public void setPosition(int position) {this.position = position % boardSize; }

    //Prints a move from the player and updates their position
    public void movePosition(int move){
        setPosition(this.position += move);
    }

    public void setBoardSize(int size) {boardSize = size; }

    public void addBalance(int amount) {
        this.balance += amount;
    }

    public boolean balanceCheck(int amount) {
        int nextBalance = this.balance + amount;
        return nextBalance >= 0;
    }

    public PlayerHousing getPlayerHousing() {
        return playerHousing;
    }
}
