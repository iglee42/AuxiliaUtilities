package fr.iglee42.auxiliautilities.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.BakedModelWrapper;
import net.neoforged.neoforge.client.model.ItemLayerModel;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WandsModelWrapper extends BakedModelWrapper<BakedModel> {


    private static final ResourceLocation BUILDERS = AuxiliaUtilities.id("item/tools/creative_builders_wand_skeleton");
    private static final ResourceLocation DESTRUCTION = AuxiliaUtilities.id("item/tools/creative_destruction_wand_skeleton");

    private final String type;

    public WandsModelWrapper(BakedModel originalModel, String type) {
        super(originalModel);
        this.type = type;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand) {
        List<BakedQuad> quads = super.getQuads(state, side, rand);
        if (type.equals("builders")){
            List<BakedQuad> added = Quad2DCache.quads2DCache.get(Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(BUILDERS));
            quads.removeAll(added);
            quads.addAll(added);
        }
        if (type.equals("destruction")){
            List<BakedQuad> added = Quad2DCache.quads2DCache.get(Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(DESTRUCTION));
            quads.removeAll(added);
            quads.addAll(added);
        }
        return quads;
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
