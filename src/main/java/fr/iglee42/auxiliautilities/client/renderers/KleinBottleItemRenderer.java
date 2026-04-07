package fr.iglee42.auxiliautilities.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import fr.iglee42.auxiliautilities.blocks.BlockOpiniumCore;
import fr.iglee42.auxiliautilities.items.AUBlockItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

public class KleinBottleItemRenderer extends BlockEntityWithoutLevelRenderer {
    public KleinBottleItemRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext ctx, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        if (Minecraft.getInstance().level == null)
            return;
        double time = (Minecraft.getInstance().level.getGameTime() + Minecraft.getInstance().getTimer().getGameTimeDeltaTicks()) / 30.0 % 4.0;
        poseStack.pushPose();
        poseStack.scale(0.75f,0.75f,0.75f);
        poseStack.mulPose(Axis.YP.rotationDegrees(180));
        poseStack.translate(-9/8f, 1/8f, 1/8f);
        KleinBottleRenderer.renderTube(poseStack,bufferSource,packedLight,packedOverlay,time);
        poseStack.popPose();
    }


    public static class Extension implements IClientItemExtensions{
        private final KleinBottleItemRenderer renderer = new KleinBottleItemRenderer();

        @Override
        public BlockEntityWithoutLevelRenderer getCustomRenderer() {
            return renderer;
        }
    }
}
