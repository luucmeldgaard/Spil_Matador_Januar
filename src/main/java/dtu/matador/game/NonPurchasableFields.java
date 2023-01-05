package dtu.matador.game;

public abstract class NonPurchasableFields implements FieldSpaces {
    String name;
    String subtext;
    String description;
    String color_1;
    String color_2;

    public NonPurchasableFields(String name, String subtext, String description, String color_1, String color_2) {
        this.name = name;
        this.subtext = subtext;
        this.description = description;
        this.color_1 = color_1;
        this.color_2 = color_2;
    }

    public String getName() {
        return this.name;
    }

    public String getSubtext() {
        return this.subtext;
    }

    public String getDescription() {
        return this.description;
    }

    public String getColor_1() {
        return this.color_1;
    }

    public String getColor_2() {
        return this.color_2;
    }

}
