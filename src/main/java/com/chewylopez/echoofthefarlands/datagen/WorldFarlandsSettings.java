package com.chewylopez.echoofthefarlands.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

public class WorldFarlandsSettings extends SavedData {

    private static final String FARLANDS_FILE_ID = "farlands";
    private static int FARLANDS_POSITION = 1000000;
    private static int FARLANDS_GEN_TYPE = 1;

    public static WorldFarlandsSettings create() {
        return new WorldFarlandsSettings();
    }

    public static WorldFarlandsSettings load(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        WorldFarlandsSettings data = WorldFarlandsSettings.create();
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        tag.putInt("FarlandsPosition", FARLANDS_POSITION);
        tag.putInt("FarlandsGenType", FARLANDS_GEN_TYPE);
        return tag;
    }

    public static WorldFarlandsSettings write(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(new Factory<>(WorldFarlandsSettings::create, WorldFarlandsSettings::load), FARLANDS_FILE_ID);
    }

}