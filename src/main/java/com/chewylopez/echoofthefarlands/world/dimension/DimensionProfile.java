package com.chewylopez.echoofthefarlands.world.dimension;


public record DimensionProfile(int chunkMinY, int chunkMaxY, int bedrockFloor, int bedrockCeiling, int oceanFloor, int seaLevel, boolean hasVoidBuffer, boolean hasBedrockCeiling) {

    public int voidBufferDepth() {
        return bedrockFloor - chunkMinY;
    }

    public int bedrockFloorTop() {
        return bedrockFloor + 4;
    }

}