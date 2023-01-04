package dtu.matador.game;

public class Player {

    //A player's attributes are implemented
    String name;
    String color;
    int position;
    static DiceCup diceCup = new DiceCup();

    public Player(String name, String color, int position) {
        this.name = name;
        this.color = color;
        this.position = position;
    }

    public int rollDie() { return diceCup.roll(); }

    //Return name, color and position
    public String getname(){
        return this.name;
    }

    public String getColor(){
        return this.color;
    }

    public int getPosition(){
        return this.position;
    }

    //Prints a move from the player and updates their position
    public void movePosition(int move){
        System.out.println(this.name + " moved " + move + " fields");
        this.position += move;
    }

}
