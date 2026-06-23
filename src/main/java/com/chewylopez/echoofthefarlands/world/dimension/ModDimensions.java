package com.chewylopez.echoofthefarlands.world.dimension;

import com.chewylopez.echoofthefarlands.EchoOfTheFarlands;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.dimension.DimensionType;

public class ModDimensions {

    public static final ResourceKey<DimensionType> CUSTOM_OVERWORLD = ResourceKey.create(Registries.DIMENSION_TYPE, ResourceLocation.fromNamespaceAndPath(EchoOfTheFarlands.MODID, "overworld_beyond"));

}
