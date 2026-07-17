package com.chewylopez.echoofthefarlands.world.structure;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModStructureTypes {

    public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPES = DeferredRegister.create(Registries.STRUCTURE_TYPE, "echoofthefarlands");
    public static final Supplier<StructureType<InnerWall>> WALL_INNER = STRUCTURE_TYPES.register("wall_inner", () -> () -> InnerWall.CODEC);
    public static final Supplier<StructureType<OuterWall>> WALL_OUTER = STRUCTURE_TYPES.register("wall_outer", () -> () -> OuterWall.CODEC);

    public static void register(IEventBus eventBus) {
        STRUCTURE_TYPES.register(eventBus);
    }
}
