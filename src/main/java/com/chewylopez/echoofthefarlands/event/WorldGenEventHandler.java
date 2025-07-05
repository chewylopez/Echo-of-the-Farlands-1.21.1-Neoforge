package com.chewylopez.echoofthefarlands.event;

import com.chewylopez.echoofthefarlands.EchoOfTheFarlands;
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

    private static boolean structurePlaced = false;

    @SubscribeEvent
    public static void onChunkLoad(LevelEvent.Load event) {

        if (!(event.getLevel() instanceof ServerLevel serverLevel)) return;
            if (!structurePlaced) {

                BlockPos startPos = new BlockPos(200, 150, 200);

                System.out.println("Structure placed at: " + startPos);

                //Objects.requireNonNull(chunk.getLevel()).setBlock(startPos, Blocks.OBSIDIAN.defaultBlockState(), 1);

                //placeStructure(event.getLevel(), startPos);
        }
    }

    private static void placeStructure(LevelAccessor level, BlockPos pos) {
        // Simple example: place a 3x3 platform of stone
        for (int x = 0; x < 3; x++) {
            for (int z = 0; z < 3; z++) {
                level.setBlock(pos.offset(x, 0, z), Blocks.OBSIDIAN.defaultBlockState(), 3);
            }
        }

        System.out.println("Structure placed at: " + pos);
    }

}
