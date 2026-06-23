package com.chewylopez.echoofthefarlands.block.blockAnimation;

import com.chewylopez.echoofthefarlands.EchoOfTheFarlands;
import com.chewylopez.echoofthefarlands.entity.block.VoidLanternBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class VoidLanternModel extends GeoModel<VoidLanternBlockEntity> {
    public ResourceLocation getModelResource(VoidLanternBlockEntity b) {
        return ResourceLocation.fromNamespaceAndPath(EchoOfTheFarlands.MODID, "geo/void_lantern.geo.json");
    }
    public ResourceLocation getTextureResource(VoidLanternBlockEntity b) {
        return ResourceLocation.fromNamespaceAndPath(EchoOfTheFarlands.MODID, "textures/block/void_lantern.png");
    }
    public ResourceLocation getAnimationResource(VoidLanternBlockEntity b) {
        return ResourceLocation.fromNamespaceAndPath(EchoOfTheFarlands.MODID, "animations/void_lantern.animation.json");
    }
}
