package com.chewylopez.echoofthefarlands.world.Features;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class VerticalShaftFeature extends Feature<VerticalShaftConfig> {

    public VerticalShaftFeature(Codec<VerticalShaftConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<VerticalShaftConfig> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();
        VerticalShaftConfig config = context.config();

        int baseRadius = Mth.nextInt(random, config.minRadius(), config.maxRadius());
        int topY = origin.getY();
        int bottomY = level.getMinBuildHeight();   // ← all the way to the floor, through bedrock

        BlockState air = Blocks.CAVE_AIR.defaultBlockState();
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

        // Meander state — shaft drifts slightly as it descends
        double centerX = origin.getX() + 0.5;
        double centerZ = origin.getZ() + 0.5;
        double driftX = 0.0;
        double driftZ = 0.0;

        for (int y = topY; y >= bottomY; y--) {
            // Gentle random-walk drift, clamped so the shaft stays near its origin
            driftX = Mth.clamp(driftX + (random.nextDouble() - 0.5) * 0.3, -2.0, 2.0);
            driftZ = Mth.clamp(driftZ + (random.nextDouble() - 0.5) * 0.3, -2.0, 2.0);
            double cx = centerX + driftX;
            double cz = centerZ + driftZ;

            // Smoothly varying radius (sine) plus a touch of per-layer randomness
            double layerRadius = baseRadius + Math.sin(y * 0.1) + (random.nextDouble() - 0.5) * 0.6;
            double rr = layerRadius * layerRadius;
            int reach = Mth.ceil(layerRadius) + 1;

            int minX = Mth.floor(cx) - reach, maxX = Mth.floor(cx) + reach;
            int minZ = Mth.floor(cz) - reach, maxZ = Mth.floor(cz) + reach;

            for (int x = minX; x <= maxX; x++) {
                for (int z = minZ; z <= maxZ; z++) {
                    double ddx = (x + 0.5) - cx;
                    double ddz = (z + 0.5) - cz;
                    if (ddx * ddx + ddz * ddz > rr) continue;
                    mutable.set(x, y, z);
                    if (level.getBlockState(mutable).isAir()) continue;
                    level.setBlock(mutable, air, 2);   // carves bedrock too — no skip
                }
            }
        }
        return true;
    }
}