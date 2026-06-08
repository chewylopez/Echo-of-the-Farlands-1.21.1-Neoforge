package com.chewylopez.echoofthefarlands.world.structure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pools.*;
import net.minecraft.world.level.levelgen.structure.pools.alias.PoolAliasLookup;
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

    private final Holder<StructureTemplatePool> startPool;
    private final int size;
    private final HeightProvider startHeight;

    public WallStructure(StructureSettings s, Holder<StructureTemplatePool> p, int size, HeightProvider h) {
        super(s);
        this.startPool = p;
        this.size = size;
        this.startHeight = h;
    }

    private static final int WALL_COORD = 10000;

    @Override
    public Optional<GenerationStub> findGenerationPoint(GenerationContext ctx) {

        ChunkPos chunk = ctx.chunkPos();

        int minX = chunk.getMinBlockX();
        int maxX = chunk.getMaxBlockX();
        int minZ = chunk.getMinBlockZ();
        int maxZ = chunk.getMaxBlockZ();

        boolean onXWall = (minX <= WALL_COORD  && WALL_COORD  <= maxX) || (minX <= -WALL_COORD && -WALL_COORD <= maxX);
        boolean onZWall = (minZ <= WALL_COORD  && WALL_COORD  <= maxZ) || (minZ <= -WALL_COORD && -WALL_COORD <= maxZ);

// Inside the 20000×20000 square
        boolean insideSquareZ = maxZ >= -WALL_COORD && minZ <= WALL_COORD;
        boolean insideSquareX = maxX >= -WALL_COORD && minX <= WALL_COORD;
        onXWall = onXWall && insideSquareZ;
        onZWall = onZWall && insideSquareX;

        if (!onXWall && !onZWall) return Optional.empty();
        if (onXWall && onZWall)   return Optional.empty();

        Rotation rot = onXWall ? Rotation.NONE : Rotation.CLOCKWISE_90;

        int x = chunk.getMinBlockX();
        int z = chunk.getMinBlockZ();

        int y = 63;
                /*
                Math.min(Math.min(
                        ctx.chunkGenerator().getFirstFreeHeight(x, z, Heightmap.Types.WORLD_SURFACE_WG, ctx.heightAccessor(), ctx.randomState()),
                        ctx.chunkGenerator().getFirstFreeHeight(x + 15, z, Heightmap.Types.WORLD_SURFACE_WG, ctx.heightAccessor(), ctx.randomState())
                ), Math.min(
                        ctx.chunkGenerator().getFirstFreeHeight(x,      z + 15, Heightmap.Types.WORLD_SURFACE_WG, ctx.heightAccessor(), ctx.randomState()),
                        ctx.chunkGenerator().getFirstFreeHeight(x + 15, z + 15, Heightmap.Types.WORLD_SURFACE_WG, ctx.heightAccessor(), ctx.randomState())));

                 */

        BlockPos pos = new BlockPos(x, y, z);

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
    @Override public StructureType<?> type() { return ModStructureTypes.WALL_STRUCTURE_TYPE.get(); }
}