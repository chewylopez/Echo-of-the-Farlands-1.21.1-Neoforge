package com.chewylopez.echoofthefarlands.client;

import com.chewylopez.echoofthefarlands.Config;

import java.util.Arrays;
import java.util.List;

public class PresetLists {

    public static MenuPair[] buildDistances(){
        int configValue = Config.FARLANDS_LOCATION_CONFIG.get();
           return new MenuPair[] {new MenuPair("Default 1000000", 1000000),
                new MenuPair("10000", 10000),
                new MenuPair("50000", 50000),
                new MenuPair("100000", 100000),
                new MenuPair("500000", 500000),
                new MenuPair("12550823", 12550823),
                new MenuPair("Config Value (" + configValue + ")", configValue)};
    }

    public static MenuPair[] buildGenTypes() {
        return new MenuPair[] {
                new MenuPair("Default Piecewise (static)", 0),
                new MenuPair("Default Piecewise (proportional)", 1),
                new MenuPair("Unscaled", 2),
                new MenuPair("Exponential (static)", 3),
                new MenuPair("Exponential (proportional)", 4),
                new MenuPair("Linear", 5),
                new MenuPair("Sine Wave", 6),
                new MenuPair("Multi Trig Density Function", 7)
        };

    };

    public static String[] getFarlandsDistances() {
        MenuPair[] Distances = buildDistances();
        String[] returnable = new String[Distances.length];
        for(int i = 0; i < Distances.length; i++){
            returnable[i] = Distances[i].getText();
        }
        return returnable;
    }

    public static String[] getFarlandsGenTypes() {
        MenuPair[] GenTypes = buildGenTypes();
        String[] returnable = new String[GenTypes.length];
        for(int i = 0; i < GenTypes.length; i++){
            returnable[i] = GenTypes[i].getText();
        }
        return returnable;
    }

    public static int getDistanceCaseFromButton(String value){
        MenuPair[] Distances = buildDistances();
        int returnable = 1000000;
        for(int i = 0; i < Distances.length; i++) {
            if(Distances[i].getText().equals(value)){
                returnable = Distances[i].getValue();
            }
        }
        return returnable;
    }

    public static int getGenTypeCaseFromButton(String value){
        MenuPair[] GenTypes = buildGenTypes();
        int returnable = 0;
        for(int i = 0; i < GenTypes.length; i++) {
            if(GenTypes[i].getText().equals(value)){
                returnable = GenTypes[i].getValue();
            }
        }
        return returnable;
    }

}

class MenuPair {
    public String text;
    public int val;
    MenuPair(String button_text, int value){
        text = button_text;
        val = value;
    }
    public String getText() {
        return text;
    }
    public int getValue() {
        return val;
    }
}
