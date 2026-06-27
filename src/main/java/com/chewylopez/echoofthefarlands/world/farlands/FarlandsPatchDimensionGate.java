package com.chewylopez.echoofthefarlands.world.farlands;

import net.minecraft.core.Holder;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;

public class FarlandsPatchDimensionGate {
    public static boolean appliesTo(NoiseGeneratorSettings settings) {
        return settings.defaultBlock().is(Blocks.STONE);
    }
    public static boolean appliesTo(Holder<NoiseGeneratorSettings> holder) {
        return appliesTo(holder.value());
    }
}
