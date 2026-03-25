package fr.iglee42.auxiliautilities.blockentities.gp.generators;

import fr.iglee42.auxiliautilities.blockentities.AUBlockEntityTypes;
import fr.iglee42.auxiliautilities.blockentities.gp.AUGPConsumerBlockEntity;
import fr.iglee42.auxiliautilities.blockentities.gp.AUGPGeneratorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.common.Tags;

public class BELavaMill extends AUGPGeneratorBlockEntity {
    public BELavaMill(BlockPos pos, BlockState state) {
        super(AUBlockEntityTypes.LAVA_MILL.get(), pos, state);
    }

    @Override
    public int getGPGeneration() {
        int gp = 0;

        if (level == null) return 0;
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BlockPos offset = getBlockPos().relative(direction);

            FluidState state = level.getFluidState(offset);

            if (state.is(Tags.Fluids.LAVA)) {
                gp = Math.min(2, Mth.floor((float) state.getAmount() /   2));
            }
        }

        return gp;
    }


}
