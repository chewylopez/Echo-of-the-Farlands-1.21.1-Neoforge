package com.chewylopez.echoofthefarlands.world.biome.biomes;

import net.minecraft.core.HolderGetter;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class AbyssalDepths extends ModBiome {

    public static Climate.ParameterPoint buildBiomePresetData(){
        return new Climate.ParameterPoint(
                Climate.Parameter.span(-1.0F, 1.0F), // temperature
                Climate.Parameter.span(-1.0F, 1.0F), // humidity
                Climate.Parameter.span(-1.05F, -0.65F), // continentalness
                Climate.Parameter.span(-1.0F, 1.0F), // erosion
                Climate.Parameter.point(0.0F), // depth
                Climate.Parameter.span(-1.0F, 1.0F), // weirdness
                0L); // offset
    }

    public static Biome getBiome(HolderGetter<PlacedFeature> features, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
        MobSpawnSettings.Builder mobs = new MobSpawnSettings.Builder();
        //mobs.addSpawn(MobCategory.UNDERGROUND_WATER_CREATURE, new MobSpawnSettings.SpawnerData(EntityType.GLOW_SQUID, 80, 4, 6));

        BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(features, carvers);
        BiomeDefaultFeatures.addDefaultOres(generation);
        BiomeDefaultFeatures.addDefaultSoftDisks(generation);

        return new Biome.BiomeBuilder().hasPrecipitation(false).temperature(0.5F).downfall(0.5F).specialEffects(new BiomeSpecialEffects.Builder()
                .fogColor(0x000000)
                .waterColor(0x0D131F)
                .waterFogColor(0x000000)
                .skyColor(0x000000)
                .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                .build()).mobSpawnSettings(mobs.build()).generationSettings(generation.build()).build();
    }


}
