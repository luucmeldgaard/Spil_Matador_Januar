package dtu.matador.game.fields;

import dtu.matador.game.FieldController;
import dtu.matador.game.NonPurchasableFields;

public class TaxField extends NonPurchasableFields {
    String name;
    String subtext;
    String description;
    String color1;
    String color2;
    int bill;

    public TaxField(String name, String subtext, String description, String color1, String color2, String bill, String position) {
        super(name, subtext, description, color1, color2, position);
        this.name = name;
        this.subtext = subtext;
        this.description = description;
        this.color1 = color1;
        this.color2 = color2;
        this.bill = Integer.parseInt(bill);

    }

    //Returns a bill to the player, so they can pay it.
    public int getBill() {
        return -this.bill;
    }
}
