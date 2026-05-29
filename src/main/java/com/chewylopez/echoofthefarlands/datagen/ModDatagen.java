package com.chewylopez.echoofthefarlands.datagen;

import com.chewylopez.echoofthefarlands.EchoOfTheFarlands;
import com.chewylopez.echoofthefarlands.world.biome.ModBiomeSourceLists;
import com.chewylopez.echoofthefarlands.world.biome.ModBiomes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = EchoOfTheFarlands.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModDatagen {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.BIOME, ModBiomes::bootstrap)
            .add(Registries.MULTI_NOISE_BIOME_SOURCE_PARAMETER_LIST, ModBiomeSourceLists::bootstrap);

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(event.includeServer(), new DatapackBuiltinEntriesProvider(generator.getPackOutput(), event.getLookupProvider(), BUILDER, Set.of(EchoOfTheFarlands.MODID)));
    }

    @SubscribeEvent
    public static void register(RegisterEvent event) {
        //event.register(Registries.DENSITY_FUNCTION_TYPE, h -> h.register(ResourceLocation.fromNamespaceAndPath(EchoOfTheFarlands.MODID, "floating_island"), FloatingIslandDensity.CODEC));
        //event.register(Registries.BIOME_SOURCE, h -> h.register(ResourceLocation.fromNamespaceAndPath(EchoOfTheFarlands.MODID, "floating_islands_source"), FloatingIslandsBiomeSource.CODEC));
    }

}