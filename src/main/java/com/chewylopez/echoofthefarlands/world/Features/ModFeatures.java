package com.chewylopez.echoofthefarlands.world.Features;

import com.chewylopez.echoofthefarlands.EchoOfTheFarlands;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class ModFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registries.FEATURE, EchoOfTheFarlands.MODID);

    public static final Supplier<Feature<VerticalShaftConfig>> VERTICAL_SHAFT = FEATURES.register("vertical_shaft", () -> new VerticalShaftFeature(VerticalShaftConfig.CODEC));

    public static void register(IEventBus eventBus) {
        FEATURES.register(eventBus);
    }

}