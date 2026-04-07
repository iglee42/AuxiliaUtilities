
package fr.iglee42.auxiliautilities.client;

import com.mojang.blaze3d.vertex.VertexConsumer;

public class TintingVertexConsumer implements VertexConsumer {
    private final VertexConsumer parent;
    private final float tintR, tintG, tintB;

    public TintingVertexConsumer(VertexConsumer parent, float r, float g, float b) {
        this.parent = parent;
        this.tintR = r;
        this.tintG = g;
        this.tintB = b;
    }

    @Override
    public VertexConsumer setColor(int r, int g, int b, int a) {
        parent.setColor(
                Math.min(255, (int)(r * tintR)),
                Math.min(255, (int)(g * tintG)),
                Math.min(255, (int)(b * tintB)),
                a
        );
        return this;
    }

    @Override public VertexConsumer addVertex(float x, float y, float z) {
        parent.addVertex(x, y, z);
        return this;
    }
    @Override public VertexConsumer setUv(float u, float v) {
        parent.setUv(u, v);
        return this;
    }
    @Override public VertexConsumer setUv1(int u, int v) {
        parent.setUv1(u, v);
        return this;
    }
    @Override public VertexConsumer setUv2(int u, int v) {
        parent.setUv2(u, v);
        return this;
    }
    @Override public VertexConsumer setNormal(float x, float y, float z) {
        parent.setNormal(x, y, z);
        return this;
    }
}