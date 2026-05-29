package com.chewylopez.echoofthefarlands.world.biome.biomes;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class Volcano extends ModBiome {

    public static Climate.ParameterPoint buildBiomePresetData() {
        return new Climate.ParameterPoint(
                Climate.Parameter.span(0.9F, 1.0F),  // ANY temperature
                Climate.Parameter.span(0.9F, 1.0F),  // ANY humidity
                Climate.Parameter.span(0.3F, 1.0F), //continentalness
                Climate.Parameter.span(-1.0F, 1.0F),  // ANY erosion
                Climate.Parameter.point(0.0F),
                Climate.Parameter.span(0.99F, 1.0F),  // ANY weirdness
                0L); //offset
    }

    public static Biome getBiome(HolderGetter<PlacedFeature> features, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
        MobSpawnSettings.Builder mobs = new MobSpawnSettings.Builder();
        mobs.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.MAGMA_CUBE, 100, 1, 2));

        BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(features, carvers);
        BiomeDefaultFeatures.addDefaultOres(generation);
        BiomeDefaultFeatures.addDefaultCarversAndLakes(generation);

        return new Biome.BiomeBuilder().hasPrecipitation(false).temperature(1.8F).downfall(0.0F).specialEffects(new BiomeSpecialEffects.Builder()
                        .fogColor(0x4D2F1A) // dark ember orange
                        .waterColor(0x5C2F1A) // murky red
                        .waterFogColor(0x2A1408) // very dark
                        .skyColor(0x6E5040) // ashy gray-brown
                        .grassColorOverride(0x4A4036)
                        .foliageColorOverride(0x4A4036)
                        .ambientParticle(new AmbientParticleSettings(ParticleTypes.ASH, 0.005F))
                        .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                        .build()).mobSpawnSettings(mobs.build()).generationSettings(generation.build()).build();
    }

}
