package com.chewylopez.echoofthefarlands.mixin.structures;

import com.chewylopez.echoofthefarlands.world.structure_overrides.MonumentPlacementData;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.structures.OceanMonumentPieces;
import net.minecraft.world.level.levelgen.structure.structures.OceanMonumentStructure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Mixin(OceanMonumentStructure.class)
public abstract class OceanMonumentMixin {

    @Inject(method = "findGenerationPoint", at = @At("HEAD"))
    private void echo$stashFloorY(
            Structure.GenerationContext context,
            CallbackInfoReturnable<Optional<Structure.GenerationStub>> cir
    ) {
        ChunkPos chunk = context.chunkPos();
        int sampleX = chunk.getMinBlockX();
        int sampleZ = chunk.getMinBlockZ();

        int floorY = context.chunkGenerator().getFirstOccupiedHeight(
                sampleX, sampleZ,
                Heightmap.Types.OCEAN_FLOOR_WG,
                context.heightAccessor(),
                context.randomState()
        );

        if (floorY <= 35) {
            MonumentPlacementData.PENDING_FLOOR_Y.put(chunk, floorY);
            //System.out.println("[EchoOfTheFarlands] Stashed floorY=" + floorY + " for chunk " + chunk);
        } else {
            MonumentPlacementData.PENDING_FLOOR_Y.remove(chunk);
        }
    }
}