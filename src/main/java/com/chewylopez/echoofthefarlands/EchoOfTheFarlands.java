package com.chewylopez.echoofthefarlands;

import com.chewylopez.echoofthefarlands.world.Features.ModFeatures;
import com.chewylopez.echoofthefarlands.world.biome.ModBiomeSourceLists;
import com.chewylopez.echoofthefarlands.world.density_function.ModDensityFunctions;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.MultiNoiseBiomeSourceParameterList;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import com.chewylopez.echoofthefarlands.client.ConfigScreen;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(EchoOfTheFarlands.MODID)
public class EchoOfTheFarlands
{
    public static final String MODID = "echoofthefarlands";

    public EchoOfTheFarlands(IEventBus modEventBus, ModContainer modContainer)
    {
        modEventBus.addListener(this::commonSetup);
        NeoForge.EVENT_BUS.register(this);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        ModLoadingContext.get().registerExtensionPoint(
                IConfigScreenFactory.class,
                () -> (minecraft, parent) -> new ConfigScreen(parent)
        );

        ModFeatures.register(modEventBus);
        ModBiomeSourceLists.registerBiomeSourcePresets();

    }

    private void commonSetup(final FMLCommonSetupEvent event) {

    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }
}


