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
    int rent1;
    int rent2;
    int rent3;
    int rent4;
    int rent5;
    String color1;
    String color2;
    String neighborhood;
    boolean purchasable = false;
    int price;
    int pawnForAmount;
    int position;
    String owner;
    int buildPrice;
    protected int housing; // MÅSKE VIRKER DENNE HER IKKE?????


    //Takes input to create the class
    public Property(String name, String subtext, String description, String rent,
                    String rent1, String rent2, String rent3, String rent4, String rent5,
                    String color1,String color2, String price,
                    String pawnForAmount, String position, String owner, String neighborhood){

        this.name = name;
        this.subtext = subtext;
        this.description = description;
        this.rent = Integer.parseInt(rent);
        this.rent1 =Integer.parseInt(rent1);
        this.rent2 =Integer.parseInt(rent2);
        this.rent3 =Integer.parseInt(rent3);
        this.rent4 =Integer.parseInt(rent4);
        this.rent5 =Integer.parseInt(rent5);
        this.color1 = color1;
        this.color2 = color2;
        this.price = Integer.parseInt(price);
        this.pawnForAmount = Integer.parseInt(pawnForAmount);
        this.position = Integer.parseInt(position);
        this.owner = owner;
        this.neighborhood = neighborhood;

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

    public String getColor2() {return this.color2; }

    public void setColor2(String color) {this.color2 = color;}

    public String getOwner() {
        return this.owner;
    }

    public int getPrice() {
        return price;
    }

    public int getPosition() {
        return this.position;
    }

    public int getRent() {
        return -rent;
    }
    public int getRent1() {
        return -rent1;
    }
    public int getRent2() {
        return -rent2;
    }
    public int getRent3() {
        return -rent3;
    }
    public int getRent4() {
        return -rent4;
    }
    public int getRent5() {
        return -rent5;
    }

    //A player can buy with the help of a transaction
    public void buy(String playerID) {
        String message = "Do you want to purchase " + this.name + "?";
        boolean purchase = controller.createTransaction(playerID,null, -this.price, false, message);
        if (purchase) {
            this.owner = playerID;
        }
        else {
            System.out.println("You have insufficient funds");
            controller.insufficientFunds();
            auction(playerID);
        }
    }

    public void auction(String playerID) {

    }

    public int getBuildPrice(){
        return buildPrice;
    }

    public void buildHouse(){
        this.housing += 1;
    }
}




