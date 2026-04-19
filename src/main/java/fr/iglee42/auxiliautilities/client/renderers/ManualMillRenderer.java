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
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;

public class ManualMillRenderer implements BlockEntityRenderer<BEManualMill> {
    public static final StandaloneModelKey<BlockStateModel> GEAR_MODEL = new StandaloneModelKey<>(AuxiliaUtilities.id("block/manual_mill_gear"));


    public ManualMillRenderer(BlockEntityRendererProvider.Context ctx) {
    }

    @Override
    public void render(BEManualMill be, float pt, PoseStack stack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, Vec3 cameraPos) {
        stack.pushPose();
        BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();
        BlockStateModel model = dispatcher.getBlockModelShaper().getModelManager().getStandaloneModel(GEAR_MODEL);
        if (model == null) return;
        VertexConsumer buffer = bufferSource.getBuffer(RenderType.CUTOUT);
        double v = be.getRenderOffset() + pt * Math.max(Math.min(be.getAnimationTime(),0.5F)-0.05D * pt,0D) * (Math.PI / 10.0);
        float pivot = 0.5F;
        stack.translate(pivot,0,pivot);
        stack.mulPose(Axis.YP.rotation((float) v));
        stack.translate(-pivot,0,-pivot);
        ModelBlockRenderer.renderModel(
                stack.last(),
                buffer,
                model,
                1F,1F,1F,
                packedLight,
                packedOverlay
        );
        stack.popPose();
    }
}
