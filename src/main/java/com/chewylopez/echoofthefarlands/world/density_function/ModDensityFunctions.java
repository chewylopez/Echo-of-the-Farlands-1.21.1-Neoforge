package com.chewylopez.echoofthefarlands.world.density_function;

import com.chewylopez.echoofthefarlands.EchoOfTheFarlands;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.RegisterEvent;

@EventBusSubscriber(modid = EchoOfTheFarlands.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModDensityFunctions {

    @SubscribeEvent
    public static void register(RegisterEvent event) {
        //event.register(Registries.DENSITY_FUNCTION_TYPE, helper -> {helper.register(ResourceLocation.fromNamespaceAndPath(EchoOfTheFarlands.MODID, "floating_island"), FloatingIslandDensity.CODEC);});
    }
}