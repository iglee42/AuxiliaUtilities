package fr.iglee42.auxiliautilities.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.blockentities.gp.generators.BEManualMill;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;

public class ManualMillRenderer implements BlockEntityRenderer<BEManualMill> {
    public static final ModelResourceLocation GEAR_MODEL = ModelResourceLocation.standalone(AuxiliaUtilities.id("block/manual_mill_gear"));
    public ManualMillRenderer(BlockEntityRendererProvider.Context ctx) {
    }

    @Override
    public void render(BEManualMill be, float pt, PoseStack stack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        stack.pushPose();
        BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();
        BakedModel model = dispatcher.getBlockModelShaper().getModelManager().getModel(GEAR_MODEL);
        VertexConsumer buffer = bufferSource.getBuffer(RenderType.CUTOUT);
        double v = be.getRenderOffset() + pt * Math.max(Math.min(be.getAnimationTime(),0.5F)-0.05D * pt,0D) * (Math.PI / 10.0);
        float pivot = 0.5F;
        stack.translate(pivot,0,pivot);
        stack.mulPose(Axis.YP.rotation((float) v));
        stack.translate(-pivot,0,-pivot);
        dispatcher.getModelRenderer().renderModel(
                stack.last(),
                buffer,
                be.getBlockState(),
                model,
                1F,1F,1F,
                packedLight,
                packedOverlay
        );
        stack.popPose();
    }
}
