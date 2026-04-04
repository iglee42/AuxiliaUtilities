package fr.iglee42.auxiliautilities.utils;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.ClientHooks;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import javax.annotation.Nonnull;

public class QuadsHelper {
    @OnlyIn(Dist.CLIENT)
    @Nonnull
    public static BakedQuad createBakedQuad(UV[] vecs, TextureAtlasSprite sprite, boolean addShading, int tint) {
        Vector3f a = new Vector3f(), b = new Vector3f(), c = new Vector3f();
        sub(vecs[1].toVector3f(), vecs[0].toVector3f(), a);
        sub(vecs[2].toVector3f(), vecs[0].toVector3f(), b);
        cross(a, b, c);
        Direction facing = Direction.getNearest(c.x, c.y, c.z);
        int col = addShading ? getFaceShadeColor(c.x, c.y, c.z) : tint;
        int[] vertex = new int[32];
        for (int i = 0; i < 4; i++) {
            vertex[i * 8] = Float.floatToRawIntBits((vecs[i]).x);
            vertex[i * 8 + 1] = Float.floatToRawIntBits((vecs[i]).y);
            vertex[i * 8 + 2] = Float.floatToRawIntBits((vecs[i]).z);
            vertex[i * 8 + 3] = col;
            vertex[i * 8 + 4] = Float.floatToRawIntBits(sprite.getU(((vecs[i]).u)));
            vertex[i * 8 + 5] = Float.floatToRawIntBits(sprite.getV((1-(vecs[i]).v)));
        }
        ClientHooks.fillNormal(vertex, facing);
        return new BakedQuad(vertex, tint, facing, sprite, false, false);
    }

    public static float getFaceBrightness(float x, float y, float z) {
        float[] norm = LightMathHelper.norm(x, y, z);
        return getFaceBrightnessNorm(norm[0], norm[1], norm[2]);
    }

    public static float getFaceBrightnessNorm(float a, float b, float c) {
        return a * a * 0.6F + b * b * 0.75F + 0.25F * b + c * c * 0.8F;
    }

    public static int getFaceShadeColor(float x, float y, float z) {
        float f = getFaceBrightness(x, y, z);
        int i = Mth.clamp((int)(f * 255.0F), 0, 255);
        return 0xFF000000 | i << 16 | i << 8 | i;
    }

    public static Vector3f sub(Vector3f left, Vector3f right, Vector3f dest) {
        if (dest == null)
            return new Vector3f(left.x - right.x, left.y - right.y, left.z - right.z);
        else {
            dest.set(left.x - right.x, left.y - right.y, left.z - right.z);
            return dest;
        }
    }

    public static Vector3f cross(
            Vector3f left,
            Vector3f right,
            Vector3f dest)
    {

        if (dest == null)
            dest = new Vector3f();

        dest.set(
                left.y * right.z - left.z * right.y,
                right.x * left.z - right.z * left.x,
                left.x * right.y - left.y * right.x
        );

        return dest;
    }

    public static BakedQuad applyMatrixTransform(BakedQuad quad, Matrix4f mat) {

        int[] data = quad.getVertices().clone();

        for (int v = 0; v < 4; v++) {

            int i = v * 8;

            float x = Float.intBitsToFloat(data[i]);
            float y = Float.intBitsToFloat(data[i + 1]);
            float z = Float.intBitsToFloat(data[i + 2]);

            Vector4f pos = new Vector4f(x, y, z, 1.0F);
            pos.mul(mat);

            data[i]     = Float.floatToRawIntBits(pos.x);
            data[i + 1] = Float.floatToRawIntBits(pos.y);
            data[i + 2] = Float.floatToRawIntBits(pos.z);
        }

        return new BakedQuad(
                data,
                quad.getTintIndex(),
                quad.getDirection(),
                quad.getSprite(),
                quad.isShade()
        );
    }


    public record UV(float x, float y, float z, float u, float v) {
        public Vector3f toVector3f() {
            return new Vector3f(x, y, z);
        }
    }

}
