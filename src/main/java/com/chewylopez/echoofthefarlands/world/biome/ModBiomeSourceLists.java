package com.chewylopez.echoofthefarlands.world.biome;

import com.chewylopez.echoofthefarlands.EchoOfTheFarlands;
import com.chewylopez.echoofthefarlands.world.biome.biomes.AbyssalDepths;
import com.chewylopez.echoofthefarlands.world.biome.biomes.FloatingIslands;
import com.chewylopez.echoofthefarlands.world.biome.biomes.Volcano;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.*;

import java.util.function.Function;


public final class ModBiomeSourceLists {
    public static final ResourceKey<MultiNoiseBiomeSourceParameterList> OVERWORLD_BEYOND = ResourceKey.create(Registries.MULTI_NOISE_BIOME_SOURCE_PARAMETER_LIST, ResourceLocation.fromNamespaceAndPath(EchoOfTheFarlands.MODID, "overworld_beyond"));

    public static final MultiNoiseBiomeSourceParameterList.Preset OVERWORLD_BEYOND_PRESET =
            new MultiNoiseBiomeSourceParameterList.Preset(
            ResourceLocation.fromNamespaceAndPath(EchoOfTheFarlands.MODID, "overworld_beyond"),
            new MultiNoiseBiomeSourceParameterList.Preset.SourceProvider() {
                @Override
                public <T> Climate.ParameterList<T> apply(Function<ResourceKey<Biome>, T> biomeFactory) {
                    ImmutableList.Builder<Pair<Climate.ParameterPoint, T>> entries = ImmutableList.builder();

                    //volcano
                    //Climate.ParameterPoint volcanoParams = Volcano.buildBiomePresetData();
                    //T volcano = biomeFactory.apply(ModBiomes.VOLCANO);
                    //entries.add(Pair.of(volcanoParams, volcano));

                    //floating islands
                    //Climate.ParameterPoint floatingParams = FloatingIslands.buildBiomePresetData();
                    //T floating = biomeFactory.apply(ModBiomes.FLOATING_ISLANDS);
                    //entries.add(Pair.of(floatingParams, floating));

                    //Abyssal depths
                    Climate.ParameterPoint abyssalParams = AbyssalDepths.buildBiomePresetData();
                    T abyssal = biomeFactory.apply(ModBiomes.ABYSSAL_DEPTHS);
                    entries.add(Pair.of(abyssalParams, abyssal));

                    new OverworldBiomeBuilder().addBiomes(pair -> {
                        ResourceKey<Biome> biomeKey = pair.getSecond();

                        //removed biomes
                        //if (biomeKey == Biomes.WINDSWEPT_SAVANNA || biomeKey == Biomes.WINDSWEPT_HILLS || biomeKey == Biomes.WINDSWEPT_FOREST || biomeKey == Biomes.WINDSWEPT_GRAVELLY_HILLS) return;

                        T mapped = biomeFactory.apply(biomeKey);
                        entries.add(Pair.of(pair.getFirst(), mapped));
                    });

                    return new Climate.ParameterList<>(entries.build());
                }
            }
    );

    public static void registerBiomeSourcePresets() {
        ResourceLocation name = ResourceLocation.fromNamespaceAndPath(EchoOfTheFarlands.MODID, "overworld_beyond");
        MultiNoiseBiomeSourceParameterList.Preset.BY_NAME.put(name, ModBiomeSourceLists.OVERWORLD_BEYOND_PRESET);
    }

    public static void bootstrap(BootstrapContext<MultiNoiseBiomeSourceParameterList> context) {
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
        context.register(OVERWORLD_BEYOND, new MultiNoiseBiomeSourceParameterList(OVERWORLD_BEYOND_PRESET, biomes));
    }
}