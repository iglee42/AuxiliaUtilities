package fr.iglee42.auxiliautilities.utils;

import net.minecraft.util.Mth;

public class LightMathHelper {
  static float[] APPROX_SQRT = new float[4096];
  
  static {
    for (int i = 0; i < APPROX_SQRT.length; i++)
      APPROX_SQRT[i] = Mth.sqrt(i / 4096.0F);
  }
  
  public static float approxSqrt(float v, float r) {
    if (v <= 0.0F)
      return 0.0F; 
    if (v >= r)
      return 1.0F; 
    return APPROX_SQRT[(int)(v / r * 4096.0F) & 0xFFF];
  }
  
  public static float[] norm(float a, float b, float c) {
    float a2 = a * a;
    float b2 = b * b;
    float c2 = c * c;
    float r = a2 + b2 + c2;
    return new float[] { approxSqrt(a2, r) * Math.signum(a), approxSqrt(b2, r) * Math.signum(b), approxSqrt(c2, r) * Math.signum(c) };
  }
  
  public static float partialDist(float x, float y, float z, float r) {
    return approxSqrt(x * x + y * y + z * z, r * r);
  }
}
