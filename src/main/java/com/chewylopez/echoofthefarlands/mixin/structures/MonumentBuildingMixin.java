package com.chewylopez.echoofthefarlands.mixin.structures;

import com.chewylopez.echoofthefarlands.world.structure_overrides.MonumentPlacementData;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.structures.OceanMonumentPieces;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(targets = "net.minecraft.world.level.levelgen.structure.structures.OceanMonumentPieces$MonumentBuilding")
public abstract class MonumentBuildingMixin {

    @Shadow
    @Final
    private List<OceanMonumentPieces.OceanMonumentPiece> childPieces;

    @Inject(method = "<init>(Lnet/minecraft/util/RandomSource;IILnet/minecraft/core/Direction;)V", at = @At("RETURN"))
    private void echo$shiftToFloor(RandomSource random, int x, int z, Direction orientation, CallbackInfo ci) {

        int chunkX = Math.floorDiv(x + 29, 16);
        int chunkZ = Math.floorDiv(z + 29, 16);
        ChunkPos chunk = new ChunkPos(chunkX, chunkZ);

        Integer customY = MonumentPlacementData.PENDING_FLOOR_Y.remove(chunk);
        if (customY == null) return;

        int dy = customY - 39;

        StructurePiece self = (StructurePiece)(Object)this;
        self.getBoundingBox().move(0, dy, 0);

        for (OceanMonumentPieces.OceanMonumentPiece child : this.childPieces) {
            child.getBoundingBox().move(0, dy, 0);
        }
    }
}