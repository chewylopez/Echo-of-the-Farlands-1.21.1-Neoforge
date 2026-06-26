package com.chewylopez.echoofthefarlands.datagen;

import com.chewylopez.echoofthefarlands.Config;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

public class WorldFarlandsSettings extends SavedData {

    private static final String FARLANDS_FILE_ID = "farlands";

    private int FARLANDS_POSITION;
    private int FARLANDS_GEN_TYPE;
    private boolean FARLANDS_WALL_TEXTURE_PATCH;
    private boolean FARLANDS_LIQUID_FIX;
    private boolean FARLANDS_BEDROCK_FIX;

    private boolean isNew = false;

    public WorldFarlandsSettings() {
        isNew = true;
    }

    public int getFarlandsPosition(){
        return FARLANDS_POSITION;
    }

    public int getFarlandsGenType(){
        return FARLANDS_GEN_TYPE;
    }

    public boolean getFarlandsWallTexturePatch(){
        return FARLANDS_WALL_TEXTURE_PATCH;
    }

    public boolean getFarlandsLiquidFix(){
        return FARLANDS_LIQUID_FIX;
    }

    public boolean getFarlandsBedrockFix(){
        return FARLANDS_BEDROCK_FIX;
    }

    public void setFarlandsPosition(int val) {
        FARLANDS_POSITION = val;
        this.setDirty();
    }

    public void setFarlandsGenType(int val) {
        FARLANDS_GEN_TYPE = val;
        this.setDirty();
    }

    public void setFarlandsWallTexturePatch(Boolean val) {
        FARLANDS_WALL_TEXTURE_PATCH = val;
        this.setDirty();
    }

    public void setFarlandsLiquidFix(Boolean val) {
        FARLANDS_LIQUID_FIX = val;
        this.setDirty();
    }

    public void setFarlandsBedrockFix(Boolean val) {
        FARLANDS_BEDROCK_FIX = val;
        this.setDirty();
    }

    public static WorldFarlandsSettings create() {
        return new WorldFarlandsSettings();
    }

    public static WorldFarlandsSettings load(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        WorldFarlandsSettings data = WorldFarlandsSettings.create();
        if (tag.contains("FarlandsPosition")) data.FARLANDS_POSITION = tag.getInt("FarlandsPosition");
        if (tag.contains("FarlandsGenType")) data.FARLANDS_GEN_TYPE = tag.getInt("FarlandsGenType");
        if (tag.contains("FarlandsWallTexturePatch")) data.FARLANDS_WALL_TEXTURE_PATCH = tag.getBoolean("FarlandsWallTexturePatch");
        if (tag.contains("FarlandsLiquidFix")) data.FARLANDS_LIQUID_FIX = tag.getBoolean("FarlandsLiquidFix");
        if (tag.contains("FarlandsBedrockFix")) data.FARLANDS_BEDROCK_FIX = tag.getBoolean("FarlandsBedrockFix");

        data.isNew = false;
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        tag.putInt("FarlandsPosition", FARLANDS_POSITION);
        tag.putInt("FarlandsGenType", FARLANDS_GEN_TYPE);
        tag.putBoolean("FarlandsWallTexturePatch", FARLANDS_WALL_TEXTURE_PATCH);
        tag.putBoolean("FarlandsLiquidFix", FARLANDS_LIQUID_FIX);
        tag.putBoolean("FarlandsBedrockFix", FARLANDS_BEDROCK_FIX);
        return tag;
    }

    public static WorldFarlandsSettings getSettings(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(new Factory<>(WorldFarlandsSettings::create, WorldFarlandsSettings::load), FARLANDS_FILE_ID);
    }

    public boolean isNewWorld() {
        return isNew;
    }

}