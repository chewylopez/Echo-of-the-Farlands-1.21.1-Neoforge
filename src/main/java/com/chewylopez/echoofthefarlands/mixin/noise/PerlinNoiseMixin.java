package com.chewylopez.echoofthefarlands.mixin.noise;

import com.chewylopez.echoofthefarlands.Config;
import net.minecraft.world.level.levelgen.synth.PerlinNoise;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({PerlinNoise.class})
public class PerlinNoiseMixin {

    @Unique
    private static final double NOISE_SCALAR_CONSTANT = 171.103;

    @Unique
    private static final double FARLANDS_ORIGINAL = 12550823;

    @Unique
    private static final double LINEAR_SCALAR_FACTOR = Math.pow(10.0, 15.0);

    @Unique
    private static double globalX;

    @Unique
    private static double globalZ;

    @Inject(method = {"getValue(DDD)D"}, at = {@At("HEAD")})
    public void getValue(double x, double y, double z, CallbackInfoReturnable<Double> cir){
        globalX = x;
        globalZ = z;
    }

    @Unique
    private static double getFarlandsLocation(){
        return Config.FARLANDS_LOCATION_WORLD;
    }

    @Unique
    private static int getFarlandsGenType(){
        return Config.FARLANDS_GEN_TYPE;
    }

    @Unique
    private static double getStartPosDelta(){
        return FARLANDS_ORIGINAL - getFarlandsLocation();
    }

    @Unique
    private static double getScaledFarlandsPosition(){
        return getFarlandsLocation() * NOISE_SCALAR_CONSTANT;
    }

    @Unique
    private static double expScaleFactor(double value){
        return 1 + (2*((Math.abs(value) - getScaledFarlandsPosition())/(NOISE_SCALAR_CONSTANT * 1000000)));
    }

    /**
     * @param value
     * value is scaled by the constant,
     * x and z are not scaled values, they use coordinates directly
     */
    @Inject(method = {"wrap"}, at = {@At("TAIL")}, cancellable = true)
    private static void wrapFarlands(double value, CallbackInfoReturnable<Double> cir) {

        double returnable = value;

        if(getFarlandsLocation() > FARLANDS_ORIGINAL) {
            returnable = farlandsDelayedNoise(value);
        }

        if(Math.abs(value) > getScaledFarlandsPosition()) {
            if (getFarlandsGenType() == 0) {
                returnable = offsetUnscaledGeneration(value);
            } else if (getFarlandsGenType() == 1) {
                returnable = linearScaling(value);
            } else if (getFarlandsGenType() == 2) {
                returnable = exponentialScaling(value);
            } else if (getFarlandsGenType() == 3) {
                returnable = sineWaveScaling(value);
            }
        }

        cir.setReturnValue(returnable);

    }

    @Unique
    private static double farlandsDelayedNoise(double value) {
        return value * (FARLANDS_ORIGINAL / getFarlandsLocation());
    }

    @Unique
    private static double exponentialScaling(double value) {

        double returnable = value;

        //positive scaling
        if(globalX > getFarlandsLocation() || globalZ > getFarlandsLocation()) {
            returnable = Math.pow(FARLANDS_ORIGINAL * NOISE_SCALAR_CONSTANT, expScaleFactor(value));
        }

        //negative scaling
        if(globalX < -getFarlandsLocation() || globalZ < -getFarlandsLocation()) {
            returnable = -Math.pow(FARLANDS_ORIGINAL * NOISE_SCALAR_CONSTANT, expScaleFactor(value));
        }

        return returnable;

    }

    @Unique
    private static double linearScaling(double value) {

        double returnable = value;

        //positive scaling
        if(globalX > getFarlandsLocation() || globalZ > getFarlandsLocation()) {
            returnable = (value * LINEAR_SCALAR_FACTOR);
        }

        //negative scaling
        if(globalX < -getFarlandsLocation() || globalZ < -getFarlandsLocation()) {
            returnable = (value * LINEAR_SCALAR_FACTOR);
        }

        return returnable;

    }

    @Unique
    private static double sineWaveScaling(double value){

        double returnable = value;

        //positive scaling
        if(globalX > getFarlandsLocation() || globalZ > getFarlandsLocation()) {
            returnable = (2 * Math.sin(value)) * (value + (getStartPosDelta() * NOISE_SCALAR_CONSTANT) );
        }

        //negative scaling
        if(globalX < -getFarlandsLocation() || globalZ < -getFarlandsLocation()) {
            returnable = -(2 * Math.sin(value)) * (value - (getStartPosDelta() * NOISE_SCALAR_CONSTANT) );
        }

        return returnable;
    }

    @Unique
    private static double offsetUnscaledGeneration(double value){

        double returnable = value;

        //positive
        if(globalX > getFarlandsLocation() || globalZ > getFarlandsLocation()) {
            returnable = value + (getStartPosDelta() * NOISE_SCALAR_CONSTANT);
        }

        //negative
        if(globalX < -getFarlandsLocation() || globalZ < -getFarlandsLocation()) {
            returnable = value - (getStartPosDelta() * NOISE_SCALAR_CONSTANT);
        }

        return returnable;
    }


}