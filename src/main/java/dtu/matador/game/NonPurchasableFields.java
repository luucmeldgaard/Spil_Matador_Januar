package dtu.matador.game;

import java.util.Map;

public abstract class NonPurchasableFields implements FieldSpaces {
    String name;
    String subtext;
    String description;
    String color1;
    String color2;

    public NonPurchasableFields(String name, String subtext, String description, String color1, String color2) {
        this.name = name;
        this.subtext = subtext;
        this.description = description;
        this.color1 = color1;
        this.color2 = color2;
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

    public Map<String, String> updateFieldMap() {
        return null;
    }

}
