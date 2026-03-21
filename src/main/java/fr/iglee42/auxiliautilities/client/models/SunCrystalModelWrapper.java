package fr.iglee42.auxiliautilities.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Transformation;
import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.items.ItemSunCrystal;
import fr.iglee42.auxiliautilities.utils.QuadsHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockElementFace;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.BakedModelWrapper;
import net.neoforged.neoforge.client.model.SimpleModelState;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;
import fr.iglee42.auxiliautilities.utils.QuadsHelper.UV;

import java.util.ArrayList;
import java.util.List;

public class SunCrystalModelWrapper extends BakedModelWrapper<BakedModel> {

    private static final ResourceLocation INNER_TEXTURE = AuxiliaUtilities.id("item/sun_crystal_gradient");
    private static final int RAY_COUNT = 16;

    private ItemStack stack = ItemStack.EMPTY;

    public SunCrystalModelWrapper(BakedModel originalModel) {
        super(originalModel);
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand) {
        List<BakedQuad> quads = new ArrayList<>(super.getQuads(state, side, rand));
        if (stack != null && !stack.isEmpty()) {
            int damageValue = stack.getDamageValue();
            int progress = (int) ((1.0F - (float) damageValue / ItemSunCrystal.MAX_DAMAGE) * 255);
            if (progress > 0) {
                float timer = (System.currentTimeMillis() % 360000L) / 4000.0F;
                quads.addAll(buildRays(rand, timer, progress));
            }
        }
        return quads;
    }

    private static List<BakedQuad> buildRays(RandomSource rand, float timer, int progress) {
        TextureAtlasSprite sprite = Minecraft.getInstance()
                .getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
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
            rays.add(QuadsHelper.createBakedQuad(new UV[] { new UV(0.5F, 0.5F, 0.5F, 0.5F, 1.0F), new UV(0.5F + b.x, 0.5F + b.y, 0.5F + b.z, 1.0F, 0.0F), new UV(0.5F + d.x, 0.5F + d.y, 0.5F + d.z, 0.5F, 0.0F), new UV(0.5F + c.x, 0.5F + c.y, 0.5F + c.z, 0.0F, 0.0F) },sprite,false,tint));
            rays.add(QuadsHelper.createBakedQuad(new UV[] { new UV(0.5F, 0.5F, 0.5F, 0.5F, 1.0F), new UV(0.5F + c.x, 0.5F + c.y, 0.5F + c.z, 0.0F, 0.0F), new UV(0.5F + d.x, 0.5F + d.y, 0.5F + d.z, 0.5F, 0.0F), new UV(0.5F + b.x, 0.5F + b.y, 0.5F + b.z, 1.0F, 0.0F) },sprite, false, tint));
        }
        return rays;
    }

    @Override
    public BakedModel applyTransform(ItemDisplayContext cameraTransformType, PoseStack poseStack, boolean applyLeftHandTransform) {
        super.applyTransform(cameraTransformType, poseStack, applyLeftHandTransform);
        return this;
    }

    @Override
    public List<BakedModel> getRenderPasses(ItemStack itemStack, boolean fabulous) {
        this.stack = itemStack;
        return List.of(this);
    }
}
