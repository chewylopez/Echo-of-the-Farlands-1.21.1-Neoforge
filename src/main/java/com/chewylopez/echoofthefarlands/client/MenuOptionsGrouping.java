package com.chewylopez.echoofthefarlands.client;

public class MenuOptionsGrouping {
    public String text;
    public int val;
    public String tooltip;
    MenuOptionsGrouping(String button_text, int value, String tool){
        text = button_text;
        val = value;
        tooltip = tool;
    }
    public String getText() {
        return text;
    }
    public int getValue() {
        return val;
    }
    public String getTooltip() {
        return tooltip;
    }
}
