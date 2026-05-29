package com.chewylopez.echoofthefarlands.world.structure_overrides;

import net.minecraft.world.level.ChunkPos;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class MonumentPlacementData {
    private MonumentPlacementData() {}
    public static final Map<ChunkPos, Integer> PENDING_FLOOR_Y = new ConcurrentHashMap<>();
}
