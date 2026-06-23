package com.chewylopez.echoofthefarlands.block;

import com.chewylopez.echoofthefarlands.block.blockAnimation.VoidLanternModel;
import com.chewylopez.echoofthefarlands.entity.block.VoidLanternBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class VoidLanternBlock extends BaseEntityBlock {

    public static final MapCodec<VoidLanternBlock> CODEC = simpleCodec(VoidLanternBlock::new);

    public VoidLanternBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new VoidLanternBlockEntity(blockPos, blockState);
    }
}
