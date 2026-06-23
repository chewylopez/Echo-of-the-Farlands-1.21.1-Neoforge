package com.chewylopez.echoofthefarlands.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(ClientLevel.ClientLevelData.class)
public abstract class ClientLevelDataMixin {

    @Shadow
    @Final
    private boolean isFlat;

    @Inject(method = {"getClearColorScale"}, at = {@At("TAIL")}, cancellable = true)
    private void changeFogColorScaleForBeyond(CallbackInfoReturnable<Float> cir) {

        ClientLevel level = Minecraft.getInstance().level;
        float returnable = this.isFlat ? 1.0F : 0.03125F;

        if(level != null){
            if(level.getMinBuildHeight() == -576){
                returnable = 1.0F/192F;
            }
        }

        cir.setReturnValue(returnable);
    }

}