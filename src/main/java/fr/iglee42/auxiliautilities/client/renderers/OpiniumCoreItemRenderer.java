package fr.iglee42.auxiliautilities.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import fr.iglee42.auxiliautilities.blocks.BlockOpiniumCore;
import fr.iglee42.auxiliautilities.items.api.AUBlockItem;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class OpiniumCoreItemRenderer implements SpecialModelRenderer<BlockOpiniumCore.Tier> {
    public OpiniumCoreItemRenderer() {
    }

    @Override
    public void render(@Nullable BlockOpiniumCore.Tier tier, ItemDisplayContext context, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, boolean hasFoil) {
        poseStack.pushPose();
        poseStack.scale(0.75f,0.75f,0.75f);
        poseStack.translate(1/8f, 1/8f, 1/8f);
        OpiniumCoreRenderer.renderOpiniumCore(poseStack,bufferSource,packedLight,packedOverlay,tier);
        poseStack.popPose();
    }

    @Override
    public @Nullable BlockOpiniumCore.Tier extractArgument(ItemStack stack) {
        if (!(stack.getItem() instanceof AUBlockItem blockItem)) return BlockOpiniumCore.Tier.MEDIOCRE;
        if (!(blockItem.getBlock() instanceof BlockOpiniumCore core)) return BlockOpiniumCore.Tier.MEDIOCRE;
        return core.getTier();
    }


    public static record Unbaked() implements SpecialModelRenderer.Unbaked {

        public static final MapCodec<OpiniumCoreItemRenderer.Unbaked> MAP_CODEC = MapCodec.unit(new OpiniumCoreItemRenderer.Unbaked());

        @Override
        public MapCodec<OpiniumCoreItemRenderer.Unbaked> type() {
            return MAP_CODEC;
        }

        @Override
        public SpecialModelRenderer<?> bake(EntityModelSet p_386553_) {
            return new OpiniumCoreItemRenderer();
        }
    }
}
