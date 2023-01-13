package dtu.matador.game.fields;

import dtu.matador.game.FieldController;
import dtu.matador.game.NonPurchasableFields;
import dtu.matador.game.Property;

public class Refuge extends NonPurchasableFields {
    public Refuge(FieldController controller, String name, String subtext, String description, String color1, String color2, String position) {
        super(controller, name, subtext, description, color1, color2, position);
    }
}
