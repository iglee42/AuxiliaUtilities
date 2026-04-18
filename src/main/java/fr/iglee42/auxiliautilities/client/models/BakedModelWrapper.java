package fr.iglee42.auxiliautilities.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.ChunkRenderTypeSet;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.common.util.TriState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BakedModelWrapper<T extends BakedModel> implements BakedModel {

    protected final BakedModel originalModel;

    public BakedModelWrapper(BakedModel originalModel) {
        this.originalModel = originalModel;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState p_235039_, @Nullable Direction p_235040_, RandomSource p_235041_) {
        return List.of();
    }

    @Override
    public boolean useAmbientOcclusion() {
        return originalModel.useAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return originalModel.isGui3d();
    }

    @Override
    public boolean usesBlockLight() {
        return originalModel.usesBlockLight();
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return originalModel.getParticleIcon();
    }

    @Override
    public ItemTransforms getTransforms() {
        return originalModel.getTransforms();
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand, ModelData data, @Nullable RenderType renderType) {
        return BakedModel.super.getQuads(state, side, rand, data, renderType);
    }

    @Override
    public TriState useAmbientOcclusion(BlockState state, ModelData data, RenderType renderType) {
        return BakedModel.super.useAmbientOcclusion(state, data, renderType);
    }

    @Override
    public void applyTransform(ItemDisplayContext transformType, PoseStack poseStack, boolean applyLeftHandTransform) {
        originalModel.applyTransform(transformType,poseStack,applyLeftHandTransform);
    }

    @Override
    public ModelData getModelData(BlockAndTintGetter level, BlockPos pos, BlockState state, ModelData modelData) {
        return BakedModel.super.getModelData(level, pos, state, modelData);
    }

    @Override
    public TextureAtlasSprite getParticleIcon(ModelData data) {
        return BakedModel.super.getParticleIcon(data);
    }

    @Override
    public ChunkRenderTypeSet getRenderTypes(BlockState state, RandomSource rand, ModelData data) {
        return BakedModel.super.getRenderTypes(state, rand, data);
    }

    @Override
    public RenderType getRenderType(ItemStack itemStack) {
        return BakedModel.super.getRenderType(itemStack);
    }

    @Override
    public List<BakedModel> getRenderPasses(ItemStack itemStack) {
        return BakedModel.super.getRenderPasses(itemStack);
    }
}
