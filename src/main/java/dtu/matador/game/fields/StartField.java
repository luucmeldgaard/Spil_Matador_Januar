package dtu.matador.game.fields;

import dtu.matador.game.NonPurchasableFields;

public class StartField extends NonPurchasableFields {
    String name;
    String subtext;
    String description;
    String color_1;
    String color_2;

    public StartField(String name, String subtext, String description, String color_1, String color_2) {
        super(name, subtext, description, color_1, color_2);
        this.name = name;
        this.subtext = subtext;
        this.description = description;
        this.color_1 = color_1;
        this.color_2 = color_2;

    }
}
