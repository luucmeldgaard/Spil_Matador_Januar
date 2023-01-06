package dtu.matador.game;

public abstract class NonPurchasableFields implements FieldSpaces {
    String name;
    String subtext;
    String description;
    int rent;
    String color1;
    String color2;
    public String getName() {
        return this.name;
    }

    public String getSubtext() {
        return this.subtext;
    }

    public String getDescription() {
        return this.description;
    }

    public String getColor1() {
        return this.color1;
    }

    public String getColor2() {
        return this.color2;
    }

}
