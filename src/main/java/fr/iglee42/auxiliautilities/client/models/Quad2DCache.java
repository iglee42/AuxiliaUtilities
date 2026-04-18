package fr.iglee42.auxiliautilities.client.models;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.world.inventory.InventoryMenu;
import org.joml.Vector3f;

import java.util.WeakHashMap;
import java.util.function.Function;
import net.minecraft.resources.ResourceLocation;

public class Quad2DCache {

    public static WeakHashMap<TextureAtlasSprite, ImmutableList<BakedQuad>> quads2DCache =
            new WeakHashMap<>() {
                @Override
                public ImmutableList<BakedQuad> get(Object key) {
                    ImmutableList<BakedQuad> bakedQuads = super.get(key);
                    if (bakedQuads == null) {
                        TextureAtlasSprite sprite = key instanceof TextureAtlasSprite texture ? texture : null;
                        if (sprite == null) {
                            Function<ResourceLocation, TextureAtlasSprite> atlas = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS);
                            sprite = atlas.apply(MissingTextureAtlasSprite.getLocation());
                        }

                        bakedQuads = bakeSpriteQuads(sprite);
                        put(sprite, bakedQuads);
                    }
                    return bakedQuads;
                }
            };

    private static ImmutableList<BakedQuad> bakeSpriteQuads(TextureAtlasSprite sprite) {
        FaceBakery bakery = new FaceBakery();
        Vector3f from = new Vector3f(0.0F, 0.0F, 8.0F);
        Vector3f to = new Vector3f(16.0F, 16.0F, 8.0F);

        BlockElementFace southFace = new BlockElementFace(null, -1, "", new BlockFaceUV(new float[]{0.0F, 0.0F, 16.0F, 16.0F}, 0));
        BlockElementFace northFace = new BlockElementFace(null, -1, "", new BlockFaceUV(new float[]{16.0F, 0.0F, 0.0F, 16.0F}, 0));

        BakedQuad south = bakery.bakeQuad(from, to, southFace, sprite, Direction.SOUTH, BlockModelRotation.X0_Y0, null, true,0);
        BakedQuad north = bakery.bakeQuad(from, to, northFace, sprite, Direction.NORTH, BlockModelRotation.X0_Y0, null, true,0);
        return ImmutableList.of(south, north);
    }


}