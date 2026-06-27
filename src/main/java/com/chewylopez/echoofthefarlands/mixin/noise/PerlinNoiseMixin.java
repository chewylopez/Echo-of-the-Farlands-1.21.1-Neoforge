package com.chewylopez.echoofthefarlands.mixin.noise;

import com.chewylopez.echoofthefarlands.Config;
import com.chewylopez.echoofthefarlands.world.farlands.FarlandsContext;
import net.minecraft.util.Mth;
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
    private static final double MOAT_STATIC_POSITION = 5000000;

    @Unique
    private static final double FARLANDS_WALL_DECORATIONS_BUFFER = 500;

    @Unique
    private static final double LINEAR_SCALAR_FACTOR = Math.pow(10.0, 15.0);

    @Unique
    private static final double LINEAR_CLIFF_MULTIPLIER = 1344.4532;

    @Unique
    private static double getMoatEnding() {
        return switch (getFarlandsGenType()) {
            case 0 -> MOAT_STATIC_POSITION;
            case 1 -> 5.0 * getFarlandsLocation();
            default -> Double.MAX_VALUE;
        };
    }

    @Unique
    private static double getFarlandsWallTextureBuffer(){
        return Config.FARLANDS_WALL_TEXTURE_PATCH ? FARLANDS_WALL_DECORATIONS_BUFFER : 0;
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
    private static double getFarlandsLocation(){
        return Config.FARLANDS_LOCATION_WORLD;
    }

    @Unique
    private static double getScaledFarlandsLocation(){
        return getFarlandsLocation() * NOISE_SCALAR_CONSTANT;
    }

    @Unique
    private static double expScaleFactorStatic(double value){
        return 1 + (2*((getApproxFarlandsProgress(Math.abs(value)))/1000000));
    }

    @Unique
    private static double expScaleFactorProportional(double value){
        return 1 + (2*(getApproxFarlandsProgress(Math.abs(value))/(getFarlandsLocation())));
    }

    @Unique
    private static double getApproxFarlandsProgress(double value){
        return (Math.abs(value) - getScaledFarlandsLocation()) / NOISE_SCALAR_CONSTANT;
    }

    /**
     * @param value
     * value is scaled by the constant,
     * x and z are not scaled values, they use coordinates directly
     */
    @Inject(method = {"wrap"}, at = {@At("HEAD")}, cancellable = true)
    private static void wrapFarlands(double value, CallbackInfoReturnable<Double> cir) {

        double radius = FarlandsContext.CHUNK_RADIUS.get();
        if(radius >= getMoatEnding()) return;

        double returnable = value;

        if (Math.abs(value) > getScaledFarlandsLocation()) {
            if (Math.abs(value) < getScaledFarlandsLocation() + getFarlandsWallTextureBuffer() * NOISE_SCALAR_CONSTANT){
                returnable = randomPorosityFarlands(value, 0.20);
            } else {
                if(getFarlandsLocation() > FARLANDS_ORIGINAL + getFarlandsWallTextureBuffer()) {
                    returnable = farlandsDelayedNoise(value);
                }
                else {
                    if(getFarlandsGenType() == 0) {
                        returnable = piecewiseStaticScaling(value);
                    } else if(getFarlandsGenType() == 1) {
                        returnable = piecewiseProportionalScaling(value);
                    } else if(getFarlandsGenType() == 2) {
                        returnable = offsetUnscaledGeneration(value);
                    } else if(getFarlandsGenType() == 3) {
                        returnable = exponentialScalingStatic(value);
                    } else if(getFarlandsGenType() == 4) {
                        returnable = exponentialScalingProportional(value);
                    } else if(getFarlandsGenType() == 5) {
                        returnable = linearScaling(value);
                    } else if(getFarlandsGenType() == 6) {
                        returnable = sineWaveScaling(value);
                    } else if(getFarlandsGenType() == 7) {
                        //standard farlands rest handled in noise router
                        returnable = offsetUnscaledGeneration(value);
                    }
                }
            }
        }
        if (returnable != value){
            cir.setReturnValue(returnable);
        }
    }

    @Unique
    private static double farlandsDelayedNoise(double value) {
        return value * (FARLANDS_ORIGINAL / getFarlandsLocation());
    }

    @Unique
    private static double exponentialScalingStatic(double value) {
        double returnable = value;
        if(value >= 0) {
            returnable = Math.pow(Integer.MAX_VALUE, expScaleFactorStatic(value));
        }
        if(value < 0) {
            returnable = -Math.pow(Integer.MAX_VALUE, expScaleFactorStatic(value));
        }
        return returnable;
    }

    @Unique
    private static double exponentialScalingProportional(double value) {
        double returnable = value;
        if(value >= 0) {
            returnable = Math.pow(Integer.MAX_VALUE, expScaleFactorProportional(value));
        }
        if(value < 0) {
            returnable = -Math.pow(Integer.MAX_VALUE, expScaleFactorProportional(value));
        }
        return returnable;
    }

    @Unique
    private static double linearScaling(double value) {
        return (value * LINEAR_SCALAR_FACTOR);
    }

    @Unique
    private static double sineWaveScaling(double value){
        double returnable = value;
        if(value >= 0) {
            returnable = (2 * Math.sin(value)) * (value + (getStartPosDelta() * NOISE_SCALAR_CONSTANT) );
        }
        if(value < 0) {
            returnable = -(2 * Math.sin(value)) * (value - (getStartPosDelta() * NOISE_SCALAR_CONSTANT) );
        }
        return returnable;
    }

    @Unique
    private static double offsetUnscaledGeneration(double value){
        double returnable = value;
        if(value >= 0) {
            returnable = value + (getStartPosDelta() * NOISE_SCALAR_CONSTANT);
        }
        if(value < 0) {
            returnable = value - (getStartPosDelta() * NOISE_SCALAR_CONSTANT);
        }
        return returnable;
    }

    /**
     * @param error_ratio
     * (0 = flat wall)
     * (1 = no generation)
     * @return homogenously random inclusions in the farlands
     */
    @Unique
    private static double randomPorosityFarlands(double value, double error_ratio) {
        double blendSlope = 1 / FARLANDS_WALL_DECORATIONS_BUFFER;
        return Integer.MAX_VALUE + (((1 - (error_ratio * (1 - blendSlope * getApproxFarlandsProgress(value)))) - Math.random()) * Math.abs(value));
    }

    @Unique
    private static double piecewiseStaticScaling(double value){

        double returnable = Math.abs(value);
        if(getApproxFarlandsProgress(value) < 4000000) {
            if (getApproxFarlandsProgress(value) >= getFarlandsWallTextureBuffer()) {
                returnable += Integer.MAX_VALUE;
            }
            if (getApproxFarlandsProgress(value) >= 500000 && getApproxFarlandsProgress(value) < 1000000) {
                returnable += 1.5 * (getApproxFarlandsProgress(value) / getFarlandsLocation()) * Math.pow(10, 51);
            }
            if (getApproxFarlandsProgress(value) >= 1000000) {
                returnable += exponentialScalingStatic((getFarlandsLocation() + 2243051.5) * NOISE_SCALAR_CONSTANT);
            }
            if (getApproxFarlandsProgress(value) >= 1500000) {
                returnable += Double.MAX_VALUE;
            }
        }
        else {
            return value - (double) Mth.lfloor(value / (double)3.3554432E7F + (double)0.5F) * (double)3.3554432E7F;
        }

        return returnable;
    }

    @Unique
    private static double piecewiseProportionalScaling(double value){

        double breakUnscaled = getFarlandsLocation() * 0.5;
        double breakFartherlands = getFarlandsLocation();
        double breakMoat = getFarlandsLocation() * 1.5;
        double breakDistantlands = getFarlandsLocation() * 4;

        double returnable = Math.abs(value);

        if(getApproxFarlandsProgress(value) < breakDistantlands) {
            if (getApproxFarlandsProgress(value) >= getFarlandsWallTextureBuffer()) {
                returnable += Integer.MAX_VALUE;
            }
            if (getApproxFarlandsProgress(value) >= breakUnscaled && getApproxFarlandsProgress(value) < breakFartherlands) {
                returnable += 1.5 * (getApproxFarlandsProgress(value) / getFarlandsLocation()) * Math.pow(10, 51);
            }
            if (getApproxFarlandsProgress(value) >= breakFartherlands) {
                returnable += exponentialScalingStatic((getFarlandsLocation() + 2243051.5) * NOISE_SCALAR_CONSTANT);
            }
            if (getApproxFarlandsProgress(value) >= breakMoat) {
                returnable += Double.MAX_VALUE;
            }
        }
        else {
            return value - (double) Mth.lfloor(value / (double)3.3554432E7F + (double)0.5F) * (double)3.3554432E7F;
        }

        return returnable;
    }


}