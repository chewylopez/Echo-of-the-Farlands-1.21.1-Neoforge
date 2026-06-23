package com.chewylopez.echoofthefarlands.world.dimension;

import com.chewylopez.echoofthefarlands.EchoOfTheFarlands;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

@EventBusSubscriber(modid = EchoOfTheFarlands.MODID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class VoidParticles {
    private static final ResourceKey<Level> VOID_DIM =
            ResourceKey.create(Registries.DIMENSION,
                    ResourceLocation.fromNamespaceAndPath(EchoOfTheFarlands.MODID, "void"));

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) return;
        if (!mc.level.dimension().equals(VOID_DIM)) return;

        RandomSource rng = mc.level.random;
        Vec3 p = mc.player.position();
        for (int i = 0; i < 4; i++) {
            double x = p.x + (rng.nextDouble() - 0.5) * 32;
            double y = p.y + (rng.nextDouble() - 0.5) * 32;
            double z = p.z + (rng.nextDouble() - 0.5) * 32;
            mc.level.addParticle(ParticleTypes.WHITE_ASH, x, y, z, 0, -0.02, 0);
        }
    }
}