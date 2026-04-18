package fr.iglee42.auxiliautilities.client.models;

import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.block.model.TextureSlots;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.util.context.ContextMap;
import net.neoforged.neoforge.client.RenderTypeGroup;
import net.neoforged.neoforge.client.model.AbstractUnbakedModel;
import net.neoforged.neoforge.client.model.NeoForgeModelProperties;
import net.neoforged.neoforge.client.model.StandardModelParameters;

public class Quad2DUnbakedModel extends AbstractUnbakedModel {
    protected Quad2DUnbakedModel(StandardModelParameters parameters) {
        super(parameters);
    }

    @Override
    public BakedModel bake(TextureSlots textures, ModelBaker baker, ModelState modelState, boolean useAmbientOcclusion, boolean usesBlockLight, ItemTransforms itemTransforms, ContextMap additionalProperties) {
        var builder = new SimpleBakedModel.Builder(useAmbientOcclusion,usesBlockLight,true,itemTransforms);
        builder.particle(baker.findSprite(textures,TextureSlot.PARTICLE.getId()));
        TextureAtlasSprite sprite = baker.findSprite(textures,TextureSlot.TEXTURE.getId());
        Quad2DCache.quads2DCache.get(sprite).forEach(builder::addUnculledFace);
        return builder.build(additionalProperties.getOrDefault(NeoForgeModelProperties.RENDER_TYPE, RenderTypeGroup.EMPTY));
    }
}
