package fr.iglee42.auxiliautilities.blockentities.gp.generators;

import fr.iglee42.auxiliautilities.blockentities.AUBlockEntityTypes;
import fr.iglee42.auxiliautilities.blockentities.gp.AUGPConsumerBlockEntity;
import fr.iglee42.auxiliautilities.blockentities.gp.AUGPGeneratorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BEFireMill extends AUGPGeneratorBlockEntity {
    public BEFireMill(BlockPos pos, BlockState state) {
        super(AUBlockEntityTypes.FIRE_MILL.get(), pos, state);
    }

    @Override
    public int getGPGeneration() {
        if (level == null) return 0;
        if (getBlockPos() == null) return 0;
        return level.getBlockState(getBlockPos().below()).is(BlockTags.FIRE) ? 4 : 0;
    }
}
