package fr.iglee42.auxiliautilities.blockentities.gp.generators;

import fr.iglee42.auxiliautilities.blockentities.AUBlockEntityTypes;
import fr.iglee42.auxiliautilities.blockentities.gp.AUGPConsumerBlockEntity;
import fr.iglee42.auxiliautilities.blockentities.gp.AUGPGeneratorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.common.Tags;

public class BEWindMill extends AUGPGeneratorBlockEntity {
    private static final int TIME_POWER = 8;
    private static final int MASK = 255;
    private static final float TIME_DIVISOR = 256.0F;

    private static final int RESULT_POW = 8;
    private static final int RESULT_MASK = 255;
    private static final float RESULT_DIVISOR = 256.0F;


    public BEWindMill(BlockPos pos, BlockState state) {
        super(AUBlockEntityTypes.WIND_MILL.get(), pos, state);
    }

    @Override
    public int getGPGeneration() {

        if (level == null) return 0;
        Direction[] sides = {Direction.NORTH,Direction.SOUTH};
        for (Direction direction : sides) {
            if (!level.isEmptyBlock(getBlockPos().relative(direction))) {
                return 0;
            }
        }
        long t = level.getGameTime();

        float v = ((int) t & MASK) / TIME_DIVISOR;

        long k = t >> TIME_POWER;
        k += level.dimension().location().hashCode() * 31L;

        long a = k * k * 42317861L + k * 11L;
        long b = a + (2L * k + 1L) * 42317861L + 11L;

        float ai = (int) (a & RESULT_MASK) / RESULT_DIVISOR;
        float bi = (int) (b & RESULT_MASK) / RESULT_DIVISOR;

        float v1 = ai + (bi - ai) * v;

        return (int)( (0.5F
                        + v1 * 2.0F
                        + (level.isRaining() ? 0.25F : 0.0F)
                        + (level.isThundering() ? 0.5F : 0.0F)) * 10);
    }


}
