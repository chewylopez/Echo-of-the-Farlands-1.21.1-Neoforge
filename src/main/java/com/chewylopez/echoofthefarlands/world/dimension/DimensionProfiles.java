package com.chewylopez.echoofthefarlands.world.dimension;

import net.minecraft.world.level.chunk.ChunkAccess;

import java.util.Map;

public final class DimensionProfiles {

    public static final DimensionProfile VANILLA = new DimensionProfile(
            -64, 320, -64,320, -54, 62, false, false);

    public static final DimensionProfile BEYOND = new DimensionProfile(
            -576, 1024, 512, 1024, -502, 62, true, false);

    private static final Map<Integer, DimensionProfile> BY_CHUNK_MIN_Y = Map.of(VANILLA.chunkMinY(), VANILLA, BEYOND.chunkMinY(), BEYOND);

    public static DimensionProfile resolve(ChunkAccess chunk) {
        return BY_CHUNK_MIN_Y.getOrDefault(chunk.getMinBuildHeight(), VANILLA);
    }

    public static DimensionProfile resolve(int chunkMinY) {
        return BY_CHUNK_MIN_Y.getOrDefault(chunkMinY, VANILLA);
    }

    private DimensionProfiles() {}
}