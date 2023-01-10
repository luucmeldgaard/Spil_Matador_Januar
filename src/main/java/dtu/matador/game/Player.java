package dtu.matador.game;

import java.awt.*;

public class Player {

    //A player's attributes are implemented

    static int nextId = 1;
    String id;
    String name;
    Color color;
    int position;
    int balance;
    static DiceCup diceCup = new DiceCup();
    static int boardSize;

    public Player(String name, Color color, int position, int balance) {
        this.name = name;
        this.color = color;
        this.position = position;
        this.balance = balance;
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

    public Color getColor(){
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

    public boolean addBalance(int balanceChange) {
        int nextBalance = this.balance + balanceChange;
        if (nextBalance >= 0) {
            this.balance += balanceChange;
            return true;
        }
        else {return false; }
    }
}
