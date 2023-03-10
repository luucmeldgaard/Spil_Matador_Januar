package dtu.matador.game;

import gui_fields.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

public class GUICreator {
    public GUICreator() {
    }
    //Checks what fieldType is used
    public GUI_Field[] setup(ArrayList<Map<String, String>> fieldMap) {

        GUI_Field[] guiFields = new GUI_Field[fieldMap.size()];
        for (Map<String, String> field : fieldMap) {
            System.out.println("Creating gui instance, nr.: " + field.get("position"));
            switch (field.get("fieldType")) {
                case "property" -> {
                    guiFields[Integer.parseInt(field.get("position"))] = new GUI_Street(field.get("title"), field.get("subText"), field.get("title"), field.get("rent"), Color.getColor(field.get("color1")), Color.getColor(field.get("color2")));
                }

                case "chance" -> {
                    guiFields[Integer.parseInt(field.get("position"))] = new GUI_Chance(field.get("title"), field.get("subText"), field.get("subText"), Color.getColor(field.get("color1")), Color.getColor(field.get("color2")));
                }
                case "jail" -> {
                    guiFields[Integer.parseInt(field.get("position"))] = new GUI_Jail("default", field.get("title"), field.get("subText"), field.get("subText"), Color.getColor(field.get("color1")), Color.getColor(field.get("color2")));
                }
                case "tax" -> {
                    guiFields[Integer.parseInt(field.get("position"))] = new GUI_Tax(field.get("title"), field.get("subText"), field.get("title"), Color.getColor(field.get("color1")), Color.getColor(field.get("color2")));
                }
                case "start" -> {
                    guiFields[Integer.parseInt(field.get("position"))] = new GUI_Start(field.get("title"), field.get("subText"), field.get("subText"), Color.getColor(field.get("color1")), Color.getColor(field.get("color2")));
                }
                case "refuge" -> {
                    guiFields[Integer.parseInt(field.get("position"))] = new GUI_Refuge("default", field.get("title"), field.get("subText"), field.get("subText"), Color.getColor(field.get("color1")), Color.getColor(field.get("color2")));
                }
                case "ferry" -> {
                    guiFields[Integer.parseInt(field.get("position"))] = new GUI_Shipping("default", field.get("title"), field.get("subText"), "Det her er en VARM beskrivelse af en b??d",
                            field.get("subText"), Color.getColor(field.get("color1")), Color.getColor(field.get("color2")));
                }
                case "brewery" -> {
                    guiFields[Integer.parseInt(field.get("position"))] = new GUI_Brewery("default", field.get("title"), field.get("subText"), "Det her er en VARM beskrivelse af et ??lbryggeri",
                            field.get("subText"), Color.getColor(field.get("color1")), Color.getColor(field.get("color2")));
                }
            }
        }
        return guiFields;
    }
}
