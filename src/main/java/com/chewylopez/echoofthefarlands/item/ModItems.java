package com.chewylopez.echoofthefarlands.item;

import com.chewylopez.echoofthefarlands.EchoOfTheFarlands;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.createItems(EchoOfTheFarlands.MODID);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
