package fr.iglee42.auxiliautilities.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.blockentities.klein.BEKleinBottle;
import fr.iglee42.auxiliautilities.blockentities.klein.KleinGeometry;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class KleinBottleRenderer implements BlockEntityRenderer<BEKleinBottle> {

    private static final ResourceLocation TEXTURE =
           AuxiliaUtilities.id("textures/block/klein_lighting.png");

    public KleinBottleRenderer(BlockEntityRendererProvider.Context ctx) {}

    @Override
    public void render(
            BEKleinBottle be,
            float partialTick,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            int packedOverlay,
            Vec3 cameraPos
    ) {
        Level level = be.getLevel();
        if (level == null) {
            return;
        }

        poseStack.pushPose();

        double time = (level.getGameTime() + partialTick) / 30.0 % 4.0;

        renderTube(poseStack, buffer, packedLight, packedOverlay, time);

        poseStack.popPose();
    }

    public static void renderTube(
            PoseStack poseStack,
            MultiBufferSource source,
            int light,
            int overlay,
            double time
    ) {
        VertexConsumer buffer =
                source.getBuffer(RenderType.entityTranslucent(TEXTURE,false));

        int angleN = 8;
        int heightN = 10;

        double[] ca = new double[angleN];
        double[] sa = new double[angleN];

        for (int i = 0; i < angleN; i++) {
            double t = i * Math.PI * 2.0 / angleN;
            ca[i] = Math.cos(t);
            sa[i] = Math.sin(t);
        }

        double scale = 0.95;

        for (int z = -1; z <= 1; z += 2) {

            for (int j = 0; (z == 1) ? (j < heightN) : (j > -heightN); j += z) {

                double tA = time + 0.05 * j;
                double tB = time + 0.05 * (j + 1);

                Vec3 stemPosA = KleinGeometry.getStemPos(tA)
                        .subtract(0.5, 0.5, 0.5)
                        .scale(scale)
                        .add(0.5, 0.5, 0.5);

                Vec3 stemPosB = KleinGeometry.getStemPos(tB)
                        .subtract(0.5, 0.5, 0.5)
                        .scale(scale)
                        .add(0.5, 0.5, 0.5);

                double radA = KleinGeometry.getStemRadius(tA) * scale;
                double radB = KleinGeometry.getStemRadius(tB) * scale;

                Vec3 normA = KleinGeometry.getStemNormal(tA);
                Vec3 normB = KleinGeometry.getStemNormal(tB);

                for (int k = 0; k < angleN; k++) {

                    int k2 = (k + 1) % angleN;

                    float u0 = k / (float) angleN;
                    float u1 = (k + 1) / (float) angleN;
                    float v0 = (1.0F + j / (float) heightN) / 2.0F;
                    float v1 = (1.0F + (j + 1) / (float) heightN) / 2.0F;

                    Vec3 p1 = stemPosA.add(normA.scale(radA * ca[k])).add(0, 0, radA * sa[k]);
                    Vec3 p2 = stemPosB.add(normB.scale(radB * ca[k])).add(0, 0, radB * sa[k]);
                    Vec3 p3 = stemPosB.add(normB.scale(radB * ca[k2])).add(0, 0, radB * sa[k2]);
                    Vec3 p4 = stemPosA.add(normA.scale(radA * ca[k2])).add(0, 0, radA * sa[k2]);

                    drawQuad(poseStack, buffer, light, overlay,
                            p1, u0, v0,
                            p2, u0, v1,
                            p3, u1, v1,
                            p4, u1, v0);
                }
            }
        }
    }

    private static void drawQuad(
            PoseStack poseStack,
            VertexConsumer buffer,
            int light,
            int overlay,
            Vec3 v1, float u1, float uv1,
            Vec3 v2, float u2, float uv2,
            Vec3 v3, float u3, float uv3,
            Vec3 v4, float u4, float uv4
    ) {

        Matrix4f mat = poseStack.last().pose();

        // Calculate normal using cross product of two edges
        Vec3 edge1 = v2.subtract(v1);
        Vec3 edge2 = v4.subtract(v1);
        Vec3 normal = edge1.cross(edge2).normalize();

        // Emit a full quad with the correct winding for outward-facing normals
        vertex(buffer, mat, light, overlay, v1, normal, u1, uv1);
        vertex(buffer, mat, light, overlay, v4, normal, u4, uv4);
        vertex(buffer, mat, light, overlay, v3, normal, u3, uv3);
        vertex(buffer, mat, light, overlay, v2, normal, u2, uv2);
    }

    private static void vertex(
            VertexConsumer buffer,
            Matrix4f mat,
            int light,
            int overlay,
            Vec3 v,
            Vec3 normal,
            float u,
            float v_uv
    ) {
        Vector3f norm = new Vector3f((float)normal.x, (float)normal.y, (float)normal.z);
        buffer.addVertex(mat,(float)v.x,(float)v.y,(float)v.z)
                .setColor(255,255,255,255)
                .setUv(u, v_uv)
                .setOverlay(overlay)
                .setLight(light)
                .setNormal(norm.x(), norm.y(), norm.z());
    }
}