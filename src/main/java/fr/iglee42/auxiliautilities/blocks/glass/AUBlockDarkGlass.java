package fr.iglee42.auxiliautilities.blocks.glass;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public class AUBlockDarkGlass extends AUBlockGlass {
    public AUBlockDarkGlass(Properties props) {
        super(props);
    }

    @Override
    protected boolean propagatesSkylightDown(BlockState p_309084_) {
        return false;
    }

    @Override
    protected int getLightBlock(BlockState p_60585_) {
        return 15;
    }
}
