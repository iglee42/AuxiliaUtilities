package fr.iglee42.auxiliautilities.blockentities.gp.generators;

import fr.iglee42.auxiliautilities.blockentities.AUBlockEntityTypes;
import fr.iglee42.auxiliautilities.blockentities.gp.AUGPConsumerBlockEntity;
import fr.iglee42.auxiliautilities.blockentities.gp.AUGPGeneratorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.common.Tags;

public class BEWaterMill extends AUGPGeneratorBlockEntity {
    public BEWaterMill(BlockPos pos, BlockState state) {
        super(AUBlockEntityTypes.WATER_MILL.get(), pos, state);
    }

    @Override
    public int getGPGeneration() {
        int gp = 0;

        if (level == null) return 0;
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BlockPos offset = getBlockPos().relative(direction);

            FluidState state = level.getFluidState(offset);

            if (state.is(Tags.Fluids.WATER) && !level.getFluidState(offset.above()).is(Tags.Fluids.WATER)) {

                int value = 8 - state.getAmount();

                if (value < 8) {
                    gp += 4* (int) ((value + 1) / 2.0F);
                }
            }
        }

        return gp;
    }


}
