package fr.iglee42.auxiliautilities.blockentities.gp;

import fr.iglee42.auxiliautilities.gp.GPNetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class AUGPGeneratorBlockEntity extends AUGPBlockEntity{

    private int previousGPGeneration = 0;

    public AUGPGeneratorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    protected final void removedFromNetwork() {
        if (!level.isClientSide && getNetworkId() != null)
            GPNetworkManager.INSTANCE.unregisterGenerator(this);
    }

    @Override
    protected final void addedToNetwork() {
        if (!level.isClientSide && getNetworkId() != null)
            GPNetworkManager.INSTANCE.registerGenerator(this);
    }

    @Override
    protected final void updateNetwork() {
        if (!level.isClientSide && getNetworkId() != null)
            GPNetworkManager.INSTANCE.getNetwork(getNetworkId()).markGeneratorDirty();
    }

    @Override
    public final int getGPConsumption() {
        return 0;
    }

    @Override
    protected boolean serverTick(ServerLevel level, BlockPos pos, BlockState state) {
        boolean update = super.serverTick(level, pos, state);
        if (getGPGeneration() != previousGPGeneration){
            previousGPGeneration = getGPGeneration();
            update = true;
        }
        return update;
    }
}
