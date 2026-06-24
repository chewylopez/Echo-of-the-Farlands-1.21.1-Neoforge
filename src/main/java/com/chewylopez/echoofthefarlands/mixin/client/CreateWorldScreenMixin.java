package com.chewylopez.echoofthefarlands.mixin.client;

import com.chewylopez.echoofthefarlands.Config;
import com.chewylopez.echoofthefarlands.client.PresetLists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.components.tabs.GridLayoutTab;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.client.gui.screens.worldselection.CreateWorldScreen$WorldTab")
public abstract class CreateWorldScreenMixin extends GridLayoutTab {

    Font font = Minecraft.getInstance().font;

    public CreateWorldScreenMixin(Component title) {
        super(title);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void addCustomButton(CreateWorldScreen parent, CallbackInfo ci) {

        CycleButton<String> FarlandsPosition = CycleButton.<String>builder(Component::literal)
                .withValues(PresetLists.getFarlandsDistances()).displayOnlyValue()
                .create(0, 0, 150, 20, Component.literal("Farlands Position"), (btn, value) -> {
                    Config.FARLANDS_LOCATION_WORLD = PresetLists.getDistanceCaseFromButton(value);
                    System.out.println("Farlands Position updated: " + Config.FARLANDS_LOCATION_WORLD);
                });

        CycleButton<String> FarlandsGenType = CycleButton.<String>builder(Component::literal)
                .withValues(PresetLists.getFarlandsGenTypes()).displayOnlyValue()
                .create(0, 0, 150, 20, Component.literal("Gen Type"), (btn, value) -> {
                    Config.FARLANDS_GEN_TYPE = PresetLists.getGenTypeCaseFromButton(value);
                    System.out.println("Farlands Gen Type updated: " + Config.FARLANDS_GEN_TYPE);
                });

        this.layout.columnSpacing(10).rowSpacing(5);
        this.layout.defaultCellSetting().alignVerticallyMiddle().alignHorizontallyLeft();
        this.layout.addChild(new StringWidget(Component.literal("Farlands Location"), font), 3, 0);
        this.layout.addChild(FarlandsPosition, 3, 1);

        this.layout.addChild(new StringWidget(Component.literal("Gen Type"), font), 4, 0);
        this.layout.addChild(FarlandsGenType, 4, 1);


    }

}
