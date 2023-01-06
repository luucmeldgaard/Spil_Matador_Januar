package dtu.matador.game;

public abstract class Property implements Properties {
    //Creating variables that will be used
    String name;
    String subtext;
    String description;
    int rent;
    String color1;
    String color2;
    String owner;
    boolean purchasable = false;
    int price;

    //Takes input to create the class
    public Property(String name, String subtext, String description, String rent,
                    String color1,String color2, String price, String owner){
        this.name = name;
        this.subtext = subtext;
        this.description = description;
        this.rent = Integer.parseInt(rent);
        this.price = Integer.parseInt(price);
        this.color1 = color1;
        this.color2 = color2;
        this.owner = owner;
    }
    //Generic getters and setters
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

    public String getColor2() {return this.color2;}

    public boolean getPurchasable() {
        return purchasable;
    }

    public int getPrice() {
        return price;
    }

    public int getRent() {
        return rent;
    }

    public String getOwner() {
        return this.owner;
    }
}




