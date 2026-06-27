package com.chewylopez.echoofthefarlands.client;

import com.chewylopez.echoofthefarlands.Config;
import net.minecraft.network.chat.Component;

public class PresetLists {

    public static MenuOptionsGrouping[] buildDistances(){
        int configValue = Config.FARLANDS_LOCATION_CONFIG.get();
           return new MenuOptionsGrouping[] { new MenuOptionsGrouping("1000000", 1000000,
                   "default value"),
                new MenuOptionsGrouping("10000", 10000, ""),
                new MenuOptionsGrouping("50000", 50000, ""),
                new MenuOptionsGrouping("100000", 100000, ""),
                new MenuOptionsGrouping("500000", 500000, ""),
                new MenuOptionsGrouping("12550823", 12550823,
                        "original farlands"),
                new MenuOptionsGrouping("Config Value (" + configValue + ")", configValue,
                        "can be any positive integer, keep in mind that low values will break proportional scalings and high values beyond the world barrier will not be accessible.\n-NOTE- value below world height will generate Y-axis Farlands")};
    }

    public static MenuOptionsGrouping[] buildGenTypes() {
        return new MenuOptionsGrouping[] { new MenuOptionsGrouping("Default Piecewise (static)", 0,
                "(default) Piecewise function:\n0-500k (unscaled)\n500k-1m (linear)\n1m-1.5m (exponential)\n1.5m-4m (moat)\n4m+ (wrapped vanilla gen)"),
                new MenuOptionsGrouping("Default Piecewise (proportional)", 1,
                        "Piecewise function:\nscaled to Farlands location\n(1m is same as static)"),
                new MenuOptionsGrouping("Unscaled", 2,
                        "Unscaled noise value with no vanilla wrapping"),
                new MenuOptionsGrouping("Exponential (static)", 3,
                        "Noise value = INT_MAX^factor\nfactor = 1+(2*(distance/1m)"),
                new MenuOptionsGrouping("Exponential (proportional)", 4,
                        "Exponential function:\nscaled to Farlands location\n(1m is same as static)"),
                new MenuOptionsGrouping("Linear", 5,
                        "Noise value = value * 10^15\nproportional to wall distance"),
                new MenuOptionsGrouping("Sine Wave", 6,
                        "Noise value = \n2*sin(value)*(value + INT_MAX)"),
                new MenuOptionsGrouping("Multi Trig Density Function", 7,
                        "Unscaled + density function:\n = value + factor\nfactor = (1/3) *\n(sin(x * 0.013 + z * 0.017) +\ncos(x * 0.007 - z * 0.011) +\nsin(y * 0.05 + x * 0.003))")
        };
    };

    public static MenuOptionsGrouping[] buildWallPatch(){
        return new MenuOptionsGrouping[] {
                new MenuOptionsGrouping("ON", 0,
                        "ENABLED:\nadds a 500 block buffer zone with random errors as texturing for the Farlands edge"),
                new MenuOptionsGrouping("OFF", 1,
                        "DISABLED:\nadds a 500 block buffer zone with random errors as texturing for the Farlands edge"),
        };
    }

    public static MenuOptionsGrouping[] buildFluidPatch(){
        return new MenuOptionsGrouping[] {
                new MenuOptionsGrouping("ON", 0,
                        "ENABLED:\nfixes DOUBLE_MAX overflow for water barriers. will appear like floating islands of lakes and oceans\n-NOTE- this largely prevents liquid flowing lag when generating Farlands"),
                new MenuOptionsGrouping("OFF", 1,
                        "DISABLED:\nfixes DOUBLE_MAX overflow for water barriers. will appear like floating islands of lakes and oceans\n-NOTE- this largely prevents liquid flowing lag when generating Farlands"),
        };
    }

    public static MenuOptionsGrouping[] buildBedrockPatch(){
        return new MenuOptionsGrouping[] {
                new MenuOptionsGrouping("ON", 0,
                        "ENABLED:\nforces bottom 5 blocks of dimension in Farlands to generate solid\n-NOTE- preserves void roof features and prevents liquids from flowing into void"),
                new MenuOptionsGrouping("OFF", 1,
                        "DISABLED:\fforces bottom 5 blocks of dimension in Farlands to generate solid\n-NOTE- preserves void roof features and prevents liquids from flowing into void"),
        };
    }

    public static String[] getTextFromGroupArray(MenuOptionsGrouping[] items) {
        MenuOptionsGrouping[] group = items;
        String[] returnable = new String[group.length];
        for(int i = 0; i < group.length; i++){
            returnable[i] = group[i].getText();
        }
        return returnable;
    }

    public static int getIntsFromButton(String value, MenuOptionsGrouping[] items){
        MenuOptionsGrouping[] group = items;
        int returnable = 0;
        for(int i = 0; i < group.length; i++) {
            if(group[i].getText().equals(value)){
                returnable = group[i].getValue();
            }
        }
        return returnable;
    }

    public static boolean getBooleanOptionsFromButton(String value, MenuOptionsGrouping[] items){
        MenuOptionsGrouping[] basic = items;
        for(int i = 0; i < basic.length; i++) {
            if(basic[i].getText().equals(value)){
                if(basic[i].getValue() == 0){
                    return true;
                }
            }
        }
        return false;
    }

    public static Component getTooltipsFromGroupArray(String value, MenuOptionsGrouping[] items) {
        for (MenuOptionsGrouping p : items) {
            if (p.getText().equals(value)) {
                return Component.literal(p.getTooltip());
            }
        }
        return Component.empty();
    }

    public static String getTextFromIntValue(int value, MenuOptionsGrouping[] items){
        for (MenuOptionsGrouping p : items) {
            if (p.getValue() == value) {
                return p.getText();
            }
        }
        return items.length > 0 ? items[0].getText() : "";
    }

}

