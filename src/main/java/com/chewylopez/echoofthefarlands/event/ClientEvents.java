package com.chewylopez.echoofthefarlands.event;

import com.chewylopez.echoofthefarlands.EchoOfTheFarlands;
import com.chewylopez.echoofthefarlands.block.blockAnimation.VoidLanternRenderer;
import com.chewylopez.echoofthefarlands.entity.block.ModBlockEntities;
import com.chewylopez.echoofthefarlands.world.biome.ModBiomes;
import com.chewylopez.echoofthefarlands.world.dimension.VoidEffects;
import net.minecraft.client.Camera;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.material.FogType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterDimensionSpecialEffectsEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.neoforged.neoforge.event.level.LevelEvent;

@EventBusSubscriber(modid = EchoOfTheFarlands.MODID, value = Dist.CLIENT)
public class ClientEvents {

    private static float fogBlend = 0.0f;
    private static long lastUpdateNanos = 0;
    private static final float HALF_LIFE_SECONDS = 2.0f;

    // Depth where the effect fully kicks in (in blocks)
    private static final double FULL_FOG_Y = -100.0;
    private static final double SURFACE_Y = 63.0;

    @SubscribeEvent
    public static void onRenderFog(ViewportEvent.RenderFog event) {
        Camera camera = event.getCamera();
        Entity entity = camera.getEntity();
        if (entity == null || entity.level() == null) return;

        if (camera.getFluidInCamera() != FogType.WATER) {
            updateBlend(0.0f);
            return;
        }

        BlockPos pos = BlockPos.containing(camera.getPosition());
        Holder<Biome> biome = entity.level().getBiome(pos);

        float biomeStrength = getBiomeFogStrength(biome);
        float depthFactor = getDepthFactor(camera.getPosition().y);
        float target = biomeStrength * depthFactor;

        updateBlend(target);

        if (fogBlend < 0.001f) return;
        float targetNear = 0.5f;
        float targetFar = 8.0f;

        event.setNearPlaneDistance(Mth.lerp(fogBlend, event.getNearPlaneDistance(), targetNear));
        event.setFarPlaneDistance(Mth.lerp(fogBlend, event.getFarPlaneDistance(), targetFar));
        event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onFogColor(ViewportEvent.ComputeFogColor event) {
        if (fogBlend < 0.001f) return;

        float inv = 1.0f - fogBlend;
        event.setRed(event.getRed() * inv);
        event.setGreen(event.getGreen() * inv);
        event.setBlue(event.getBlue() * inv);
    }

    private static float getBiomeFogStrength(Holder<Biome> biome) {
        if (biome.is(ModBiomes.ABYSSAL_DEPTHS)) return 1.0f;

        if (biome.is(Biomes.DEEP_OCEAN)
                || biome.is(Biomes.DEEP_COLD_OCEAN)
                || biome.is(Biomes.DEEP_LUKEWARM_OCEAN)
                || biome.is(Biomes.DEEP_FROZEN_OCEAN)) {
            return 0.5f;
        }

        return 0.0f;
    }

    private static float getDepthFactor(double y) {
        // 0 at sea level, 1 at FULL_FOG_Y or below
        double range = SURFACE_Y - FULL_FOG_Y;  // 163 blocks
        return (float) Mth.clamp((SURFACE_Y - y) / range, 0.0, 1.0);
    }

    private static void updateBlend(float target) {
        long now = System.nanoTime();
        float dt;
        if (lastUpdateNanos == 0) {
            dt = 1.0f / 60.0f;
        } else {
            dt = (now - lastUpdateNanos) / 1_000_000_000.0f;
            dt = Math.min(dt, 0.1f);
        }
        lastUpdateNanos = now;

        float decay = 1.0f - (float) Math.exp(-dt / HALF_LIFE_SECONDS);
        fogBlend += (target - fogBlend) * decay;
    }

    @SubscribeEvent
    public static void onLevelUnload(LevelEvent.Unload event) {
        fogBlend = 0.0f;
        lastUpdateNanos = 0;
    }

    @SubscribeEvent
    public static void onRegisterEffects(RegisterDimensionSpecialEffectsEvent event) {
        event.register(ResourceLocation.fromNamespaceAndPath("echoofthefarlands", "void"),
                new VoidEffects());
    }

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.VOID_LANTERN_ENTITY.get(), VoidLanternRenderer::new);
    }

}