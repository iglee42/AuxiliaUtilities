package fr.iglee42.auxiliautilities.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import fr.iglee42.auxiliautilities.interblocks.FlatTransferNodeHandler;
import fr.iglee42.auxiliautilities.interblocks.FlatTransferNodeHandler.Type;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.BakedModelWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FlatTransferNodeModelWrapper extends BakedModelWrapper<BakedModel> {

    public FlatTransferNodeModelWrapper(BakedModel originalModel) {
        super(originalModel);
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand) {
        return new ArrayList<>(Quad2DCache.quads2DCache.get(getParticleIcon()));
    }

    @Override
    public BakedModel applyTransform(ItemDisplayContext cameraTransformType, PoseStack poseStack, boolean applyLeftHandTransform) {
        super.applyTransform(cameraTransformType, poseStack, applyLeftHandTransform);
        return this;
    }

    @Override
    public List<BakedModel> getRenderPasses(ItemStack itemStack, boolean fabulous) {
        return List.of(this);
    }

}
