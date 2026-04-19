package fr.iglee42.auxiliautilities.blockentities.gp;

import fr.iglee42.auxiliautilities.blockentities.AUBlockEntity;
import fr.iglee42.auxiliautilities.gp.GPHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.UUID;

public abstract class AUGPBlockEntity extends AUBlockEntity implements GPHolder {

    private UUID owner;

    public AUGPBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    protected void save(CompoundTag tag, HolderLookup.Provider registries, boolean forClient) {
        super.save(tag, registries, forClient);
        if (owner != null) tag.store("owner", UUIDUtil.CODEC, owner);
    }

    @Override
    protected void load(CompoundTag tag, HolderLookup.Provider registries) {
        super.load(tag, registries);
        if (tag.contains("owner"))owner = tag.read("owner",UUIDUtil.CODEC).orElseThrow();
    }

    protected abstract void removedFromNetwork();
    protected abstract void addedToNetwork();
    protected abstract void updateNetwork();


    public void setPlacedBy(Player player){
        if (!level.isClientSide) {
            owner = player.getUUID();
            changed();
        }
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (!level.isClientSide)
            addedToNetwork();
    }

    @Override
    public void onChunkUnloaded() {
        super.onChunkUnloaded();
        if (!level.isClientSide)
            removedFromNetwork();
    }

    @Override
    protected void changed() {
        super.changed();
        if (!level.isClientSide)
            updateNetwork();
    }

    @Override
    public void destroy(BlockPos pos, BlockState state) {
        super.destroy(pos,state);
        removedFromNetwork();
    }

    @Override
    public UUID getNetworkId() {
        return owner;
    }

    @Override
    public String name() {
        return getBlockState().getBlock().getDescriptionId();
    }
}
