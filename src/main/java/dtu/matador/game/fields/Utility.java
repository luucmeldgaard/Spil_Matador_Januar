package dtu.matador.game.fields;

import dtu.matador.game.Property;

public class Utility extends Property {
    String name;
    String subtext;
    String description;
    String rent;
    String color1;
    String color2;
    String price;
    String owner;
    public Utility(String name, String subtext, String description, String rent, String color1, String color2, String price, String owner) {
        super(name, subtext, description, rent, color1, color2, price, owner);
        this.name = name;
        this.subtext = subtext;
        this.description = description;
        this.rent = rent;
        this.color1 = color1;
        this.color2 = color2;
        this.price = price;
        this.owner = owner;
    }
}
