package com.chewylopez.echoofthefarlands.mixin;

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
    private static final double CONVERSION_FACTOR_CONSTANT = 171.10301428;

    @Unique
    private static final double FARLANDS_ORIGINAL = 12550821;

    @Unique
    private static final double FARLANDS_EDGE_BUFFER = 10;

    @Unique
    private static final double FARLANDS_SCALING_BUFFER = 0;

    @Unique
    private static final long LINEAR_SCALAR_FACTOR = (long) Math.pow(10, 12);

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
    private static double getStripelandsLocation(){
        return getFarlandsLocation() * 2; //find values
    }

    @Unique
    private static double getMoatLocation(){
        return getFarlandsLocation() * 2.1; //find values
    }

    @Unique
    private static double getStartPosDelta(){
        return FARLANDS_ORIGINAL - getFarlandsLocation();
    }

    @Unique
    private static double getScaledFarlandsPosition(){
        return getFarlandsLocation() * CONVERSION_FACTOR_CONSTANT;
    }

    @Unique
    private static double expScaleFactor(double value){
        return 1 + (2*((Math.abs(value) - getScaledFarlandsPosition())/(CONVERSION_FACTOR_CONSTANT * 1000000)));
    }

    //powFactor increases by (some constant) per 1,000,000 blocks
    //with 2: farther lands = 3,222,396, moat = 3,225,040
    @Unique
    private static double powFactorX(double value){
        return 1 + (3*((Math.abs(value) - getScaledFarlandsPosition())/(CONVERSION_FACTOR_CONSTANT * 1000000)));
    }
    //z scaling seems to be smooth, barely any sheer changes like the x axis
    //make constant to equal scaling, for farther lands = 5 million
    @Unique
    private static double powFactorZ(double value){
        return 1 + (7.5*((Math.abs(value) - getScaledFarlandsPosition())/(CONVERSION_FACTOR_CONSTANT * 1000000)));
    }

    /**
     * @param value
     * value is scaled by the constant,
     * x and z are not scaled values, they use coordinates directly
     */
    @Inject(method = {"wrap"}, at = {@At("TAIL")}, cancellable = true)
    private static void wrapFarlands(double value, CallbackInfoReturnable<Double> cir) {

        double returnable = value;

        //returnable = farlandsEdgeTweak(value);

        if(getFarlandsLocation() > FARLANDS_ORIGINAL) {
            returnable = farlandsDelayedNoise(value);
        }

        if(value > (CONVERSION_FACTOR_CONSTANT * (getFarlandsLocation() + FARLANDS_SCALING_BUFFER)) || value < -(CONVERSION_FACTOR_CONSTANT * (getFarlandsLocation() + FARLANDS_SCALING_BUFFER))) {
            if (getFarlandsGenType() == 0) {
                returnable = exponentialScaling(value);
            } else if (getFarlandsGenType() == 1) {
                returnable = linearScaling(value);
            } else if (getFarlandsGenType() == 2) {
                returnable = offsetUnscaledGeneration(value);
            }
        }

        cir.setReturnValue(returnable);

    }

    private static double farlandsDelayedNoise(double value) {
        return value * (FARLANDS_ORIGINAL / getFarlandsLocation());
    }

    private static double farlandsEdgeTweak(double value){

        double returnable = value;

        //far lands edge (X-axis)
        if((globalX > ((getFarlandsLocation() - FARLANDS_EDGE_BUFFER)) && value > ((getFarlandsLocation() - FARLANDS_EDGE_BUFFER) * CONVERSION_FACTOR_CONSTANT))) {
            returnable = (value + (CONVERSION_FACTOR_CONSTANT * getStartPosDelta()));
        }
        else if((globalX < -((getFarlandsLocation() - FARLANDS_EDGE_BUFFER)) && value < -((getFarlandsLocation() - FARLANDS_EDGE_BUFFER) * CONVERSION_FACTOR_CONSTANT))) {
            returnable = -(Math.abs(value) + (CONVERSION_FACTOR_CONSTANT * getStartPosDelta()));
        }

        //far lands edge(Z-axis)
        if((globalZ > ((getFarlandsLocation() - FARLANDS_EDGE_BUFFER)) && value > ((getFarlandsLocation() - FARLANDS_EDGE_BUFFER) * CONVERSION_FACTOR_CONSTANT))) {
            returnable = (value + (CONVERSION_FACTOR_CONSTANT * getStartPosDelta()));
        }
        else if((globalZ < -((getFarlandsLocation() - FARLANDS_EDGE_BUFFER)) && value < -((getFarlandsLocation() - FARLANDS_EDGE_BUFFER) * CONVERSION_FACTOR_CONSTANT))) {
            returnable = -(Math.abs(value) + (CONVERSION_FACTOR_CONSTANT * getStartPosDelta()));
        }

        return returnable;

    }

    private static double exponentialScaling(double value) {

        double returnable = value;

        //positive scaling
        if(globalX > getFarlandsLocation() || globalZ > getFarlandsLocation()) {
            returnable = (FARLANDS_ORIGINAL * CONVERSION_FACTOR_CONSTANT) +
                    (Math.pow( (value + (CONVERSION_FACTOR_CONSTANT * getStartPosDelta())) - (getFarlandsLocation() * CONVERSION_FACTOR_CONSTANT), expScaleFactor(value)));
        }

        //negative scaling
        if(globalX < -getFarlandsLocation() || globalZ < -getFarlandsLocation()) {
            returnable = -((FARLANDS_ORIGINAL * CONVERSION_FACTOR_CONSTANT) +
                    (Math.pow( (value + (CONVERSION_FACTOR_CONSTANT * getStartPosDelta())) - (getFarlandsLocation() * CONVERSION_FACTOR_CONSTANT), expScaleFactor(value))));
        }

        return returnable;

    }

    private static double linearScaling(double value) {

        double returnable = value;

        //positive scaling
        if(globalX > getFarlandsLocation() || globalZ > getFarlandsLocation()) {
                returnable = ((value + (CONVERSION_FACTOR_CONSTANT * getStartPosDelta())) - (getFarlandsLocation() * CONVERSION_FACTOR_CONSTANT)) * LINEAR_SCALAR_FACTOR;
        }

        //negative scaling
        if(globalX < -getFarlandsLocation() || globalZ < -getFarlandsLocation()) {
                returnable = -(((value + (CONVERSION_FACTOR_CONSTANT * getStartPosDelta())) - (getFarlandsLocation() * CONVERSION_FACTOR_CONSTANT)) * LINEAR_SCALAR_FACTOR);
        }

        return returnable;

    }

    private static double offsetUnscaledGeneration(double value){

        double returnable = value;

        //positive
        if(globalX > getFarlandsLocation() || globalZ > getFarlandsLocation()) {
            returnable = value + (getStartPosDelta() * CONVERSION_FACTOR_CONSTANT);
        }

        //negative
        if(globalX < -getFarlandsLocation() || globalZ < -getFarlandsLocation()) {
            returnable = value - (getStartPosDelta() * CONVERSION_FACTOR_CONSTANT);
        }

        return returnable;
    }


}