package com.chewylopez.echoofthefarlands.entity.block;

import com.chewylopez.echoofthefarlands.EchoOfTheFarlands;
import com.chewylopez.echoofthefarlands.block.ModBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, EchoOfTheFarlands.MODID);

    public static final Supplier<BlockEntityType<VoidLanternBlockEntity>> VOID_LANTERN_ENTITY = BLOCK_ENTITIES.register("void_lantern", () -> BlockEntityType.Builder.of(VoidLanternBlockEntity::new, ModBlocks.VOID_LAMP.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }

}
