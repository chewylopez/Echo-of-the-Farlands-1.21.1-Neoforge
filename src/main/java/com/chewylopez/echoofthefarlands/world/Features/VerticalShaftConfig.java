package com.chewylopez.echoofthefarlands.world.Features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record VerticalShaftConfig(int minRadius, int maxRadius) implements FeatureConfiguration {
    public static final Codec<VerticalShaftConfig> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("min_radius").forGetter(VerticalShaftConfig::minRadius),
                    Codec.INT.fieldOf("max_radius").forGetter(VerticalShaftConfig::maxRadius)
            ).apply(instance, VerticalShaftConfig::new)
    );
}