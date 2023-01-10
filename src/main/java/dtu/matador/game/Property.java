package dtu.matador.game;

import com.sun.jdi.Field;

import java.util.HashMap;
import java.util.Map;

public abstract class Property implements PropertyFields {
    static FieldController controller = new FieldController();
    //Creating variables that will be used
    FieldController field;
    String name;
    String subtext;
    String description;
    int rent;
    String color1;
    String color2;
    String neighborhood;
    boolean purchasable = false;
    int price;
    int pawnForAmount;
    int position;
    String owner;

    //Takes input to create the class
    public Property(String name, String subtext, String description, String rent,
                    String color1,String color2, String price,
                    String pawnForAmount, String position, String owner){

        this.name = name;
        this.subtext = subtext;
        this.description = description;
        this.rent = Integer.parseInt(rent);
        this.color1 = color1;
        this.color2 = color2;
        this.price = Integer.parseInt(price);
        this.pawnForAmount = Integer.parseInt(pawnForAmount);
        this.position = Integer.parseInt(position);
        this.owner = owner;

        if (this.owner.equals("")) {
            this.owner = null;
        }
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

    public void setColor2(String color) {this.color2 = color;}

    public String getOwner() {
        return this.owner;
    }

    public int getPrice() {
        return price;
    }

    public int getRent() {
        return rent;
    }

    public void buy(String playerID) {
        boolean purchase = controller.bill(playerID, this.price);
        if (purchase) {
            this.owner = playerID;
        }
        else {
            System.out.println("You have insufficient funds");
        }
    }

    // Makes it possible to update Map being parsed to the gui and JSON later on
    public Map<String, String> updateGuiField() {
        Map<String, String> field = new HashMap<>();
        field.put("color2", this.color2);
        return field;
    }

    public void auction() {

    }
}




