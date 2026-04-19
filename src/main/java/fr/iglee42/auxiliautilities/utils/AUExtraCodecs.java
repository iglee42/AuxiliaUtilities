package fr.iglee42.auxiliautilities.utils;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;

public class AUExtraCodecs {
    public static final Codec<BlockPos> BLOCK_POS = RecordCodecBuilder.create(instance->
            instance.group(
                    Codec.INT.fieldOf("x").forGetter(BlockPos::getX),
                    Codec.INT.fieldOf("y").forGetter(BlockPos::getY),
                    Codec.INT.fieldOf("z").forGetter(BlockPos::getZ)
            ).apply(instance,BlockPos::new));

}
