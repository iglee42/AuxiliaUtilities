package fr.iglee42.auxiliautilities.blocks;

import fr.iglee42.auxiliautilities.blocks.api.AUBlockBase;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class BlockAngel extends AUBlock {
    public BlockAngel(BlockBehaviour.Properties props) {
        super(props);
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos p_49853_, BlockState p_49854_, Player player) {
        if (!level.isClientSide && !player.getAbilities().instabuild){
            player.addItem(AUBlocks.ANGEL_BLOCK.get().asItem().getDefaultInstance());
        }
        return super.playerWillDestroy(level, p_49853_, p_49854_, player);
    }
}
