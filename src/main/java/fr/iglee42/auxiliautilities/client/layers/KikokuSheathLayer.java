package fr.iglee42.auxiliautilities.client.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.items.registries.AUItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;

import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

public class KikokuSheathLayer extends RenderLayer<PlayerRenderState, PlayerModel> {
    public static final ResourceLocation MODEL_LOCATION = AuxiliaUtilities.id("item/kikoku_sheath_full");
    public static final ResourceLocation EMPTY_LOCATION = AuxiliaUtilities.id("item/kikoku_sheath_empty");

    public KikokuSheathLayer(RenderLayerParent<PlayerRenderState, PlayerModel> parent) {
        super(parent);
    }


    @Override
    public void render(
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            PlayerRenderState state,
            float limbSwing,
            float limbSwingAmount
    ) {

        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        if (!hasKikoku(player))
            return;

        poseStack.pushPose();

        poseStack.translate(0.3, 1.0, 0.2);

        poseStack.mulPose(Axis.XP.rotationDegrees(190));
        poseStack.mulPose(Axis.ZP.rotationDegrees(40));
        poseStack.mulPose(Axis.YP.rotationDegrees(90));

        poseStack.scale(0.3f, 0.3f, 0.3f);

        renderModel(buffer, poseStack, packedLight, player.getMainHandItem().is(AUItems.KIKOKU.get()) || player.getOffhandItem().is(AUItems.KIKOKU.get()));

        poseStack.popPose();
    }

    private boolean hasKikoku(Player player) {

        for (ItemStack stack : player.getInventory().items) {
            if (stack.is(AUItems.KIKOKU.get()))
                return true;
        }

        return false;
    }

    private void renderModel(MultiBufferSource bufferSource, PoseStack poseStack, int light, boolean isInHand){
        BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();
        BakedModel model = dispatcher.getBlockModelShaper().getModelManager().getStandaloneModel(isInHand ? EMPTY_LOCATION : MODEL_LOCATION);
        VertexConsumer buffer = bufferSource.getBuffer(RenderType.cutout());

        dispatcher.getModelRenderer().renderModel(
                poseStack.last(),
                buffer,
                Blocks.AIR.defaultBlockState(),
                model,
                1.0F, 1.0F, 1.0F,
                light,
                OverlayTexture.NO_OVERLAY
        );

    }
}