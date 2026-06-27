package com.chewylopez.echoofthefarlands.world.density_function;

import com.chewylopez.echoofthefarlands.EchoOfTheFarlands;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModDensityFunctions {

    public static final DeferredRegister<MapCodec<? extends DensityFunction>> TYPES =
            DeferredRegister.create(Registries.DENSITY_FUNCTION_TYPE, EchoOfTheFarlands.MODID);

    public static final DeferredHolder<MapCodec<? extends DensityFunction>, MapCodec<FarlandsDensityFunction>>
            DENSITY_TEST_FARLANDS = TYPES.register("farlands", () -> FarlandsDensityFunction.CODEC_MAP);

    public static void register(IEventBus bus) {
        TYPES.register(bus);
    }

}