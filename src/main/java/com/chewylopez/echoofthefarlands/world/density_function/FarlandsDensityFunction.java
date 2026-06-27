package com.chewylopez.echoofthefarlands.world.density_function;

import com.chewylopez.echoofthefarlands.Config;
import com.chewylopez.echoofthefarlands.world.dimension.DimensionProfile;
import com.chewylopez.echoofthefarlands.world.farlands.FarlandsContext;
import com.mojang.serialization.MapCodec;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.DensityFunction;

public record FarlandsDensityFunction(DensityFunction inner) implements DensityFunction {

    public static final MapCodec<FarlandsDensityFunction> CODEC_MAP = MapCodec.unit(() -> new FarlandsDensityFunction(null));
    public static final KeyDispatchDataCodec<FarlandsDensityFunction> CODEC = new KeyDispatchDataCodec<>(CODEC_MAP);

    private static boolean getAquiferFix() {
        return Config.FARLANDS_LIQUID_FIX;
    }

    private static boolean getBedrockFix() {
        return Config.FARLANDS_BEDROCK_FIX;
    }

    @Override
    public double compute(FunctionContext ctx) {

        DimensionProfile p = FarlandsContext.PROFILE.get();
        double base = inner.compute(ctx);

        if (!FarlandsContext.APPLY_FARLANDS.get()) return base;

        int wx = ctx.blockX();
        int wy = ctx.blockY();
        int wz = ctx.blockZ();

        if ( (Double.isNaN(base) || Double.isInfinite(base)) && getAquiferFix() ) {
            base = 0.0;
        }

        if (wy <= p.bedrockFloorTop() && getBedrockFix()){
            base = 1.0;
        }

        double radius = Math.max(Math.abs(wx), Math.abs(wz));
        double threshold = Config.FARLANDS_LOCATION_WORLD;
        double progress = radius - threshold;
        double farlands = base;

        if (progress <= 0) {
            return base;
        }

        if (Config.FARLANDS_GEN_TYPE == 7) {
            farlands = multiTrigDensity(base, wx, wy, wz, progress);
        }

        double yw = yWindow(wy, p);
        return Mth.lerp(yw, base, farlands);
    }

    private static double yWindow(int y, DimensionProfile p) {
        int bottomFull = p.bedrockFloor() + 64;
        int bottomFade = p.bedrockFloor() + 8;
        int topFull    = p.bedrockCeiling() - 96;
        int topFade    = p.bedrockCeiling() - 64;
        double w = 1.0;
        if (y > topFull) {
            w = Math.min(w, Mth.clamp((topFade - y) / (double)(topFade - topFull), 0.0, 1.0));
        }
        if (y < bottomFull) {
            w = Math.min(w, Mth.clamp((y - bottomFade) / (double)(bottomFull - bottomFade), 0.0, 1.0));
        }
        return w;
    }

    @Override
    public void fillArray(double[] arr, ContextProvider provider) {
        provider.fillAllDirectly(arr, this);
    }

    @Override
    public DensityFunction mapAll(Visitor visitor) {
        return visitor.apply(new FarlandsDensityFunction(this.inner.mapAll(visitor)));
    }

    @Override
    public double minValue() {
        return -Double.MAX_VALUE;
    }

    @Override
    public double maxValue() {
        return Double.MAX_VALUE;
    }

    @Override
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC;
    }

    private static double multiTrigDensity(double base, int x, int y, int z, double progress) {
        double amplitude = progress * 0.003;

        double a = Math.sin(x * 0.013 + z * 0.017);
        double b = Math.cos(x * 0.007 - z * 0.011);
        double c = Math.sin(y * 0.05 + x * 0.003);

        double chaos = (a + b + c) * (1.0 / 3.0);

        return base + chaos * amplitude;
    }
}