package fr.iglee42.auxiliautilities.blockentities.klein;

import net.minecraft.world.phys.Vec3;

public class KleinGeometry {

    public static double getStemRadius(double t) {
        if (t < 0) t = (t + 10) % 1;
        else if (t > 1) t %= 1;

        if (t < 0.35)
            return getBulbWidth(t / 0.35);

        if (t < 0.45) {
            double v = (t - 0.35) / 0.1;
            v = (1 + Math.cos(v * Math.PI)) / 2;
            return (1 - v) * 0.1 + v * getBulbWidth(1);
        }

        if (t > 0.9 && t < 1) {
            double v = (t - 0.9) / 0.1;
            if (v > 1) v = 1;
            v = Math.sqrt(1 - v * v);
            return (1 - v) * 0.1 + v * getBulbWidth(0);
        }

        return 0.1;
    }

    public static double getBulbWidth(double t) {
        double a = 0.1 + 0.2 * circle(-1 + t * 3);
        double v2 = 0.8 * (1 - t) + t * 0.1;
        return t * v2 + (1 - t) * a;
    }

    public static Vec3 getStemNormal(double t) {
        boolean returning;

        if (t < 0)
            t = (t + 10) % 1;

        if (t > 1) {
            returning = true;
            t %= 1;
        } else returning = false;

        double x;
        double y;

        if (t < 0.35) {
            x = -1;
            y = 0;
        } else if (t < 0.65) {

            double k = (t - 0.35) / 0.3 * Math.PI;

            x = -Math.cos(k);
            y = Math.sin(k);

        } else {

            double k = (t - 0.65) / 0.35;

            y = -Math.sin(k * Math.PI);

            double v = 1 / Math.sqrt(1 + y * y);

            x = v;
            y *= v;
        }

        if (returning) {
            x *= -1;
            y *= -1;
        }

        return new Vec3(x, y, 0);
    }

    public static Vec3 getStemPos(double t) {

        if (t < 0) t = (t + 10) % 1;
        else if (t > 1) t %= 1;

        double x;
        double y;

        if (t < 0.35) {

            x = 0;
            y = t / 0.35 * 0.7;

        } else if (t < 0.65) {

            double k = (t - 0.35) / 0.3 * Math.PI;

            x = 0.3 - 0.3 * Math.cos(k);
            y = 0.7 + 0.3 * Math.sin(k);

        } else {

            double k = (t - 0.65) / 0.35;

            x = 0.3 + 0.3 * Math.cos(k * Math.PI);
            y = 0.7 * (1 - k);
        }

        return new Vec3(
                0.34455 + x,
                -0.05 + y,
                0.5
        );
    }

    public static double circle(double t) {
        if (t < -1 || t > 1) return 0;
        return Math.sqrt(1 - t * t);
    }
}