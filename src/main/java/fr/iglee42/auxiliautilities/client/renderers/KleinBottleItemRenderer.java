package fr.iglee42.auxiliautilities.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.Set;

public class KleinBottleItemRenderer implements SpecialModelRenderer<Void> {
    public KleinBottleItemRenderer() {
    }


    @Override
    public void render(@Nullable Void p_387335_, ItemDisplayContext ctx, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, boolean hasFoil) {
        if (Minecraft.getInstance().level == null)
            return;
        double time = (Minecraft.getInstance().level.getGameTime() + Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaTicks()) / 30.0 % 4.0;
        poseStack.pushPose();
        poseStack.scale(0.75f,0.75f,0.75f);
        poseStack.mulPose(Axis.YP.rotationDegrees(180));
        poseStack.translate(-9/8f, 1/8f, 1/8f);
        KleinBottleRenderer.renderTube(poseStack,bufferSource,packedLight,packedOverlay,time);
        poseStack.popPose();
    }

    @Override
    public void getExtents(Set<Vector3f> p_428206_) {

    }

    @Override
    public @Nullable Void extractArgument(ItemStack p_387212_) {
        return null;
    }

    public static record Unbaked() implements SpecialModelRenderer.Unbaked {

        public static final MapCodec<KleinBottleItemRenderer.Unbaked> MAP_CODEC = MapCodec.unit(new KleinBottleItemRenderer.Unbaked());

        @Override
        public MapCodec<KleinBottleItemRenderer.Unbaked> type() {
            return MAP_CODEC;
        }

        @Override
        public SpecialModelRenderer<?> bake(EntityModelSet p_386553_) {
            return new KleinBottleItemRenderer();
        }
    }

}

