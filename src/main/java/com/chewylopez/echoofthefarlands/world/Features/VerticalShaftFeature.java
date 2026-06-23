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

    private static final int[][] HORIZONTAL = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

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
        int bottomY = level.getMinBuildHeight();

        BlockState air = Blocks.CAVE_AIR.defaultBlockState();
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        BlockPos.MutableBlockPos wall = new BlockPos.MutableBlockPos();

        double centerX = origin.getX() + 0.5;
        double centerZ = origin.getZ() + 0.5;
        double driftX = 0.0;
        double driftZ = 0.0;

        for (int y = topY; y >= bottomY; y--) {
            driftX = Mth.clamp(driftX + (random.nextDouble() - 0.5) * 0.3, -2.0, 2.0);
            driftZ = Mth.clamp(driftZ + (random.nextDouble() - 0.5) * 0.3, -2.0, 2.0);
            double cx = centerX + driftX;
            double cz = centerZ + driftZ;

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
                    level.setBlock(mutable, air, 2);
                    sealLiquidNeighbors(level, x, y, z, cx, cz, rr, wall);
                }
            }
        }
        return true;
    }

    /**
     * edge check for liquids and replace with stone
     */
    private void sealLiquidNeighbors(WorldGenLevel level, int x, int y, int z,
                                     double cx, double cz, double rr,
                                     BlockPos.MutableBlockPos wall) {
        for (int[] d : HORIZONTAL) {
            int nx = x + d[0];
            int nz = z + d[1];
            double ddx = (nx + 0.5) - cx;
            double ddz = (nz + 0.5) - cz;
            if (ddx * ddx + ddz * ddz <= rr) continue; // inside the shaft — will be carved, don't seal
            wall.set(nx, y, nz);
            if (level.getFluidState(wall).isSource()) {
                level.setBlock(wall, sealant(y), 2);
            }
        }
    }

    private static BlockState sealant(int y) {
        return y < 0 ? Blocks.DEEPSLATE.defaultBlockState() : Blocks.STONE.defaultBlockState();
    }
}