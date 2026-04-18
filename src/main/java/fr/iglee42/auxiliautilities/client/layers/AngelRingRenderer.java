package fr.iglee42.auxiliautilities.client.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.items.ItemAngelRing;
import fr.iglee42.auxiliautilities.items.registries.AUDataComponents;
import fr.iglee42.auxiliautilities.items.registries.AUItems;
import fr.iglee42.auxiliautilities.utils.AUCuriosHelper;
import fr.iglee42.auxiliautilities.utils.InventoryHelper;
import fr.iglee42.igleelib.api.utils.InventoryUtil;
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
import net.neoforged.fml.ModList;
import org.joml.Quaternionf;

public class AngelRingRenderer extends RenderLayer<PlayerRenderState, PlayerModel> {


    protected static final double FLAP_FREQUENCY = 0.5;
    protected static final double MAX_ANGLE = 25.0;

    public AngelRingRenderer(RenderLayerParent<PlayerRenderState, PlayerModel> parent) {
        super(parent);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource source, int light, PlayerRenderState state, float pt, float age) {
        if (Minecraft.getInstance().player == null) return;

        Player player = Minecraft.getInstance().player;
        if (state.isInvisible) return;

        int index = InventoryUtil.getFirstInventoryIndex(player, AUItems.ANGEL_RING.asItem());
        ItemStack stack = index == -1 ? ItemStack.EMPTY : player.getInventory().getItem(index);
        if (ModList.get().isLoaded("curios") && stack.isEmpty()) {
            stack = AUCuriosHelper.getItemFromCuriosForCosmetic(AUItems.ANGEL_RING.asItem(), player);
        }

        if (InventoryHelper.isStackEmpty(stack)) return;
        ItemAngelRing.AngelRingWings wings = stack.get(AUDataComponents.WINGS);
        if (wings == null || wings == ItemAngelRing.AngelRingWings.NONE) return;

        renderWing(wings, poseStack, source, light, true, player.getAbilities().flying,getParentModel());
        renderWing(wings, poseStack, source, light, false, player.getAbilities().flying,getParentModel());
    }

    public static void renderWing(ItemAngelRing.AngelRingWings wings, PoseStack poseStack, MultiBufferSource buffer, int packedLight, boolean right, boolean isFlying,PlayerModel model) {
        poseStack.pushPose();
        model.body.translateAndRotate(poseStack);

        poseStack.translate((right ? -1 : 1) * 0.5, 0.1, 0.45);
        poseStack.scale(0.9f, 0.9f, 0.9f);

        poseStack.mulPose(new Quaternionf().rotateXYZ(
                .0f,
                (float) ((Math.PI/2.f) - (right ? 1 : -1) * (Math.PI/2.f - Math.PI /6.f)),
                (float) Math.PI
        ));

        poseStack.translate(-0.5,0,0);
        double angle = 0;
        if (isFlying){
            angle = (right ? 1 : -1) * MAX_ANGLE * Math.sin(2*Math.PI * FLAP_FREQUENCY * (System.currentTimeMillis()/1000.));
        }
        poseStack.mulPose(Axis.YN.rotationDegrees((float) angle));
        poseStack.translate(0, -0.5F, -0.5F);
        renderModel(buffer,poseStack,packedLight,wings);
        poseStack.popPose();
    }

    private static void renderModel(MultiBufferSource bufferSource, PoseStack poseStack, int light, ItemAngelRing.AngelRingWings wings){
        BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();
        BakedModel model = dispatcher.getBlockModelShaper().getModelManager().getStandaloneModel(getWingLocation(wings));
        VertexConsumer buffer = bufferSource.getBuffer(RenderType.cutout());

        poseStack.pushPose();
        dispatcher.getModelRenderer().renderModel(
                poseStack.last(),
                buffer,
                Blocks.AIR.defaultBlockState(),
                model,
                1.0F, 1.0F, 1.0F,
                light,
                OverlayTexture.NO_OVERLAY
        );
        poseStack.popPose();
    }

    public static ResourceLocation getWingLocation(ItemAngelRing.AngelRingWings wings){
        return AuxiliaUtilities.id("item/"+wings.getSerializedName()+"_wing");
    }
}
