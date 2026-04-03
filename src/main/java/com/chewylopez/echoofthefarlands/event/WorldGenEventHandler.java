package com.chewylopez.echoofthefarlands.event;

import com.chewylopez.echoofthefarlands.Config;
import com.chewylopez.echoofthefarlands.EchoOfTheFarlands;
import com.chewylopez.echoofthefarlands.datagen.WorldFarlandsSettings;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.event.level.LevelEvent;

@EventBusSubscriber(modid = EchoOfTheFarlands.MODID)
public class WorldGenEventHandler {

    @SubscribeEvent
    public static void onworldOpen(LevelEvent.Load event) {

        if ((event.getLevel() instanceof ServerLevel level)) {

            WorldFarlandsSettings settings = WorldFarlandsSettings.getSettings(level);

            if(settings.isNewWorld()){
                settings.setFarlandsPosition(Config.FARLANDS_LOCATION_WORLD);
                settings.setFarlandsGenType(Config.FARLANDS_GEN_TYPE);

                System.out.println("Farlands Position: " + Config.FARLANDS_LOCATION_WORLD);
                System.out.println("Farlands GenType: " + Config.FARLANDS_GEN_TYPE);
            }

            //every time on startup
            Config.FARLANDS_LOCATION_WORLD = settings.getFarlandsPosition();
            Config.FARLANDS_GEN_TYPE = settings.getFarlandsGenType();
            settings.setDirty();
        }
    }
}
