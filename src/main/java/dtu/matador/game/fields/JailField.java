package dtu.matador.game.fields;

import dtu.matador.game.NonPurchasableFields;

public class JailField extends NonPurchasableFields {
    String name;
    String subtext;
    String description;
    String color1;
    String color2;

    public JailField(String name, String subtext, String description, String color1, String color2) {
        super(name, subtext, description, color1, color2);
        this.name = name;
        this.subtext = subtext;
        this.description = description;
        this.color1 = color1;
        this.color2 = color2;

    }

}
