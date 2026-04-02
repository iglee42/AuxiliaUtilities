package fr.iglee42.auxiliautilities.client.renderers;

import com.mojang.blaze3d.vertex.*;
import fr.iglee42.auxiliautilities.blockentities.generators.BERainbowGenerator;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import org.joml.Matrix4f;

import java.awt.*;
import java.util.Random;

public class RainbowGeneratorRenderer implements BlockEntityRenderer<BERainbowGenerator> {

    private static final RenderType RAINBOW_RENDER = RenderType.create(
            "rainbow_generator",
            DefaultVertexFormat.POSITION_COLOR,
            VertexFormat.Mode.QUADS,
            256,
            false,
            true,
            RenderType.CompositeState.builder()
                    .setTransparencyState(RenderStateShard.ADDITIVE_TRANSPARENCY)
                    .setShaderState(RenderStateShard.POSITION_COLOR_SHADER)
                    .setCullState(RenderStateShard.NO_CULL)
                    .createCompositeState(false)
    );

    public RainbowGeneratorRenderer(BlockEntityRendererProvider.Context ctx) {
    }

    @Override
    public void render(
            BERainbowGenerator be,
            float partialTicks,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            int packedOverlay
    ) {
        if (!be.isProviding())
            return;

        BlockPos pos = be.getBlockPos();
        VertexConsumer consumer = buffer.getBuffer(RAINBOW_RENDER);

        // Fixed seed so ray directions are stable; timer animates the spin
        Random rand = new Random(425L + pos.hashCode());
        float timer = (System.currentTimeMillis() % 360000L) / 4000.0F;

        poseStack.pushPose();
        // Centre on the block in BER-local space (BER origin = block corner)
        poseStack.translate(0.5, 0.5, 0.5);
        // Capture the pose matrix AFTER the translate, and never mutate it
        Matrix4f poseMatrix = poseStack.last().pose();

        for (int i = 0; i < 32; i++) {
            // Build a fresh, independent rotation matrix for each ray.
            // Previously the code mutated poseStack.last().pose() directly and
            // accumulated all rotations across rays — that corrupted the world
            // transform and caused the screen-aligned / invisible bug.
            Matrix4f rotMat = new Matrix4f(); // identity
            rotMat.rotate(rand.nextFloat() * ((float) Math.PI * 2f) + timer, 1, 0, 0);
            rotMat.rotate(rand.nextFloat() * ((float) Math.PI * 2f) + timer, 0, 1, 0);
            rotMat.rotate(rand.nextFloat() * ((float) Math.PI * 2f) + timer, 0, 0, 1);
            rotMat.rotate(rand.nextFloat() * ((float) Math.PI * 2f) + timer, 1, 0, 0);
            rotMat.rotate(rand.nextFloat() * ((float) Math.PI * 2f) + timer, 0, 1, 0);
            rotMat.rotate(rand.nextFloat() * ((float) Math.PI * 2f) + timer, 0, 0, 1);

            float r = (1.0F + rand.nextFloat() * 2.5F) * 2.0F;
            rotMat.rotate(timer / 180F, 0, 1, 0);

            // Combined: pose (block-centre in camera space) × local ray rotation.
            // Copying poseMatrix so it is never modified.
            Matrix4f combined = new Matrix4f(poseMatrix).mul(rotMat);

            int rgb = Color.HSBtoRGB(i / 16F, 1F, 1F);
            float rCol = ((rgb & 0xFF0000) >> 16) / 255F;
            float gCol = ((rgb & 0xFF00) >> 8) / 255F;
            float bCol = (rgb & 0xFF) / 255F;

            // Flat needle shape along local +Z; addVertex(matrix, x, y, z) applies
            // the combined transform so vertices land in the correct world position.
            consumer.addVertex(combined,  0,            0,          0).setColor(rCol, gCol, bCol, 0.9F);
            consumer.addVertex(combined,  0,  0.126f * r,  0.5f * r).setColor(rCol, gCol, bCol, 0.0F);
            consumer.addVertex(combined,  0,           0,  0.6f * r).setColor(rCol, gCol, bCol, 0.0F);
            consumer.addVertex(combined,  0, -0.126f * r,  0.5f * r).setColor(rCol, gCol, bCol, 0.0F);
        }

        poseStack.popPose();
    }
}
