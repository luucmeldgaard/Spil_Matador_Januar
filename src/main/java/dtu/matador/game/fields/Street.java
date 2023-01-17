package dtu.matador.game.fields;

import dtu.matador.game.FieldController;
import dtu.matador.game.Property;

public class Street extends Property {

    int buildPrice;

    public Street(String name, String subtext, String description, String rent, String rent1, String rent2, String rent3, String rent4, String rent5,
                  String color1, String color2, String price, String pawnForAmount, String position, String owner, String housing, String neighborhood, String groupSize, String buildPrice) {
        super(name, subtext, description, rent, rent1, rent2, rent3, rent4, rent5, color1, color2, price, pawnForAmount, position, owner, neighborhood, groupSize);

        this.housing = Integer.parseInt(housing);
        this.buildPrice = Integer.parseInt(buildPrice);

    }

    //Returns the rent of a street, based on how many houses are owned
    private int currentRent(int owned) {
        int[] rent = new int[] {this.rent0, this.rent1, this.rent2, this.rent3, this.rent4, this.rent5};
        return rent[this.housing];
    }

    //Gets the current rent and subtracts it from the player that lands on a street someone else owns.
    public int getRent() {
        return -currentRent(this.housing);
    }

    //Gets the buildPrice of a house on the specific street and substracts it from the player choosing to buy a house
    public int getBuildPrice(){
        return -this.buildPrice;
    }

    public int getHousing() {
        return this.housing;
    }

    public void setHousing(int amount) {
        this.housing = amount;
    }

}
