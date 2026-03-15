package com.chewylopez.echoofthefarlands.event;

import com.chewylopez.echoofthefarlands.EchoOfTheFarlands;
import com.chewylopez.echoofthefarlands.datagen.WorldFarlandsSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.neoforge.event.level.LevelEvent;

import java.util.Objects;

@EventBusSubscriber(modid = EchoOfTheFarlands.MODID)
public class WorldGenEventHandler {

    @SubscribeEvent
    public static void onworldcreate(LevelEvent.Load event) {
        if ((event.getLevel() instanceof ServerLevel level)) {
            WorldFarlandsSettings settings = WorldFarlandsSettings.write(level);
            settings.setDirty();
        }
    }
}
