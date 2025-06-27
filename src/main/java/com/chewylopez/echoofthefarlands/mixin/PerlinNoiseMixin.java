package com.chewylopez.echoofthefarlands.mixin;

import net.minecraft.world.level.levelgen.synth.PerlinNoise;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({PerlinNoise.class})
public class PerlinNoiseMixin {

    @Unique
    private static final double CONVERSION_FACTOR = 171.10301428;

    private static final double FARLANDS_LOCATION = 1000000;
    private static final double FARLANDS_ORIGINAL = 12550823;
    private static final double STRIPELANDS_LOCATION = 6499000; //find new values
    private static final double MOAT_LOCATION = 21103542; // find new values
    private static final double START_POS_DELTA = FARLANDS_ORIGINAL - FARLANDS_LOCATION;
    private static final double SCALED_FARLANDS_POSITION = FARLANDS_LOCATION * CONVERSION_FACTOR;
    private static final double STRIPE_OFFSET_X = (Math.pow((STRIPELANDS_LOCATION + START_POS_DELTA + 10) * CONVERSION_FACTOR, powFactorX(STRIPELANDS_LOCATION * CONVERSION_FACTOR)));
    private static final double STRIPE_OFFSET_Z = (Math.pow((STRIPELANDS_LOCATION + START_POS_DELTA + 10) * CONVERSION_FACTOR, powFactorZ(STRIPELANDS_LOCATION * CONVERSION_FACTOR)));

    @Unique
    private static double globalX;
    @Unique
    private static double globalZ;

    @Inject(method = {"getValue(DDD)D"}, at = {@At("HEAD")})
    public void getValue(double x, double y, double z, CallbackInfoReturnable<Double> cir){
        globalX = x;
        globalZ = z;
    }

    //powFactor increases by (some constant) per 1,000,000 blocks
    @Unique
    private static double powFactorX(double value){
        return 5 + (0.8*((Math.abs(value) - SCALED_FARLANDS_POSITION)/(CONVERSION_FACTOR * 1000000)));
    }
    @Unique
    private static double powFactorZ(double value){
        return 5 + (5.725065*((Math.abs(value) - SCALED_FARLANDS_POSITION)/(CONVERSION_FACTOR * 1000000)));
    }


