package com.chewylopez.echoofthefarlands.event;

import com.chewylopez.echoofthefarlands.EchoOfTheFarlands;
import com.chewylopez.echoofthefarlands.world.biome.ModBiomes;
import net.minecraft.client.Camera;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.material.FogType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.neoforged.neoforge.event.level.LevelEvent;

@EventBusSubscriber(modid = EchoOfTheFarlands.MODID, value = Dist.CLIENT)
public class ClientEvents {

    // 0 = not in abyssal, 1 = fully abyssal. Smoothly interpolated.
    private static float abyssalBlend = 0.0f;
    private static long lastUpdateNanos = 0;

    // Time (seconds) for the blend to cover half the remaining distance to the target.
    // Lower = snappier. Higher = more gradual.
    private static final float HALF_LIFE_SECONDS = 0.5f;

    @SubscribeEvent
    public static void onRenderFog(ViewportEvent.RenderFog event) {
        Camera camera = event.getCamera();
        Entity entity = camera.getEntity();
        if (entity == null || entity.level() == null) return;

        // Update the blend toward the target (1 if camera is in abyssal, 0 otherwise).
        BlockPos pos = BlockPos.containing(camera.getPosition());
        boolean inAbyssal = entity.level().getBiome(pos).is(ModBiomes.ABYSSAL_DEPTHS);
        updateBlend(inAbyssal ? 1.0f : 0.0f);

        if (abyssalBlend < 0.001f) return; // negligible; let vanilla pass through

        boolean underwater = camera.getFluidInCamera() == FogType.WATER;
        float targetNear = underwater ? 0.5f : 4.0f;
        float targetFar  = underwater ? 8.0f : 48.0f;

        // Blend from the current (vanilla) values toward the abyssal targets.
        event.setNearPlaneDistance(Mth.lerp(abyssalBlend, event.getNearPlaneDistance(), targetNear));
        event.setFarPlaneDistance(Mth.lerp(abyssalBlend, event.getFarPlaneDistance(), targetFar));
        event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onFogColor(ViewportEvent.ComputeFogColor event) {
        if (abyssalBlend < 0.001f) return;

        // Lerp each color channel toward black (0).
        float inv = 1.0f - abyssalBlend;
        event.setRed(event.getRed() * inv);
        event.setGreen(event.getGreen() * inv);
        event.setBlue(event.getBlue() * inv);
    }

    private static void updateBlend(float target) {
        long now = System.nanoTime();
        float dt;
        if (lastUpdateNanos == 0) {
            dt = 1.0f / 60.0f;
        } else {
            dt = (now - lastUpdateNanos) / 1_000_000_000.0f;
            dt = Math.min(dt, 0.1f); // clamp to avoid huge jumps after pause/lag
        }
        lastUpdateNanos = now;

        // Exponential approach. Frame-rate independent.
        float decay = 1.0f - (float) Math.exp(-dt / HALF_LIFE_SECONDS);
        abyssalBlend += (target - abyssalBlend) * decay;
    }

    @SubscribeEvent
    public static void onLevelUnload(LevelEvent.Unload event) {
        abyssalBlend = 0.0f;
        lastUpdateNanos = 0;
    }

}