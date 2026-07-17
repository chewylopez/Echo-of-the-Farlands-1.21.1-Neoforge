package com.chewylopez.echoofthefarlands.world.structure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

public class InnerWall extends WallStructure {

    public static final MapCodec<InnerWall> CODEC = RecordCodecBuilder.mapCodec(inst ->
            inst.group(
                    settingsCodec(inst),
                    StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter(s -> s.startPool),
                    Codec.intRange(0, 20).fieldOf("size").forGetter(s -> s.size),
                    HeightProvider.CODEC.fieldOf("start_height").forGetter(s -> s.startHeight)
            ).apply(inst, InnerWall::new));

    public InnerWall(StructureSettings s, Holder<StructureTemplatePool> p, int size, HeightProvider h) {
        super(s, p, size, h);
    }

    @Override
    public StructureType<?> type() {
        return ModStructureTypes.WALL_INNER.get();
    }

    @Override
    protected int getDistance(){
        if(getFarlandsDistance() < 20000) {
            return (int) (getFarlandsDistance()/2);
        }
        else if(getFarlandsDistance() >= 20000 && getFarlandsDistance() < 1000000){
            return 10000;
        }
        else if(getFarlandsDistance() >= 1000000) {
            return (int) (getFarlandsDistance()/100);
        }
        return 10000;
    }
}