    /**
     * @param value
     * value is scaled by the constant,
     * x and z are not scaled values, use coordinates directly
     */
    @Inject(method = {"wrap"}, at = {@At("TAIL")}, cancellable = true)
    private static void wrapFarlands(double value, CallbackInfoReturnable<Double> cir) {

        double returnable = value;

        //prevent monoliths pre far lands (X-axis)
        if((globalX < (FARLANDS_LOCATION - 10) && value < (CONVERSION_FACTOR * (FARLANDS_LOCATION - 10)))  &&  (globalX > (FARLANDS_LOCATION - 100000) && value > (CONVERSION_FACTOR * (FARLANDS_LOCATION - 100000)))) {
            returnable = (value - (CONVERSION_FACTOR * 100000));
        }
        else if((globalX > -(FARLANDS_LOCATION - 10) && value > (CONVERSION_FACTOR * -(FARLANDS_LOCATION - 10)))  &&  (globalX < -(FARLANDS_LOCATION - 100000) && value < (CONVERSION_FACTOR * -(FARLANDS_LOCATION - 100000)))) {
            returnable = (value + (CONVERSION_FACTOR * 100000));
        }
        //prevent monoliths pre far lands (Z-axis)
        if((globalZ < (FARLANDS_LOCATION - 10) && value < (CONVERSION_FACTOR * (FARLANDS_LOCATION - 10)))  &&  (globalZ > (FARLANDS_LOCATION - 100000) && value > (CONVERSION_FACTOR * (FARLANDS_LOCATION - 100000)))) {
            returnable = (value - (CONVERSION_FACTOR * 100000));
        }
        else if((globalZ > -(FARLANDS_LOCATION - 10) && value > (CONVERSION_FACTOR * -(FARLANDS_LOCATION - 10)))  &&  (globalZ < -(FARLANDS_LOCATION - 100000) && value < (CONVERSION_FACTOR * -(FARLANDS_LOCATION - 100000)))) {
            returnable = (value + (CONVERSION_FACTOR * 100000));
        }


        //far lands edge facade (X-axis)
        if((globalX > ((FARLANDS_LOCATION - 100)) && value > ((FARLANDS_LOCATION - 10) * CONVERSION_FACTOR)) && (globalX < ((FARLANDS_LOCATION + 100000)) && value < ((FARLANDS_LOCATION + 100) * CONVERSION_FACTOR))) {
            returnable = (value + (CONVERSION_FACTOR * START_POS_DELTA));
        }
        else if((globalX < -((FARLANDS_LOCATION - -100)) && value < -((FARLANDS_LOCATION - 10) * CONVERSION_FACTOR)) && (globalX > -((FARLANDS_LOCATION + 100000)) && value > -((FARLANDS_LOCATION + 100) * CONVERSION_FACTOR))) {
            returnable = -(Math.abs(value) + (CONVERSION_FACTOR * START_POS_DELTA));
        }
        //far lands edge facade (Z-axis)
        if((globalZ > ((FARLANDS_LOCATION - 100)) && value > ((FARLANDS_LOCATION - 10) * CONVERSION_FACTOR)) && (globalZ < ((FARLANDS_LOCATION + 100000)) && value < ((FARLANDS_LOCATION + 100) * CONVERSION_FACTOR))) {
            returnable = (value + (CONVERSION_FACTOR * START_POS_DELTA));
        }
        else if((globalZ < -((FARLANDS_LOCATION - 100)) && value < -((FARLANDS_LOCATION - 10) * CONVERSION_FACTOR)) && (globalZ > -((FARLANDS_LOCATION + 100000)) && value > -((FARLANDS_LOCATION + 100) * CONVERSION_FACTOR))) {
            returnable = -(Math.abs(value) + (CONVERSION_FACTOR * START_POS_DELTA));
        }

        //SOLVE
        //far lands adjusted noise progression into cubelands (X-axis straight)
        if(globalZ < (FARLANDS_LOCATION) && globalZ > -(FARLANDS_LOCATION)) {
            if (value > (CONVERSION_FACTOR * (FARLANDS_LOCATION + 10)) && value < (CONVERSION_FACTOR * (STRIPELANDS_LOCATION + 1000))) {
                returnable = (Math.pow(value + (CONVERSION_FACTOR * START_POS_DELTA), powFactorX(value)));
            } else if (value < -(CONVERSION_FACTOR * (FARLANDS_LOCATION + 10)) && value > -(CONVERSION_FACTOR * (STRIPELANDS_LOCATION + 1000)))
                returnable = (Math.pow(Math.abs(value) + (CONVERSION_FACTOR * START_POS_DELTA), powFactorX(value)));
        }
        //far lands adjusted noise progression into cubelands (Z-axis straight)
        if(globalX < (FARLANDS_LOCATION) && globalX > -(FARLANDS_LOCATION)) {
            if (value > (CONVERSION_FACTOR * (FARLANDS_LOCATION + 10)) && value < (CONVERSION_FACTOR * (STRIPELANDS_LOCATION + 10))) {
                returnable = (Math.pow(value + (CONVERSION_FACTOR * START_POS_DELTA), powFactorZ(value)));
            } else if (value < -(CONVERSION_FACTOR * (FARLANDS_LOCATION + 10)) && value > -(CONVERSION_FACTOR * (STRIPELANDS_LOCATION + 10))) {
                returnable = (Math.pow(Math.abs(value) + (CONVERSION_FACTOR * START_POS_DELTA), powFactorZ(value)));
            }
        }


        //stripe lands extended (X-axis straight)
        if(globalZ < (FARLANDS_LOCATION) && globalZ > -(FARLANDS_LOCATION)) {
            if (value > (CONVERSION_FACTOR * (STRIPELANDS_LOCATION + 10)) && value < (CONVERSION_FACTOR * (MOAT_LOCATION))) {
                returnable = value + STRIPE_OFFSET_X;
            } else if (value < -(CONVERSION_FACTOR * (STRIPELANDS_LOCATION + 10)) && value > -(CONVERSION_FACTOR * (MOAT_LOCATION))) {
                returnable = value - STRIPE_OFFSET_X;
            }
        }
        //stripe lands extended (Z-axis straight)
        if(globalX < (FARLANDS_LOCATION) && globalX > -(FARLANDS_LOCATION)) {
            if (value > (CONVERSION_FACTOR * (STRIPELANDS_LOCATION + 10)) && value < (CONVERSION_FACTOR * (MOAT_LOCATION))) {
                returnable = value + STRIPE_OFFSET_Z;
            } else if (value < -(CONVERSION_FACTOR * (STRIPELANDS_LOCATION + 10)) && value > -(CONVERSION_FACTOR * (MOAT_LOCATION))) {
                returnable = value - STRIPE_OFFSET_Z;
            }
        }

        cir.setReturnValue(returnable);

    }
}