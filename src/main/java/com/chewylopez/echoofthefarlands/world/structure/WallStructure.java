package com.chewylopez.echoofthefarlands.world.structure;

import com.chewylopez.echoofthefarlands.Config;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pools.*;
import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;

import java.util.Optional;

public class WallStructure extends Structure {
    public static final MapCodec<WallStructure> CODEC = RecordCodecBuilder.mapCodec(inst ->
            inst.group(
                    settingsCodec(inst),
                    StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter(s -> s.startPool),
                    Codec.intRange(0, 20).fieldOf("size").forGetter(s -> s.size),
                    HeightProvider.CODEC.fieldOf("start_height").forGetter(s -> s.startHeight)
            ).apply(inst, WallStructure::new));

    final Holder<StructureTemplatePool> startPool;
    final int size;
    final HeightProvider startHeight;

    public WallStructure(StructureSettings s, Holder<StructureTemplatePool> p, int size, HeightProvider h) {
        super(s);
        this.startPool = p;
        this.size = size;
        this.startHeight = h;
    }

    @Override
    public Optional<GenerationStub> findGenerationPoint(GenerationContext ctx) {

        int distance = getDistance();

        ChunkPos chunk = ctx.chunkPos();

        int minX = chunk.getMinBlockX();
        int maxX = chunk.getMaxBlockX();
        int minZ = chunk.getMinBlockZ();
        int maxZ = chunk.getMaxBlockZ();
        int y = 63;

        boolean onXWall = (minX <= distance && distance <= maxX) || (minX <= -distance && -distance <= maxX);
        boolean onZWall = (minZ <= distance && distance <= maxZ) || (minZ <= -distance && -distance <= maxZ);

// Inside the 20000×20000 square
        boolean insideSquareZ = maxZ >= -distance && minZ <= distance;
        boolean insideSquareX = maxX >= -distance && minX <= distance;
        onXWall = onXWall && insideSquareZ;
        onZWall = onZWall && insideSquareX;

        if (!onXWall && !onZWall) return Optional.empty();
        if (onXWall && onZWall)   return Optional.empty();

        Rotation rot = onXWall ? Rotation.NONE : Rotation.CLOCKWISE_90;

        BlockPos pos = new BlockPos(minX, y, minZ);

        return Optional.of(new GenerationStub(pos, builder -> {
            StructureTemplatePool pool = startPool.value();
            StructurePoolElement element = pool.getRandomTemplate(ctx.random());
            if (element == EmptyPoolElement.INSTANCE) return;

            PoolElementStructurePiece piece = new PoolElementStructurePiece(
                    ctx.structureTemplateManager(),
                    element,
                    pos,
                    element.getGroundLevelDelta(),
                    rot,
                    element.getBoundingBox(ctx.structureTemplateManager(), pos, rot),
                    LiquidSettings.APPLY_WATERLOGGING
            );
            builder.addPiece(piece);
        }));
    }

    @Override
    public StructureType<?> type() {
        return null;
    }

    protected int getDistance(){
        return 10000;
    }

    public double getFarlandsDistance(){
        return Config.FARLANDS_LOCATION_WORLD.get();
    }

}