package com.chewylopez.echoofthefarlands.world.biome;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

import java.util.stream.Stream;

public class FloatingIslandsBiomeSource extends BiomeSource {

    public static final MapCodec<FloatingIslandsBiomeSource> CODEC =
            RecordCodecBuilder.mapCodec(instance -> instance.group(
                    RegistryFixedCodec.create(Registries.MULTI_NOISE_BIOME_SOURCE_PARAMETER_LIST)
                            .fieldOf("preset").forGetter(s -> s.presetHolder),
                    Biome.CODEC.fieldOf("floating_islands").forGetter(s -> s.floatingIslands)
            ).apply(instance, FloatingIslandsBiomeSource::new));

    // island band in QUART coords: block 300..700  ->  >>2  ->  75..175
    private static final int ISLAND_QY_MIN = QuartPos.fromBlock(300);
    private static final int ISLAND_QY_MAX = QuartPos.fromBlock(700);
    private static final double REGION_THRESHOLD = 0.35;
    private static final double CONTINENT_MIN = -0.11;

    private final Holder<MultiNoiseBiomeSourceParameterList> presetHolder;
    private final MultiNoiseBiomeSource delegate;
    private final Holder<Biome> floatingIslands;

    // injected by the chunk-generator mixin (same seeded instance the density uses)
    private volatile NormalNoise islandRegionNoise;

    public FloatingIslandsBiomeSource(Holder<MultiNoiseBiomeSourceParameterList> preset,
                                      Holder<Biome> floatingIslands) {
        this.presetHolder = preset;
        this.delegate = MultiNoiseBiomeSource.createFromPreset(preset);
        this.floatingIslands = floatingIslands;
    }

    public boolean hasNoise() { return this.islandRegionNoise != null; }
    public void setIslandRegionNoise(NormalNoise noise) { this.islandRegionNoise = noise; }

    @Override
    protected MapCodec<? extends BiomeSource> codec() { return CODEC; }

    @Override
    protected Stream<Holder<Biome>> collectPossibleBiomes() {
        return Stream.concat(delegate.possibleBiomes().stream(), Stream.of(floatingIslands));
    }

    @Override
    public Holder<Biome> getNoiseBiome(int qx, int qy, int qz, Climate.Sampler sampler) {
        NormalNoise noise = this.islandRegionNoise;
        if (noise != null && qy >= ISLAND_QY_MIN && qy <= ISLAND_QY_MAX) {
            int bx = QuartPos.toBlock(qx);
            int bz = QuartPos.toBlock(qz);
            double reg = noise.getValue(bx * 0.04, 0.0, bz * 0.04);   // same field as the density
            if (reg >= REGION_THRESHOLD) {
                Climate.TargetPoint t = sampler.sample(qx, qy, qz);
                double cont = Climate.unquantizeCoord(t.continentalness());  // same ocean gate
                if (cont >= CONTINENT_MIN) {
                    return this.floatingIslands;
                }
            }
        }
        return this.delegate.getNoiseBiome(qx, qy, qz, sampler);
    }
}