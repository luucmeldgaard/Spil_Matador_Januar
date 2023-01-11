package dtu.matador.game.fields;

import dtu.matador.game.FieldController;
import dtu.matador.game.Property;

public class Street extends Property {
    public Street(String name, String subtext, String description, String rent,
                  String color1, String color2, String price, String pawnForAmount, String position, String owner, String housing) {
        super(name, subtext, description, rent, color1, color2, price, pawnForAmount, position, owner);

        this.housing = Integer.parseInt(housing);

    }

    public int getHousing() {
        return this.housing;
    }

}
