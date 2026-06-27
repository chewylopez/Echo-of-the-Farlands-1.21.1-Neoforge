package com.chewylopez.echoofthefarlands.mixin.client;

import com.chewylopez.echoofthefarlands.Config;
import com.chewylopez.echoofthefarlands.client.PresetLists;
import com.chewylopez.echoofthefarlands.client.MenuOptionsGrouping;
import com.chewylopez.echoofthefarlands.client.WorldCreationFarlandsScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.tabs.GridLayoutTab;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
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

        Button openConfig = Button.builder(
                Component.literal("Farlands Settings"),
                b -> Minecraft.getInstance().setScreen(new WorldCreationFarlandsScreen(parent))
        ).width(150).build();

        this.layout.columnSpacing(10).rowSpacing(5);
        this.layout.defaultCellSetting().alignVerticallyMiddle().alignHorizontallyLeft();
        this.layout.addChild(new StringWidget(Component.literal("Farlands Options"), this.font), 3, 0);
        this.layout.addChild(openConfig, 3, 1);
    }
}