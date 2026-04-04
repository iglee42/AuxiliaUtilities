package fr.iglee42.auxiliautilities.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import fr.iglee42.auxiliautilities.blockentities.BEOpiniumCore;
import fr.iglee42.auxiliautilities.blocks.BlockOpiniumCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemDisplayContext;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

@OnlyIn(Dist.CLIENT)
public class OpiniumCoreRenderer implements BlockEntityRenderer<BEOpiniumCore> {

    private static final int NUMBER_ORBITS = 4;
    private static final float ORBIT_REDUCTION = 0.25F;
    private static final float CORE_REDUCTION = 0.375F;
    private static final float ORBIT_DISTANCE = 0.4375F;

    public OpiniumCoreRenderer(BlockEntityRendererProvider.Context ctx) {
    }

    @Override
    public void render(BEOpiniumCore be, float partialTick, PoseStack stack,
                       MultiBufferSource bufferSource, int packedLight, int packedOverlay) {

        if (!(be.getBlockState().getBlock() instanceof BlockOpiniumCore core)) return;

        BlockOpiniumCore.Tier tier = core.getTier();

        renderOpiniumCore(stack, bufferSource, packedLight, packedOverlay, tier);
    }

    public static void renderOpiniumCore(PoseStack stack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, BlockOpiniumCore.Tier tier) {
        // Animated timer — evaluated every frame so the model actually moves
        float renderTimer = (System.currentTimeMillis() % 360000L) / 40.0F;
        renderTimer /= 2.0F;
        renderTimer += (float)(tier.ordinal() * tier.ordinal()) + tier.ordinal() * 1.21F;

        // ── Compute wobbling orbit-plane axes ──────────────────────────────
        Vector3f ringAxisA = new Vector3f(0F, 1F, 0F);
        Vector3f ringAxisB = new Vector3f(1F, 0F, 0F);

        Matrix4f axesMat = new Matrix4f().identity();
        float r = renderTimer / 5F;
        Vector3f[] vecs = {
                new Vector3f(0F, 1F, 0F), new Vector3f(0F, 0F, 1F), new Vector3f(1F, 0F, 0F),
                new Vector3f(0F, 1F, 0F), new Vector3f(0F, 0F, 1F)
        };
        for (Vector3f axis : vecs) {
            axesMat.rotate(r, axis.x, axis.y, axis.z, axesMat);
            r *= 0.546F;
        }
        axesMat.transformDirection(ringAxisA);
        axesMat.transformDirection(ringAxisB);

        // ── Compute self-rotation axis for orbit items ─────────────────────
        axesMat.identity();
        r = renderTimer / 2F;
        for (Vector3f axis : vecs) {
            axesMat.rotate(r, axis.x, axis.y, axis.z, axesMat);
            r /= 2F;
        }
        Vector3f orbitAxis = new Vector3f(0F, 1F, 0F);
        axesMat.transformDirection(orbitAxis);
        orbitAxis.normalize();

        var itemRenderer = Minecraft.getInstance().getItemRenderer();

        // ── Center model ───────────────────────────────────────────────────
        // Desired vertex transform (right-to-left):
        //   T(+0.5)  ← place at block centre
        //   R(angle) ← spin
        //   S(scale) ← shrink
        //   T(-0.5)  ← centre model at origin
        //
        // In PoseStack (post-multiply), write them in the SAME order as above:
        stack.pushPose();
        stack.translate(0.5, 0.5, 0.5);                              // T(+0.5) – last applied to vertex
        stack.mulPose(Axis.YP.rotation(renderTimer / 15F)); // R
        stack.scale(CORE_REDUCTION, CORE_REDUCTION, CORE_REDUCTION);  // S
        itemRenderer.renderStatic(
                tier.getMain().asItem().getDefaultInstance(),
                ItemDisplayContext.NONE,
                packedLight, packedOverlay, stack, bufferSource, null, 0
        );
        stack.popPose();

        // ── Orbit models ───────────────────────────────────────────────────
        // Desired vertex transform (right-to-left):
        //   T(+0.5 + orbitOffset) ← orbit position + block centre
        //   R_self(angle)         ← self-rotation
        //   S(scale)              ← shrink
        for (int i = 0; i < NUMBER_ORBITS; i++) {
            float ang = (float) (i * Math.PI * 2D) / NUMBER_ORBITS;
            float ca = (float) Math.cos(ang);
            float sa = (float) Math.sin(ang);

            float orbitX = (ringAxisA.x * ca + ringAxisB.x * sa) * ORBIT_DISTANCE;
            float orbitY = (ringAxisA.y * ca + ringAxisB.y * sa) * ORBIT_DISTANCE;
            float orbitZ = (ringAxisA.z * ca + ringAxisB.z * sa) * ORBIT_DISTANCE;

            stack.pushPose();
            stack.translate(0.5 + orbitX, 0.5 + orbitY, 0.5 + orbitZ);  // T(+0.5 + orbit)
            stack.mulPose(new Quaternionf().rotationAxis(renderTimer / 6F + ang,
                    orbitAxis.x, orbitAxis.y, orbitAxis.z));               // R_self
            stack.scale(ORBIT_REDUCTION, ORBIT_REDUCTION, ORBIT_REDUCTION); // S
            itemRenderer.renderStatic(
                    tier.getOrbit().asItem().getDefaultInstance(),
                    ItemDisplayContext.NONE,
                    packedLight, packedOverlay, stack, bufferSource, null, 0
            );
            stack.popPose();
        }
    }

    @Override
    public boolean shouldRenderOffScreen(BEOpiniumCore be) {
        // Orbit items can extend slightly outside the block bounds; keep rendering
        return true;
    }
}

