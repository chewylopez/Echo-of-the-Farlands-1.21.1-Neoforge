package com.chewylopez.echoofthefarlands.mixin;

import net.minecraft.world.level.levelgen.synth.PerlinNoise;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({PerlinNoise.class})
public class PerlinNoiseMixin {
    @Inject(method = {"wrap"}, at = {@At("TAIL")}, cancellable = true)
    private static void wrapFarlands(double value, CallbackInfoReturnable<Double> cir) {

        //conversion rate = 171.10301428
        double returnable = value;
        if(value > 1711029629.49) {returnable = (value * 1.2550823);}
        cir.setReturnValue(returnable);
    }
}