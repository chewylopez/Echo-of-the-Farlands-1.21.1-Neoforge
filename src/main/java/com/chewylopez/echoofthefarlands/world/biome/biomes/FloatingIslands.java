package com.chewylopez.echoofthefarlands.world.biome.biomes;

import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderGetter;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.awt.*;

public class FloatingIslands extends ModBiome{

    public static Climate.ParameterPoint buildBiomePresetData() {
        return new Climate.ParameterPoint(
                Climate.Parameter.span(-0.3F, 0.7F), // temperature: temperate-warm
                Climate.Parameter.span(-0.6F, 0.6F), // humidity: moderate
                Climate.Parameter.span(0.1F, 0.7F),  // continentalness: inland
                Climate.Parameter.span(-0.3F, -0.15F),// erosion: MATCHES THE GATE
                Climate.Parameter.point(0.0F), // depth
                Climate.Parameter.span(0.3F, 0.7F), // weirdness: central band
                0L
        );
    }

    public static Biome getBiome(HolderGetter<PlacedFeature> features, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
        MobSpawnSettings.Builder mobs = new MobSpawnSettings.Builder();
        mobs.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.PARROT, 100, 1, 2));

        BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(features, carvers);
        BiomeDefaultFeatures.addDefaultOres(generation);
        BiomeDefaultFeatures.addDefaultCarversAndLakes(generation);

        return new Biome.BiomeBuilder().hasPrecipitation(true).temperature(0.7F).downfall(0.4F).specialEffects(new BiomeSpecialEffects.Builder()
                        .fogColor(0xC0D8FF)        // bright sky-blue fog
                        .waterColor(0x3F76E4)
                        .waterFogColor(0x050533)
                        .skyColor(0x88BBFF)         // vivid sky
                        .grassColorOverride(ChatFormatting.DARK_AQUA.getColor())
                        .foliageColorOverride(0x6B9C4F)
                        .build()).mobSpawnSettings(mobs.build()).generationSettings(generation.build()).build();
    }

}
