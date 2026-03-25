package fr.iglee42.auxiliautilities.blockentities.gp.generators;

import fr.iglee42.auxiliautilities.blockentities.AUBlockEntityTypes;
import fr.iglee42.auxiliautilities.blockentities.gp.AUGPConsumerBlockEntity;
import fr.iglee42.auxiliautilities.blockentities.gp.AUGPGeneratorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class BEDragonEggMill extends AUGPGeneratorBlockEntity {
    public BEDragonEggMill(BlockPos pos, BlockState state) {
        super(AUBlockEntityTypes.DRAGON_EGG_MILL.get(), pos, state);
    }

    @Override
    public int getGPGeneration() {
        if (level == null) return 0;
        if (getBlockPos() == null) return 0;
        return level.getBlockState(getBlockPos().above()).is(Blocks.DRAGON_EGG) ? 500 : 0;
    }
}
