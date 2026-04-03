package com.chewylopez.echoofthefarlands;
import net.neoforged.neoforge.common.ModConfigSpec;

public class Config
{
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec.IntValue FARLANDS_LOCATION_CONFIG = BUILDER.comment("farlands location").defineInRange("farlandsLocation", 1000000, 1, Integer.MAX_VALUE);
    public static final ModConfigSpec SPEC = BUILDER.build();

    public static int FARLANDS_LOCATION_WORLD = 10000;
    public static int FARLANDS_GEN_TYPE = 0;

}