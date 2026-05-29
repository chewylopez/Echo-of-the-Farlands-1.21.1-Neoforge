package com.chewylopez.echoofthefarlands.world.biome;

import com.chewylopez.echoofthefarlands.EchoOfTheFarlands;
import com.chewylopez.echoofthefarlands.world.biome.biomes.AbyssalDepths;
import com.chewylopez.echoofthefarlands.world.biome.biomes.FloatingIslands;
import com.chewylopez.echoofthefarlands.world.biome.biomes.Volcano;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

public final class ModBiomes {

    //biomes
    public static final ResourceKey<Biome> ABYSSAL_DEPTHS = ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(EchoOfTheFarlands.MODID, "abyssal_depths"));
    //public static final ResourceKey<Biome> VOLCANO = ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(EchoOfTheFarlands.MODID, "volcano"));
    //public static final ResourceKey<Biome> FLOATING_ISLANDS = ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(EchoOfTheFarlands.MODID, "floating_islands"));

    public static void bootstrap(BootstrapContext<Biome> context) {
        HolderGetter<PlacedFeature> features = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<ConfiguredWorldCarver<?>> carvers = context.lookup(Registries.CONFIGURED_CARVER);

        context.register(ABYSSAL_DEPTHS, AbyssalDepths.getBiome(features, carvers));
        //context.register(VOLCANO, Volcano.getBiome(features, carvers));
        //context.register(FLOATING_ISLANDS, FloatingIslands.getBiome(features, carvers));
    }
}

