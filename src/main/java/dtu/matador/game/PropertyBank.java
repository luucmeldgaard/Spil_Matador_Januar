package dtu.matador.game;

import dtu.matador.game.fields.Street;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertyBank {

    private final HashMap<String, ArrayList<Property>> propertyMap;
    public PropertyBank(ArrayList<Map<String, String>> fieldList, ArrayList<String> allPlayerIDs) {
        this.propertyMap = new HashMap<>();
    }

    public HashMap<String, ArrayList<Property>> getPropertyMap() {
        return propertyMap;
    }

    public FieldSpaces addProperty(String group, Property property){
        if (!getPropertyMap().containsKey(group)) {
            getPropertyMap().put(group, new ArrayList<>());
        }
        getPropertyMap().get(group).add(property);
        return ((FieldSpaces) property);
    }

    public ArrayList<Property> getPropertiesFromGroup(String group){
        return getPropertyMap().get(group);
    }

    public boolean canBuyHouse(String playerID, String group){

        ArrayList<Property> propertyList = getPropertiesFromGroup(group);
        if (propertyList == null) {
            System.out.println("Neighborhood doesn't exist");
            return false; };

        for (Property property : propertyList) {
            if (!(property instanceof Street)) {
                System.out.println("Property is not an instance of Street");
                return false;
            }
            if (property.getOwner() != playerID) {
                return false;
            }
        }
        return true;

    }
}
