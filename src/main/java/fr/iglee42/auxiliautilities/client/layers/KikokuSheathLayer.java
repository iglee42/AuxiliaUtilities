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
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;

public class KikokuSheathLayer extends RenderLayer<PlayerRenderState, PlayerModel> {
    public static final StandaloneModelKey<BlockStateModel> MODEL_LOCATION = new StandaloneModelKey<>(AuxiliaUtilities.id("item/kikoku_sheath_full"));
    public static final StandaloneModelKey<BlockStateModel> EMPTY_LOCATION = new StandaloneModelKey<>(AuxiliaUtilities.id("item/kikoku_sheath_empty"));

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

        for (ItemStack stack : player.getInventory().getNonEquipmentItems()) {
            if (stack.is(AUItems.KIKOKU.get()))
                return true;
        }

        return false;
    }

    private void renderModel(MultiBufferSource bufferSource, PoseStack poseStack, int light, boolean isInHand){
        BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();
        BlockStateModel model = dispatcher.getBlockModelShaper().getModelManager().getStandaloneModel(isInHand ? EMPTY_LOCATION : MODEL_LOCATION);
        if (model == null) return;
        VertexConsumer buffer = bufferSource.getBuffer(RenderType.cutout());

        ModelBlockRenderer.renderModel(
                poseStack.last(),
                buffer,
                model,
                1.0F, 1.0F, 1.0F,
                light,
                OverlayTexture.NO_OVERLAY
        );

    }
}