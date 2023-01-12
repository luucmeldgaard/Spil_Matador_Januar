package dtu.matador.game.fields;

import dtu.matador.game.FieldController;
import dtu.matador.game.Property;

public class Street extends Property {
    public Street(String name, String subtext, String description, String rent, String rent1, String rent2, String rent3, String rent4, String rent5,
                  String color1, String color2, String price, String pawnForAmount, String position, String owner, String housing) {
        super(name, subtext, description, rent, rent1, rent2, rent3, rent4, rent5, color1, color2, price, pawnForAmount, position, owner);

        this.housing = Integer.parseInt(housing);

    }

    public int getHousing() {
        return this.housing;
    }

}
