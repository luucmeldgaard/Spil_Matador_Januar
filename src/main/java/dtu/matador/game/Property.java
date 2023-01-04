package dtu.matador.game;

public abstract class Property implements Properties {
    //Creating variables that will be used
    String propertyName;
    String subtext;
    String description;
    int rent;
    String color_1;
    String color_2;
    String owner;
    boolean purchaseable = false;
    int price;

    //Takes input to create the class
    public Property(String name, String subtext, String description, String rent,
                    String color_1,String color_2, String price, String owner){
        this.propertyName = name;
        this.subtext = subtext;
        this.description = description;
        this.rent = Integer.parseInt(rent);
        this.price = Integer.parseInt(price);
        this.color_1 = color_1;
        this.color_2 = color_2;
        this.owner = owner;
    }
}




