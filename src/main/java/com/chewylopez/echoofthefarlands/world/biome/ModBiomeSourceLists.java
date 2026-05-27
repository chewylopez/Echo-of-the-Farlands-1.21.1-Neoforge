package com.chewylopez.echoofthefarlands.world.biome;

import com.chewylopez.echoofthefarlands.EchoOfTheFarlands;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.MultiNoiseBiomeSourceParameterList;
import net.minecraft.world.level.biome.OverworldBiomeBuilder;

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

                            // Vanilla overworld biomes
                            new OverworldBiomeBuilder().addBiomes(pair -> {
                                T mapped = biomeFactory.apply(pair.getSecond());
                                entries.add(Pair.of(pair.getFirst(), mapped));
                            });

                            // Abyssal depths
                            Climate.ParameterPoint abyssalParams = new Climate.ParameterPoint(
                                    Climate.Parameter.span(-1.0F, 1.0F), // temperature
                                    Climate.Parameter.span(-1.0F, 1.0F), // humidity
                                    Climate.Parameter.span(-1.05F, -0.75F), // continentalnesss
                                    Climate.Parameter.span(-1.0F, 1.0F), // erosion
                                    Climate.Parameter.point(0.0F), // depth
                                    Climate.Parameter.span(-1.0F, 1.0F), // weirdness
                                    0L); // offset
                            T abyssal = biomeFactory.apply(ModBiomes.ABYSSAL_DEPTHS);
                            entries.add(Pair.of(abyssalParams, abyssal));
                            return new Climate.ParameterList<>(entries.build());
                        }
                    }
            );

    public static void registerBiomeSourcePresets() {
        ResourceLocation name = ResourceLocation.fromNamespaceAndPath(EchoOfTheFarlands.MODID, "overworld_beyond");
        MultiNoiseBiomeSourceParameterList.Preset.BY_NAME.put(name, ModBiomeSourceLists.OVERWORLD_BEYOND_PRESET);
        System.out.println("[EchoOfTheFarlands] Registered preset in BY_NAME: " + name
                + " (map now contains " + MultiNoiseBiomeSourceParameterList.Preset.BY_NAME.size() + " entries)");
    }

    public static void bootstrap(BootstrapContext<MultiNoiseBiomeSourceParameterList> context) {
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
        context.register(OVERWORLD_BEYOND, new MultiNoiseBiomeSourceParameterList(OVERWORLD_BEYOND_PRESET, biomes));
    }
}