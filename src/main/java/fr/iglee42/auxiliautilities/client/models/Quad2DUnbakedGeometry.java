package fr.iglee42.auxiliautilities.client.models;

import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.renderer.block.model.TextureSlots;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.util.context.ContextMap;
import net.neoforged.neoforge.client.model.AbstractUnbakedModel;
import net.neoforged.neoforge.client.model.ExtendedUnbakedGeometry;
import net.neoforged.neoforge.client.model.StandardModelParameters;
import org.jetbrains.annotations.Nullable;

public class Quad2DUnbakedGeometry implements ExtendedUnbakedGeometry {

    @Override
    public QuadCollection bake(TextureSlots textures, ModelBaker baker, ModelState modelState,ModelDebugName modelDebugName, ContextMap additionalProperties) {
        var builder = new QuadCollection.Builder();
        TextureAtlasSprite sprite = baker.sprites().resolveSlot(textures, TextureSlot.TEXTURE.getId(),modelDebugName);
        Quad2DCache.quads2DCache.get(sprite).forEach(builder::addUnculledFace);
        return builder.build();
    }

    public static class Quad2DUnbakedModel extends AbstractUnbakedModel {

        private final Quad2DUnbakedGeometry geometry;

        protected Quad2DUnbakedModel(StandardModelParameters parameters, Quad2DUnbakedGeometry geometry) {
            super(parameters);
            this.geometry = geometry;
        }

        @Override
        public @Nullable UnbakedGeometry geometry() {
            return geometry;
        }
    }
}
