package fr.iglee42.auxiliautilities.blocks.glass;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public class AUBlockDarkGlass extends AUBlockGlass {
    public AUBlockDarkGlass(Properties props) {
        super(props);
    }

    @Override
    protected boolean propagatesSkylightDown(BlockState p_309084_, BlockGetter p_309133_, BlockPos p_309097_) {
        return false;
    }

    @Override
    protected int getLightBlock(BlockState p_60585_, BlockGetter getter, BlockPos p_60587_) {
        return getter.getMaxLightLevel();
    }
}
