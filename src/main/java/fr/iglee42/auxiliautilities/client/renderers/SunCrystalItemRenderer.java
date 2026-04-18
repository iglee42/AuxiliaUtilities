package fr.iglee42.auxiliautilities.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.mojang.serialization.MapCodec;
import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.items.ItemSunCrystal;
import fr.iglee42.auxiliautilities.utils.QuadsHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

public class SunCrystalItemRenderer implements SpecialModelRenderer<ItemStack> {
    public SunCrystalItemRenderer() {
    }

    private static final ResourceLocation INNER_TEXTURE = AuxiliaUtilities.id("item/sun_crystal_gradient");
    private static final int RAY_COUNT = 16;

    @Override
    public void render(@Nullable ItemStack stack, ItemDisplayContext ctx, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, boolean hasFoil) {
        if (Minecraft.getInstance().level == null)
            return;
        poseStack.pushPose();
        //poseStack.scale(0.75f,0.75f,0.75f);
        //poseStack.mulPose(Axis.YP.rotationDegrees(180));
        //poseStack.translate(-9/8f, 1/8f, 1/8f);
        BakedModel model = new BakedModel() {
            @Override
            public List<BakedQuad> getQuads(@Nullable BlockState p_235039_, @Nullable Direction p_235040_, RandomSource rand) {
                if (stack != null && !stack.isEmpty()) {
                    int damageValue = stack.getDamageValue();
                    int progress = (int) ((1.0F - (float) damageValue / ItemSunCrystal.MAX_DAMAGE) * 255);
                    if (progress > 0) {
                        float timer = (System.currentTimeMillis() % 360000L) / 4000.0F;
                        return buildRays(rand, timer, progress);
                    }
                }
                return List.of();
            }

            @Override
            public boolean useAmbientOcclusion() {
                return true;
            }

            @Override
            public boolean isGui3d() {
                return true;
            }

            @Override
            public boolean usesBlockLight() {
                return false;
            }

            @Override
            public TextureAtlasSprite getParticleIcon() {
                return null;
            }

            @Override
            public ItemTransforms getTransforms() {
                return null;
            }
        };
        VertexConsumer buffer = bufferSource.getBuffer(RenderType.CUTOUT);
        Minecraft.getInstance().getBlockRenderer().getModelRenderer()
                        .renderModel(
                                poseStack.last(),
                                buffer,
                                Blocks.AIR.defaultBlockState(),
                                model,
                                1,1,1,
                                packedLight,
                                packedOverlay
                        );
        poseStack.popPose();
    }


    private static List<BakedQuad> buildRays(RandomSource rand, float timer, int progress) {
        TextureAtlasSprite sprite = Minecraft.getInstance()
                .getTextureAtlas(TextureAtlas.LOCATION_BLOCKS)
                .apply(INNER_TEXTURE);

        List<BakedQuad> rays = new ArrayList<>();

        Matrix4f matrix = new Matrix4f().identity();

        Vector4f b = new Vector4f();
        Vector4f c = new Vector4f();
        Vector4f d = new Vector4f();

        Vector3f[] vecs = new Vector3f[]{
                new Vector3f(1,0,0),
                new Vector3f(0,1,0),
                new Vector3f(0,0,1),
                new Vector3f(1,0,0),
                new Vector3f(0,1,0),
                new Vector3f(0,0,1)
        };

        for (int i = 0; i < RAY_COUNT; i++) {

            for (Vector3f vec : vecs) {
                matrix.rotate(rand.nextFloat() * ((float) Math.PI * 2f) + timer, vec);
            }

            float r = (0.75F + rand.nextFloat() * 0.5F) * progress / 255.0F;
            matrix.rotate(timer / 180F,0,1,0,matrix);
            b.set(0.0F,0.126 * r, 0.5*r,1.0F);
            c.set(0.0F,-0.126 * r, 0.5*r,1.0F);
            d.set(0.0F,0.0F,0.6F*r,1.0F);
            matrix.transform(b);
            matrix.transform(c);
            matrix.transform(d);
            int tint = 0x50FFFFFF;
            rays.add(QuadsHelper.createBakedQuad(new QuadsHelper.UV[] { new QuadsHelper.UV(0.5F, 0.5F, 0.5F, 0.5F, 1.0F), new QuadsHelper.UV(0.5F + b.x, 0.5F + b.y, 0.5F + b.z, 1.0F, 0.0F), new QuadsHelper.UV(0.5F + d.x, 0.5F + d.y, 0.5F + d.z, 0.5F, 0.0F), new QuadsHelper.UV(0.5F + c.x, 0.5F + c.y, 0.5F + c.z, 0.0F, 0.0F) },sprite,false,tint));
            rays.add(QuadsHelper.createBakedQuad(new QuadsHelper.UV[] { new QuadsHelper.UV(0.5F, 0.5F, 0.5F, 0.5F, 1.0F), new QuadsHelper.UV(0.5F + c.x, 0.5F + c.y, 0.5F + c.z, 0.0F, 0.0F), new QuadsHelper.UV(0.5F + d.x, 0.5F + d.y, 0.5F + d.z, 0.5F, 0.0F), new QuadsHelper.UV(0.5F + b.x, 0.5F + b.y, 0.5F + b.z, 1.0F, 0.0F) },sprite, false, tint));
        }
        return rays;
    }
    @Override
    public @Nullable ItemStack extractArgument(ItemStack stack) {
        return stack.copy();
    }

    public static record Unbaked() implements SpecialModelRenderer.Unbaked {

        public static final MapCodec<SunCrystalItemRenderer.Unbaked> MAP_CODEC = MapCodec.unit(new SunCrystalItemRenderer.Unbaked());

        @Override
        public MapCodec<SunCrystalItemRenderer.Unbaked> type() {
            return MAP_CODEC;
        }

        @Override
        public SpecialModelRenderer<?> bake(EntityModelSet p_386553_) {
            return new SunCrystalItemRenderer();
        }
    }

}

