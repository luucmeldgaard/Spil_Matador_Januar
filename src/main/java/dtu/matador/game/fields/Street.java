package dtu.matador.game.fields;

import dtu.matador.game.FieldController;
import dtu.matador.game.Property;

public class Street extends Property {
    public Street(String name, String subtext, String description, String rent, String rent1, String rent2, String rent3, String rent4, String rent5,
                  String color1, String color2, String price, String pawnForAmount, String position, String owner, String housing, String neighborhood, String groupSize) {
        super(name, subtext, description, rent, rent1, rent2, rent3, rent4, rent5, color1, color2, price, pawnForAmount, position, owner, neighborhood, groupSize);

        this.housing = Integer.parseInt(housing);

    }

    private int currentRent() {
        int[] rent = new int[] {this.rent0, this.rent1, this.rent2, this.rent3, this.rent4, this.rent5};
        return rent[this.housing];
    }

    @Override
    public int getRent(int ferriesOwned) {
        System.out.println("normal rent: " + this.rent0 + " actual rent with " + this.housing + " housing: " + currentRent());
        return currentRent();
    }

    public int getHousing() {
        return this.housing;
    }

    public void setHousing(int amount) {
        this.housing = amount;
    }

    public void sellHousing(String playerID, Street street){
        if (street.getOwner().equals(playerID) && street.getHousing() > 0) {
            street.setHousing(street.getHousing() - 1);
        }
    }

}
