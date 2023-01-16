package dtu.matador.game;

//NonPurchasableFields is implemented as an abstract class
public abstract class NonPurchasableFields implements FieldSpaces {
    private final String name;
    private final String subtext;
    private final String description;
    private final String color1;
    private final String color2;
    private final int position;

    //NonPurchasableFields has all the parameters that the properties which you cannot own, have in common
    public NonPurchasableFields(String name, String subtext, String description, String color1, String color2, String position) {
        this.name = name;
        this.subtext = subtext;
        this.description = description;
        this.color1 = color1;
        this.color2 = color2;
        this.position = Integer.parseInt(position);
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

    public String getColor1() {
        return this.color1;
    }

    public String getColor2() {
        return this.color2;
    }

    public int getPosition() {return this.position;}

}
