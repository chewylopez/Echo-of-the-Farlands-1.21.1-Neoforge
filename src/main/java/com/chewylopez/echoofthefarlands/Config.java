package com.chewylopez.echoofthefarlands;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

public class Config
{
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    //ingame config values
    public static final ModConfigSpec.IntValue FARLANDS_LOCATION_CUSTOM =
            BUILDER.comment("this is where the custom value you can change from the in-game menu is stored, this is not referenced by the server")
                    .comment("FARLANDS CUSTOM LOCATION").defineInRange("farlandsCustomLocation", 1000000, 1, Integer.MAX_VALUE);

    //server config values
    public static final ModConfigSpec.IntValue FARLANDS_LOCATION_WORLD =
            BUILDER.comment("\nthese are the internal values, only change these manually if you are setting up a server")
                    .comment("FARLANDS LOCATION").defineInRange("farlandsLocation", 1000000, 1, Integer.MAX_VALUE);
    public static final ModConfigSpec.IntValue FARLANDS_GEN_TYPE =
            BUILDER.comment("\nFARLANDS GEN TYPE")
                    .comment("types are as follows:\n0. piecewise\n1. proportional piecewise\n2. Unscaled\n3. exponential\n4. proportional exponential\n5. Linear\n6. Sine\n7. Multi Trig")
                    .defineInRange("farlandsGenType", 0, 0, 7);
    public static ModConfigSpec.BooleanValue FARLANDS_WALL_TEXTURE_PATCH =
            BUILDER.comment("\nFARLANDS WALL TEXTURE PATCH").define("farlandsWallTexturePatch", true);
    public static ModConfigSpec.BooleanValue FARLANDS_LIQUID_FIX =
            BUILDER.comment("\nFARLANDS LIQUID FIX").define("farlandsLiquidFix", true);
    public static ModConfigSpec.BooleanValue FARLANDS_BEDROCK_FIX =
            BUILDER.comment("\nFARLANDS BEDROCK FIX").define("farlandsBedrockFix", true);
    public static ModConfigSpec.BooleanValue WALL_STRUCTURES_GENERATION =
            BUILDER.comment("\nWALL STRUCTURES GENERATION").define("wallStructuresGeneration", true);

    public static final ModConfigSpec SPEC = BUILDER.build();

}