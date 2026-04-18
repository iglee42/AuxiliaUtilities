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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.fml.ModList;
import org.joml.Quaternionf;

import static fr.iglee42.auxiliautilities.client.layers.AngelRingRenderer.renderWing;

public class ChickenRingRenderer extends RenderLayer<PlayerRenderState, PlayerModel> {


    public ChickenRingRenderer(RenderLayerParent<PlayerRenderState, PlayerModel> parent) {
        super(parent);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource source, int light, PlayerRenderState state, float pt, float age) {
        if (Minecraft.getInstance().player == null) return;
        Player player = Minecraft.getInstance().player;

        if (state.isInvisible) return;

        int index = InventoryUtil.getFirstInventoryIndex(player, AUItems.CHICKEN_RING.asItem());
        ItemStack stack = index == -1 ? ItemStack.EMPTY : player.getInventory().getItem(index);
        if (ModList.get().isLoaded("curios") && stack.isEmpty()) {
            stack = AUCuriosHelper.getItemFromCuriosForCosmetic(AUItems.CHICKEN_RING.asItem(), player);
        }

        if (InventoryHelper.isStackEmpty(stack)) return;

        renderWing(ItemAngelRing.AngelRingWings.FEATHER, poseStack, source, light, true, player.getDeltaMovement().y() < 0,getParentModel());
        renderWing(ItemAngelRing.AngelRingWings.FEATHER, poseStack, source, light, false, player.getDeltaMovement().y() < 0,getParentModel());
    }
}
