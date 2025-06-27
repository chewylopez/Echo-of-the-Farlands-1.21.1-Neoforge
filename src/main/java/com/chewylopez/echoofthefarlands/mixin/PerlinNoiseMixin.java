package com.chewylopez.echoofthefarlands.mixin;

import net.minecraft.world.level.levelgen.synth.PerlinNoise;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 (at position 10,000,000 and powFactor starting value 1):

 *cube errors start appearing rarely and gradually increase until stripe lands.
 *stripe lands begin at 17,433,584 blocks out.
 *stripe lands end at 17,443,856 blocks out.
 -the moat ends begins after
 **/
@Mixin({PerlinNoise.class})
public class PerlinNoiseMixin {



    @Unique
    private static final double CONVERSION_FACTOR = 171.10301428;
    @Unique
    private static final double FARLANDS_LOCATION = 10000000;
    @Unique
    private static final double FARLANDS_ORIGINAL = 12550823;
    @Unique
    private static final double STRIPELANDS_LOCATION = 15493684;
    @Unique
    private static final double START_POS_DELTA = FARLANDS_ORIGINAL - FARLANDS_LOCATION;
    @Unique
    private static final double SCALED_POSITION = FARLANDS_LOCATION * CONVERSION_FACTOR;

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
    private static double powFactorX(double value, double offset){
        return 1 + (0.8*((Math.abs(value) - offset)/(CONVERSION_FACTOR * 1000000))); //stripelands 15,493,684
    }
    private static double powFactorZ(double value, double offset){
        return 1 + (5.725065*((Math.abs(value) - offset)/(CONVERSION_FACTOR * 1000000)));
    }


