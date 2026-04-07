package com.chewylopez.echoofthefarlands.client;

import com.chewylopez.echoofthefarlands.Config;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.FocusableTextWidget;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.font.TextFieldHelper;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ConfigScreen extends Screen {

    private final Screen parent;

    public ConfigScreen(Screen parent) {
        super(Component.literal("Echo of the Farlands Config"));
        this.parent = parent;
    }

    @Override
    protected void init() {

        EditBox textbox = new EditBox(font.self(), this.width/2-100, 100, 200, 20, Component.literal("farlandsLocation"));
        textbox.setValue("" + Config.FARLANDS_LOCATION_CONFIG.get());
        textbox.setFilter(text -> {
            try {
                Integer.parseInt(text);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        });
        addRenderableWidget(textbox);

        Button done = Button.builder(Component.literal("Done"), button -> this.minecraft.setScreen(parent)).bounds(this.width / 2 - 100, this.height - 40, 200, 20).build();
        addRenderableWidget(done);

        addRenderableWidget(new StringWidget(this.width/2-100, 70, 200, 20, Component.literal("farlands location (default is 1,000,000)"), font.self()));
        addRenderableWidget(Button.builder(Component.literal("Update Value"), button -> {
                Config.FARLANDS_LOCATION_CONFIG.set(Integer.parseInt(textbox.getValue()));
                Config.FARLANDS_LOCATION_CONFIG.save();
                }).bounds(this.width / 2 - 100, 130, 200, 20).build());
    }
}