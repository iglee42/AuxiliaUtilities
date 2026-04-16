package fr.iglee42.auxiliautilities.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import fr.iglee42.auxiliautilities.blocks.BlockOpiniumCore;
import fr.iglee42.auxiliautilities.items.api.AUBlockItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

public class OpiniumCoreItemRenderer extends BlockEntityWithoutLevelRenderer {
    public OpiniumCoreItemRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext ctx, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        if (!(stack.getItem() instanceof AUBlockItem blockItem)) return;
        if (!(blockItem.getBlock() instanceof BlockOpiniumCore core)) return;
        poseStack.pushPose();
        poseStack.scale(0.75f,0.75f,0.75f);
        poseStack.translate(1/8f, 1/8f, 1/8f);
        OpiniumCoreRenderer.renderOpiniumCore(poseStack,bufferSource,packedLight,packedOverlay,core.getTier());
        poseStack.popPose();
    }


    public static class Extension implements IClientItemExtensions{
        private final OpiniumCoreItemRenderer renderer = new OpiniumCoreItemRenderer();

        @Override
        public BlockEntityWithoutLevelRenderer getCustomRenderer() {
            return renderer;
        }
    }
}
