package com.chewylopez.echoofthefarlands.mixin.noise;

import com.chewylopez.echoofthefarlands.world.density_function.FarlandsDensityFunction;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.NoiseRouter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NoiseRouter.class)
public abstract class NoiseRouterMixin {

    @Shadow @Final @Mutable private DensityFunction finalDensity;
    @Shadow @Final @Mutable private DensityFunction initialDensityWithoutJaggedness;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void farlandsNoiseRouter(CallbackInfo ci) {
        if (!(this.finalDensity instanceof FarlandsDensityFunction)) {
            this.finalDensity = new FarlandsDensityFunction(this.finalDensity);
        }
        if (!(this.initialDensityWithoutJaggedness instanceof FarlandsDensityFunction)) {
            this.initialDensityWithoutJaggedness = new FarlandsDensityFunction(this.initialDensityWithoutJaggedness);
        }
    }
}