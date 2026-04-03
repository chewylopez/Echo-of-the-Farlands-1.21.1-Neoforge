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

    public void setFarlandsPosition(int val) {
        FARLANDS_POSITION = val;
        this.setDirty();
    }

    public void setFarlandsGenType(int val) {
        FARLANDS_GEN_TYPE = val;
        this.setDirty();
    }

    public static WorldFarlandsSettings create() {
        return new WorldFarlandsSettings();
    }

    public static WorldFarlandsSettings load(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        WorldFarlandsSettings data = WorldFarlandsSettings.create();
        if (tag.contains("FarlandsPosition")) data.FARLANDS_POSITION = tag.getInt("FarlandsPosition");
        if (tag.contains("FarlandsGenType")) data.FARLANDS_GEN_TYPE = tag.getInt("FarlandsGenType");

        data.isNew = false;
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        tag.putInt("FarlandsPosition", FARLANDS_POSITION);
        tag.putInt("FarlandsGenType", FARLANDS_GEN_TYPE);
        return tag;
    }

    public static WorldFarlandsSettings getSettings(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(new Factory<>(WorldFarlandsSettings::create, WorldFarlandsSettings::load), FARLANDS_FILE_ID);
    }

    public boolean isNewWorld() {
        return isNew;
    }

}