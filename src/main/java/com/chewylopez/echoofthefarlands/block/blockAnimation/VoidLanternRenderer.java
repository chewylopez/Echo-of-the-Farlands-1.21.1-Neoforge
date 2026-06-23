package com.chewylopez.echoofthefarlands.block.blockAnimation;

import com.chewylopez.echoofthefarlands.entity.block.VoidLanternBlockEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class VoidLanternRenderer extends GeoBlockRenderer<VoidLanternBlockEntity> {

    public VoidLanternRenderer(BlockEntityRendererProvider.Context ctx) {
        super(new VoidLanternModel());
    }

}