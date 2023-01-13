package dtu.matador.game.fields;

import dtu.matador.game.FieldController;
import dtu.matador.game.NonPurchasableFields;

public class Jail extends NonPurchasableFields {
    String name;
    String subtext;
    String description;
    String color1;
    String color2;

    public Jail(FieldController controller, String name, String subtext, String description, String color1, String color2, String position) {
        super(controller, name, subtext, description, color1, color2, position);
        this.name = name;
        this.subtext = subtext;
        this.description = description;
        this.color1 = color1;
        this.color2 = color2;

    }

}
