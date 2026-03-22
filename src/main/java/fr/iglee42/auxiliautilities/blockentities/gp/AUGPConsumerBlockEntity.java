package fr.iglee42.auxiliautilities.blockentities.gp;

import fr.iglee42.auxiliautilities.gp.GPNetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class AUGPConsumerBlockEntity extends AUGPBlockEntity{
    public AUGPConsumerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    protected final void removedFromNetwork() {
        if (!level.isClientSide && getNetworkId() != null)
            GPNetworkManager.INSTANCE.unregisterConsumer(this);
    }

    @Override
    protected final void addedToNetwork() {
        if (!level.isClientSide && getNetworkId() != null)
            GPNetworkManager.INSTANCE.registerConsumer(this);
    }

    @Override
    protected final void updateNetwork() {
        if (!level.isClientSide && getNetworkId() != null)
            GPNetworkManager.INSTANCE.getNetwork(getNetworkId()).markConsumerDirty();
    }

    @Override
    public final int getGPGeneration() {
        return 0;
    }

}
