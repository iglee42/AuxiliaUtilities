package fr.iglee42.auxiliautilities.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BEOpiniumCore extends AUBlockEntity {

    public BEOpiniumCore(BlockPos pos, BlockState state) {
        super(AUBlockEntityTypes.OPINIUM_CORE.get(), pos, state);
    }
}

