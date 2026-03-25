package fr.iglee42.auxiliautilities.menu;

import fr.iglee42.auxiliautilities.blockentities.AUBlockEntity;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.NotNull;

public class AUBEMenu extends AUMenu {

    protected final AUBlockEntity blockEntity;

    protected AUBEMenu(@NotNull MenuType<?> type, int id, AUBlockEntity blockEntity) {
        super(type, id);
        this.blockEntity = blockEntity;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos()), player, blockEntity.getBlockState().getBlock());
    }

    @Override
    public void slotsChanged(Container container) {
        super.slotsChanged(container);
        blockEntity.setChanged();
    }

    protected void addTitle(){
        addTitle(blockEntity.getDisplayName());
    }
}
