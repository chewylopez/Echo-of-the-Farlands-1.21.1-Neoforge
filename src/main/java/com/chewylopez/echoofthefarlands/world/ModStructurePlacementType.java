package com.chewylopez.echoofthefarlands.world;

import com.chewylopez.echoofthefarlands.EchoOfTheFarlands;
import com.chewylopez.echoofthefarlands.world.placement.WallStructurePlacement;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModStructurePlacementType<SP extends StructurePlacement> {

    public static final DeferredRegister<StructurePlacementType<?>> STRUCTURE_POSITIONS = DeferredRegister.create(BuiltInRegistries.STRUCTURE_PLACEMENT, EchoOfTheFarlands.MODID);

    //public static final Supplier<StructurePlacementType<WallStructurePlacement>> WALL_PLACEMENT_1 = STRUCTURE_POSITIONS.register("first_wall", () -> ModStructurePlacementType.STRUCTURE_POSITIONS);

    private static void register(IEventBus eventBus) {
        STRUCTURE_POSITIONS.register(eventBus);
    }
}
