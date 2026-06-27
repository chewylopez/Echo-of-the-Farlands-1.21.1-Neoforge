package com.chewylopez.echoofthefarlands.mixin.noise;

import com.chewylopez.echoofthefarlands.Config;
import com.chewylopez.echoofthefarlands.world.dimension.DimensionProfile;
import com.chewylopez.echoofthefarlands.world.dimension.DimensionProfiles;
import com.chewylopez.echoofthefarlands.world.farlands.FarlandsContext;
import com.chewylopez.echoofthefarlands.world.farlands.FarlandsPatchDimensionGate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.blending.Blender;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.CompletableFuture;

@Mixin({NoiseBasedChunkGenerator.class})
public class NoiseChunkGeneratorMixin {

    @Unique
    private static final double MOAT_INNER_STATIC = 2500000.0;

    @Unique
    private static final double MOAT_OUTER_STATIC = 5000000.0;

    @Unique
    private static int OCEAN_FLOOR  = -46;

    @Unique
    private static final int SEA_LEVEL = 62;

    @Unique
    private static double getMoatInnerProportional() {
        return 2.5 * getFarlandsLocation();
    }

    @Unique
    private static double getMoatOuterProportional(){
        return 5.0 * getFarlandsLocation();
    }

    @Unique
    private static int getFarlandsGenType(){
        return Config.FARLANDS_GEN_TYPE;
    }

    @Unique
    private static double getFarlandsLocation(){
        return Config.FARLANDS_LOCATION_WORLD;
    }

    @Shadow
    @Final
    protected Holder<NoiseGeneratorSettings> settings;

    @Unique
    private boolean getFarlandsGate() {
        return FarlandsPatchDimensionGate.appliesTo(this.settings);
    }

    @Inject(method = {"createFluidPicker"}, at = {@At("TAIL")}, cancellable = true)
    private static void lavaFillChange(NoiseGeneratorSettings settings, CallbackInfoReturnable<Aquifer.FluidPicker> returnable) {
        // 0.172 is vanilla ratio

        if (!FarlandsPatchDimensionGate.appliesTo(settings)) return;

        int lavaOffset = (int)(Math.abs(settings.noiseSettings().minY()) * 0.172);
        Aquifer.FluidStatus lava = new Aquifer.FluidStatus(settings.noiseSettings().minY() + lavaOffset, Blocks.LAVA.defaultBlockState());
        int water_level = settings.seaLevel();
        Aquifer.FluidStatus water = new Aquifer.FluidStatus(water_level, settings.defaultFluid());
        returnable.setReturnValue( (x,y,z) -> {
            return y < Math.min(settings.noiseSettings().minY() + lavaOffset, water_level) ? lava : water;
        });
    }

    @Inject(method = "fillFromNoise", at = @At("HEAD"), cancellable = true)
    private void fillMoat(Blender blender, RandomState randomState, StructureManager structureManager, ChunkAccess chunk, CallbackInfoReturnable<CompletableFuture<ChunkAccess>> cir) {
        if (!getFarlandsGate()) return;
        int cx = chunk.getPos().getMinBlockX();
        int cz = chunk.getPos().getMinBlockZ();
        double radius = Math.max(Math.abs(cx), Math.abs(cz));

        if(getFarlandsGenType() == 0) {
            if (radius >= MOAT_INNER_STATIC && radius < MOAT_OUTER_STATIC) {
                cir.setReturnValue(CompletableFuture.completedFuture(fillOceanChunk(chunk)));
            }
        } else if(getFarlandsGenType() == 1) {
            if (radius >= getMoatInnerProportional() && radius < getMoatOuterProportional()) {
                cir.setReturnValue(CompletableFuture.completedFuture(fillOceanChunk(chunk)));
            }
        }
    }

