package com.chewylopez.echoofthefarlands.client;

import com.chewylopez.echoofthefarlands.Config;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.LayoutSettings;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class WorldCreationFarlandsScreen extends Screen {

    private final Screen parent;

    public WorldCreationFarlandsScreen(Screen parent) {
        super(Component.literal("Farlands Settings"));
        this.parent = parent;
    }

    @Override
    protected void init() {

        MenuOptionsGrouping[] positions    = PresetLists.buildDistances();
        MenuOptionsGrouping[] genTypes     = PresetLists.buildGenTypes();
        MenuOptionsGrouping[] wallPatch    = PresetLists.buildWallPatch();
        MenuOptionsGrouping[] fluidPatch   = PresetLists.buildFluidPatch();
        MenuOptionsGrouping[] bedrockPatch = PresetLists.buildBedrockPatch();

        CycleButton<String> farlandsPosition = buildCycle(positions,
                PresetLists.getTextFromIntValue(Config.FARLANDS_LOCATION_WORLD, positions),
                "Farlands Position", (btn, value) -> {
                    Config.FARLANDS_LOCATION_WORLD = PresetLists.getIntsFromButton(value, positions);
                    System.out.println("Farlands Position updated: " + Config.FARLANDS_LOCATION_WORLD);
                });

        CycleButton<String> farlandsGenType = buildCycle(genTypes,
                PresetLists.getTextFromIntValue(Config.FARLANDS_GEN_TYPE, genTypes),
                "Gen Type", (btn, value) -> {
                    Config.FARLANDS_GEN_TYPE = PresetLists.getIntsFromButton(value, genTypes);
                    System.out.println("Farlands Gen Type updated: " + Config.FARLANDS_GEN_TYPE);
                });

        CycleButton<String> farlandsWallPatch = buildCycle(wallPatch,
                PresetLists.getTextFromIntValue(Config.FARLANDS_WALL_TEXTURE_PATCH ? 0 : 1, wallPatch),
                "Wall Patch", (btn, value) -> {
                    Config.FARLANDS_WALL_TEXTURE_PATCH = PresetLists.getBooleanOptionsFromButton(value, wallPatch);
                    System.out.println("Farlands wall patch updated: " + Config.FARLANDS_WALL_TEXTURE_PATCH);
                });

        CycleButton<String> farlandsFluidPatch = buildCycle(fluidPatch,
                PresetLists.getTextFromIntValue(Config.FARLANDS_LIQUID_FIX ? 0 : 1, fluidPatch),
                "Fluid Patch", (btn, value) -> {
                    Config.FARLANDS_LIQUID_FIX = PresetLists.getBooleanOptionsFromButton(value, fluidPatch);
                    System.out.println("Farlands fluid patch updated: " + Config.FARLANDS_LIQUID_FIX);
                });

        CycleButton<String> farlandsBedrockPatch = buildCycle(bedrockPatch,
                PresetLists.getTextFromIntValue(Config.FARLANDS_BEDROCK_FIX ? 0 : 1, bedrockPatch),
                "Bedrock Patch", (btn, value) -> {
                    Config.FARLANDS_BEDROCK_FIX = PresetLists.getBooleanOptionsFromButton(value, bedrockPatch);
                    System.out.println("Farlands bedrock patch updated: " + Config.FARLANDS_BEDROCK_FIX);
                });

        // --- layout ---
        GridLayout grid = new GridLayout();
        grid.columnSpacing(10).rowSpacing(5);

        LayoutSettings labelCell  = LayoutSettings.defaults().alignVerticallyMiddle().alignHorizontallyLeft();
        LayoutSettings buttonCell = LayoutSettings.defaults().alignVerticallyMiddle().alignHorizontallyRight();

        int row = 0;
        grid.addChild(new StringWidget(Component.literal("Farlands Location"), this.font), row, 0, labelCell);
        grid.addChild(farlandsPosition, row++, 1, buttonCell);

        grid.addChild(new StringWidget(Component.literal("Generation Type"), this.font), row, 0, labelCell);
        grid.addChild(farlandsGenType, row++, 1, buttonCell);

        grid.addChild(new StringWidget(Component.literal("Wall Texture Patch"), this.font), row, 0, labelCell);
        grid.addChild(farlandsWallPatch, row++, 1, buttonCell);

        grid.addChild(new StringWidget(Component.literal("Fluid Aquifer Patch"), this.font), row, 0, labelCell);
        grid.addChild(farlandsFluidPatch, row++, 1, buttonCell);

        grid.addChild(new StringWidget(Component.literal("Bedrock Floor Patch"), this.font), row, 0, labelCell);
        grid.addChild(farlandsBedrockPatch, row++, 1, buttonCell);

        grid.arrangeElements();
        // center the grid in the upper area, leaving room for the Done button
        FrameLayout.centerInRectangle(grid, 0, 0, this.width, this.height - 40);
        grid.visitWidgets(this::addRenderableWidget);

        // Done button
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, b -> this.onClose())
                .bounds(this.width / 2 - 100, this.height - 28, 200, 20)
                .build());
    }

    private CycleButton<String> buildCycle(MenuOptionsGrouping[] items, String initial,
                                           String label, CycleButton.OnValueChange<String> onChange) {
        return CycleButton.<String>builder(Component::literal)
                .withValues(PresetLists.getTextFromGroupArray(items))
                .withInitialValue(initial)
                .displayOnlyValue()
                .withTooltip(value -> {
                    String t = PresetLists.getTooltipsFromGroupArray(value, items).getString();
                    return t.isEmpty() ? null : Tooltip.create(Component.literal(t));
                })
                .create(0, 0, 150, 20, Component.literal(label), onChange);
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        super.render(g, mouseX, mouseY, partialTick); // draws background + widgets + tooltips
        g.drawCenteredString(this.font, this.title, this.width / 2, 15, 0xFFFFFF);
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(this.parent);
    }
}