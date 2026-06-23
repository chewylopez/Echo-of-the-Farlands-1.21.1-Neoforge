package com.chewylopez.echoofthefarlands.block;

import com.chewylopez.echoofthefarlands.EchoOfTheFarlands;
import com.chewylopez.echoofthefarlands.entity.block.VoidLanternBlockEntity;
import com.chewylopez.echoofthefarlands.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlocks {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(EchoOfTheFarlands.MODID);

    public static final DeferredBlock<Block> VOID_CHAIN = registerBlock("void_chain", () -> new VoidChainBlock(BlockBehaviour.Properties.of().strength(15, 100F)));
    public static final DeferredBlock<Block> VOID_LAMP = registerBlock("void_lantern", () -> new VoidLanternBlock(BlockBehaviour.Properties.of().strength(15, 100F).lightLevel((value -> {return 12;})).noOcclusion()));

    static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
