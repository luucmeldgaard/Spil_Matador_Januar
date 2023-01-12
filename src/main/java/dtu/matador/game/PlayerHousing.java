package dtu.matador.game;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayerHousing {

    private HashMap<String, ArrayList<Property>> propertyMap = new HashMap<>();


    public HashMap<String, ArrayList<Property>> getPropertyMap() {
        return propertyMap;
    }

    public void addProperty(String color, ArrayList<Property> property){
        getPropertyMap().put(color, property);
    }

    public ArrayList<Property> getPropertiesFromColors(String color){

    }
}
