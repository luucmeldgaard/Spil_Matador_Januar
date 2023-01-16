package dtu.matador.game.fields;

import dtu.matador.game.FieldController;
import dtu.matador.game.Property;

public class Brewery extends Property {
    public Brewery(String name, String subtext, String description, String rent, String rent1, String rent2, String rent3, String rent4, String rent5, String color1, String color2, String price, String pawnForAmount, String position, String owner, String neighborhood, String groupSize) {
        super(name, subtext, description, rent, rent1, rent2, rent3, rent4, rent5, color1, color2, price, pawnForAmount, position, owner, neighborhood, groupSize);
    }

    public int getRent(int breweriesOwned) {
        int[] owned = new int[] {rent0, rent1};
        return owned[breweriesOwned-1];
    }

    public int getMultiplier(int amount) {
        int[] multiplier = new int[]{4, 10};
        if (amount == 1) { return multiplier[0]; }
        else { return multiplier[1]; }
    }

}
