package dtu.matador.game.fields;

import dtu.matador.game.Property;

public class Utility extends Property {
    String name;
    String subtext;
    String description;
    String rent;
    String color_1;
    String color_2;
    String price;
    String owner;
    public Utility(String name, String subtext, String description, String rent, String color_1, String color_2, String price, String owner) {
        super(name, subtext, description, rent, color_1, color_2, price, owner);
        this.name = name;
        this.subtext = subtext;
        this.description = description;
        this.rent = rent;
        this.color_1 = color_1;
        this.color_2 = color_2;
        this.price = price;
        this.owner = owner;
    }
}
