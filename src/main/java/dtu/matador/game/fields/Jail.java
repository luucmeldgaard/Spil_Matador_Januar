package dtu.matador.game.fields;

import dtu.matador.game.FieldController;
import dtu.matador.game.NonPurchasableFields;

public class Jail extends NonPurchasableFields {

    //The variables InstanceOfJail and firstJailInstancePosition are static, since they belong to this class
    static int InstanceOfJail = 0;
    static int firstJailInstancePosition;
    int jailInstance;
    String name;
    String subtext;
    String description;
    String color1;
    String color2;

    public Jail(String name, String subtext, String description, String color1, String color2, String position) {
        super(name, subtext, description, color1, color2, position);
        this.name = name;
        this.subtext = subtext;
        this.description = description;
        this.color1 = color1;
        this.color2 = color2;
        setInstanceOfJail();


    }

    //Checks the instance of jail
    public void setInstanceOfJail() {
        if (InstanceOfJail == 0) {
            firstJailInstancePosition = this.getPosition();
        }
        this.jailInstance = InstanceOfJail;
        InstanceOfJail += 1;
        System.out.println("Instans af fængsel: " + this.jailInstance);
    }

    public int getInstanceOfJail() {
        return this.jailInstance;
    }

    public int getFirstJailInstancePosition() {
        return firstJailInstancePosition;
    }
}
