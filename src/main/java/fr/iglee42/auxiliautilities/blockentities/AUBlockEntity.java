package fr.iglee42.auxiliautilities.blockentities;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AUBlockEntity extends BlockEntity implements MenuProvider {

    protected int tickCount = 0;

    public AUBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    // Data Management
    @Override
    protected final void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        tickCount = tag.getInt("tickCount");
        load(tag,registries);
    }

    @Override
    protected final void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("tickCount", tickCount);
        save(tag,registries,false);
    }

    protected void load(CompoundTag tag, HolderLookup.Provider registries) {};
    protected void save(CompoundTag tag, HolderLookup.Provider registries,boolean forClient) {};

    @Override
    public final @NotNull CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag nbt = super.getUpdateTag(registries);
        save(nbt,registries,true);
        return nbt;
    }

    @Override
    public final @NotNull Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    // Ticking Management

    public void tick(Level level, BlockPos pos, BlockState state){
        if (level == null || pos == null || state == null) return;
        if (level.isClientSide)
            clientTick((ClientLevel) level,pos,state);
        else {
            boolean sendUpdatePacket = serverTick((ServerLevel) level,pos,state);
            tickCount++;
            if (sendUpdatePacket) setChanged();
        }
    }

    protected void clientTick(ClientLevel level, BlockPos pos, BlockState state) {}
    protected boolean serverTick(ServerLevel level, BlockPos pos, BlockState state) {return false;}

    @Override
    public final void setChanged() {
        super.setChanged();
        changed();
        if (level == null) return;
        if (level.isClientSide) return;
        level.sendBlockUpdated(getBlockPos(),getBlockState(),getBlockState(), Block.UPDATE_CLIENTS);
    }

    protected void changed(){}

    public void setPlacedBy(Player player){};
    public void destroy(){}

    @Override
    public @Nullable AbstractContainerMenu createMenu(int id, Inventory playerInv, Player player) {
        return null;
    }

    @Override
    public Component getDisplayName() {
        return getBlockState().getBlock().getName();
    }
}
