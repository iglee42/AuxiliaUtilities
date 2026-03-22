package fr.iglee42.auxiliautilities.blockentities.gp.generators;

import fr.iglee42.auxiliautilities.blockentities.AUBlockEntityTypes;
import fr.iglee42.auxiliautilities.blockentities.gp.AUGPGeneratorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BECreativeMill extends AUGPGeneratorBlockEntity {
    public BECreativeMill( BlockPos pos, BlockState state) {
        super(AUBlockEntityTypes.CREATIVE_MILL.get(), pos, state);
    }

    @Override
    public int getGPGeneration() {
        return 10000;
    }
}
