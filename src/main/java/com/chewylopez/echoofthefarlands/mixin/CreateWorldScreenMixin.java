package com.chewylopez.echoofthefarlands.mixin;

import com.chewylopez.echoofthefarlands.Config;
import com.chewylopez.echoofthefarlands.datagen.WorldFarlandsSettings;
import net.minecraft.client.gui.components.CycleButton;
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

    public CreateWorldScreenMixin(Component title) {
        super(title);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void addCustomButton(CreateWorldScreen parent, CallbackInfo ci) {

        GridLayout layout = this.layout;

        CycleButton<String> FarlandsPosition = CycleButton.<String>builder(value -> Component.literal(value))
                .withValues("10000", "50000", "100000", "500000", "1000000", "original (12550823)", "Custom Config Value")
                .create(0, 0, 150, 20, Component.literal("Farlands Location"), (btn, value) -> {
                    Config.FARLANDS_LOCATION_WORLD = switch (value) {
                        case "10000" -> 10000;
                        case "50000" -> 50000;
                        case "100000" -> 100000;
                        case "500000" -> 500000;
                        case "1000000" -> 1000000;
                        case "original (12550823)" -> 12550823;
                        case "Custom Config Value" -> Config.FARLANDS_LOCATION_CONFIG.get();
                        default -> Config.FARLANDS_LOCATION_CONFIG.get();
                    };
                    System.out.println("Farlands Position updated: " + Config.FARLANDS_LOCATION_WORLD);
                });

        CycleButton<String> FarlandsGenType = CycleButton.<String>builder(value -> Component.literal(value))
                .withValues("Original Farlands (unscaled)", "Exponential", "Linear", "Sine Wave")
                .create(0, 0, 150, 20, Component.literal("Gen Type"), (btn, value) -> {
                    Config.FARLANDS_GEN_TYPE = switch (value) {
                        case "Original (unscaled)" -> 0;
                        case "Linear" -> 1;
                        case "Exponential" -> 2;
                        case "Sine Wave" -> 3;
                        default -> 0;
                    };
                    System.out.println("Farlands Gen Type updated: " + Config.FARLANDS_GEN_TYPE);
                });

        layout.addChild(FarlandsPosition, 4, 0);
        layout.addChild(FarlandsGenType, 4, 1);
    }

}