    @Unique
    private static ChunkAccess fillOceanChunk(ChunkAccess chunk) {
        BlockState water   = Blocks.WATER.defaultBlockState();
        BlockState stone   = Blocks.STONE.defaultBlockState();
        BlockState bedrock = Blocks.BEDROCK.defaultBlockState();

        DimensionProfile p = DimensionProfiles.resolve(chunk);
        int chunkMinY = chunk.getMinBuildHeight();
        int chunkMaxY = chunk.getMaxBuildHeight();

        Heightmap oceanFloor   = chunk.getOrCreateHeightmapUnprimed(Heightmap.Types.OCEAN_FLOOR_WG);
        Heightmap worldSurface = chunk.getOrCreateHeightmapUnprimed(Heightmap.Types.WORLD_SURFACE_WG);

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                LevelChunkSection cur = null;
                int curIdx = -1;

                for (int y = p.bedrockFloor(); y < chunkMaxY; y++) {
                    BlockState state;
                    if (y == p.bedrockFloor()){
                        state = bedrock;
                    } else if (y < p.oceanFloor()){
                        state = stone;
                    } else if (y <= p.seaLevel()) {
                        state = water;
                    } else {
                        continue;
                    }

                    int idx = chunk.getSectionIndex(y);
                    if (idx != curIdx) {
                        cur = chunk.getSection(idx); curIdx = idx;
                    }
                    cur.setBlockState(x, y & 15, z, state, false);
                    oceanFloor.update(x, y, z, state);
                    worldSurface.update(x, y, z, state);
                }
            }
        }
        return chunk;
    }

    @Inject(method = "applyCarvers", at = @At("HEAD"), cancellable = true)
    private void skipCarversInMoat(WorldGenRegion level, long seed, RandomState randomState, BiomeManager biomeManager, StructureManager structureManager, ChunkAccess chunk, GenerationStep.Carving step, CallbackInfo ci) {
        if (!getFarlandsGate()) return;
        int cx = chunk.getPos().getMinBlockX();
        int cz = chunk.getPos().getMinBlockZ();
        double radius = Math.max(Math.abs(cx + 8), Math.abs(cz + 8));
        if(getFarlandsGenType() == 0) {
            if (radius >= MOAT_INNER_STATIC && radius < MOAT_OUTER_STATIC) {
                ci.cancel();
            }
        } else if(getFarlandsGenType() == 1) {
            if (radius >= getMoatInnerProportional() && radius < getMoatOuterProportional()) {
                ci.cancel();
            }
        }
    }

    @Inject(method = "doFill(Lnet/minecraft/world/level/levelgen/blending/Blender;Lnet/minecraft/world/level/StructureManager;Lnet/minecraft/world/level/levelgen/RandomState;Lnet/minecraft/world/level/chunk/ChunkAccess;II)Lnet/minecraft/world/level/chunk/ChunkAccess;", at = @At("HEAD"))
    private void setChunkContext(Blender blender, StructureManager sm, RandomState rs, ChunkAccess chunk, int min, int height, CallbackInfoReturnable<ChunkAccess> cir) {
        boolean applies = getFarlandsGate();
        int cx = chunk.getPos().getMinBlockX();
        int cz = chunk.getPos().getMinBlockZ();
        double radius = Math.max(Math.abs(cx + 8), Math.abs(cz + 8));
        FarlandsContext.CHUNK_RADIUS.set(radius);
        FarlandsContext.PROFILE.set(DimensionProfiles.resolve(chunk));
        FarlandsContext.APPLY_FARLANDS.set(applies);
    }

    @Inject(method = "doFill(Lnet/minecraft/world/level/levelgen/blending/Blender;Lnet/minecraft/world/level/StructureManager;Lnet/minecraft/world/level/levelgen/RandomState;Lnet/minecraft/world/level/chunk/ChunkAccess;II)Lnet/minecraft/world/level/chunk/ChunkAccess;", at = @At("RETURN"))
    private void clearChunkContext(CallbackInfoReturnable<ChunkAccess> cir) {
        FarlandsContext.CHUNK_RADIUS.remove();
        FarlandsContext.PROFILE.remove();
        FarlandsContext.APPLY_FARLANDS.remove();
    }
}
