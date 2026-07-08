package com.chewylopez.echoofthefarlands;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

public class Config
{
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    //ingame config values
    public static final ModConfigSpec.IntValue FARLANDS_LOCATION_CUSTOM =
            BUILDER.comment("this is not the server value, this is where the custom value you can change from the in-game menu is stored\n")
                    .comment("farlands custom location").defineInRange("farlandsCustomLocation", 1000000, 1, Integer.MAX_VALUE);

    //server config values
    public static final ModConfigSpec.IntValue FARLANDS_LOCATION_WORLD =
            BUILDER.comment("\nthese are the internal values, only change these manually if you are setting up a server\n")
                    .comment("farlands location").defineInRange("farlandsLocation", 1000000, 1, Integer.MAX_VALUE);
    public static final ModConfigSpec.IntValue FARLANDS_GEN_TYPE =
            BUILDER.comment("farlands gen type").comment().defineInRange("farlandsGenType", 0, 0, 7);
    public static ModConfigSpec.BooleanValue FARLANDS_WALL_TEXTURE_PATCH =
            BUILDER.comment("farlands wall texture patch").define("farlandsWallTexturePatch", true);
    public static ModConfigSpec.BooleanValue FARLANDS_LIQUID_FIX =
            BUILDER.comment("farlands liquid fix").define("farlandsLiquidFix", true);
    public static ModConfigSpec.BooleanValue FARLANDS_BEDROCK_FIX =
            BUILDER.comment("farlands bedrock patch").define("farlandsBedrockPatch", true);

    public static final ModConfigSpec SPEC = BUILDER.build();

}