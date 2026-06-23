package com.chewylopez.echoofthefarlands.entity.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class VoidLanternBlockEntity extends BlockEntity implements GeoBlockEntity {

    private static final RawAnimation SPIN =
            RawAnimation.begin().thenLoop("keyframe-rotate");
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public VoidLanternBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.VOID_LANTERN_ENTITY.get(), pos, blockState);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, state -> state.setAndContinue(SPIN)));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
