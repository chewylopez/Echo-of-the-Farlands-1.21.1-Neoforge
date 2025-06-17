package com.chewylopez.echoofthefarlands.mixin;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.Aquifer;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({NoiseBasedChunkGenerator.class})
public class NoiseChunkGeneratorMixin {
    @Inject(method = {"createFluidPicker"}, at = {@At("TAIL")}, cancellable = true)
    private static void createFluidPacker(NoiseGeneratorSettings settings, CallbackInfoReturnable<Aquifer.FluidPicker> returnable) {

        int lavaOffset = (int)(Math.abs(settings.noiseSettings().minY()) * 0.172);
        Aquifer.FluidStatus aquiferdefaultlava = new Aquifer.FluidStatus(settings.noiseSettings().minY() + lavaOffset, Blocks.LAVA.defaultBlockState());
        int i = settings.seaLevel();

        Aquifer.FluidStatus aquiferdefaultwater = new Aquifer.FluidStatus(i, settings.defaultFluid());

        returnable.setReturnValue( (x,y,z) -> {
            return y < Math.min(settings.noiseSettings().minY() + lavaOffset, i) ? aquiferdefaultlava : aquiferdefaultwater;
        });
    }

}
