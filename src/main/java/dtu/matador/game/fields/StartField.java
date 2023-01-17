package dtu.matador.game.fields;

import dtu.matador.game.FieldController;
import dtu.matador.game.NonPurchasableFields;

public class StartField extends NonPurchasableFields {
    String name;
    String subtext;
    String description;
    String color1;
    String color2;
    int income;

    public StartField(String name, String subtext, String description, String color1, String color2, String position, String income) {
        super(name, subtext, description, color1, color2, position);
        this.name = name;
        this.subtext = subtext;
        this.description = description;
        this.color1 = color1;
        this.color2 = color2;
        this.income = Integer.parseInt(income);
    }


    public int getIncome() {
        return this.income;
    }

}
