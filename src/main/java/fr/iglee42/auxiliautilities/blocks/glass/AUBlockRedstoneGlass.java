package fr.iglee42.auxiliautilities.blocks.glass;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public class AUBlockRedstoneGlass extends AUBlockGlass {
    public AUBlockRedstoneGlass(Properties props) {
        super(props);
    }

    @Override
    protected boolean isSignalSource(BlockState p_60571_) {
        return true;
    }

    @Override
    protected int getSignal(BlockState p_60483_, BlockGetter p_60484_, BlockPos p_60485_, Direction p_60486_) {
        return 15;
    }
}
