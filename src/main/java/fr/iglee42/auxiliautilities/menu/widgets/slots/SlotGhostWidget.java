package fr.iglee42.auxiliautilities.menu.widgets.slots;

import fr.iglee42.auxiliautilities.menu.AUMenu;
import fr.iglee42.auxiliautilities.menu.widgets.api.AUSlotClickWidget;
import fr.iglee42.auxiliautilities.utils.InventoryHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;

public class SlotGhostWidget extends SlotItemHandlerWidget implements AUSlotClickWidget {
    public SlotGhostWidget(IItemHandler container, int slot, int x, int y) {
        super(container, slot, x, y);
    }


    @Override
    public void onSlotClick(AUMenu menu, int slotId, int button, ClickType clickType, Player player) {
        set(menu.getCarried());
        menu.slotsChanged(this.container);
    }

    @Override
    public void set(ItemStack stack) {
        if (InventoryHelper.isStackNotEmpty(stack)){
            stack = stack.copy();
            stack.setCount(1);
        }
        super.set(stack);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return false;
    }

    @Override
    public boolean mayPickup(Player playerIn) {
        return false;
    }

    @Override
    public boolean isFake() {
        return false;
    }
}
