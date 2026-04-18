package fr.iglee42.auxiliautilities.utils;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import com.mojang.blaze3d.platform.NativeImage;
import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import net.minecraft.client.Minecraft;

import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.TextureAtlasStitchedEvent;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.concurrent.ExecutionException;

@EventBusSubscriber(modid = AuxiliaUtilities.MODID,value = Dist.CLIENT)
public class FluidColorHelper {

    public static final LoadingCache<Fluid, Integer> FLUID_COLOR =
            CacheBuilder.newBuilder().build(new CacheLoader<>() {
                @Override
                public Integer load(Fluid fluid) {
                    return computeFluidColor(fluid);
                }
            });

    @SubscribeEvent
    public static void cleanCache(TextureAtlasStitchedEvent event){
        if (event.getAtlas().location().equals(TextureAtlas.LOCATION_BLOCKS)) {
            FLUID_COLOR.invalidateAll();
        }
    }

    private static int computeFluidColor(Fluid fluid) {

        Minecraft mc = Minecraft.getInstance();

        if (mc.level == null)
            return -1;

        ResourceLocation still =
                IClientFluidTypeExtensions.of(fluid).getStillTexture();

        if (still == null)
            return -1;

        TextureAtlas atlas =
                mc.getModelManager().getAtlas(TextureAtlas.LOCATION_BLOCKS);

        TextureAtlasSprite sprite = atlas.getSprite(still);

        if (sprite == null)
            return -1;

        NativeImage img = sprite.contents().getOriginalImage();

        int w = img.getWidth();
        int h = img.getHeight();

        int[] xs = {w / 4, w / 2, (3 * w) / 4};
        int[] ys = {h / 4, h / 2, (3 * h) / 4};

        float r = 0;
        float g = 0;
        float b = 0;

        int samples = 0;

        for (int x : xs) {
            for (int y : ys) {

                int col = abgrToArgb(img.getPixel(x, y));

                int a = (col >> 24) & 255;

                if (a < 16)
                    continue;

                r += ((col >> 16) & 255) / 255f;
                g += ((col >> 8) & 255) / 255f;
                b += (col & 255) / 255f;

                samples++;
            }
        }

        if (samples == 0)
            return -1;

        r /= samples;
        g /= samples;
        b /= samples;

        return colorClamp(r, g, b, 1f);
    }

    public static int getColor(FluidStack stack) {

        if (stack == null)
            return -1;

        Fluid fluid = stack.getFluid();

        int base;

        try {
            base = FLUID_COLOR.get(fluid);
        } catch (ExecutionException e) {
            base = -1;
        }

        IClientFluidTypeExtensions extensions = IClientFluidTypeExtensions.of(fluid);
        int tint = extensions.getTintColor(stack);

        if (tint == -1)
            return base;

        float r = ((tint >> 16) & 255) / 255f * ((base >> 16) & 255) / 255f;
        float g = ((tint >> 8) & 255) / 255f * ((base >> 8) & 255) / 255f;
        float b = (tint & 255) / 255f * (base & 255) / 255f;

        return colorClamp(r, g, b, 1f);
    }

    private static int abgrToArgb(int c) {
        int a = (c >> 24) & 255;
        int b = (c >> 16) & 255;
        int g = (c >> 8) & 255;
        int r = c & 255;

        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    private static int colorClamp(float r, float g, float b, float a) {

        int ri = (int) (Math.min(1, r) * 255);
        int gi = (int) (Math.min(1, g) * 255);
        int bi = (int) (Math.min(1, b) * 255);
        int ai = (int) (Math.min(1, a) * 255);

        return (ai << 24) | (ri << 16) | (gi << 8) | bi;
    }
}