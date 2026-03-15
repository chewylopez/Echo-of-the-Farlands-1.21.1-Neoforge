package com.chewylopez.echoofthefarlands;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
@EventBusSubscriber(modid = EchoOfTheFarlands.MODID, bus = EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.IntValue FARLANDS_LOCATION = BUILDER
            .comment("farlands location")
            .defineInRange("farlandsLocation", 1000000, 1, Integer.MAX_VALUE);

    public static final ModConfigSpec SPEC = BUILDER.build();

    public static int farlands_location_global = 1000000;

    private static boolean validateItemName(final Object obj)
    {
        return obj instanceof String itemName && BuiltInRegistries.ITEM.containsKey(ResourceLocation.parse(itemName));
    }

    @SubscribeEvent
    private static void onLoad(ModConfigEvent.Loading event) {
            System.out.println("it works: " + FARLANDS_LOCATION.get());
            farlands_location_global = FARLANDS_LOCATION.get();
    }
}
