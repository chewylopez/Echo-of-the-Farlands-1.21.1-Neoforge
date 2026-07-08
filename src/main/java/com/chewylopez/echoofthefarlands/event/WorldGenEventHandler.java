package com.chewylopez.echoofthefarlands.event;

import com.chewylopez.echoofthefarlands.Config;
import com.chewylopez.echoofthefarlands.EchoOfTheFarlands;
import com.chewylopez.echoofthefarlands.datagen.WorldFarlandsSettings;
import com.chewylopez.echoofthefarlands.world.biome.FloatingIslandsBiomeSource;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

@EventBusSubscriber(modid = EchoOfTheFarlands.MODID)
public class WorldGenEventHandler {

    @SubscribeEvent
    public static void onworldOpen(LevelEvent.Load event) {

        if ((event.getLevel() instanceof ServerLevel level)) {

            WorldFarlandsSettings settings = WorldFarlandsSettings.getSettings(level);

            if(settings.isNewWorld()){
                settings.setFarlandsPosition(Config.FARLANDS_LOCATION_WORLD.get());
                settings.setFarlandsGenType(Config.FARLANDS_GEN_TYPE.get());
                settings.setFarlandsWallTexturePatch(Config.FARLANDS_WALL_TEXTURE_PATCH.get());
                settings.setFarlandsLiquidFix(Config.FARLANDS_LIQUID_FIX.get());
                settings.setFarlandsBedrockFix(Config.FARLANDS_BEDROCK_FIX.get());

                System.out.println("Farlands position: " + Config.FARLANDS_LOCATION_WORLD);
                System.out.println("Farlands gen type: " + Config.FARLANDS_GEN_TYPE);
                System.out.println("Farlands wall texture: " + Config.FARLANDS_WALL_TEXTURE_PATCH);
                System.out.println("Farlands liquid fix: " + Config.FARLANDS_LIQUID_FIX);
                System.out.println("Farlands bedrock fix: " + Config.FARLANDS_BEDROCK_FIX);
            }

            //every time on startup
            Config.FARLANDS_LOCATION_WORLD.set(settings.getFarlandsPosition());
            Config.FARLANDS_GEN_TYPE.set(settings.getFarlandsGenType());
            Config.FARLANDS_WALL_TEXTURE_PATCH.set(settings.getFarlandsWallTexturePatch());
            Config.FARLANDS_LIQUID_FIX.set(settings.getFarlandsLiquidFix());
            Config.FARLANDS_BEDROCK_FIX.set(settings.getFarlandsBedrockFix());
            settings.setDirty();
        }
    }
}
