package fr.iglee42.auxiliautilities.blockentities.klein;

import fr.iglee42.auxiliautilities.blockentities.AUBlockEntity;
import fr.iglee42.auxiliautilities.blockentities.AUBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BEKleinBottle extends AUBlockEntity {

    public BEKleinBottle(BlockPos pos, BlockState state) {
        super(AUBlockEntityTypes.KLEIN_BOTTLE.get(), pos, state);
    }
}

