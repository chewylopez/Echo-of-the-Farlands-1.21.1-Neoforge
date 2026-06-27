package com.chewylopez.echoofthefarlands.world.farlands;

import com.chewylopez.echoofthefarlands.world.dimension.DimensionProfile;
import com.chewylopez.echoofthefarlands.world.dimension.DimensionProfiles;

public final class FarlandsContext {
    public static final ThreadLocal<Double> CHUNK_RADIUS = ThreadLocal.withInitial(() -> 0.0);
    public static final ThreadLocal<DimensionProfile> PROFILE = ThreadLocal.withInitial(() -> DimensionProfiles.VANILLA);
    public static final ThreadLocal<Boolean> APPLY_FARLANDS = ThreadLocal.withInitial(() -> false);
    private FarlandsContext() {}
}
