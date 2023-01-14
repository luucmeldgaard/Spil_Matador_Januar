package dtu.matador.game;

public abstract class Property implements PropertyFields {
    //Creating variables that will be used
    private static FieldController controller;
    private final String name;
    private final String subtext;
    private final String description;
    private int rent;
    private final int rent0;
    private final int rent1;
    private final int rent2;
    private final int rent3;
    private final int rent4;
    private final int rent5;
    private final String color1;
    private String color2;
    private final String neighborhood;
    //boolean purchasable = false;
    private final int price;
    private final int pawnForAmount;
    private final int position;
    private String owner;
    private int buildPrice;
    protected int housing;

    private final int groupSize;


    //Takes input to create the class
    public Property(FieldController controller, String name, String subtext, String description, String rent,
                    String rent1, String rent2, String rent3, String rent4, String rent5,
                    String color1, String color2, String price,
                    String pawnForAmount, String position, String owner, String neighborhood, String groupSize){

        this.controller = controller;
        this.name = name;
        this.subtext = subtext;
        this.description = description;
        this.rent0 = Integer.parseInt(rent);
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
        this.groupSize = Integer.parseInt(groupSize);

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

    private int currentRent() {
        int[] rent = new int[] {this.rent0, this.rent1, this.rent2, this.rent3, this.rent4, this.rent5};
        return rent[this.housing];
    }
    public int getRent() {
        System.out.println("normal rent: " + this.rent0 + " actual rent with " + this.housing + " housing: " + currentRent());
        return currentRent();
    }
    public int getRent1(){return rent1;}
    public int getRent2(){return rent2;}
    public int getRent3(){return rent3;}

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
    public String getNeighborhood(){
        return neighborhood;
    }
    public void buildHouse(){
        this.housing += 1;
    }

    public int getGroupSize(){
        return groupSize;
    }
}