    /**
     * @param value
     * value is scaled by the constant,
     * x and z are not scaled values, use coordinates directly
     */
    @Inject(method = {"wrap"}, at = {@At("TAIL")}, cancellable = true)
    private static void wrapFarlands(double value, CallbackInfoReturnable<Double> cir) {

        double returnable = value;

        double offset = FARLANDS_LOCATION * CONVERSION_FACTOR;

        //value offset for the stripe lands (Z-axis) (approx = 9.84 x 10^306)
        double stripeOffsetStart = (Math.pow(17433590 * CONVERSION_FACTOR + (CONVERSION_FACTOR * 2550823), powFactorX(17433590, SCALED_POSITION)));
        double stripeOffsetEnd = (Math.pow(17443850  * CONVERSION_FACTOR + (CONVERSION_FACTOR * 2550823), powFactorX(17443850, SCALED_POSITION)));

        //prevent monoliths pre far lands (X-axis)
        if((globalX < (FARLANDS_LOCATION - 10) && value < (CONVERSION_FACTOR * (FARLANDS_LOCATION - 10)))  &&  (globalX > (FARLANDS_LOCATION - 1000000) && value > (CONVERSION_FACTOR * (FARLANDS_LOCATION - 1000000)))) {
            returnable = (value - (CONVERSION_FACTOR * 1000000));
        }
        else if((globalX > -(FARLANDS_LOCATION - 10) && value > (CONVERSION_FACTOR * -(FARLANDS_LOCATION - 10)))  &&  (globalX < -(FARLANDS_LOCATION - 1000000) && value < (CONVERSION_FACTOR * -(FARLANDS_LOCATION - 1000000)))) {
            returnable = (value + (CONVERSION_FACTOR * 1000000));
        }

        //prevent monoliths pre far lands (Z-axis)
        if((globalZ < (FARLANDS_LOCATION - 10) && value < (CONVERSION_FACTOR * (FARLANDS_LOCATION - 10)))  &&  (globalZ > (FARLANDS_LOCATION - 1000000) && value > (CONVERSION_FACTOR * (FARLANDS_LOCATION - 1000000)))) {
            returnable = (value - (CONVERSION_FACTOR * 1000000));
        }
        else if((globalZ > -(FARLANDS_LOCATION - 10) && value > (CONVERSION_FACTOR * -(FARLANDS_LOCATION - 10)))  &&  (globalZ < -(FARLANDS_LOCATION - 1000000) && value < (CONVERSION_FACTOR * -(FARLANDS_LOCATION - 1000000)))) {
            returnable = (value + (CONVERSION_FACTOR * 1000000));
        }

        //far lands edge facade (X-axis)
        if((globalX > ((FARLANDS_LOCATION - 10)) && value > ((FARLANDS_LOCATION - 10) * CONVERSION_FACTOR)) && (globalX < ((FARLANDS_LOCATION + 1000000)) && value < ((FARLANDS_LOCATION + 10) * CONVERSION_FACTOR))) {
            returnable = (value + (CONVERSION_FACTOR * START_POS_DELTA));
        }
        else if((globalX < -((FARLANDS_LOCATION - -100)) && value < -((FARLANDS_LOCATION - 10) * CONVERSION_FACTOR)) && (globalX > -((FARLANDS_LOCATION + 1000000)) && value > -((FARLANDS_LOCATION + 100) * CONVERSION_FACTOR))) {
            returnable = -(Math.abs(value) + (CONVERSION_FACTOR * START_POS_DELTA));
        }

        //far lands edge facade (Z-axis)
        if((globalZ > ((FARLANDS_LOCATION - 10)) && value > ((FARLANDS_LOCATION - 10) * CONVERSION_FACTOR)) && (globalZ < ((FARLANDS_LOCATION + 1000000)) && value < ((FARLANDS_LOCATION + 10) * CONVERSION_FACTOR))) {
            returnable = (value + (CONVERSION_FACTOR * START_POS_DELTA));
        }
        else if((globalZ < -((FARLANDS_LOCATION - 100)) && value < -((FARLANDS_LOCATION - 10) * CONVERSION_FACTOR)) && (globalZ > -((FARLANDS_LOCATION + 1000000)) && value > -((FARLANDS_LOCATION + 100) * CONVERSION_FACTOR))) {
            returnable = -(Math.abs(value) + (CONVERSION_FACTOR * START_POS_DELTA));
        }

        //far lands adjusted noise progression into cubelands (X-axis straight)
        if(globalZ < (FARLANDS_LOCATION) && globalZ > -(FARLANDS_LOCATION)) {
            if (value > (CONVERSION_FACTOR * (FARLANDS_LOCATION + 10))) {
                returnable = (Math.pow(value + (CONVERSION_FACTOR * START_POS_DELTA), powFactorX(value, SCALED_POSITION)));
            } else if (value < -(CONVERSION_FACTOR * (FARLANDS_LOCATION + 10))) {
                returnable = (Math.pow(Math.abs(value) + (CONVERSION_FACTOR * START_POS_DELTA), powFactorX(value, SCALED_POSITION)));
            }
        }
        //far lands adjusted noise progression into cubelands (Z-axis straight)
        if(globalX < (FARLANDS_LOCATION) && globalX > -(FARLANDS_LOCATION)) {
            if (value > (CONVERSION_FACTOR * (FARLANDS_LOCATION + 10))) {
                returnable = (Math.pow(value + (CONVERSION_FACTOR * START_POS_DELTA), powFactorZ(value, SCALED_POSITION)));
            } else if (value < -(CONVERSION_FACTOR * (FARLANDS_LOCATION + 10))) {
                returnable = (Math.pow(Math.abs(value) + (CONVERSION_FACTOR * START_POS_DELTA), powFactorZ(value, SCALED_POSITION)));
            }
        }

        /*
        //far lands into cube lands
        else if(value > (CONVERSION_FACTOR * 10000000) && value <= (CONVERSION_FACTOR * 17533590)) {
            returnable = (Math.pow(value + (CONVERSION_FACTOR * 2550823), powFactor(value, SCALED_POSITION)));
        }
        //stripe lands extension
        else if(value > (CONVERSION_FACTOR * 17433590) && value <= (CONVERSION_FACTOR * 25000000)) {
            returnable = value + stripeSCALED_POSITIONStart;
        }
        //the moat
        else if(value > (CONVERSION_FACTOR * 25000000)){
            returnable = value + stripeSCALED_POSITIONEnd;
        }
         */

        cir.setReturnValue(returnable);
    }
}