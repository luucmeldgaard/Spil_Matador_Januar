package dtu.matador.game;

public abstract class Property implements PropertyFields {

    //Creating variables that will be used
    protected final String name;
    protected final String subtext;
    protected final String description;
    protected int rent;
    protected int rent0;
    protected int rent1;
    protected int rent2;
    protected int rent3;
    protected int rent4;
    protected int rent5;
    protected String color1;
    protected String color2;
    protected final String neighborhood;
    //boolean purchasable = false;
    protected final int price;
    protected final int pawnForAmount;
    protected final int position;
    private String owner;
    protected int housing;
    protected boolean pawned;

    private final int groupSize;


    //Takes input to create the class
    public Property(String name, String subtext, String description, String rent,
                    String rent1, String rent2, String rent3, String rent4, String rent5,
                    String color1, String color2, String price,
                    String pawnForAmount, String position, String owner, String neighborhood, String groupSize){

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
        this.pawned = pawned;

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

    public void setOwner(String playerID) { this.owner = playerID; }

    public int getPrice() {
        return -this.price;
    }

    public int getPosition() {
        return this.position;
    }
    public int getRent0(){return rent0;}
    public int getRent1(){return rent1;}
    public int getRent2(){return rent2;}
    public int getRent3(){return rent3;}
    public int getRent4(){return rent4;}
    public int getRent5(){return rent5;}


    //A player can buy with the help of a transaction
    public String buyMessage() {
        return "??nsker du at k??be " + this.name;
    }

    public String auctionMessage() {
        return this.name + " er nu p?? auktion og s??lges til den h??jeste byder!";

    }

    public String getNeighborhood(){
        return neighborhood;
    }
    public void buildHouse(){
        this.housing += 1;
    }

    public int getCurrentRentToShow(){
        if (this.housing == 0) {return this.rent0;}
        if (this.housing == 1) {return getRent1();}
        if (this.housing == 2) {return getRent2();}
        if (this.housing == 3) {return getRent3();}
        if (this.housing == 4) {return this.rent4;} //This might not be the cleanest thing in the world
        if (this.housing == 5) {return this.rent5;}
        return 0;
    }

    public int getGroupSize(){
        return groupSize;
    }

}




