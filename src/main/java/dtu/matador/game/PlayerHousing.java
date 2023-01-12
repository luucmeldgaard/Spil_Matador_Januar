package dtu.matador.game;

import java.awt.*;
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

    public ArrayList<Property> getPropertiesFromColor(String color){
        return getPropertyMap().get(color);
    }
}
