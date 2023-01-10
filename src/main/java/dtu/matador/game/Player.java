package dtu.matador.game;

import java.awt.*;

public class Player {

    //A player's attributes are implemented

    String id;
    String name;
    Color color;
    int position;
    int balance;
    static DiceCup diceCup = new DiceCup();

    public Player(String name, Color color, int position, int balance) {
        this.name = name;
        this.color = color;
        this.position = position;
        this.balance = balance;
    }

    /**
     *
     * @return - returns the value of each die with the total as the last value
     */
    public int[] rollDice() {
        return diceCup.roll();
    }

    public String getId() {return this.id;}
    public void setId(String ID) {this.id = ID;}

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

    //Prints a move from the player and updates their position
    public void movePosition(int move){
        System.out.println(this.name + " moved " + move + " fields");
        this.position += move;
    }

}
